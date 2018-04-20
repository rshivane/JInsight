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

package ai.apptuit.metrics.jinsight.modules.servlet;

import ai.apptuit.metrics.jinsight.WebRequestContext;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Rajiv Shivane
 */
public class ExceptionServlet extends BaseTestServlet {

  static final String PATH = "/fail";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String requestId = WebRequestContext.getCurrentRequest().getRequestID();
    response.addCookie(new Cookie(RUNTIME_REQUEST_ID_COOKIENAME, requestId));
    RuntimeException exception = new RuntimeException("Always fail");
    exception.setStackTrace(new StackTraceElement[0]);
    throw exception;
  }

  @Override
  public String getPath() {
    return PATH;
  }
}
