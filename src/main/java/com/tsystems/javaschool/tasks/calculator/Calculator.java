package com.tsystems.javaschool.tasks.calculator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {

        if (stringEmpty(statement) || !isValidBrackets(statement) || !isValidSymbols(statement)) { // Check String empty, correct brackets, correct symbols
            return null;
        }

        List<String> infixExpressionList = toInfixExpressionList(statement.replaceAll(" ", "")); // Return list all values without spaces

        if (!isValidOperator(infixExpressionList)) { // Checking for the absence of double functions (//, *) *, * /, etc.) without noticing parentheses
            return null;
        }

        List<String> suffixExpressionList = ShuntingYard(infixExpressionList); // Return suffix expression(RPN) with shunting yard

        return calculateRPN(suffixExpressionList); // calculate Revers Poland Notation
    }

    public static List<String> toInfixExpressionList(String s){
        List<String> list = new ArrayList<>(); // Storing all numbers and functions
        int i = 0; // Iterating over characters
        char c;
        StringBuilder str; // Joining multiple numbers

        do {
            if (!isNumberOrDot(c = s.charAt(i))) { // If c is not a number, add it to list
                list.add("" + c);
                i++;
            } else { // If this is a number, it can be large
                str = new StringBuilder();
                while (isNumberOrDot(c = s.charAt(i))) { // While these are numbers, stick them
                    str.append(c);
                    i ++;
                    if (i >= s.length()) {
                        break;
                    }
                }
                list.add(str.toString()); // Add ready number
            }
        } while (i < s.length());
        return list;
    }

    public static List<String> ShuntingYard(List<String> infix){
        List<String> output = new ArrayList<>(); // result list
        Deque<String> stack  = new LinkedList<>(); // temp storage for brackets expressions

        for (String item : infix) {
            if (ops.containsKey(item)) { // if item is operator
                while ( ! stack.isEmpty() && isHigherPre(item, stack.peek())) { // if stack is not empty and item higher priority of top item stack
                    output.add(stack.pop()); // list add top item stack
                }
                stack.push(item); // last add operator item
            } else if (item.equals("(")) {
                stack.push(item);
            } else if (item.equals(")")) {
                while ( !(stack.peek() != null && stack.peek().equals("("))) {
                    output.add(stack.pop()); // write to list full brackets expressions
                }
                stack.pop();
            } else {
                output.add(item); // write numbers
            }
        }

        while ( ! stack.isEmpty()) {
            output.add(stack.pop()); // stack to list
        }

        return output;
    }

    private enum Operator { // enum operators with precedence
        ADD(1),
        SUBTRACT(2),
        MULTIPLY(3),
        DIVIDE(4);
        final int precedence;
        Operator(int p) { precedence = p; }
    }

    private static final Map<String, Operator> ops = new HashMap<String, Operator>() {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
    }};

    private static boolean isHigherPre(String op, String sub) {
        return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(op).precedence);
    }

    private String calculateRPN(List<String> suffixEx) {
        Deque<Double> deque = new LinkedList<>();
        for (String s : suffixEx) {
            if (ops.containsKey(s)) {
                double right = deque.removeFirst();
                double left = deque.removeFirst();
                if (s.equals("*")) {
                    deque.addFirst(left * right);
                }
                if (s.equals("/")) {
                    if (right == 0) { // divide for zero
                        return null;
                    }
                    deque.addFirst(left / right);
                }
                if (s.equals("+")) {
                    deque.addFirst(left + right);
                }
                if (s.equals("-")) {
                    deque.addFirst(left - right);
                }
            } else {
                deque.addFirst(Double.valueOf(s));
            }
        }
        if (!deque.isEmpty()) { // Null Pointer Exception
            DecimalFormat df = new DecimalFormat("0.####", DecimalFormatSymbols.getInstance(Locale.ENGLISH)); // formatting
            df.setMaximumFractionDigits(4);
            return df.format(deque.peek());
        } else {
            return null;
        }
    }

    public static boolean isNumberOrDot(char c) {
        return (c >= '0' && c <= '9') || c == '.';
    }

    private boolean stringEmpty(String statement) {
        return statement == null || statement.equals("");
    }

    private boolean isValidSymbols(String statement) {
        boolean doubleDot = false; // check for double dot
        for (int i = 0; i < statement.length() - 1; i++) {
            if (statement.charAt(i) == '.' && statement.charAt(i + 1) == '.') {
                doubleDot = true;
                break;
            }
        }
        return statement.matches("^[\\d+/*.\\- ()]*$") && !doubleDot; // valid only 1234567890 + / * . - ( ) symbols && check dont have double dot
    }

    public static boolean isValidBrackets(String string) { // After each opening brackets, there must be a closing
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '(')
                stack.push('(');
            if (string.charAt(i) == ')')
                if(stack.empty() || stack.pop() != '(')
                    return false;
        }
        return stack.empty();
    }

    private boolean isValidOperator(List<String> infixExpressionList) {
        if (ops.containsKey(infixExpressionList.get(0))) { // First item dont be a operator (+ - * /)
            return false;
        }

        boolean beforeIsOperator = false;
        for (String s : infixExpressionList) { // check for double operator ignoring brackets
            if (ops.containsKey(s)) {
                if (beforeIsOperator) {
                    return false;
                } else {
                    beforeIsOperator = true;
                }
            } else if (!(s.equals("(") || s.equals(")"))){
                beforeIsOperator = false;
            }
        }
        return true;
    }
}