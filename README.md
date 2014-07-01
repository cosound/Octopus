Octopus
=======
Requirements:
	Oracle Java 1.7+ or equivalent openjdk
	jsvc

NB: Not all versions of jsvc support relative paths, so make sure all paths are absolute.
	
In the orchestrator/octopus.properties file, make sure the following is defined:

	The port the orchestrator should be listening on
	# listening.port = <PORT> 

	The URI to the CHAOS Api
	# orchestrator.chaosApiUrl = <URI>

	The CHAOS authentication key the orchestrator should use
	# orchestrator.chaosApiKey = <KEY>

	In the orchestrator/octopus (and agent/octopus) file, make sure the following is defined:

	The absolute path to the jsvc binary (/usr/bin/jsvc).
	# EXEC = <PATH_TO_JSVC>

	The absolute path to the java binary (/usr/java/default).
	# JAVA_HOME = <PATH_TO_JAVA>

	The absolute path to the octopus folder.
	# BASE_PATH = <PATH_TO_OCTOPUS>

In the agent/octopus.properties file, make sure the following is defined:

	The port the agent should be listening on. If running on the same server as the orchestrator 
	make sure the port is different than the orchestrators
	# listening.port = <PORT> 

	The ip or hostname of the orchestrator.
	# orchestrator.ip = <IP_OF_ORCHESTRATOR>

	The port the orchestrator is listening on.
	# orchestrator.port = <PORT>

	The maximum number of concurrent tasks that should be run. A good starting value is the number of cores (x2 for hyper-threading).
	# agent.numberOfParallelTasks = <MAX_NUMBER_OF_CONCURRENT_TASKS>
	
	For the agent/octopus, repeat the same steps as with the orchestrator/octopus

Octopus can be started by running:
	# ./octopus start
and stopped by running 	
	# ./octopus stop
	
	Make sure the orchestrator is started first.
	
Any errors will be written to:
	$BASE_PATH/$USER.$HOST.orchestrator.stderr.log
	$BASE_PATH/$USER.$HOST.agent.stderr.log

