/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.sdk;

import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.commons.exception.InsufficientPermissionsException;
import com.chaos.sdk.model.McmObject;
import com.chaos.sdk.v6.dto.ClusterState;
import com.chaos.sdk.v6.dto.PortalResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AuthenticatedChaosClient implements HeartbeatGateway{
  private ChaosGateway gateway;
  private String sessionId;
  private final Gson gson;

  public AuthenticatedChaosClient(ChaosGateway gateway, String sessionId) {
    this.gateway = gateway;
    this.sessionId = sessionId;
    gson = new Gson();
  }

  public McmObject objectCreate(String guid, int objectTypeId, int folderId) throws IOException {
    PortalResponse session = gateway.call("GET", "v6/Object/Create", "sessionGUID=" + sessionId + "&objectTypeID=" + objectTypeId + "&folderID=" + folderId);

    return new McmObject(session.Body.getResults().get(0).get("Guid").toString());
  }

  public int metadataSet(String objectGuid, String metadataSchemaGuid, String languageCode, String revisionID, String metadataXml) throws IOException {
    PortalResponse response = gateway.call("POST", "v6/Metadata/Set", "sessionGUID=" + sessionId + "&objectGuid=" + objectGuid + "&metadataSchemaGuid=" + metadataSchemaGuid + "&languageCode=" + languageCode + "&revisionID=" + revisionID + "&metadataXml=" + metadataXml);

    Double value = Double.parseDouble(response.Body.getResults().get(0).get("Value").toString());
    return value.intValue();
  }

  public Iterable<Job> jobGet() throws IOException {
    PortalResponse response = gateway.call("GET", "v6/Job/GetIncomplete", "sessionGUID=" + sessionId);
    ArrayList<Job> jobs = new ArrayList<>();

    for (HashMap<String, Object> job : response.Body.getResults()) {
      String id = job.get("id").toString();
      String status = job.get("status").toString();
      String data = job.get("data").toString();

      Job res = gson.fromJson(data, Job.class);
      res.id = id;
      res.status = status;

      jobs.add(res);
    }

    return jobs;
  }

  public void jobSet(Iterable<Job> jobs) throws IOException {
    for (Job job : jobs) {
      jobSet(job);
    }
  }

  public void jobSet(Job job) throws IOException {
    if (job.validate())
      job.status = job.isComplete() ? "complete" : "inprogress";
    else
      job.status = "Invalid Job";

    String data = gson.toJson(job);
    PortalResponse response = gateway.call("POST", "v6/Job/Set", "sessionGUID=" + sessionId + "&data=" + data);
  }

  public void set(ClusterState heartbeat) {
    try {
      String json = gson.toJson(heartbeat);
      PortalResponse response = gateway.call("POST", "v6/Heartbeat/Set", "sessionGUID=" + sessionId + "&state=" + json);

      if("Chaos.Portal.Core.Exceptions.InsufficientPermissionsException".equals(response.Error.Fullname))
        throw new InsufficientPermissionsException();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
