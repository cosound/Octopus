/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.sdk;

import com.chaos.sdk.v6.dto.PortalResponse;

import java.io.IOException;

public interface ChaosGateway {
  PortalResponse call(String method, String path, String query) throws IOException;
}
