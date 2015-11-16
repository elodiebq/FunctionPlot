package functionEvaluator;

import java.util.HashMap;
import java.util.List;

public class Parser {
    int ptr = 0;
    HashMap<Token.Type, Integer> priorityTable = new HashMap<Token.Type, Integer>();
    List<Token> tokens;

    private Token.Type lookUpNextType() {
        if (ptr >= tokens.size()) {
            return Token.Type.EndofFile;
        } else
            return tokens.get(ptr).type;
    }
    
    private String readNext(Token.Type tp) throws Exception {
        if (ptr >= tokens.size() || tokens.get(ptr).type != tp) {
            throw new Exception("syntax error at " + ptr);
        } else {
            String tmp = tokens.get(ptr).value;
            ptr++;
            return tmp;
        }
    }

    private BinaryNode.Operator tokenTypeToOperator(Token.Type tp) throws Exception {
        switch (tp) {
        case Add:
            return BinaryNode.Operator.Add;
        case Min:
            return BinaryNode.Operator.Min;
        case Mul:
            return BinaryNode.Operator.Mul;
        case Div:
            return BinaryNode.Operator.Div;
        case Power:
            return BinaryNode.Operator.Power;
        default:
            throw new Exception("Wrong operater type");   
        }
    }
    public Parser() {
        priorityTable.put(Token.Type.Power, 1);
        priorityTable.put(Token.Type.Mul, 2);
        priorityTable.put(Token.Type.Div, 2);
        priorityTable.put(Token.Type.Add, 3);
        priorityTable.put(Token.Type.Min, 3);
    }   
        
        public Node parseNode() throws Exception {
            return parseNode(3);
        }
        
        public Node parseNode0() throws Exception {
            
            if (ptr >= tokens.size()) {
                throw new Exception("Syntax error");
            } else if (lookUpNextType() == Token.Type.Min) {
                UnaryNode un = new UnaryNode();
                readNext(Token.Type.Min);
                un.node = parseNode0();
                return un;
            } else if (lookUpNextType() == Token.Type.Number) {
                NumberNode n = new NumberNode();
                n.value = Float.parseFloat(readNext(Token.Type.Number));
                return n;
            }  else if (lookUpNextType() == Token.Type.LParent) {
                readNext(Token.Type.LParent);
                Node n1 = parseNode();
                readNext(Token.Type.RParent);
                return n1;
            }
            else if (lookUpNextType() == Token.Type.Identifier) {
                String funcName = readNext(Token.Type.Identifier);
                if (lookUpNextType() == Token.Type.LParent) {
                    readNext(Token.Type.LParent);
                    Node n1 = parseNode();
                    InvocationNode nn = new InvocationNode();
                    nn.funcName = funcName;
                    nn.arg = n1;
                    readNext(Token.Type.RParent);
                    return nn;
                } else {
                    VarNode vn = new VarNode();
                    vn.varName = funcName;
                    return vn;
                }
            }
            return null;
        }
        
        public Node parseNode(int level) throws Exception {
            if(level == 0) {
                return parseNode0();
            }
            Node n0 = parseNode(level - 1);
            while (ptr < tokens.size()
                    && priorityTable.containsKey(lookUpNextType())
                    && priorityTable.get(lookUpNextType()) == level) {
                Token.Type tp = lookUpNextType();
                readNext(tp);
                Node n1 = parseNode(level - 1);
                Node nn;
                nn = new BinaryNode(tokenTypeToOperator(tp), n0, n1, ptr);
                n0 = nn;

            }
            return n0;
        }
        
        public Node parseProgram(List<Token> tokens)
                throws Exception {
            this.tokens = tokens;
            
           
                Node pn = parseNode();
            
            return pn;
        }
}
