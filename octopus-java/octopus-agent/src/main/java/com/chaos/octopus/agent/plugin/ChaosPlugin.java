/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.agent.plugin;

import com.chaos.octopus.commons.core.Plugin;
import com.chaos.octopus.commons.core.PluginDefinition;
import com.chaos.octopus.commons.core.Task;
import com.chaos.sdk.AuthenticatedChaosClient;
import com.chaos.sdk.Chaos;
import com.chaos.sdk.model.McmObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ChaosPlugin implements Plugin, PluginDefinition {
  private Task _Task;

  @Override
  public String getId() {
    return "com.chaos.octopus.plugins.ChaosPlugin, 1.0.0";
  }

  public ChaosPlugin() {
  }

  public ChaosPlugin(Task task) {
    setTask(task);
  }

  @Override
  public Plugin create(Task data) {
    return new ChaosPlugin(data);
  }

  @Override
  public Task getTask() {
    return _Task;
  }

  private void setTask(Task task) {
    _Task = task;
  }

  @Override
  public void execute() throws Exception {
    // TODO once more actions are implemented, replace with Strategy pattern
    if ("object.createFromJson".equals(getAction())) {
      String metadata = getInoutXmlContent();

      Chaos api = new Chaos(getChaosLocation());
      AuthenticatedChaosClient client = api.authenticate(getApiKey());

      getTask().progress = 0.3;
      McmObject obj = client.objectCreate(null, getObjectTypeId(), getFolderId());
      getTask().progress = 0.6;
      int result = client.metadataSet(obj.getId(), getMetadataSchemaId(), "en", "0", metadata);
      System.out.println(result);
      getTask().progress = 1.0;
    }
  }

  private String getInoutXmlContent() throws IllegalAccessException, IOException {
    File f = new File(getInputXml());
    StringBuffer result = new StringBuffer();

    if (!f.exists()) throw new IllegalAccessException("xml path is incorrect");

    try (FileReader fr = new FileReader(getInputXml())) {
      try (BufferedReader in = new BufferedReader(fr)) {
        String inputLine;

        while ((inputLine = in.readLine()) != null)
          result.append(inputLine);
      }
    }

    return result.toString();
  }

  @Override
  public void rollback() {
  }

  @Override
  public void commit() {
  }

  public String getAction() {
    return getTask().properties.get("action");
  }

  public String getInputXml() {
    return getTask().properties.get("input-xmlfilepath");
  }

  public String getChaosLocation() {
    return getTask().properties.get("chaos-location");
  }

  public String getApiKey() {
    return getTask().properties.get("chaos-apikey");
  }

  public int getObjectTypeId() {
    return Integer.parseInt(getTask().properties.get("chaos-objecttypeid"));
  }

  public int getFolderId() {
    return Integer.parseInt(getTask().properties.get("chaos-folderid"));
  }

  public String getMetadataSchemaId() {
    return getTask().properties.get("chaos-metadataschemaid");
  }
}
