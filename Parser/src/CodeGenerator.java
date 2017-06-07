/**
 * Created by afra on 6/7/17.
 */

import jdk.nashorn.internal.parser.*;

import java.util.*;

public class CodeGenerator {

    static ArrayList<String> PB = new ArrayList<>();
    Stack<String> SS = new Stack<>();
    long lastMainMemory = 100;
    long lastTmpMemory = 500;

    public static void print() {
        for (int i = 0; i < PB.size(); i++) {
            System.out.println(i + ": " + PB.get(i));
        }
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
                caller(tokens);
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
                paramAssign(tokens);
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
            case "Op":
                op(tokens);
                break;
            default:
                funcEnd(tokens);
                break;
        }

    }

    private void op(Token[] tokens) {
        SS.push(tokens[1].type);
    }

    private void funcEnd(Token[] tokens) {

    }

    private void funcSetup(Token[] tokens) {
        int i = Integer.parseInt(SS.peek());
        PB.set(i, "(JP, " + PB.size() + ")"); //main

        Target target = getTarget(tokens[1]);
        target.address = PB.size();
        target.type = "function";
        target.scope = Scanner.scopeStack.peek();

        Scanner.incScope();
    }

    private void arrayMemory(Token[] tokens) {
        int currentScope = Scanner.scopeStack.peek();
        Index idx = new Index(tokens[2].name, currentScope);
        Target target = Scanner.symbolTable.get(idx);
        target.address = lastMainMemory;
        target.scope = currentScope;
        target.dimension = 1;
        target.type = "array";
        lastMainMemory += Integer.parseInt(tokens[0].name) * 4;

        System.out.println(tokens[2].name + " " + target.address + " " + lastMainMemory + "***&*&*");


    }

    private void varMemory(Token[] tokens) {
        int currentScope = Scanner.scopeStack.peek();
        Index idx = new Index(tokens[1].name, currentScope);
        Target target = Scanner.symbolTable.get(idx);
        target.address = lastMainMemory;
        target.scope = currentScope;
        target.dimension = 0;
        target.type = "int";
        lastMainMemory += 4;

        System.out.println(tokens[1].name + " " + target.address + " " + target.scope + "&*&*");

    }

    private void gc(Token[] tokens) {

    }

    private void jpMain(Token[] tokens) {

    }

    private void callee(Token[] tokens) {

    }

    private void paramAssign(Token[] tokens) {
       /* String param = SS.pop();
        String arg = "";
        if (tokens[1].type.equals("]")) {
            int currentScope = Scanner.scopeStack.peek();
            Index idx = new Index(tokens[3].name, currentScope);
            Target target = Scanner.symbolTable.get(idx);
            target.address = Integer.parseInt(param);
            target.scope = currentScope;
            target.dimension = 0;
            target.type = "array";
            lastMainMemory += 4;

        } else {
            int currentScope = Scanner.scopeStack.peek();
            Index idx = new Index(tokens[1].name, currentScope);
            Target target = Scanner.symbolTable.get(idx);
            target.address = lastMainMemory;
            target.scope = currentScope;
            target.dimension = 0;
            target.type = "int";
            lastMainMemory += 4;

            arg = tokens[1].name;

            PB.add("(ASSIGN, " + param + ", " + arg + ")");
        }*/


//        PB.add("(ASSIGN")


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
        System.out.println(PB.get(PB.size() - 1) + "____");
    }

    private void lt(Token[] tokens) {
        String var2 = SS.pop();
        String var1 = SS.pop();

        PB.add("(LT, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;
        System.out.println(PB.get(PB.size() - 1) + "____");
    }

    private void eq(Token[] tokens) {
        String var2 = SS.pop();
        String var1 = SS.pop();

        PB.add("(EQ, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;
        System.out.println(PB.get(PB.size() - 1) + "____");
    }

    private void jp(Token[] tokens) {
        int i = Integer.parseInt(SS.pop());
        PB.set(i, "(JP, " + PB.size() + ")");
        System.out.println(PB.get(i) + "^^^^");
    }

    private void jpfSave(Token[] tokens) {
        int i = Integer.parseInt(SS.pop());
        String exp = SS.pop();
        PB.add("");
        PB.set(i, "(JPF, " + exp + ", " + PB.size() + ")");
        SS.push(Integer.toString(PB.size() - 1));
        System.out.println(PB.get(i) + "^^^^");
    }

    private void jpf(Token[] tokens) {
        int i = Integer.parseInt(SS.pop());
        String exp = SS.pop();
        PB.set(i, "(JPF, " + exp + ", " + PB.size() + ")");
        System.out.println(PB.get(i) + "^^^^");
    }

    private void save(Token[] tokens) {
        PB.add("");
        SS.push(Integer.toString(PB.size() - 1));
    }

    private void pidNum(Token[] tokens) {
        SS.push("#" + tokens[0].name);
    }

    private void caller(Token[] tokens) {

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

        System.out.println(op + PB.get(PB.size() - 1) + "____");

        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;

    }

    private void arrayPid(Token[] tokens) {
        String exp = SS.pop();
        String arr = SS.pop();
        String res = "";
        if (exp.charAt(0) == '#')
            exp = exp.substring(1);
        res = Integer.toString(Integer.parseInt(arr) + 4 * Integer.parseInt(exp));
        SS.push(res);
        System.out.println(res + "____");
    }

    private void pid(Token[] tokens) {
        Target t = getTarget(tokens[1]);
        SS.push(Long.toString(t.address));

    }

    private void assign(Token[] tokens) {
        String exp = SS.pop();
        String var = SS.pop();
        System.out.println("(ASSIGN, " + exp + ", " + var + "____");
        System.out.println("size " + PB.size());
        PB.add("(ASSIGN, " + exp + ", " + var + ")");
    }

    private Target getTarget(Token t) {
        int currentScope = Scanner.scopeStack.peek();
        Index idx1 = new Index(t.name, currentScope);
        Index idx2 = new Index(t.name, 0);
        if (!Scanner.symbolTable.containsKey(idx1))
            return Scanner.symbolTable.get(idx2);
        return Scanner.symbolTable.get(idx1);
    }

}
