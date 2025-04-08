# mvn clean package
mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt


java -cp target/classes:target/test-classes:$(cat target/classpath.txt) org.junit.runner.JUnitCore pdp.scrabble.MainTest &
# java -cp target/classes:target/test-classes:$(cat target/classpath.txt) org.junit.runner.JUnitCore pdp.scrabble.dictionary.DictionaryTest &
# java -cp target/classes:target/test-classes:$(cat target/classpath.txt) org.junit.runner.JUnitCore pdp.scrabble.configuration.ConfigurationBuilderTest &

# Wait a few seconds for the test JVM to launch
sleep 0.1

# Find the right PID (e.g., the longest-running java process)
pid=$(jps | grep -v Jps | awk '{print $1}' | head -n 1)

/mnt/d/javaTools/async-profiler-3.0-linux-x64/bin/asprof -d 12 -f target/flamegraph.html "$pid"