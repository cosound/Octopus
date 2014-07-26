/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.commons.core;

public interface Plugin
{
	String getId();
  Task getTask();
	void execute() throws Exception;
	void rollback();
	void commit();
}
