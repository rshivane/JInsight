/*
 * Copyright 2017 Agilx, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.apptuit.metrics.jinsight.modules.jvm;

import static java.lang.management.ManagementFactory.getOperatingSystemMXBean;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.sun.management.UnixOperatingSystemMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rajiv Shivane
 */
class FileDescriptorMetrics implements MetricSet {

  private static final Logger LOGGER = Logger.getLogger(FileDescriptorMetrics.class.getName());

  private final Class unixOSMBeanClass;
  private final OperatingSystemMXBean osMxBean;

  public FileDescriptorMetrics() throws ClassNotFoundException {
    this(getOperatingSystemMXBean());
  }

  public FileDescriptorMetrics(OperatingSystemMXBean osMxBean) throws ClassNotFoundException {
    this.osMxBean = osMxBean;
    unixOSMBeanClass = Class.forName("com.sun.management.UnixOperatingSystemMXBean");
    unixOSMBeanClass.cast(this.osMxBean);
  }

  public Map<String, Metric> getMetrics() {
    final Map<String, Metric> gauges = new HashMap<>();
    if (osMxBean instanceof UnixOperatingSystemMXBean) {
      gauges.put("open", (Gauge<Long>) () -> getMetricLong("getOpenFileDescriptorCount"));
      gauges.put("max", (Gauge<Long>) () -> getMetricLong("getMaxFileDescriptorCount"));
    }
    return gauges;
  }

  private Long getMetricLong(String name) {
    try {
      final Method method = unixOSMBeanClass.getDeclaredMethod(name);
      return (Long) method.invoke(osMxBean);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      LOGGER.log(Level.SEVERE,
          "Error fetching file descriptor metrics from OperatingSystemMXBean", e);
      return -1L;
    }
  }

}
