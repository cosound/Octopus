package com.chaos.octopus.commons.exception;

/**
 * Copyright (c) 2014 Chaos ApS. All rights reserved. See LICENSE.TXT for details.
 */
public class DisconnectException extends Exception
{
    public DisconnectException() {
    }

    public DisconnectException(String message) {
        super(message);
    }

    public DisconnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisconnectException(Throwable cause) {
        super(cause);
    }
}
