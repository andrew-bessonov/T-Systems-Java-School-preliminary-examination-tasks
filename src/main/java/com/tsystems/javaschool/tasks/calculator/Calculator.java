package com.tsystems.javaschool.tasks.calculator;

import java.math.BigDecimal;
import java.util.Stack;

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
        Stack stack = new Stack();
        Stack stack1 = new Stack();
        boolean key = false;

        if (!pi(statement)) {
            System.out.println("Brackets do not match");
            return null;
        }

        for (int i = 0; i < statement.length(); i++) {
            char c = statement.charAt(i);
            while (!isfu(c) && !isNumber(c)) {
                ++i;
                if(i < statement.length())
                    c = statement.charAt(i);
                if( i == statement.length())
                    break;
            }
            if ( i == statement.length())
                break;
            StringBuilder SB = new StringBuilder("");
            if (c == '-' && key) {
                ++i;
                if(i < statement.length())
                    c = statement.charAt(i);
                SB.append('-');
            }
            while (isNumber(c) || c == '.') {
                key = false;
                SB.append(c);
                if (++i >= statement.length()) break;
                c = statement.charAt(i);
            }
            if (!SB.toString().equals("")){
                stack.push(new BigDecimal(SB.toString()));

            }
            if (c == '^') {
                if ((!stack1.empty()))
                    if ( ((char)stack1.peek() == '^'))
                    {
                        stack.push(stack1.pop());
                    }
            }
            if (c == '*' || c == '/' || c == '+' || c == '-') {
                if ((!stack1.empty()))
                    if ( ((char)stack1.peek() == '*') || ((char)stack1.peek() == '/') || ((char)stack1.peek() == '^')) {
                        stack.push(stack1.pop());
                    }
            }
            if ( c == '+' || c == '-') {
                if ((!stack1.empty()))
                    if ( ((char)stack1.peek() == '*') || ((char)stack1.peek() == '/') || ((char)stack1.peek() == '+') || ((char)stack1.peek() == '-') || ((char)stack1.peek() == '^')) {
                        stack.push(stack1.pop());
                    }
            }
            if (isfu(c)) {
                stack1.push(c);
                key = true;
            }
            if (c == ')') {
                char cha = c;
                while (cha != '(') {
                    cha = (char) stack1.pop();
                    if (cha != '(' && cha != ')')
                        stack.push(cha);
                }
            }
        }
        while (!stack1.empty()) {
            stack.push(stack1.pop());
        }
        Stack stack2 = new Stack<>();

        while (!stack.empty()) {
            stack2.push(stack.pop());
        }
        while (!stack2.empty()) {
            Object c = stack2.pop();
            if (c.getClass() == BigDecimal.class) {
                stack1.push(c);
                continue;
            }
            else {
                if ((char) c == '*' && (stack1.size() != 1))
                    stack1.push( ( (BigDecimal)stack1.pop() ).multiply( (BigDecimal) stack1.pop() ) );
                if ((char) c == '^' && (stack1.size() != 1)) {
                    BigDecimal a = (BigDecimal) stack1.pop();
                    if(a.intValue() >= 1)
                        stack1.push(((BigDecimal) stack1.pop()).pow(a.intValue()));
                    else {
                        stack1.push(new BigDecimal(Math.pow(((BigDecimal) (stack1.pop())).doubleValue(), a.doubleValue())));
                    }
                }
                if ((char) c == '/' && (stack1.size() != 1)) {
                    Double a = ((BigDecimal) stack1.pop()).doubleValue();
                    stack1.push (new BigDecimal( ( (Double) ( ( (BigDecimal)stack1.pop() ).doubleValue() / a)).toString()));
                }
                if ((char) c == '+' && (stack1.size() != 1))
                    stack1.push(( (BigDecimal)stack1.pop() ).add( (BigDecimal) stack1.pop() ));
                if ((char) c == '-') {
                    BigDecimal a = (BigDecimal) stack1.pop();
                    if (!stack1.empty() && stack1.peek().getClass() != char.class ) {
                        stack1.push(((BigDecimal) stack1.pop()).subtract(a));
                    }
                    else
                        stack1.push((a).multiply(new BigDecimal(-1)));
                }
            }
        }

        BigDecimal result = (BigDecimal) stack1.pop();
        return (result.doubleValue() % 1 == 0) ? String.format("%s", result) : String.format("%.6s", result);
    }

    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isfu(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == '^';
    }

    public static boolean pi(String string) {
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
}

