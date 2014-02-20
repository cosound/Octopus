package com.chaos.octopus.commons.exception;

/**
 * Copyright (c) 2014 Chaos ApS. All rights reserved. See LICENSE.TXT for details.
 */
public class ConnectException extends Error
{
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
