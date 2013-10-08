package com.chaos.octopus;

import java.io.*;

import com.chaos.octopus.agent.Agent;
import com.chaos.octopus.core.TestPlugin;
import com.chaos.octopus.server.*;
import com.google.gson.*;

public class Program 
{

	public static void main(String[] args) throws Exception 
	{
		System.out.println("Initializing Octopus...");
		
		try(OrchestratorImpl leader = new OrchestratorImpl(20000);
		    Agent agent = new Agent("localhost", 20000, 20001))
		{
			agent.addPlugin(new TestPlugin());
			leader.open();
			agent.open();
			
			System.out.println("\tOrchestration leader chosen [localhost:20000]");
			System.out.println("\tAgent connected [localhost:20001]");
			System.out.println("Octopus initialized");
			System.out.println(System.getProperty("user.dir"));
			// keyboard input loop
			while(true)
			{
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    String s = bufferRead.readLine();
				
			    String[] split = s.split(" ");

			    if(split[0].equals("") || split[0].equals("help"))
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
			    	// load-job ..\..\doc\sample-job.json
			    	
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
			    		
			    		Job job = gson.fromJson(fr, new Job().getClass());
			    		
			    		leader.enqueue(job);
			    		String json = gson.toJson(job);
						System.out.println(json);
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
