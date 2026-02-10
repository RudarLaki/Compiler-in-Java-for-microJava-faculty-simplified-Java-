"# Compiler-in-Java-for-microJava-faculty-simplified-Java-" 

# MicroJava Compiler

This project is an implementation of a compiler for the **MicroJava** programming language.
The compiler is developed as part of a university course and follows the standard compilation pipeline.

The implementation is divided into multiple phases, each responsible for a specific part of the compilation process.

---

## Project Structure

The compiler is organized into the following phases:

1. **Lexical Analysis**
2. **Syntax Analysis**
3. **Semantic Analysis**
4. **Code Generation** *(not included in this version)*

---

## Phase 1: Lexical Analysis

The lexical analyzer (scanner) reads the source code and converts it into a sequence of tokens.

### Responsibilities

* Recognizing keywords, identifiers, constants, and operators.
* Ignoring whitespace and comments.
* Reporting lexical errors.

### Tools

* Implemented using **JFlex**.

---

## Phase 2: Syntax Analysis

The syntax analyzer (parser) checks whether the token sequence follows the language grammar.

### Responsibilities

* Building the Abstract Syntax Tree (AST).
* Detecting syntax errors.
* Providing structured program representation for later phases.

### Tools

* Implemented using **Java CUP**.

---

## Phase 3: Semantic Analysis

The semantic analyzer performs checks that cannot be expressed through grammar alone.

### Responsibilities

* Symbol table management.
* Type checking.
* Scope resolution.
* Validation of:

  * Variable and constant declarations
  * Method declarations and calls
  * Expressions and assignments
  * Control flow statements

### Output

* Decorated AST with semantic information.
* Error reports for semantic violations.

---

## Phase 4: Code Generation

*This phase is not included in the current version of the project.*

Planned responsibilities:

* Generating bytecode for the MicroJava virtual machine.
* Translating AST nodes into executable instructions.

---

## Technologies Used

* **Java**
* **JFlex** – lexical analyzer generator
* **Java CUP** – parser generator
* **MicroJava runtime and symbol table library**

---

## How to Build

1. Generate the lexer:

```bash
jflex spec/mjlexer.flex
```

2. Generate the parser:

```bash
java -jar lib/java-cup.jar -parser MJParser -symbols sym spec/mjparser.cup
```

3. Compile the project:

```bash
javac -cp "lib/*" -d bin src/**/*.java
```

---

## How to Run

```bash
java -cp "bin;lib/*" rs.ac.bg.etf.pp1.Compiler input.mj
```

---

## Example

Input program:

```mj
program Example {
    int x;

    void main() {
        x = 5;
        print(x);
    }
}
```

---

## Author

**Lazar Rudinac**
Software Engineering Student
University of Belgrade – School of Electrical Engineering
