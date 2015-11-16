package functionEvaluator;

import java.util.ArrayList;

public class Lexer {
    public ArrayList<Token> parse(String input) throws Exception {
        ArrayList<Token> tokenList = new ArrayList<Token>();
        String tmp = "";
        int pos = 0;
        int state = 0;
        while (pos < input.length()) {
            char curChar = input.charAt(pos);
            char nextChar = '\0';
            if (pos < input.length() - 1)
                nextChar = input.charAt(pos + 1);
            if (state == 0) {
                if (Character.isDigit(curChar)) {
                    state = 1;

                } else if (curChar == '+' || curChar == '-' || curChar == '*'
                        || curChar == '/' || curChar == '(' || curChar == ')'
                        || curChar == '^') {
                    state = 2;
                } else if (Character.isWhitespace(curChar)) {
                    pos++;
                } else if (Character.isAlphabetic(curChar)) {
                    state = 3;
                } else
                    throw new Exception("invalid input");
            } else if (state == 1) {
                if (Character.isDigit(curChar)) {
                    tmp += curChar;
                    pos++;
                } else if (curChar == '.') {
                    tmp += curChar;
                    pos++;
                    state = 4;
                } else {
                    Token num = new Token(tmp, Token.Type.Number, pos);
                    tokenList.add(num);
                    state = 0;
                    tmp = "";
                }
            } else if (state == 2) {
                if (curChar == '+') {
                    tokenList.add(new Token(curChar + "", Token.Type.Add, pos));
                } else if (curChar == '-') {
                    tokenList.add(new Token(curChar + "", Token.Type.Min, pos));
                } else if (curChar == '^') {
                    tokenList.add(new Token(curChar + "", Token.Type.Power, pos));
                } else if (curChar == '*') {
                    tokenList.add(new Token(curChar + "", Token.Type.Mul, pos));
                } else if (curChar == '/') {
                    tokenList.add(new Token(curChar + "", Token.Type.Div, pos));
                } else if (curChar == '(') {
                    tokenList.add(new Token(curChar + "", Token.Type.LParent,
                            pos));
                } else if (curChar == ')') {
                    tokenList.add(new Token(curChar + "", Token.Type.RParent,
                            pos));
                } else {
                    throw new Exception("Wrong operater");
                }
                pos++;
                state = 0;
            } else if (state == 3) {
                if (Character.isAlphabetic(curChar)) {
                    tmp += curChar;
                    pos++;
                } else {
                    Token identifier = new Token(tmp, Token.Type.Identifier,
                            pos);
                    tokenList.add(identifier);
                    state = 0;
                    tmp = "";
                }
            } else if (state == 4) {
                if (Character.isDigit(curChar)) {
                    tmp += curChar;
                    pos++;
                } else if (curChar == 'e') {
                    tmp += curChar;
                    pos++;
                    state = 5;
                } else {
                    Token floatNum = new Token(tmp, Token.Type.Number, pos);
                    tokenList.add(floatNum);
                    tmp = "";
                    state = 0;
                }
            } else if (state == 5) {
                if ((curChar == '-' || Character.isDigit(curChar))
                        && nextChar != '-') {
                    tmp += curChar;
                    pos++;
                    state = 6;
                } else {
                    throw new Exception("Incorret float input at" + pos);
                }
            } else if (state == 6) {
                if (Character.isDigit(curChar)) {
                    tmp += curChar;
                    pos++;
                } else {
                    Token floatNum = new Token(tmp, Token.Type.Number, pos);
                    tokenList.add(floatNum);
                    tmp = "";
                    state = 0;
                }
            } else {
                throw new Exception("Uncought error");
            }

        }
        if (state == 1 || state == 4 || state == 6) {
            tokenList.add(new Token(tmp, Token.Type.Number, pos));
        } else if (state == 3) {
            tokenList.add(new Token(tmp, Token.Type.Identifier, pos));
        }
        return tokenList;
    }
}
