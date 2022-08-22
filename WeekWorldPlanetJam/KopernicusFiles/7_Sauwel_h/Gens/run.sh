#!/bin/bash
set -e -o xtrace

javac -classpath ".:./ProceduralPlanet.jar:./jocl-2.0.4.jar:./DDSUtils.jar:./jsquish.jar:.;" tholin/wwpj/Gens.java
java -Xmx8G -classpath ".:./ProceduralPlanet.jar:./jocl-2.0.4.jar:./DDSUtils.jar:./jsquish.jar:.;" tholin.wwpj.Gens
