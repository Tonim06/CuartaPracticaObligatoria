import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Evaluator {

    public static int calculate(String expr) {
        // Convertim l'string d'entrada en una llista de tokens
        Token[] tokens = Token.getTokens(expr);
        // Efectua el procediment per convertir la llista de tokens en notaciÃ³ RPN
        Token[] tokenRpn = infixToRpn(tokens);
        // Finalment, crida a calcRPN amb la nova llista de tokens i torna el resultat
        return calcRPN(tokenRpn);
    }

    //UNARI = Principi || Despres d'0P || Despres de PAREN ().

    private static Token[] infixToRpn(Token[] tokens) {
        List<Token> stackSalida = new ArrayList<>();
        Stack<Token> stackOperador = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            Token token = tokens[i];
            if (token.getTtype().equals(Token.Toktype.NUMBER)) {
                stackSalida.add(token);
            } else if (token.getTtype().equals(Token.Toktype.OP)) {
                if ((stackSalida.isEmpty()
                        || tokens[i - 1].getTk() == '(')
                        || tokens[i - 1].getTtype().equals(Token.Toktype.OP)
                        && token.getTk() == '-') {
                    token.setTk('m');
                    stackOperador.push(token);
                } else {
                    while (!stackOperador.isEmpty() && masPrecedencia(stackOperador.peek().getTk(), token.getTk())) {
                        stackSalida.add(stackOperador.pop());
                    }
                    stackOperador.push(token);
                }
            } else if (token.getTtype().equals(Token.Toktype.PAREN)) {
                if (token.getTk() == '(') {
                    stackOperador.push(Token.tokParen('('));
                } else if (token.getTk() == ')') {
                    while (!stackOperador.isEmpty() && stackOperador.peek().getTk() != '(') {
                        stackSalida.add(stackOperador.pop());
                    }
                    stackOperador.pop();
                }
            }
        }

        while (!stackOperador.isEmpty()) {
            stackSalida.add(stackOperador.pop());
        }

        Token[] res = new Token[stackSalida.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = stackSalida.get(i);
        }
        System.out.println(Arrays.toString(res));
        return res;
    }

    static int conseguirPrecedencia(char op) {
        switch (op) {
            case 'm':
                return 5;
            case '^':
                return 4;
            case '*', '/':
                return 3;
            case '+', '-':
                return 2;
            case '(':
                return 1;
            default:
                throw new RuntimeException("El operador no se encuentra");
        }
    }

    static boolean masPrecedencia(char actualOPerador, char nouOperador) {
        return conseguirPrecedencia(actualOPerador) >= conseguirPrecedencia(nouOperador);
    }
    public static int calcRPN(Token[] list) {
        Stack<Integer> stack = new Stack<>();

        for (Token token : list) {
            if (token.getTtype() == Token.Toktype.NUMBER) {
                stack.push(token.getValue());
            } else if (token.getTtype() == Token.Toktype.OP) {
                int result = 0;
                if (token.getTk() == 'm') {
                    int numero1 = stack.pop();
                    result = -numero1;
                } else {
                    int operand1 = stack.pop();
                    int operand2 = stack.pop();

                    switch (token.getTk()) {
                        case '+':
                            result = operand2 + operand1;
                            break;
                        case '-':
                            result = operand2 - operand1;
                            break;
                        case '*':
                            result = operand2 * operand1;
                            break;
                        case '/':
                            if (operand1 != 0) result = operand2 / operand1;
                            break;
                        case '^':
                            result = (int) Math.pow(operand2, operand1);
                            break;
                    }
                }
                stack.push(result);
            }
        }
        return stack.pop();
    }
}