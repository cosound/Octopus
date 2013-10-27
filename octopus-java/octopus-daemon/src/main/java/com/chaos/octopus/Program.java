package com.chaos.octopus;

import java.io.*;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.agent.plugin.CommandLinePlugin;
import com.chaos.octopus.commons.core.TestPlugin;
import com.chaos.octopus.server.*;
import com.google.gson.*;

public class Program 
{
	public static void main(String[] args) throws Exception 
	{
        int port = 20000;

        if(args.length == 1)
        {
            port = Integer.parseInt(args[0]);
        }

		System.out.println("Initializing Octopus...");
		
		try(OrchestratorImpl leader = new OrchestratorImpl(port);
		    Agent agent = new Agent("localhost", port, port +1))
		{
			agent.addPlugin(new TestPlugin());
            agent.addPlugin(new CommandLinePlugin());
			leader.open();
			agent.open();

			System.out.println("\tOrchestration leader chosen [localhost:" + port + "]");
			System.out.println("\tAgent connected [localhost:" + (port +1) + "]");
			System.out.println("Octopus initialized");

			// keyboard input loop
			while(true)
			{
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    String s = bufferRead.readLine();
				
			    String[] split = s.split(" ");


                if(split[0].equals("") || split[0].equals("help") && split.length == 1)
                {
                    System.out.println("The following commands are available:");
                    System.out.println("\thelp\t\tDisplay the help menu.");
                    System.out.println("\tload-job\tLoads a json formattet job file");
                    System.out.println("\texit");
                }
                else if(split[0].equals("help") && split[1].equals("load-job"))
		    	{
		    		System.out.println("\tload-job [/path/to/local/file]");
		    	}
			    else if(split[0].equals("load-job"))
			    {
			    	// load-job ..\doc\sample-job.json
			    	
			    	String filepath = split[1];
			    	
			    	File f = new File(filepath);
			    	
			    	if(!f.exists())
			    	{
			    		System.out.println("File doesn't exist");
			    		continue;
			    	}
			    	
			    	try(FileReader fr = new FileReader(filepath))
			    	{
			    		Gson gson = new Gson();
			    		
			    		Job job = gson.fromJson(fr, Job.class);

                        if(job.validate())
                            leader.enqueue(job);
                        else
                            System.out.println("The job file is invalid");
			    	}
			    }
			    else if(split[0].equals("exit"))
			    {
			    	break;
			    }
			}
			
			System.out.println("Octopus Shutting down...");
		}
		
		System.out.println("Octopus Shutdown!");
	}
}
