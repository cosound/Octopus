package com.chaos.sdk.test;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.exception.InsufficientPermissionsException;
import com.chaos.sdk.Chaos;
import com.chaos.sdk.model.McmObject;
import com.chaos.sdk.AuthenticatedChaosClient;
import com.chaos.sdk.v6.dto.ClusterState;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 27-10-13
 * Time: 20:05
 */
public class ChaosTest {
  @Test
  public void authenticate_GivenValidKey_ReturnAuthenticatedClient() throws IOException {
    String key = "somekey";
    MockGateway gateway = new MockGateway("v6/AuthKey/Login?token=" + key, "{\"Header\": {\"Duration\": 177.4577},\"Body\": {\"Count\": 1,\"TotalCount\": 1,\"Results\": [{\"Guid\": \"d01755b9-e019-4d7c-98d2-ca4583196a4f\",\"UserGuid\": \"33333633-3136-3433-2d33-3633382d3633\",\"DateCreated\": 1382903500,\"DateModified\": 1382903500,\"FullName\": \"Chaos.Mcm.Data.Dto.Object\"}]},\"Error\": {\"Fullname\": null,\"Message\": null,\"InnerException\": null}}");
    Chaos api = new Chaos(gateway);

    AuthenticatedChaosClient result = api.authenticate(key);

    assertNotNull(result);
  }

  @Test
  public void objectCreate_GivenValidSessionAndFolder_ReturnObject() throws IOException {
    int objectTypeId = 1;
    int folderId = 1;
    String sessionGuid = "someguid";
    MockGateway gateway = new MockGateway("v6/Object/Create?sessionGUID=" + sessionGuid + "&objectTypeID=" + objectTypeId + "&folderID=" + folderId, "{\"Header\": {\"Duration\": 1303.1397},\"Body\": {\"Count\": 1,\"TotalCount\": 1,\"Results\": [{\"Guid\": \"1a9fb3fb-753d-491f-89f6-dbf3c674bd78\",\"ObjectTypeID\": 1,\"DateCreated\": 1382970919,\"Metadatas\": [],\"ObjectRelationInfos\": [],\"Files\": [],\"AccessPoints\": [],\"ObjectFolders\": [],\"FullName\": \"Chaos.Mcm.Data.Dto.Object\"}]},\"Error\": {\"Fullname\": null,\"Message\": null,\"InnerException\": null}}");
    Chaos api = new Chaos(gateway);

    McmObject result = api.objectCreate(sessionGuid, null, objectTypeId, folderId);

    assertEquals("1a9fb3fb-753d-491f-89f6-dbf3c674bd78", result.getId());
  }

  @Test
  public void metadataSet_GivenValidSessionObjectId_ReturnOne() throws IOException {
    String objectGuid = "someobjectGuid";
    String metadataSchemaGuid = "somemetadataSchemaGuid";
    String languageCode = "somelanguageCode";
    String revisionID = "somerevisionID";
    String metadataXml = "somemetadataXml";
    String sessionGuid = "someguid";
    String response = "{\"Header\": {\"Duration\": 177.4577},\"Body\": {\"Count\": 1,\"TotalCount\": 1,\"Results\": [{\"Value\": \"1\",\"FullName\": \"Chaos.Mcm.Data.Dto.ScalarResult\"}]},\"Error\": {\"Fullname\": null,\"Message\": null,\"InnerException\": null}}";
    MockGateway gateway = new MockGateway("v6/Metadata/Set?sessionGUID=" + sessionGuid + "&objectGuid=someobjectGuid&metadataSchemaGuid=somemetadataSchemaGuid&languageCode=somelanguageCode&revisionID=somerevisionID&metadataXml=somemetadataXml", response);
    Chaos api = new Chaos(gateway);

    int result = api.metadataSet(sessionGuid, objectGuid, metadataSchemaGuid, languageCode, revisionID, metadataXml);

    assertEquals(1, result);
  }

  @Test
  public void jobGet_GivenStatusNew_ReturnListOfJobs() throws IOException {
    String sessionId = "someguid";
    String response = "{\"Header\": {\"Duration\": 26.8528},\"Body\": {\"Count\": 1,\"TotalCount\": 1,\"Results\": [{\"id\": \"0123456789\",\"status\": \"new\",\"data\": \"{\\\"id\\\":\\\"0123456789\\\",\\\"steps\\\":[{\\\"tasks\\\":[{\\\"pluginId\\\":\\\"com.chaos.octopus.agent.unit.TestPlugin, 1.0.0\\\",\\\"properties\\\":{\\\"sleep\\\":\\\"3000\\\",\\\"number\\\":\\\"2\\\"}}]}]}\",\"datecreated\": 1391004000,\"FullName\": \"Chaos.Octopus.Module.Extension.Dto.Job\"}]},\"Error\": {\"Fullname\": null,\"Message\": null,\"InnerException\": null}}";
    MockGateway gateway = new MockGateway("v6/Job/GetIncomplete?sessionGUID=" + sessionId, response);
    AuthenticatedChaosClient client = new AuthenticatedChaosClient(gateway, sessionId);

    Iterable<Job> results = client.jobGet();
    assertTrue(results.iterator().hasNext());
    Job result = results.iterator().next();
    assertEquals("0123456789", result.id);
  }

  @Test(expected = InsufficientPermissionsException.class)
  public void set_GivenInvalidSession_Throw() throws IOException {
    String sessionGuid = "someguid";
    String response = "{\"Header\": {\"Duration\": 177.4577},\"Body\": {\"Count\": 0,\"TotalCount\": 0,\"Results\": []},\"Error\": {\"Fullname\": \"Chaos.Portal.Core.Exceptions.InsufficientPermissionsException\",\"Message\": \"\",\"InnerException\": null}}";
    MockGateway gateway = new MockGateway("v6/Heartbeat/Set?sessionGUID=" + sessionGuid + "&state={\"ConnectedAgents\":0,\"jobsInQueue\":0}", response);
    AuthenticatedChaosClient api = new AuthenticatedChaosClient(gateway, sessionGuid);

    api.set(new ClusterState());
  }

  @Test
  public void set_GivenValidSession_ReturnTrue() throws IOException {
    String sessionGuid = "someguid";
    String response = "{\"Header\": {\"Duration\": 177.4577},\"Body\": {\"Count\": 1,\"TotalCount\": 1,\"Results\": [{\"WasSucess\":true}]},\"Error\": {\"Fullname\": null,\"Message\": null,\"InnerException\": null}}";
    MockGateway gateway = new MockGateway("v6/Heartbeat/Set?sessionGUID=" + sessionGuid + "&state={\"ConnectedAgents\":0,\"jobsInQueue\":0}", response);
    AuthenticatedChaosClient api = new AuthenticatedChaosClient(gateway, sessionGuid);

    api.set(new ClusterState());
  }
}
