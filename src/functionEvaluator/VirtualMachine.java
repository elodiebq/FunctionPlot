package functionEvaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class VirtualMachine {

    public float excute(String input, float x) throws Exception {
        ArrayList<Float> stack = new ArrayList<Float>();
        HashMap<String, Float> hm = new HashMap<String, Float>();
        Scanner sc = new Scanner(input);
        while (sc.hasNext()) {
            String cur = sc.next();
            if (cur.equals("push")) {
                stack.add(sc.nextFloat());
            } else if (cur.equals("load")) {
                stack.add((float) x);
            } else if (cur.equals("call")) {
                String func = sc.next();
                if (func.equals("sin")) {
                    stack.set(stack.size() - 1, (float) Math.sin(stack.get(stack.size() - 1)));
                } else if (func.equals("cos")) {
                    stack.set(stack.size() - 1, (float) Math.cos(stack.get(stack.size() - 1)));
                } else if (func.equals("log")) {
                    stack.set(stack.size() - 1, (float) Math.log(stack.get(stack.size() - 1)));
                } else if (func.equals("exp")) {
                    stack.set(stack.size() - 1, (float) Math.exp(stack.get(stack.size() - 1)));
                } else if (func.equals("sqrt")) {
                    stack.set(stack.size() - 1, (float) Math.sqrt(stack.get(stack.size() - 1)));
                } else {
                    throw new Exception("Unrecognize method");
                }
            } else if (cur.equals("add")) {
                stack.set(
                        stack.size() - 2,
                        stack.get(stack.size() - 2)
                                + stack.get(stack.size() - 1));
                stack.remove(stack.size() - 1);
            } else if (cur.equals("min")) {
                stack.set(
                        stack.size() - 2,
                        stack.get(stack.size() - 2)
                                - stack.get(stack.size() - 1));
                stack.remove(stack.size() - 1);
            } else if (cur.equals("mul")) {
                stack.set(
                        stack.size() - 2,
                        stack.get(stack.size() - 2)
                                * stack.get(stack.size() - 1));
                stack.remove(stack.size() - 1);
            } else if (cur.equals("div")) {
                stack.set(
                        stack.size() - 2,
                        stack.get(stack.size() - 2)
                                / stack.get(stack.size() - 1));
                stack.remove(stack.size() - 1);
            } else if (cur.equals("power")) {
                stack.set(
                        stack.size() - 2,
                        (float) Math.pow(stack.get(stack.size() - 2),
                                stack.get(stack.size() - 1)));
                stack.remove(stack.size() - 1);
            } else if (cur.equals("neg")) {
                stack.set(stack.size() - 1,
                        (float) -stack.get(stack.size() - 1));
            }
        }
        return stack.get(stack.size() - 1);
    }

  
}
