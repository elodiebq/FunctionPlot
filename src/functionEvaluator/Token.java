package functionEvaluator;

public class Token {
    public Type type;
    public String value;
    public int pos;
    
    public Token(String value, Type type, int pos) {
        this.type = type;
        this.value = value;
        this.pos = pos;
    }
    enum Type {
        Add, Min, Mul, Div, LParent, RParent, Power, Identifier,Number, EndofFile
    }
    
}
