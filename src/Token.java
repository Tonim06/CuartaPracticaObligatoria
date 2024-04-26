import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Token {
    enum Toktype {
        NUMBER, OP, PAREN
    }

    // Pensa a implementar els "getters" d'aquests atributs
    private Toktype ttype;
    private int value;
    private char tk;

    public Toktype getTtype() {
        return ttype;
    }

    public int getValue() {
        return value;
    }

    public char getTk() {
        return tk;
    }

    public void setTtype(Toktype ttype) {
        this.ttype = ttype;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setTk(char tk) {
        this.tk = tk;
    }

    // Constructor privat. Evita que es puguin construir objectes Token externament
    private Token() {
    }


    // Torna un token de tipus "NUMBER"
    static Token tokNumber(int value) {
        Token token = new Token();
        token.setValue(value);
        token.setTtype(Toktype.NUMBER);
        return token;
    }

    // Torna un token de tipus "OP"
    static Token tokOp(char c) {
        Token token = new Token();
        token.setTk(c);
        token.setTtype(Toktype.OP);
        return token;
    }

    // Torna un token de tipus "PAREN"
    static Token tokParen(char c) {
        Token token = new Token();
        token.setTk(c);
        token.setTtype(Toktype.PAREN);
        return token;
    }

    // Mostra un token (conversió a String)
    @Override
    public String toString() {
        return "Token{" +
                "ttype=" + ttype +
                ", value=" + value +
                ", tk=" + tk +
                '}';
    }

    // Mètode equals. Comprova si dos objectes Token són iguals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return value == token.value && tk == token.tk && ttype == token.ttype;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ttype, value, tk);
    }

    // A partir d'un String, torna una llista de tokens
    public static Token[] getTokens(String expr) {
        List<Token> tokens = new ArrayList<>();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (Character.isDigit(c)) {
                int num = Integer.parseInt(String.valueOf(c));
                while (i + 1 < expr.length() && Character.isDigit(expr.charAt(i + 1))) {
                    num = num * 10 + Integer.parseInt(String.valueOf(expr.charAt(++i)));
                }
                tokens.add(tokNumber(num));
            } else if (c == '^' || c == '+' || c == '-' || c == '*' || c == '/') {
                tokens.add(tokOp(c));
            } else if (c == '(' || c == ')') {
                tokens.add(tokParen(c));
            }
        }
        return tokens.toArray(new Token[0]);
    }
}
