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

package ai.apptuit.metrics.jinsight;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import org.jboss.byteman.agent.Main;

/**
 * The main agent class for JInsight. A wrapper around ByteMan Agent to easy configuration and
 * setup
 *
 * @author Rajiv Shivane
 */
public class Agent {

  private static final String BTM_SCRIPTS_RESOURCE_PATH = "btm-scripts/jinsight.btm";
  private static final String RESOURCE_SCRIPT_PARAM = "resourcescript:" + BTM_SCRIPTS_RESOURCE_PATH;

  public static void premain(String agentArgs, Instrumentation instrumentation) throws Exception {
    if (!canStartAgent()) {
      return;
    }
    Main.premain(RESOURCE_SCRIPT_PARAM, instrumentation);
  }

  public static void agentmain(String agentArgs, Instrumentation instrumentation) throws Exception {
    if (!canStartAgent()) {
      return;
    }
    Main.agentmain(RESOURCE_SCRIPT_PARAM, instrumentation);
  }

  private static boolean canStartAgent() {
    if (ClassLoader.getSystemResource(BTM_SCRIPTS_RESOURCE_PATH) == null) {
      System.err.println("Could not load " + BTM_SCRIPTS_RESOURCE_PATH + "."
          + "Agent will not be started.");
      return false;
    }

    try {
      ConfigService.initialize();
    } catch (ConfigurationException | IOException | RuntimeException e) {
      System.err.println(e.getMessage());
      System.err.println("Agent will not be started.");
      return false;
    }

    return true;
  }
}