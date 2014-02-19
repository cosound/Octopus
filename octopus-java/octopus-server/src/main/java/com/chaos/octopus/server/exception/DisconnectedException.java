package com.chaos.octopus.server.exception;

/**
 * Copyright (c) 2014 Chaos ApS. All rights reserved. See LICENSE.TXT for details.
 */
public class DisconnectedException extends Exception
{
    public DisconnectedException() {
    }

    public DisconnectedException(String message) {
        super(message);
    }

    public DisconnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisconnectedException(Throwable cause) {
        super(cause);
    }
}
