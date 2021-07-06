package com.tsystems.javaschool.tasks.calculator;

import org.mariuszgromada.math.mxparser.Expression;

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
        if (!isValid(statement)) {
            return null;
        }

        Expression e = new Expression(statement);
        double v = e.calculate();

        if (!isValid(String.valueOf(v))) {
            return null;
        }

        if (v % 1 == 0) {
            return String.valueOf(v).replace(".0", "");
        }

        return String.valueOf(v);
    }

    private boolean isValid(String statement) {
        if (statement == null || statement.equals("")) {
            return false;
        }
        return statement.matches("^[\\d\\+\\/\\*\\.\\- \\(\\)]*$");
    }
}

