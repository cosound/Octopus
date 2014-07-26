/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.commons.exception;

public class ConnectException extends Error {
  public ConnectException() {
  }

  public ConnectException(Throwable cause) {
    super(cause);
  }

  public ConnectException(String message, Throwable cause) {

    super(message, cause);
  }

  public ConnectException(String message) {

    super(message);
  }
}
