package com.chaos.octopus;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.agent.plugin.ChaosPlugin;
import com.chaos.octopus.agent.plugin.CommandLinePlugin;
import com.chaos.octopus.commons.core.TestPlugin;
import com.chaos.octopus.server.Job;
import com.chaos.octopus.server.OrchestratorImpl;
import com.google.gson.Gson;
import org.apache.commons.cli.*;

import java.io.*;

public class Program
{
    public static void main(String[] args) throws IOException
    {
        Options options = createOptions();

        CommandLineParser parser = new BasicParser();

        try
        {
            CommandLine cmd = parser.parse(options, args);

            if(!cmd.hasOption("d"))
            {
                System.setOut(createPrintStreamNullObject());
            }

            if(cmd.hasOption("h"))
            {
                HelpFormatter help = new HelpFormatter();
                help.printHelp("octopus-cli [OPTION]... [FILE]...", options);
            }

            executeJob(cmd);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    private static PrintStream createPrintStreamNullObject()
    {
        return new PrintStream(new OutputStream()
        {
            @Override
            public void write(int b) throws IOException
            {

            }
        });
    }

    private static void executeJob(CommandLine cmd) throws Exception
    {
        CommandLineFileHelper fileHelper = new CommandLineFileHelper(cmd);

        try(OrchestratorImpl leader = new OrchestratorImpl(56541);
            Agent agent = new Agent("localhost", 56541, 56541 +1))
        {
            agent.addPlugin(new TestPlugin());
            agent.addPlugin(new CommandLinePlugin());
            agent.addPlugin(new ChaosPlugin());
            leader.open();
            agent.open();

            try(BufferedReader reader = fileHelper.getStream())
            {
                Gson gson = new Gson();

                Job job = gson.fromJson(reader, Job.class);

                if(job.validate())
                {
                    leader.enqueue(job);
                    waitForJobToComplete(job);
                }
                else
                    System.err.println("The job format is not valid, ensure all steps contain tasks");
            }
        }
    }

    private static void waitForJobToComplete(Job job) throws InterruptedException
    {
        while(!job.isComplete())
        {
            Thread.sleep(10);
        }
    }

    private static Options createOptions()
    {
        Options options = new Options();
        options.addOption("d", "debug", false, "Prints debug messages to stdout");
        options.addOption("h", "help", false, "Print help");

        return options;
    }
}
