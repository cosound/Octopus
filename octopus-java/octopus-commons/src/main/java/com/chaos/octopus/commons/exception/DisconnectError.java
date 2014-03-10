package com.chaos.octopus.commons.exception;

/**
 * Copyright (c) 2014 Chaos ApS. All rights reserved. See LICENSE.TXT for details.
 */
public class DisconnectError extends Error
{
    public DisconnectError() {
    }

    public DisconnectError(String message) {
        super(message);
    }

    public DisconnectError(String message, Throwable cause) {
        super(message, cause);
    }

    public DisconnectError(Throwable cause) {
        super(cause);
    }
}
