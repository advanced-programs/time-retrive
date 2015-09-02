#!/bin/bash

javac time_Extraction/*.java
time java time_Extraction/main $1 $2
exit 0
