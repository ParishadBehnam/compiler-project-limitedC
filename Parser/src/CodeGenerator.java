/**
 * Created by afra on 6/7/17.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class CodeGenerator {

    static ArrayList<String> PB = new ArrayList<>();
    HashMap<String, ArrayList<String>> paramTypes = new HashMap<>();
    Stack<String> SS = new Stack<>();
    Stack<Target> IDsStack = new Stack<>();
    Stack<Integer> argsStack = new Stack<>();
    long[] display = new long[2];
    long lastMainMemory = 0;
    long oldLastMainMemory = 0;
    long lastTmpMemory = 500;
    int paramsNum = 0;
//    int argsNum = 0;
    Token funcToken;
    Token calleeToken;
    boolean returnSeen = false;

    public CodeGenerator() {
        display[0] = 100;
        display[1] = 104;
        PB.add("(ASSIGN, #400, " + display[0] + ")");
    }

    public static void print() {
        for (int i = 0; i < PB.size(); i++) {
            System.out.println(i + ": " + PB.get(i));
        }
    }

    public void printStack() {
        Stack<String> copiedStack = (Stack<String>) SS.clone();
        for (int i = 0; i < SS.size(); i++) {
            System.out.print(copiedStack.pop() + " ");
        }
        System.out.println("");
        System.out.println("%%%%%");
    }

    public void generateCode(String type, Token[] tokens) {
        switch (type) {
            case "X1":
                assign(tokens);
                break;
            case "X2":
                pid(tokens);
                break;
            case "X3":
                arrayPid(tokens);
                break;
            case "X4":
                operation(tokens);
                break;
            case "X5":
//                caller(tokens);
                break;
            case "X6":
                pidNum(tokens);
                break;
            case "X7":
                save(tokens);
                break;
            case "X8":
                jpf(tokens);
                break;
            case "X9":
                jpfSave(tokens);
                break;
            case "X10":
                jp(tokens);
                break;
            case "X11":
                eq(tokens);
                break;
            case "X12":
                lt(tokens);
                break;
            case "X13":
                and(tokens);
                break;
            case "X14":
                label(tokens);
                break;
            case "X15":
                loop(tokens);
                break;
            case "X16":
//                paramAssign(tokens);
                break;
            case "X17":
                callee(tokens);
                break;
            case "X18":
                jpMain(tokens);
                break;
            case "X19":
                gc(tokens);
                break;
            case "X20":
                varMemory(tokens);
                break;
            case "X21":
                arrayMemory(tokens);
                break;
            case "X22":
                funcSetup(tokens);
                break;
            case "X24":
                caller(tokens);
                break;
            case "output": //output
                output(tokens);
                break;
            case "ArgList":
                args(tokens);
                break;
            case "jmpToFunc":
                jpFunc(tokens);
                break;
            case "parameter":
                parameter(tokens);
                break;
            case "Var":
                pidPop(tokens);
                break;
            case "Op":
                op(tokens);
                break;
            default:
                funcEnd(tokens);
                break;
        }
    }

    private void parameter(Token[] tokens) {
        paramsNum ++;
        System.out.println(paramsNum + "paramssssssss");
        ArrayList<String> list = paramTypes.get(funcToken.name);

        PB.add("(SUB, " + display[1] + ", #" + (1 + paramsNum) * 4 + ", " + lastTmpMemory + ")");
        PB.add("(ADD, " + display[1] + ", #" + (paramsNum - 1) * 4 + ", " + (lastTmpMemory + 4) + ")");
        PB.add("(ASSIGN, @" + lastTmpMemory + ", @" + (lastTmpMemory + 4) + ")");
        lastTmpMemory += 8;
        Target t;
        if (tokens[1].type.equals("]")) {
            t = getTarget(tokens[3]);
            t.address = lastMainMemory;
            t.type = "pointer";
            list.add("pointer");
        } else {
            t = getTarget(tokens[1]);
            t.address = lastMainMemory;
            t.type = "int";
            list.add("int");
        }
        lastMainMemory += 4;

    }

    private void output(Token[] tokens) {
        System.out.println("output");
        printStack();
        String exp = SS.pop();
        PB.add("(PRINT, " + exp + ")");
    }

    private void jpFunc(Token[] tokens) {

        int argsNum = argsStack.peek();
        if (argsNum != 0) {
            System.out.println(argsNum);
            System.out.println("SEMANTIC ERROR: arguments' number of function " + calleeToken.name + " don't match!");
            System.exit(0);
        }
        argsStack.pop();
        int s = PB.size() + 7;
        PB.add("(ADD, " + display[1] + ", #" + (lastMainMemory) + ", " + lastTmpMemory +")");
        PB.add("(ASSIGN, #" + s + ", @" + lastTmpMemory + ")");
        lastTmpMemory += 4;
        Target t = getTarget(calleeToken);
        PB.add("(ADD, " + display[1] + ", #" + (8 + 4 * t.paramsNum) + ", " + lastTmpMemory +")");
        PB.add("(ASSIGN, " + display[1] + ", @" + lastTmpMemory + ")");
        lastTmpMemory += 4;
        PB.add("(ADD, " + display[1] + ", #" + (12 + 4 * t.paramsNum) + ", " + lastTmpMemory + ")");
        PB.add("(ASSIGN, " + lastTmpMemory + ", " + display[1] + ")");
        lastTmpMemory += 4;

        PB.add("(JP, " + t.address + ")");
        PB.add("(ADD, " + display[1] + ", #" + (lastMainMemory + 4) + ", " + lastTmpMemory + ")");
        PB.add("(ASSIGN, @" + lastTmpMemory + ", " + (lastTmpMemory + 4) + ")");
        lastTmpMemory += 8;
        SS.push(Long.toString(lastTmpMemory-4));

        printStack();


    }

    private void args(Token[] tokens) {
        int argsNum = argsStack.pop();
        String exp = SS.pop();
        System.out.println(argsNum + "argsssssssssssssssssss");
        PB.add("(ADD, " + display[1] + ", #" + (8 + (argsNum - 1) * 4) + ", " + lastTmpMemory + ")");
        PB.add("(ASSIGN, " + exp + ", @" + lastTmpMemory + ")");
        lastTmpMemory += 4;
        ArrayList<String> list = paramTypes.get(calleeToken.name);
        if ((!tokens[1].type.equals("ID") || !getTarget(tokens[1]).type.equals("pointer")) && list.get(list.size() - argsNum).equals("pointer")) {
            System.out.println("SEMANTIC ERROR: passed parameters to function " + calleeToken.name + " mismatched");
            System.exit(0);
        }
        argsNum --;
        argsStack.push(argsNum);
    }

    private void op(Token[] tokens) {
        SS.push(tokens[1].type);
    }

    private void funcEnd(Token[] tokens) {
        Target target = getTarget(funcToken);
        if (!returnSeen) {
            if (!getTarget(funcToken).isVoid) {
                System.out.println("SEMANTIC ERROR: return type of function " + funcToken.name + " mismatched");
                System.exit(0);
            }
            Scanner.decScope();
            lastMainMemory = oldLastMainMemory;
            PB.add("(SUB, " + display[1] + ", #" + 4 + ", " + lastTmpMemory + ")");
            PB.add("(ASSIGN, @" + lastTmpMemory + ", " + display[1] + ")");
            lastTmpMemory += 4;
            PB.add("(SUB, " + display[1] + ", #" + (12 + 4 * paramsNum) + ", " + lastTmpMemory + ")");
            PB.add("(JP, @" + lastTmpMemory + ")");
            lastTmpMemory += 4;
            target.paramsNum = paramsNum;
            System.out.println("targeeeeet" + target.paramsNum);
            paramsNum = 0;
        }
        returnSeen = false;
    }

    private void funcSetup(Token[] tokens) {
        int i = Integer.parseInt(SS.peek());
        PB.set(i, "(JP, " + PB.size() + ")"); //main

        paramTypes.put(tokens[1].name, new ArrayList<String>());

        Target target = getTarget(tokens[1]);
        if (!target.type.equals("")) {
            System.out.println("SEMANTIC ERROR: duplicate declaration of " + tokens[1].name);
            System.exit(0);
        }
        target.address = (long)PB.size();
        target.type = "function";
        target.scope = Scanner.symbolTable.size();
        target.paramsNum = paramsNum;
        target.isVoid = tokens[2].name.equals("void");

        if (tokens[1].name.equals("main"))
            PB.add("(ASSIGN, #" + (display[1]+4) + ", " + display[1] + ")");

        oldLastMainMemory = lastMainMemory;
        lastMainMemory = 0;
        funcToken = tokens[1];
        Scanner.incScope();
    }

    private void arrayMemory(Token[] tokens) {
        Index idx = new Index(tokens[2].name);
        Target target = Scanner.lookup(idx);
        System.out.println(target.type);
        if (!target.type.equals("")) {
            System.out.println("SEMANTIC ERROR: duplicate declaration of " + tokens[2].name);
            System.exit(0);
        }
        target.length = Integer.parseInt(tokens[0].name);
        target.address = lastMainMemory;
        target.scope = Scanner.symbolTable.size();
        target.dimension = 1;
        target.type = "pointer";
        PB.add("(ADD, " + display[target.scope - 1] + ", #" + target.address + ", " + lastTmpMemory + ")");
        PB.add("(ADD, #" + 4 + ", " + lastTmpMemory + ", " + (lastTmpMemory + 4) + ")");
        PB.add("(ASSIGN, " + (lastTmpMemory + 4) + ", @" + lastTmpMemory + ")");
        lastMainMemory += (Integer.parseInt(tokens[0].name) + 1) * 4;
        lastTmpMemory += 8;

//        System.out.println(tokens[2].name + " " + target.address + " " + lastMainMemory + "***&*&*");

    }

    private void varMemory(Token[] tokens) {
//        int currentScope = Scanner.scopeStack.peek();
        Index idx = new Index(tokens[1].name);
        Target target = Scanner.lookup(idx);
        System.out.println(target.type);
        if (!target.type.equals("")) {
            System.out.println("SEMANTIC ERROR: duplicate declaration of " + tokens[1].name);
            System.exit(0);
        }
        target.address = lastMainMemory;
        target.scope = Scanner.symbolTable.size();
        target.dimension = 0;
        target.type = "int";
        lastMainMemory += 4;
//        System.out.println("sizeeee: " + Scanner.symbolTable.size());
//        System.out.println(tokens[1].name + " " + target.address + " " + target.scope + "&*&*");
    }

    private void gc(Token[] tokens) {
        SS.pop();
    }

    private void jpMain(Token[] tokens) {

    }

    private void callee(Token[] tokens) {
        returnSeen = true;
        Scanner.decScope();
        lastMainMemory = oldLastMainMemory;
        if (!tokens[2].name.equals("return")) {

            if (getTarget(funcToken).isVoid) {
                System.out.println("SEMANTIC ERROR: return type of function " + funcToken.name + " mismatched");
                System.exit(0);
            }

            PB.add("(SUB, " + display[1] + ", #" + (8 + 4 * paramsNum) + ", " + lastTmpMemory + ")");
            PB.add("(ASSIGN, " + SS.pop() + ", @" + lastTmpMemory + ")");
            lastTmpMemory += 4;
        } else if (!getTarget(funcToken).isVoid) {
                System.out.println("SEMANTIC ERROR: return type of function " + funcToken.name + " mismatched");
                System.exit(0);
        }
        PB.add("(SUB, " + display[1] + ", #" + 4 + ", " + lastTmpMemory + ")");
        PB.add("(ASSIGN, @" + lastTmpMemory + ", " + display[1] + ")");
        lastTmpMemory += 4;
        PB.add("(SUB, " + display[1] + ", #" + (12 + 4 * paramsNum) + ", " + lastTmpMemory + ")");
        PB.add("(JP, @" + lastTmpMemory + ")");
        lastTmpMemory += 4;
        Target target = getTarget(funcToken);
        target.paramsNum = paramsNum;
        paramsNum = 0;
    }

    private void paramAssign(Token[] tokens) {
        if (tokens[1].type.equals("]")) {
//            int currentScope = Scanner.scopeStack.peek();
            Index idx = new Index(tokens[3].name);
            Target target = Scanner.lookup(idx);
            target.address = lastMainMemory;
            target.scope = Scanner.symbolTable.size();
            target.dimension = 1;
            target.type = "pointer";

            lastMainMemory += 4;
        } else {
//            int currentScope = Scanner.scopeStack.peek();
            Index idx = new Index(tokens[1].name);
            Target target = Scanner.lookup(idx);
            target.address = lastMainMemory;
            target.scope = Scanner.symbolTable.size();
            target.dimension = 0;
            target.type = "int";

            lastMainMemory += 4;
        }
    }

    private void loop(Token[] tokens) {
        int i = Integer.parseInt(SS.pop());
        String exp = SS.pop();
        int label = Integer.parseInt(SS.pop());
        PB.add("(JP, " + label + ")");
        PB.set(i, "(JPF, " + exp + ", " + PB.size() + ")");
    }

    private void label(Token[] tokens) {
        SS.push(Integer.toString(PB.size()));
    }

    private void and(Token[] tokens) {
        String var2 = SS.pop();
        String var1 = SS.pop();

        PB.add("(AND, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;
//        System.out.println(PB.get(PB.size() - 1) + "____");
    }

    private void lt(Token[] tokens) {
        String var2 = SS.pop();
        String var1 = SS.pop();

        PB.add("(LT, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;
//        System.out.println(PB.get(PB.size() - 1) + "____");
    }

    private void eq(Token[] tokens) {
        String var2 = SS.pop();
        String var1 = SS.pop();

        PB.add("(EQ, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;
        System.out.println(lastTmpMemory - 4 + "hehe");
//        System.out.println(PB.get(PB.size() - 1) + "____");
    }

    private void jp(Token[] tokens) {
        int i = Integer.parseInt(SS.pop());
        PB.set(i, "(JP, " + PB.size() + ")");
//        System.out.println(PB.get(i) + "^^^^");
    }

    private void jpfSave(Token[] tokens) {
        int i = Integer.parseInt(SS.pop());
        String exp = SS.pop();
        PB.add("");
        PB.set(i, "(JPF, " + exp + ", " + PB.size() + ")");
        SS.push(Integer.toString(PB.size() - 1));
//        System.out.println(PB.get(i) + "^^^^");
    }

    private void jpf(Token[] tokens) {
        printStack();
        int i = Integer.parseInt(SS.pop());
        String exp = SS.pop();
        PB.set(i, "(JPF, " + exp + ", " + PB.size() + ")");
    }

    private void save(Token[] tokens) {
        PB.add("");
        SS.push(Integer.toString(PB.size() - 1));
    }

    private void pidNum(Token[] tokens) {
        if (tokens[0].name.matches("\\d+\\.\\d+")) {
            System.out.println("SEMANTIC ERROR: type mismatched. int expected");
            Double d = Double.parseDouble(tokens[0].name);
            tokens[0].name = d.intValue() + "";
        }
        SS.push("#" + tokens[0].name);
    }

    private void caller(Token[] tokens) {
        Target target = getTarget(tokens[1]);
        calleeToken = tokens[1];
        argsStack.push(target.paramsNum);
        System.out.println("argsssssss caller" + target.type);
        if (target.type.equals("")) {
            System.out.println("SEMANTIC ERROR: function " + tokens[1].name + " hasn't been declared");
            System.exit(0);
        }

    }

    private void operation(Token[] tokens) {
        String var2 = SS.pop();
        String op = SS.pop();
        String var1 = SS.pop();

        switch (op) {
            case "+":
                PB.add("(ADD, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
                break;
            case "-":
                PB.add("(SUB, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
                break;
            case "*":
                PB.add("(MULT, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
                break;
            default:
                PB.add("(DIV, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
                break;
        }

//        System.out.println(op + PB.get(PB.size() - 1) + "____");

        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;

    }

    private void arrayPid(Token[] tokens) {
        String exp = SS.pop();
        String arr = SS.pop();
        Target top = IDsStack.peek();


        PB.add("(LT, " + exp + ", #" + top.length + " ," + lastTmpMemory + ")");
        PB.add("(JPF, " + lastTmpMemory + ", 1)");
        lastTmpMemory += 4;

        PB.add("(MULT, #4, " + exp + ", " + lastTmpMemory + ")");
        PB.add("(ADD, " + lastTmpMemory + ", " + arr + ", " + (lastTmpMemory + 4) + ")");
        lastTmpMemory += 4;
        SS.push("@" + Long.toString(lastTmpMemory));
        lastTmpMemory += 4;

        if (!top.type.equals("pointer")) {
            System.out.println("SEMANTIC ERROR: type mismatched");
            System.exit(0);
        }
    }

    private void pid(Token[] tokens) {
        Target t = getTarget(tokens[1]);
        if (t.type.equals("")) {
            System.out.println("SEMANTIC ERROR: ID " + tokens[1].name + " hasn't been declared");
            System.exit(0);
        }
        PB.add("(ADD, " + display[t.scope - 1] + ", #" + t.address + ", " + lastTmpMemory + ")");
        SS.push("@" + Long.toString(lastTmpMemory));
        IDsStack.push(t);
        lastTmpMemory += 4;

    }

    private void pidPop(Token[] tokens) {
        IDsStack.pop();
    }

    private void assign(Token[] tokens) {
        String exp = SS.pop();
        String var = SS.pop();
//        System.out.println("(ASSIGN, " + exp + ", " + var + "____");
//        System.out.println("size " + PB.size());
        PB.add("(ASSIGN, " + exp + ", " + var + ")");
    }

    private Target getTarget(Token t) {
        Index idx = new Index(t.name);
        return Scanner.lookup(idx);
    }

//    private Target getTargetByAddress(Long addr) {
//        HashSet<Index> keys;
//        for (HashMap<Index, Target> map : Scanner.symbolTable) {
//            keys = new HashSet<>(map.keySet());
//        }
//        return Scanner.lookup(idx);
//    }

}
