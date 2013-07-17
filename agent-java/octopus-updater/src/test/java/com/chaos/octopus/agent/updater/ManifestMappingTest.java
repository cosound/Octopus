package com.chaos.octopus.agent.updater;

import org.junit.Assert;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 13-07-13
 * Time: 17:00
 */
public class ManifestMappingTest
{
/*    @org.junit.Test
    public void Map_GivenManifestString_ReturnManifest() throws Exception
    {
        ManifestMapping mapping = Make_ManifestMappinG();
        String response = "";

        Manifest result = mapping.map(response);

        Assert.assertNotNull(result);
    }*/

    private ManifestMapping Make_ManifestMappinG()
    {
        return new ManifestMapping();
    }
}
