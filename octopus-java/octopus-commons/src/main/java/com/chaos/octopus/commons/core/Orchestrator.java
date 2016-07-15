/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.commons.core;

import java.net.ConnectException;

public interface Orchestrator extends AutoCloseable, JobQueue {
  void open() throws ConnectException;
  void taskCompleted(Task task);
  void taskUpdate(Task task);
  int get_localListenPort();
}
