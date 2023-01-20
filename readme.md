# Postfix/Infix Calculator 

> This project uses an ArrayDeque as a stack to compute the result of expressions written in both postfix notation and infix notation by implementing the Shunting-yard algorithm.

<a name="toc"/></a>
## Table of Contents

1. [Overview](#overview)

2. [Technologies](#technologies)

3. [Launch](#launch)

<a name="overview"/></a>
## 1. Overview
[Back to ToC](#toc)

This project uses an ArrayDeque as a stack to compute the result of expressions written in both postfix notation (e.g. 3 2 + 5 *) and infix notation (e.g. (3+2) * 5) by implementing the Shunting-yard algorithm.

When reading the expression from the command line arguments, it is converted into a series of discrete tokens using a Scanner that reads its input from a StringReader, the tokens will be put into a queue to be operated on one at a time.

The postfix expressions will be computed using the following algorithm:

* Take a token one at a time from the queue and process it as follows:
  - If the token is a number, push(...) it onto the stack
  - If the token is an operator, pop() two numbers off the stack, combine them using the operator, and push(...) the result back onto the stack
* Once all the tokens have been processed in this way, the stack should contain exactly one element: the result.
* If the stack runs out of elements needed for processing an operator, or if there are too many items on the stack at the end, then the starting expression was malformed and    an exception will be thrown to report the error.

The infix expressions will be computed using a simplified version (ignoring operator associativity) of the full shunting yard algorithm that utilizes two instances of type ArrayDeque<Object>: one to use as the stack, and another to use as the queue.

* While there are tokens to be read:
   - Read a token.
   - If the token is a number, then add it to the output queue.
   - If the token is an operator (the "queue operator") then:
       + while there is an operator token at the top of the stack (the "stack operator"), and the stack operator has greater precedence than the queue operator, pop the stack operator off the stack and add it to the output queue;
       + When no more high-precedence stack operators remain, finally push the queue operator onto the stack.
   - If the token is a left parenthesis, then push it onto the stack.
   - If the token is a right parenthesis:
       + Until the token at the top of the stack is a left parenthesis, pop operators off the stack onto the output queue.
       + Pop the left parenthesis from the stack, but not onto the output queue.
       + If the stack runs out without finding a left parenthesis, then there are mismatched parentheses.
* When there are no more tokens to read:
    - While there are still tokens in the stack:
        + If the token on the top of the stack is a parenthesis, then there are mismatched parentheses.
        + If it is an operator, pop it onto the output queue.
* Exit.
 
<a name="technologies"/></a>
## 2. Technologies
[Back to ToC](#toc)

java version "1.8.0_181"<br />
Java(TM) SE Runtime Environment (build 1.8.0_181-b13)<br />
Java HotSpot(TM) 64-Bit Server VM (build 25.181-b13, mixed mode)<br />

<a name="launch"/></a>
## 3. Launch
[Back to ToC](#toc)
```bash
javac -classpath .:target/dependency/* -d . $(find . -type f -name '*.java')

java Postfix <postfix-expr>

java Calculate <infix-expr>
```