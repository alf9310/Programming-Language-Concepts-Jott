## Authors:
- Kaiy Muhammad     kkm4289@rit.edu
- Sejal Bhattad     sab2713@rit.edu
- Lauren Kaestle    lk2958@rit.edu
- Audrey Fuller     alf9310@rit.edu

## Build Instructions
In the Programming-Language-Language-Concepts-Jott directory, run
```
java -XX:+ShowCodeDetailsInExceptionMessages -cp /home/stu15/s0/lk2958/Courses/CSCI344/Programming-Language-Concepts-Jott/bin msc.Jott [jott_file_to_run]
```
## Project Overview
CSCI.344 - Programming Language Concepts Fall 2024
This is an interpreter for the programming language Jott using Java. 

There are four distinct phases in the program; scanning (tokenizing), parsing (build parse tree), semantic analysis (building an AST), and execution.

### Phase 1: Tokenizer
Defined by the JottTokenizer class. Will take in the absolute/relative path of the file to parse. It will return an ArrayList of tokens.
### Phase 2: Parser
Defined by the JottParser class. Will take in an ArrayList of tokens created by a JottTokenizer and return the root of the tree represented by those tokens.
### Phase 3: Semantic Analysis /AST
Defined by the validateTree function in Jott.java. Will determine if the parse tree follows all the required semantic rules.
### Phase 4: Interpretation
Defined by the execute function in Jott.java. Interprets the code.

## Library Structure
- `src`: contains the working code
    - `nodes`       - Contains the implementations of token parsing, semantic analysis and execusion
    - `provided`    - Where the helper classes are defined for interpreting the Jott Language
    - `testers`     - Testers for the various phases of the project
- `test-cases`: contains the test inputs and outputs, as used by the testers
- `bin`: contains the compiled output files