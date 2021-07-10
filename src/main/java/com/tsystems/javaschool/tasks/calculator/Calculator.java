package com.tsystems.javaschool.tasks.calculator;

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

        if (stringEmpty(statement) || !isValidBrackets(statement) || !isValidSymbols(statement)) {
            return null;
        }

        List<String> infixExpressionList = toInfixExpressionList(statement.replaceAll(" ", "")); // Возвращаем лист всех переменных и операций

        if (!isValidFunction(infixExpressionList)) { // Проверка на отсутствие двойных функций (//, *)*, */ и т.п.) не замечая скобки
            return null;
        }

        List<String> suffixExpressionList = ShuntingYard(infixExpressionList); // Возвращает суффикс выражение в виде листа

        return calculateRPN(suffixExpressionList);
    }

    public static List<String> toInfixExpressionList(String s){
        List<String> ls = new ArrayList<>(); // Хранение всех чисел и переменных
        int i = 0; // Индекс каждого символа
        char c; // Каждый новый символ помещается в с
        StringBuilder str;// Склейка нескольких цифр

        do {
            if (!isNumberOrDot(c = s.charAt(i))) { // Если c не является числом, его нужно добавить к ls
                ls.add("" + c);
                i++;
            } else { // Если это число, нужно учитывать проблему нескольких цифр.
                str = new StringBuilder();
                while (isNumberOrDot(c = s.charAt(i))) { // Пока это числа, склеиваем их
                    str.append(c);
                    i ++;
                    if (i >= s.length()) {
                        break;
                    }
                }
                ls.add(str.toString()); // Записываем целое число
            }
        } while (i < s.length());
        return ls;
    }

    public static List<String> ShuntingYard(List<String> infix){
        List<String> output2 = new ArrayList<>();
        Deque<String> stack  = new LinkedList<>();

        for (String item : infix) {
            if (ops.containsKey(item)) { // Если объект это операция
                while ( ! stack.isEmpty() && isHigerPrec(item, stack.peek())) {
                    output2.add(stack.pop());
                }
                stack.push(item);
            } else if (item.equals("(")) {
                stack.push(item);
            } else if (item.equals(")")) {
                while ( !(stack.peek() != null && stack.peek().equals("("))) {
                    output2.add(stack.pop());
                }
                stack.pop();
            } else {
                output2.add(item);
            }
        }

        while ( ! stack.isEmpty()) {
            output2.add(stack.pop());
        }

        return output2;
    }

    private enum Operator {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4);
        final int precedence;
        Operator(int p) { precedence = p; }
    }

    private static final Map<String, Operator> ops = new HashMap<String, Operator>() {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
    }};

    private static boolean isHigerPrec(String op, String sub) {
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
                    if (right == 0) { // деление на ноль
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
            double result = deque.peek();
            return (result % 1 == 0) ? String.valueOf((int)result) : String.format(Locale.US,"%.4f", result); // Убираю ноль после точки
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
        boolean doubleDot = false;
        for (int i = 0; i < statement.length() - 1; i++) {
            if (statement.charAt(i) == '.' && statement.charAt(i + 1) == '.') {
                doubleDot = true;
                break;
            }
        }
        return statement.matches("^[\\d+/*.\\- ()]*$") && !doubleDot; // valid only 1234567890 + / * . - ( ) && check dont have double dot
    }

    public static boolean isValidBrackets(String string) { // После каждой открывающей скобки, должна быть закрывающая
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

    private boolean isValidFunction(List<String> infixExpressionList) {
        if (ops.containsKey(infixExpressionList.get(0))) { // Первый элемент не может быть функцией (+ - * /)
            return false;
        }
        boolean beforeIsFunction = false;

        for (String s : infixExpressionList) {
            if (ops.containsKey(s)) { // Если этот элемент функция?  || s.equals("(")
                if (beforeIsFunction) { // И прошлый функция?
                    return false; // Ошибка
                } else {
                    beforeIsFunction = true; // Если прошлый не функция, то меняем boolean
                }
            } else if (!(s.equals("(") || s.equals(")"))){
                beforeIsFunction = false; // Если данный элемент не был функцией, то меняем boolean
            }
        }
        return true; // Если не встретили ни одной ошибки
    }
}

