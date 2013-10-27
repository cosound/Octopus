package com.chaos.sdk.test;

import com.chaos.sdk.Chaos;
import com.chaos.sdk.model.Session;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 27-10-13
 * Time: 20:05
 */
public class ChaosTest
{
    @Test
    public void authenticate_GivenValidKey_ReturnAuthenticatedSession() throws IOException
    {
        String      key     = "somekey";
        MockGateway gateway = new MockGateway("v6/SiteAccess/Auth?apiKey=" + key,"{\"Header\": {\"Duration\": 177.4577},\"Body\": {\"Count\": 1,\"TotalCount\": 1,\"Results\": [{\"Guid\": \"d01755b9-e019-4d7c-98d2-ca4583196a4f\",\"UserGuid\": \"33333633-3136-3433-2d33-3633382d3633\",\"DateCreated\": 1382903500,\"DateModified\": 1382903500,\"FullName\": \"Chaos.Portal.Core.Data.Model.Session\"}]},\"Error\": {\"Fullname\": null,\"Message\": null,\"InnerException\": null}}");
        Chaos       api     = new Chaos(gateway);

        Session result = api.authenticate(key);

        assertEquals("d01755b9-e019-4d7c-98d2-ca4583196a4f", result.getId());
    }
}
