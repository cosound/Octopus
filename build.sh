rm -r -f build
mkdir build
mkdir build/orchestrator
mkdir build/orchestrator/lib
mkdir build/agent
mkdir build/agent/lib

cp README.md build/README
cp -R octopus-java/octopus-daemon/target/lib/* build/orchestrator/lib
cp -R octopus-java/octopus-daemon/target/octopus-daemon.jar build/orchestrator/
cp -R octopus-java/octopus-daemon/target/lib/* build/agent/lib
cp -R octopus-java/octopus-daemon/target/octopus-daemon.jar build/agent/
