#!/bin/bash

if [ ! -f "target/scrabble-1.0-SNAPSHOT.jar" ]; then
    mvn package

    if [! -f "target/scrabble-1.0-SNAPSHOT.jar"]; then
        echo "Error: JAR file not found"
        exit 1
    fi
fi

java -cp target/scrabble-1.0-SNAPSHOT.jar pdp.scrabble.Main "$@"
