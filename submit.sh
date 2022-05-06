#!/bin/bash
git log -oneline > pa6.log
./clean.sh
zip -r compiler.zip src benchmark deps build.gradle settings.gradle xic xic-build \
   -x \*XiLexer~.java \*XiLexer.java \*.class \*.results \*parser.java \*sym.java \*/*_given/*

