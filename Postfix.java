
/**
 *  Class to interpret and compute the result of arithmetic expressions 
 *  in POSTFIX format
 */

import java.util.Scanner;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.Stack;

public class Postfix {
  /** Pattern that matches on words */
  public static final String WORD = "[a-zA-Z]*\\b";

  /** Pattern that matches on arithmetic symbols */
  public static final String SYMBOL = "[^\\w]";

  /** Pattern that matches on arithmetic symbols */
  public static final String NUMBER = "-?\\d+(\\.\\d+)?";

  /**
   * Main method to calculate based on postfix format
   * 
   * @param args String array with command line argument
   */
  public static void main(String[] args) {

    // An ArrayDeque Object that contains the tokens
    Queue<Object> queueInput = new ArrayDeque<Object>();

    if (args.length == 0) {
      // If no arguments passed, print instructions
      System.err.println("Usage:  java Postfix <expr>");
    } else {
      // Otherwise, echo what was read in for now
      Scanner scanner = new Scanner(new StringReader(args[0]));

      scanner.useDelimiter("(\\s+" // whitespace
          + "|(?<=[a-zA-Z])(?=[^a-zA-Z])" // word->non-word
          + "|(?<=[^a-zA-Z])(?=[a-zA-Z])" // non-word->word
          + "|(?<=[^0-9\\056])(?=[0-9\\056])" // non-number->number
          + "|(?<=[0-9\\056])(?=[^0-9\\056])" // number->non-number
          + "|(?<=[^\\w])(?=[^\\w]))"); // symbol->symbol

      // read in input
      while (scanner.hasNext()) {
        if (scanner.hasNextDouble()) {
          queueInput.add(scanner.nextDouble());
        } else if (scanner.hasNext(WORD)) {
          queueInput.add(scanner.next(WORD));
        } else if (scanner.hasNext(SYMBOL)) {
          queueInput.add(scanner.next(SYMBOL).charAt(0)); // add as Character
        } else {
          queueInput.add(scanner.next());
        }
      }
      processPostfix(queueInput);
    }
  }

  /**
   * Method that reads tokens from the queue and does calculations based on
   * postfix format
   *
   * @param queueInput a Queue object that contains the tokens
   */
  public static void processPostfix(Queue<Object> queueInput) {
    Deque<Object> stackPostfix = new ArrayDeque<Object>();

    int queueSize = queueInput.size();
    for (int i = 0; i < queueSize; i++) {
      Object token = queueInput.remove();
      // token is a number
      if (token instanceof Double) {
        stackPostfix.add(((Double) token).doubleValue());
        // token is an operator
      } else if (token instanceof Character) {
        if (stackPostfix.size() < 2) {
          throw new IllegalArgumentException("Malformed expression");
        }
        double num1 = ((Double) stackPostfix.removeLast()).doubleValue();
        double num2 = ((Double) stackPostfix.removeLast()).doubleValue();

        // token is + sign
        if ((Character) token == '+') {
          stackPostfix.add(num1 + num2);
          // token is - sign
        } else if ((Character) token == '-') {
          stackPostfix.add(num2 - num1);
          // token is * sign
        } else if ((Character) token == '*') {
          stackPostfix.add(num1 * num2);
          // token is / sign
        } else if ((Character) token == '/') {
          stackPostfix.add(num2 / num1);
          // token is ^ sign
        } else if ((Character) token == '^') {
          stackPostfix.add(Math.pow(num2, num1));
        }
      }
    }
    if (stackPostfix.size() > 1) {
      throw new IllegalArgumentException("Malformed expression");
    }

    System.out.println("Answer: " + stackPostfix.peek());
  }
}
