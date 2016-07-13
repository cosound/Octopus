package com.chaos.octopus;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.agent.plugin.ChaosPlugin;
import com.chaos.octopus.agent.plugin.CommandLinePlugin;
import com.chaos.octopus.commons.core.TestPlugin;
import com.chaos.octopus.commons.core.Job;
import com.chaos.octopus.server.OrchestratorImpl;
import com.google.gson.Gson;
import org.apache.commons.cli.*;

import java.io.*;

public class Program {
  public static void main(String[] args) throws IOException, ParseException {
    Options options = createOptions();

    CommandLineParser parser = new BasicParser();
    CommandLine cmd = parser.parse(options, args);

    if (!cmd.hasOption("d"))
      System.setOut(createPrintStreamNullObject());

    if (cmd.hasOption("h")) {
      HelpFormatter help = new HelpFormatter();
      help.printHelp("octopus-cli [OPTION]", options);
    }

    int port = Integer.parseInt(cmd.getOptionValue("p", "44000"));
    String orchestratorAddress = cmd.getOptionValue("oa", "localhost");

    if (cmd.hasOption("op")) {
      System.out.println("Starting Agent");
      int orchestratorport = Integer.parseInt(cmd.getOptionValue("op"));

      instanciateAgent(port, orchestratorAddress, orchestratorport);
    } else{
      System.out.println("Starting Orchestrator");
      instanciateOrcestrator(port);
    }



  }


  private static void instanciateAgent(int port, String orchestratorAddress, int orchestratorport) {
    try (Agent agent = new Agent(orchestratorAddress, orchestratorport, port)) {
      agent.addPlugin(new TestPlugin());
      agent.addPlugin(new CommandLinePlugin());
      agent.addPlugin(new ChaosPlugin());
      agent.open();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void instanciateOrcestrator(int port) {
    try (OrchestratorImpl leader = new OrchestratorImpl(port)) {
      leader.open();

      try {
        Thread.sleep(60000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static PrintStream createPrintStreamNullObject() {
    return new PrintStream(new OutputStream() {
      @Override
      public void write(int b) throws IOException {

      }
    });
  }

  private static Options createOptions() {
    Options options = new Options();
    options.addOption("p", "port", false, "Specify the listening port [44000]");
    options.addOption("oa", "orchestrator-address", false, "Specify the IP address/hostname of the Orchestrator");
    options.addOption("op", "orchestrator-port", false, "Specify the port of the Orchestrator");
    options.addOption("d", "debug", false, "Prints debug message to stdout");
    options.addOption("h", "help", false, "Print help");

    return options;
  }
}
