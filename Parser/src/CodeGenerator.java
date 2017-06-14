/**
 * Created by afra on 6/7/17.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class CodeGenerator {

    static ArrayList<String> PB = new ArrayList<>();
    HashMap<String, ActivationRecord> records = new HashMap<>();
    Stack<String> SS = new Stack<>();
    ActivationRecord lastRecord = null;
    ActivationRecord currentRecord = null;
    long[] display = new long[2];
    long lastMainMemory = 0;
    long oldLastMainMemory = 0;
    long lastTmpMemory = 500;
    int paramsNum = 0;

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
            case "X24":
                caller(tokens);
                break;
            case "X25": //output
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

        PB.add("(SUB, " + display[1] + ", #" + (1 + paramsNum) * 4 + ", " + lastTmpMemory + ")");
        PB.add("(ADD, " + display[1] + ", #" + (paramsNum - 1) * 4 + ", " + (lastTmpMemory + 4) + ")");
        PB.add("(ASSIGN, @" + lastTmpMemory + ", @" + (lastTmpMemory + 4) + ")");
        lastTmpMemory += 8;

    }

    private void output(Token[] tokens) {
        String exp = SS.pop();
        PB.add("(PRINT, " + exp + ")");
    }

    private void jpFunc(Token[] tokens) {
        SS.pop();
        int s = PB.size() + 3;
        PB.add("(ASSIGN, " + s + ", " + currentRecord.returnLineAddress + ")");
        PB.add("(ASSIGN, #" + lastTmpMemory + ", " + currentRecord.returnValuePointer + ")");
        PB.add("(JP, " + currentRecord.firstLine + ")");
        currentRecord.returnValueAddress = lastTmpMemory;
        SS.push(Long.toString(currentRecord.returnValueAddress));
        lastTmpMemory += 4;
//        SS.push(Long.toString(currentRecord.returnValueAddress));
    }

    private void args(Token[] tokens) {
        String exp = SS.pop();
        int index = Integer.parseInt(SS.pop());
        PB.add("(ASSIGN, " + exp + ", " + currentRecord.params.get(index).address + ")");
        SS.push(Integer.toString(index + 1));
//        System.out.println(index + " args");
    }

    private void op(Token[] tokens) {
        SS.push(tokens[1].type);
    }

    private void funcEnd(Token[] tokens) {
        PB.add("(JP, @" + lastRecord.returnLineAddress + ")");
        Scanner.decScope();
        lastMainMemory = oldLastMainMemory;
//        System.out.println("sizeeee: " + Scanner.symbolTable.size());
//        System.out.println(Scanner.scopeStack.peek() + "scooope");
    }

    private void funcSetup(Token[] tokens) {
        int i = Integer.parseInt(SS.peek());
        PB.set(i, "(JP, " + PB.size() + ")"); //main

        Target target = getTarget(tokens[1]);
        target.address = PB.size();
        target.type = "function";
        target.scope = Scanner.symbolTable.size();
        target.paramsNum = paramsNum;
        target.isVoid = tokens[2].name.equals("void");
        paramsNum = 0;

        if (tokens[1].name.equals("main"))
            PB.add("(ASSIGN, #" + (display[1]+4) + ", " + display[1] + ")");

        lastRecord = new ActivationRecord();
        lastRecord.firstLine = PB.size();
        lastRecord.returnLineAddress = lastTmpMemory;
        lastTmpMemory += 4;
        lastRecord.returnValuePointer = lastTmpMemory;
        lastTmpMemory += 4;

        records.put(tokens[1].name, lastRecord);
        oldLastMainMemory = lastMainMemory;
        lastMainMemory = 0;
        Scanner.incScope();
    }

    private void arrayMemory(Token[] tokens) {
//        int currentScope = Scanner.scopeStack.peek();
        Index idx = new Index(tokens[2].name);
        Target target = Scanner.lookup(idx);
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
        if (!tokens[2].type.equals("return")) {
            String returnVal = SS.pop();
            PB.add("(ASSIGN, " + returnVal + ", @" + lastRecord.returnValuePointer + ")");
        }
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

            lastRecord.params.add(target);
            lastMainMemory += 4;
        } else {
//            int currentScope = Scanner.scopeStack.peek();
            Index idx = new Index(tokens[1].name);
            Target target = Scanner.lookup(idx);
            target.address = lastMainMemory;
            target.scope = Scanner.symbolTable.size();
            target.dimension = 0;
            target.type = "int";

            lastRecord.params.add(target);
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
        int i = Integer.parseInt(SS.pop());
        String exp = SS.pop();
        PB.set(i, "(JPF, " + exp + ", " + PB.size() + ")");
//        System.out.println(PB.get(i) + "^^^^");
    }

    private void save(Token[] tokens) {
        PB.add("");
        SS.push(Integer.toString(PB.size() - 1));
    }

    private void pidNum(Token[] tokens) {
        SS.push("#" + tokens[0].name);
    }

    private void caller(Token[] tokens) {

        currentRecord = records.get(tokens[1].name);
        SS.push("0");
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

        PB.add("(MULT, #4, " + exp + ", " + lastTmpMemory + ")");
        PB.add("(ADD, " + lastTmpMemory + ", " + arr + ", " + (lastTmpMemory + 4) + ")");
        lastTmpMemory += 4;
        SS.push("@" + Long.toString(lastTmpMemory));
        lastTmpMemory += 4;
    }

    private void pid(Token[] tokens) {
        Target t = getTarget(tokens[1]);
        PB.add("(ADD, " + display[t.scope - 1] + ", #" + t.address + ", " + lastTmpMemory + ")");
        SS.push("@" + Long.toString(lastTmpMemory));
        lastTmpMemory += 4;

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

}
