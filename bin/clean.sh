#!/bin/bash
MY_PATH=`dirname "$0"`
rm ${MY_PATH}/src/main/java/apd67/lexer/XiLexer.java
rm ${MY_PATH}/src/main/java/apd67/lexer/XiLexer.java~
rm ${MY_PATH}/src/main/java/apd67/parser/parser.java
rm ${MY_PATH}/src/main/java/apd67/parser/sym.java
gradle clean
