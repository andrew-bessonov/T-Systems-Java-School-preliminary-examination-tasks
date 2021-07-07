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

        if (!pi(statement)) { // Check brackets
            System.out.println("Brackets do not match");
            return null;
        }

        // Циклом по всей строке
        for (int i = 0; i < statement.length(); i++) {

            // Ищем число или функцию
            char c = statement.charAt(i); // Берем один символ (С)
            while (!isFu(c) && !isNumber(c)) { // Пока это не функция и не число
                if (++i < statement.length()) // Если строка еще не закончилась
                    c = statement.charAt(i); // Заменяем этот символ (С)
                if (i == statement.length()) // Если это был последний символ
                    break; // Остановить цикл
            }

            if (i == statement.length()) // Если это был последний символ
                break; // Остановить цикл

            StringBuilder SB = new StringBuilder("");

            if (c == '-' && key) { // Если символ '-' и key
                if (++i < statement.length()) // Если строка еще не закончилась
                    c = statement.charAt(i); // Заменяем этот символ (С)
                SB.append('-'); // Добавляем функцию в SB
            }

            while (isNumber(c) || c == '.') { // Пока это число или точка
                key = false;
                SB.append(c); // Добавляем число в SB
                if (++i < statement.length())  // Если строка еще не закончилась
                    c = statement.charAt(i); // Заменяем этот символ (С)
            }

            if (!SB.toString().equals("")) { // Если SB не пустой
                stack.push(new BigDecimal(SB.toString())); // Добавляем его содержимое в стак
            }

            if (c == '*' || c == '/' || c == '+' || c == '-') { // Если символ это функция
                if ((!stack1.empty())) // Если стак1 не пустой
                    if (((char)stack1.peek() == '*') || ((char)stack1.peek() == '/')) { // Если это * или /
                        stack.push(stack1.pop()); // То переместить из стак1 в стак
                    }
            }
            if ( c == '+' || c == '-') {
                if ((!stack1.empty()))
                    if (((char)stack1.peek() == '*') || ((char)stack1.peek() == '/') || ((char)stack1.peek() == '+') || ((char)stack1.peek() == '-')) {
                        stack.push(stack1.pop());
                    }
            }

            if (isFu(c)) {
                stack1.push(c);
                key = true;
            }
            if(c == ')') {
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
//            System.out.println(c.getClass() + " " + c);
            if (c.getClass() == BigDecimal.class) {
                stack1.push((BigDecimal) c);
//                System.out.println(1);
                continue;
            }
            else {
//                System.out.println(2);
                if ((char) c == '*' && (stack1.size() != 1))
                    stack1.push( ( (BigDecimal)stack1.pop() ).multiply( (BigDecimal) stack1.pop() ) );
                if ((char) c == '/' && (stack1.size() != 1)) {
                    Double a = ((BigDecimal) stack1.pop()).doubleValue();
                    stack1.push (new BigDecimal( ( (Double) ( ( (BigDecimal)stack1.pop() ).doubleValue() / a)).toString()));
                }
                if ((char) c == '+' && (stack1.size() != 1))
                    stack1.push(( (BigDecimal)stack1.pop() ).add( (BigDecimal) stack1.pop() ));
                if ((char) c == '-') {
                    BigDecimal a = (BigDecimal) stack1.pop();
                    if(!stack1.empty() && stack1.peek().getClass() != char.class ) {
                        stack1.push(((BigDecimal) stack1.pop()).subtract(a));
                    }
                    else
                        stack1.push((a).multiply(new BigDecimal(-1)));
                }
            }
        }
        //System.out.printf("%.6s",stack1.pop());
        return String.format("%.6s", stack1.pop());
    }

    // Check is Number?
    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    // Check is Function?
    public static boolean isFu(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(';
    }

    // Check brackets
    public static boolean pi(String string) {
        Stack<Character> stack = new Stack<>();
        for(int i = 0; i < string.length(); i++) {
            if(string.charAt(i) == '(')
                stack.push('(');
            if(string.charAt(i) == ')')
                if(stack.empty() || stack.pop() != '(')
                    return false;
        }
        return stack.empty();
    }
}

