package com.chaos.sdk.test;

import com.chaos.sdk.Chaos;
import com.chaos.sdk.model.McmObject;
import com.chaos.sdk.model.Session;
import com.chaos.sdk.v6.dto.AuthenticatedChaosClient;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 27-10-13
 * Time: 20:05
 */
public class ChaosTest
{
    @Test
    public void authenticate_GivenValidKey_ReturnAuthenticatedClient() throws IOException
    {
        String      key     = "somekey";
        MockGateway gateway = new MockGateway("v6/SiteAccess/Auth?apiKey=" + key, "{\"Header\": {\"Duration\": 177.4577},\"Body\": {\"Count\": 1,\"TotalCount\": 1,\"Results\": [{\"Guid\": \"d01755b9-e019-4d7c-98d2-ca4583196a4f\",\"UserGuid\": \"33333633-3136-3433-2d33-3633382d3633\",\"DateCreated\": 1382903500,\"DateModified\": 1382903500,\"FullName\": \"Chaos.Mcm.Data.Dto.Object\"}]},\"Error\": {\"Fullname\": null,\"Message\": null,\"InnerException\": null}}");
        Chaos       api     = new Chaos(gateway);

        AuthenticatedChaosClient result = api.authenticate(key);

        assertNotNull(result);
    }

    @Test
    public void objectCreate_GivenValidSessionAndFolder_ReturnObject() throws IOException
    {
        int objectTypeId    = 1;
        int folderId        = 1;
        String sessionGuid  = "someguid";
        MockGateway gateway = new MockGateway("v6/Object/Create?sessionGUID="+ sessionGuid +"&objectTypeID="+objectTypeId+"&folderID=" + folderId,"{\"Header\": {\"Duration\": 1303.1397},\"Body\": {\"Count\": 1,\"TotalCount\": 1,\"Results\": [{\"Guid\": \"1a9fb3fb-753d-491f-89f6-dbf3c674bd78\",\"ObjectTypeID\": 1,\"DateCreated\": 1382970919,\"Metadatas\": [],\"ObjectRelationInfos\": [],\"Files\": [],\"AccessPoints\": [],\"ObjectFolders\": [],\"FullName\": \"Chaos.Mcm.Data.Dto.Object\"}]},\"Error\": {\"Fullname\": null,\"Message\": null,\"InnerException\": null}}");
        Chaos       api     = new Chaos(gateway);

        McmObject result = api.objectCreate(sessionGuid, null, objectTypeId, folderId);

        assertEquals("1a9fb3fb-753d-491f-89f6-dbf3c674bd78", result.getId());
    }

    @Test
    public void metadataSet_GivenValidSessionObjectId_ReturnOne() throws IOException
    {
        String objectGuid         = "someobjectGuid";
        String metadataSchemaGuid = "somemetadataSchemaGuid";
        String languageCode       = "somelanguageCode";
        String revisionID         = "somerevisionID";
        String metadataXml        = "somemetadataXml";
        String sessionGuid        = "someguid";
        MockGateway gateway = new MockGateway("v6/Metadata/Set?sessionGUID=" +sessionGuid+ "&objectGuid=someobjectGuid&metadataSchemaGuid=somemetadataSchemaGuid&languageCode=somelanguageCode&revisionID=somerevisionID&metadataXml=somemetadataXml", "{\"Header\": {\"Duration\": 177.4577},\"Body\": {\"Count\": 1,\"TotalCount\": 1,\"Results\": [{\"Value\": \"1\",\"FullName\": \"Chaos.Mcm.Data.Dto.ScalarResult\"}]},\"Error\": {\"Fullname\": null,\"Message\": null,\"InnerException\": null}}");
        Chaos       api     = new Chaos(gateway);

        int result = api.metadataSet(sessionGuid, objectGuid, metadataSchemaGuid, languageCode, revisionID, metadataXml);

        assertEquals(1, result);
    }


}
