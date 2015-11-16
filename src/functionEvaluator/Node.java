package functionEvaluator;


abstract public class Node {

    abstract void compile(StringBuilder sb);
    abstract Node derive();
    abstract String tostring();

}

class NumberNode extends Node {
    Float value;

    void compile(StringBuilder sb) {
        sb.append("push ");
        sb.append(value);
        sb.append(" ");
    }

    
    Node derive() {
        NumberNode rt = new NumberNode();
        rt.value = 0f;
        return rt;
    }

    String tostring() {
        
        return Float.toString(value);
    }
}

class BinaryNode extends Node {
    Operator opt;
    Node left;
    Node right;
    int pos;

    public BinaryNode() {
        
    }
    public BinaryNode(Operator opt, Node left, Node right, int pos){
        this.opt = opt;
        this.left = left;
        this.right = right;
        this.pos = pos;
    }
    enum Operator {
        Add, Min, Mul, Div, Power
    }

    void compile(StringBuilder sb) {
        left.compile(sb);
        right.compile(sb);
        switch (opt) {
        case Add:
            sb.append("add ");
            break;
        case Min:
            sb.append("min ");
            break;
        case Mul:
            sb.append("mul ");
            break;
        case Div:
            sb.append("div ");
            break;
        case Power:
            sb.append("power ");
            break;
        default:
            break;

        }

    }
    Node derive() {
        BinaryNode rt = new BinaryNode();
        switch (opt) {
        case Add:{
            rt.left = this.left.derive();
            rt.right = this.right.derive();
            rt.opt = Operator.Add;
            return rt;
        }
        case Min:{
            rt.left = this.left.derive();
            rt.right = this.right.derive();
            rt.opt = Operator.Min;
            return rt;
        }
        case Mul:{
            BinaryNode leftNew = new BinaryNode();
            leftNew.left = this.left .derive();
            leftNew.right = this.right;
            leftNew.opt = Operator.Mul;
            rt.left = leftNew;
            BinaryNode rightNew = new BinaryNode();
            rightNew.left = this.right.derive();
            rightNew.right = this.left;
            rightNew.opt = Operator.Mul;
            rt.right = rightNew;
            rt.opt = Operator.Add;
            return rt;
        }
        case Div:{
            BinaryNode up = new BinaryNode();
            BinaryNode leftNew = new BinaryNode();
            leftNew.left = this.left .derive();
            leftNew.right = this.right;
            leftNew.opt = Operator.Mul;
            up.left = leftNew;
            BinaryNode rightNew = new BinaryNode();
            rightNew.left = this.right.derive();
            rightNew.right = this.left;
            rightNew.opt = Operator.Mul;
            up.right = rightNew;
            up.opt = Operator.Min;
            BinaryNode down = new BinaryNode();
            down.left = this.right;
            down.right = this.right;
            down.opt = Operator.Mul;
            rt.left = up;
            rt.right = down;
            rt.opt = Operator.Div;
            return rt;
            }
        case Power:{
            InvocationNode rtLeft = new InvocationNode();
            BinaryNode rtRight = new BinaryNode();
            rtLeft.funcName = "exp";
            BinaryNode arg = new BinaryNode();
            
            arg.left = this.right;
            InvocationNode rtLeftArgRight = new InvocationNode();
            rtLeftArgRight.funcName = "log";
            rtLeftArgRight.arg = this.left;
            arg.right = rtLeftArgRight;
            arg.opt = BinaryNode.Operator.Mul;
            rtLeft.arg = arg;
            BinaryNode rtRightLeft = new BinaryNode();
            rtRightLeft.left = this.right.derive();
            InvocationNode rtRightLeftRightArg = new InvocationNode();
            rtRightLeftRightArg.funcName = "log";
            rtRightLeftRightArg.arg = this.left;
            rtRightLeft.right = rtRightLeftRightArg;
            rtRightLeft.opt = Operator.Mul;
            BinaryNode rtRightRight = new BinaryNode();
            rtRightRight.left = this.right;
            rtRightRight.right = this.left;
            rtRightRight.opt = Operator.Div;
            rtRight.left = rtRightLeft;
            rtRight.right = rtRightRight;
            rtRight.opt = Operator.Add;
            rt.left = rtLeft;
            rt.right = rtRight;
            rt.opt = Operator.Mul;
            return rt;
            
        }    
        default:{
            break;
        }
       
    }
        return rt;
    }

