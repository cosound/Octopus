/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.commons.exception;

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
