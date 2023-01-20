/**
 *  Class to interpret and compute the result of arithmetic expressions in 
 *  INFIX format
 */

import java.util.Scanner;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;

public class Calculate {

  /** Pattern that matches on words */
  public static final String WORD = "[a-zA-Z]*\\b";

  /** Pattern that matches on arithmetic symbols */
  public static final String SYMBOL = "[^\\w]";

  /** Pattern that matches on arithmetic symbols */
  public static final String NUMBER = "-?\\d+(\\.\\d+)?";

  /** 
   *  Main method to calculate based on infix format              
   *  @param args      String array with command line argument
   */
  public static void main(String[] args) {

    // instances of ArrayDeque to use as the queue to store 
    // converted postfix expressions
    Queue<Object> queueInfix = new ArrayDeque<Object>();

    // input queue with tokens
    Queue<Object> queueInput = new ArrayDeque<Object>();

    // instances of ArrayDeque to use as the stack to store operators
    Deque<Object> stackInfix = new ArrayDeque<Object>();

    if (args.length == 0) {
      // If no arguments passed, print instructions
      System.err.println("Usage:  java Calculate <expr>");
    } else {

      Scanner input = new Scanner(new StringReader(args[0]));

      input.useDelimiter("(\\s+" // whitespace
          + "|(?<=[a-zA-Z])(?=[^a-zA-Z])" // word->non-word
          + "|(?<=[^a-zA-Z])(?=[a-zA-Z])" // non-word->word
          + "|(?<=[^0-9\\056])(?=[0-9\\056])" // non-number->number
          + "|(?<=[0-9\\056])(?=[^0-9\\056])" // number->non-number
          + "|(?<=[^\\w])(?=[^\\w]))"); // symbol->symbol 

      // read in input
      while (input.hasNext()) {
        if (input.hasNextDouble()) {
          queueInput.add(input.nextDouble());
        } else if (input.hasNext(WORD)) {
          queueInput.add(input.next(WORD));
        } else if (input.hasNext(SYMBOL)) {
          queueInput.add(input.next(SYMBOL).charAt(0)); // add as Character
        } else {
          queueInput.add(input.next());
        }
      }

      // check if an postfix expression is entered
      Object lastQueueElement=' ';
      for(Object element : queueInput) { 
        lastQueueElement=element;
      }

      if (lastQueueElement instanceof Character &&
          ((Character)lastQueueElement == '+' || (Character)lastQueueElement == '-' ||  (Character)lastQueueElement == '*' || (Character)lastQueueElement == '/' || (Character)lastQueueElement == '^')) {
        throw new IllegalArgumentException("Malformed expression");
      }

      // add ')' at the end
      queueInput.add(')');
      int queueSize = queueInput.size();

      // push '(' to the stack
      stackInfix.add('(');
      
      for (int i = 0; i < queueSize; i++) {
        Object token = queueInput.remove(); 

        // token is a number
        if (token instanceof Double) {
          queueInfix.add(token);
        // token is an operator or parentheses
        } else if (token instanceof Character) { 
          // token is a + sign or - sign
          if ((Character) token == '+' || (Character) token == '-') {
            // token is at the beginning of the stack
            if (stackInfix.size() == 0) {
              stackInfix.add(token);
            } else {
              Character tokenInStack = (Character)stackInfix.peekLast();
              while (tokenInStack== '-' || tokenInStack == '+' || tokenInStack == '/' || tokenInStack == '*' || tokenInStack == '^'){
                queueInfix.add(tokenInStack);
                stackInfix.remove(tokenInStack);
                tokenInStack = (Character)stackInfix.peekLast();
              }
              stackInfix.add(token);
            }
            // token is a * sign or / sign
          } else if ((Character) token == '*' || (Character) token == '/') {
            // token is at the beginning of the stack
            if (stackInfix.size() == 0) {
              stackInfix.add(token);
            } else {
              Character tokenInStack = (Character)stackInfix.peekLast();
              while (tokenInStack == '/' || tokenInStack == '*' || tokenInStack == '^'){
                queueInfix.add(tokenInStack);
                stackInfix.remove(tokenInStack);
                tokenInStack = (Character)stackInfix.peekLast();
              }
              stackInfix.add(token);
            }
          // token is ^ sign
          } else if ((Character) token == '^') { 
              stackInfix.add(token);
          // token is (
          } else if ((Character)token == '(') {
            stackInfix.add(token);
          // token is )
          } else if((Character)token == ')') {
              Character tokenInStack = (Character)stackInfix.peekLast();
              while (tokenInStack != '(') {
                queueInfix.add(tokenInStack);
                stackInfix.remove(tokenInStack);
                tokenInStack = (Character)stackInfix.peekLast();
              }
          }
        }
      }
    }
    Postfix.processPostfix(queueInfix);
  }
}