    String tostring() {
        StringBuilder sb = new StringBuilder();
        switch (opt) {
        case Add:{
            sb.append("(");
            sb.append(this.left.tostring());
            sb.append(" + ");
            sb.append(this.right.tostring());
            sb.append(")");
            break;
        }case Min:{
            sb.append("(");
            sb.append(this.left.tostring());
            sb.append(" - ");
            sb.append(this.right.tostring());
            sb.append(")");
            break;
        }case Mul:{
            sb.append("(");
            sb.append(this.left.tostring());
            sb.append(" * ");
            sb.append(this.right.tostring());
            sb.append(")");
            break;
        }case Div:{
            sb.append("(");
            sb.append(this.left.tostring());
            sb.append(" / ");
            sb.append(this.right.tostring());
            sb.append(")");
            break;
        }case Power:{
            sb.append("(");
            sb.append(this.left.tostring());
            sb.append(" ^ ");
            sb.append(this.right.tostring());
            sb.append(")");
            break;
        }
        }
        return sb.toString();
    }

}

class InvocationNode extends Node {
    String funcName;
    Node arg;
   
    void compile(StringBuilder sb) {
        arg.compile(sb);
        sb.append("call ");
        sb.append(funcName);  
        sb.append(" ");
    }

    Node derive() {
        BinaryNode rt = new BinaryNode();
        switch(funcName) {
        case "sin":{
            InvocationNode rtLeft = new InvocationNode();
            rtLeft.funcName = "cos";
            rtLeft.arg = this.arg;
            rt.left = rtLeft;
            rt.right = this.arg.derive();
            rt.opt = BinaryNode.Operator.Mul;
            return rt;
        }
        case "cos":{
            UnaryNode rtLeft = new UnaryNode();
            InvocationNode rtLeftRight = new InvocationNode();
            rtLeftRight.funcName = "sin";
            rtLeftRight.arg = this.arg;
            rtLeft.node = rtLeftRight;
            rt.left = rtLeft;
            rt.right = this.arg.derive();
            rt.opt = BinaryNode.Operator.Mul;
            return rt;
        }case "log":{
            rt.left = this.arg.derive();
            rt.right = this.arg;
            rt.opt = BinaryNode.Operator.Div;
            return rt;
        }case "exp":{
            rt.left = this;
            rt.right = this.arg.derive();
            rt.opt = BinaryNode.Operator.Mul;
            return rt;
        }case "sqrt":{
            rt.left = this.arg.derive();
            BinaryNode rtRight = new BinaryNode();
            NumberNode num1 = new NumberNode();
            num1.value = 2f;
            rtRight.left = num1;
            rtRight.right = this;
            rtRight.opt = BinaryNode.Operator.Mul;
            rt.right = rtRight;
            rt.opt = BinaryNode.Operator.Div;
            return rt;
        } default:
            break;   
        }
        return null;
    }


    String tostring() {
        StringBuilder sb = new StringBuilder();
        sb.append(funcName);
        sb.append("(");
        sb.append(this.arg.tostring());
        sb.append(")");
        return sb.toString();
    }
    
}

class VarNode extends Node {
    String varName;

    void compile(StringBuilder sb) {
        sb.append("load ");
    }

    Node derive() {
        NumberNode num = new NumberNode();
        num.value = 1f;
        return num;
    }

    String tostring() {
        
        return varName;
    }
    
    
}

class UnaryNode extends Node {
    Node node;

    void compile(StringBuilder sb) {
        node.compile(sb);
        sb.append("neg ");
       
    }

    Node derive() {
        UnaryNode nd = new UnaryNode();
        nd.node = this.node.derive(); 
        return nd;
    }


    @Override
    String tostring() {
        StringBuilder sb = new StringBuilder();
        sb.append("-");
        sb.append("(");
        sb.append(this.node.tostring());
        sb.append(")");
        return sb.toString();
        
    }
}