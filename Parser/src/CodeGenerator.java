/**
 * Created by afra on 6/7/17.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class CodeGenerator {

    private static ArrayList<String> PB;    //Program Block
    private HashMap<String, ArrayList<String>> paramTypes;  //types of functions' parameters.
    private Stack<String> SS;
    private Stack<Long> mainMemoryStack;
    private Stack<Target> IDsStack;
    private Stack<Integer> argsStack;   //functions' arguments number
    private long[] display;
    private long lastMainMemory = 0;
    private long lastTmpMemory = 500;
    private int paramsNum = 0;

    private Token funcToken;
    private Token calleeToken;
    private boolean returnSeen = false;
    private static boolean errorSeen = false;

    public CodeGenerator() {

        PB = new ArrayList<>();
        paramTypes = new HashMap<>();
        SS = new Stack<>();
        mainMemoryStack = new Stack<>();
        mainMemoryStack = new Stack<>();
        IDsStack = new Stack<>();
        argsStack = new Stack<>();
        display = new long[2];

        display[0] = 100;
        display[1] = 104;

        PB.add("(JP, " + (PB.size() + 2) + ")");
        PB.add("");
        PB.add("(ASSIGN, #400, " + display[0] + ")");
    }

    public static void print() {
        if (!errorSeen)
            for (int i = 0; i < PB.size(); i++) {
                System.out.println(i + ": " + PB.get(i));
            }
        errorSeen = true;
    }

    public void printStack() {
        Stack<String> copiedStack = (Stack<String>) SS.clone();
        for (int i = 0; i < SS.size(); i++) {
            System.out.print(copiedStack.pop() + " ");
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
                add(tokens);
                break;
            case "X26":
                sub(tokens);
                break;
            case "X27":
                mul(tokens);
                break;
            case "X28":
                div(tokens);
                break;
            case "X6":
                pidNum(tokens);
                break;
            case "X7":
                incScope();
                save(tokens);
                break;
            case "X8":
                jpf(tokens);
                break;
            case "stmt":
                decScope();
                break;
            case "X9":
                decScope();
                jpfSave(tokens);
                incScope();
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
            case "Call":
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
            case "X23":
                funcEnd(tokens);
                break;
        }
    }

    private void incScope() {
        mainMemoryStack.push(lastMainMemory);
        Scanner.incScope();
    }

    private void decScope() {
        Scanner.decScope();
        lastMainMemory = mainMemoryStack.pop();
    }

    private void parameter(Token[] tokens) {

        paramsNum++;
        ArrayList<String> list = paramTypes.get(funcToken.name);

        PB.add("(SUB, " + display[1] + ", #" + (1 + paramsNum) * 4 + ", " + lastTmpMemory + ")");   //parameter assignment
        PB.add("(ADD, " + display[1] + ", #" + (paramsNum - 1) * 4 + ", " + (lastTmpMemory + 4) + ")");
        PB.add("(ASSIGN, @" + lastTmpMemory + ", @" + (lastTmpMemory + 4) + ")");
        lastTmpMemory += 8;

        Target t;
        if (tokens[1].type.equals("]")) {   //array passing
            t = getTarget(tokens[3]);
            t.address = lastMainMemory;
            t.type = "pointer";
            t.isParam = true;
            list.add("pointer");
        } else {
            t = getTarget(tokens[1]);
            t.address = lastMainMemory;
            t.type = "int";
            t.isParam = true;
            list.add("int");
        }
        lastMainMemory += 4;
    }

    private void output(Token[] tokens) {
        String exp = SS.pop();
        PB.add("(PRINT, " + exp + ")");
    }

    private void jpFunc(Token[] tokens) {
        int argsNum = argsStack.peek();
        if (argsNum != 0) { //arguments number mismatched with declared function
            System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE +
                    "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) + ": Arguments' number of function "
                    + calleeToken.name + " don't match!" + Color.ANSI_RESET);
            System.exit(0);
        }
        argsStack.pop();

        int s = PB.size() + 7;
        PB.add("(ADD, " + display[1] + ", #" + (lastMainMemory) + ", " + lastTmpMemory + ")");  //return line address
        PB.add("(ASSIGN, #" + s + ", @" + lastTmpMemory + ")");
        lastTmpMemory += 4;
        Target t = getTarget(calleeToken);
        PB.add("(ADD, " + display[1] + ", #" + (8 + 4 * t.paramsNum + lastMainMemory) + ", " + lastTmpMemory + ")"); //display exchange
        PB.add("(ASSIGN, " + display[1] + ", @" + lastTmpMemory + ")");
        lastTmpMemory += 4;
        PB.add("(ADD, " + display[1] + ", #" + (12 + 4 * t.paramsNum + lastMainMemory) + ", " + lastTmpMemory + ")");
        PB.add("(ASSIGN, " + lastTmpMemory + ", " + display[1] + ")");
        lastTmpMemory += 4;

        PB.add("(JP, " + t.address + ")");
        PB.add("(ADD, " + display[1] + ", #" + (lastMainMemory + 4) + ", " + lastTmpMemory + ")");
        PB.add("(ASSIGN, @" + lastTmpMemory + ", " + (lastTmpMemory + 4) + ")");    //return value of fynction assigned to a tmp
        lastTmpMemory += 8;
        SS.push(Long.toString(lastTmpMemory - 4));
    }

    private void args(Token[] tokens) {
        int argsNum = argsStack.pop();
        String exp = SS.pop();
        PB.add("(ADD, " + display[1] + ", #" + (8 + (argsNum - 1) * 4) + ", " + lastTmpMemory + ")");   //arguments assignment
        PB.add("(ASSIGN, " + exp + ", @" + lastTmpMemory + ")");
        lastTmpMemory += 4;
        ArrayList<String> list = paramTypes.get(calleeToken.name);
        if ((!tokens[1].type.equals("ID") || !getTarget(tokens[1]).type.equals("pointer")) && list.get(list.size() - argsNum).equals("pointer")) {
            System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE
                    + "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) + ": Passed parameters to function "
                    + calleeToken.name + " mismatched" + Color.ANSI_RESET);
            System.exit(0);
        }
        argsNum--;  //check mark a parameter
        argsStack.push(argsNum);
    }

    private void op(Token[] tokens) {
        SS.push(tokens[1].type);
    }

    private void funcEnd(Token[] tokens) {
        Target target = getTarget(funcToken);
        if (!returnSeen) {
            if (funcToken.name.equals("main") && !getTarget(funcToken).isVoid) {
                System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE
                        + "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) +
                        ": Return type of function main should be void" + Color.ANSI_RESET);
                System.exit(0);
            }
            if (funcToken.name.equals("main") && paramsNum != 0) {
                System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE +
                        "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) +
                        ": Argument of function main should be void" + Color.ANSI_RESET);
                System.exit(0);
            }
            if (!getTarget(funcToken).isVoid) {
                System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE +
                        "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) +
                        ": Return type of function " + funcToken.name + " mismatched" + Color.ANSI_RESET);
                System.exit(0);
            }
            Scanner.decScope();
            lastMainMemory = mainMemoryStack.pop();
            PB.add("(SUB, " + display[1] + ", #" + 4 + ", " + lastTmpMemory + ")");
            PB.add("(ASSIGN, @" + lastTmpMemory + ", " + display[1] + ")");     //display exchange
            lastTmpMemory += 4;
            PB.add("(SUB, " + display[1] + ", #" + (12 + 4 * paramsNum) + ", " + lastTmpMemory + ")");  //jump back and continue code
            PB.add("(JP, @" + lastTmpMemory + ")");
            lastTmpMemory += 4;
            target.paramsNum = paramsNum;
            paramsNum = 0;  //reset
        }
        returnSeen = false;
    }

    private void funcSetup(Token[] tokens) {
        int i = Integer.parseInt(SS.peek());
        PB.set(i, "(JP, " + PB.size() + ")"); //main jump

        paramTypes.put(tokens[1].name, new ArrayList<String>());

        Target target = getTarget(tokens[1]);
        if (!target.type.equals("")) {
            System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE +
                    "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) +
                    ": Duplicate declaration of " + tokens[1].name + Color.ANSI_RESET);
            System.exit(0);
        }
        target.address = (long) PB.size();
        target.type = "function";
        target.scope = Scanner.symbolTable.size();
        target.paramsNum = paramsNum;
        target.isVoid = tokens[2].name.equals("void");

        if (tokens[1].name.equals("main"))  //display assignment
            PB.add("(ASSIGN, #108, " + display[1] + ")");

        mainMemoryStack.push(lastMainMemory);
        lastMainMemory = 0;
        funcToken = tokens[1];
        Scanner.incScope();
    }

    private void arrayMemory(Token[] tokens) {
        Index idx = new Index(tokens[2].name);
        Target target = Scanner.lookup(idx);

        assert target != null;
        target.length = Integer.parseInt(tokens[0].name);
        target.address = lastMainMemory;
        target.scope = Scanner.symbolTable.size();
        target.dimension = 1;
        target.type = "pointer";
        PB.add("(ADD, " + display[fixScope(target.scope - 2)] + ", #" + target.address + ", " + lastTmpMemory + ")");
        PB.add("(ADD, #" + 4 + ", " + lastTmpMemory + ", " + (lastTmpMemory + 4) + ")");
        PB.add("(ASSIGN, " + (lastTmpMemory + 4) + ", @" + lastTmpMemory + ")");
        lastMainMemory += (Integer.parseInt(tokens[0].name) + 1) * 4;   //array fixed length
        lastTmpMemory += 8;
    }

    private void varMemory(Token[] tokens) {
        Index idx = new Index(tokens[1].name);
        Target target = Scanner.lookup(idx);

        target.address = lastMainMemory;
        target.scope = Scanner.symbolTable.size();
        target.dimension = 0;
        target.type = "int";
        lastMainMemory += 4;
    }

    public void gc(Token[] tokens) {
        SS.pop();
        PB.set(1, "(JP, " + PB.size() + ")");
    }

    public void emptyStack() {
        while (SS.size() > 1)
            SS.pop();
    }

    private void jpMain(Token[] tokens) {

    }

    private void callee(Token[] tokens) {
        if (!returnSeen) {
            returnSeen = true;
            Scanner.decScope();
            lastMainMemory = mainMemoryStack.pop();
            if (funcToken.name.equals("main") && paramsNum != 0) {
                System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE +
                        "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) +
                        ": Argument of function main should be void" + Color.ANSI_RESET);
                System.exit(0);
            }
            if (!tokens[2].name.equals("return")) {
                if (funcToken.name.equals("main") && !getTarget(funcToken).isVoid) {
                    System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE +
                            "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) +
                            ": Return type of function main should be void" + Color.ANSI_RESET);
                    System.exit(0);
                }
                if (getTarget(funcToken).isVoid) {
                    System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE +
                            "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) +
                            ": Return type of function " + funcToken.name + " mismatched" + Color.ANSI_RESET);
                    System.exit(0);
                }

                PB.add("(SUB, " + display[1] + ", #" + (8 + 4 * paramsNum) + ", " + lastTmpMemory + ")");
                PB.add("(ASSIGN, " + SS.pop() + ", @" + lastTmpMemory + ")");
                lastTmpMemory += 4;
            } else if (!getTarget(funcToken).isVoid) {
                System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE +
                        "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) +
                        ": Return type of function " + funcToken.name + " mismatched" + Color.ANSI_RESET);
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
        } else {
            System.out.println(Color.ANSI_RED + "SEMANTIC WARNING" + Color.ANSI_BLUE + "Line " +
                    Scanner.line + " Character " + (Scanner.pointer + 1) +
                    ": Unreachable statement" + Color.ANSI_RESET);
            SS.pop();
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
    }

    private void lt(Token[] tokens) {
        String var2 = SS.pop();
        String var1 = SS.pop();

        PB.add("(LT, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;
    }

    private void eq(Token[] tokens) {
        String var2 = SS.pop();
        String var1 = SS.pop();

        PB.add("(EQ, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;
    }

    private void jp(Token[] tokens) {
        int i = Integer.parseInt(SS.pop());
        PB.set(i, "(JP, " + PB.size() + ")");
    }

    private void jpfSave(Token[] tokens) {
        int i = Integer.parseInt(SS.pop());
        String exp = SS.pop();
        PB.add("");
        PB.set(i, "(JPF, " + exp + ", " + PB.size() + ")");
        SS.push(Integer.toString(PB.size() - 1));
    }

    private void jpf(Token[] tokens) {
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
            System.out.println(Color.ANSI_RED + "SEMANTIC ERROR" + Color.ANSI_BLUE +
                    "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) +
                    ": Type mismatched. float number cast to int" + Color.ANSI_RESET);
            Double d = Double.parseDouble(tokens[0].name);
            tokens[0].name = d.intValue() + "";
        }
        SS.push("#" + tokens[0].name);
    }

    private void caller(Token[] tokens) {
        Target target = getTarget(tokens[1]);
        calleeToken = tokens[1];
        argsStack.push(target.paramsNum);
        if (target.type.equals("")) {
            System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE +
                    "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) +
                    ": Function " + tokens[1].name + " hasn't been declared" + Color.ANSI_RESET);
            System.exit(0);
        }
    }

    private void add(Token[] tokens) {
        String var2 = SS.pop();
        String var1 = SS.pop();

        PB.add("(ADD, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");

        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;
    }

    private void sub(Token[] tokens) {
        String var2 = SS.pop();
        String var1 = SS.pop();

        PB.add("(SUB, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");

        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;
    }

    private void mul(Token[] tokens) {
        String var2 = SS.pop();
        String var1 = SS.pop();

        PB.add("(MULT, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");

        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;
    }

    private void div(Token[] tokens) {
        String var2 = SS.pop();
        String var1 = SS.pop();

        PB.add("(DIV, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");

        SS.push(Long.toString(lastTmpMemory));
        lastTmpMemory += 4;
    }

    private void arrayPid(Token[] tokens) {
        String exp = SS.pop();
        String arr = SS.pop();
        Target top = IDsStack.peek();

        if (!top.isParam) {
            PB.add("(LT, " + exp + ", #" + top.length + " ," + lastTmpMemory + ")");
            PB.add("(JPF, " + lastTmpMemory + ", 1)");
            lastTmpMemory += 4;
        }

        PB.add("(MULT, #4, " + exp + ", " + lastTmpMemory + ")");
        PB.add("(ADD, " + lastTmpMemory + ", " + arr + ", " + (lastTmpMemory + 4) + ")");
        lastTmpMemory += 4;
        SS.push("@" + Long.toString(lastTmpMemory));
        lastTmpMemory += 4;

        if (!top.type.equals("pointer")) {
            System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE +
                    "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) +
                    " Type mismatched" + Color.ANSI_RESET);
            System.exit(0);
        }
    }

    private void pid(Token[] tokens) {
        Target t = getTarget(tokens[1]);
        if (t == null || t.type.equals("")) {
            System.out.println(Color.ANSI_RED + "SEMANTIC ERROR:\n" + Color.ANSI_BLUE +
                    "Line " + Scanner.line + " Character " + (Scanner.pointer + 1) +
                    ": ID " + tokens[1].name + " hasn't been declared" + Color.ANSI_RESET);
            System.exit(0);
        }
        PB.add("(ADD, " + display[fixScope(t.scope - 2)] + ", #" + t.address + ", " + lastTmpMemory + ")");
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
        PB.add("(ASSIGN, " + exp + ", " + var + ")");
    }

    private int fixScope(int scope) {
        return scope > 0 ? 1 : 0;
    }

    private Target getTarget(Token t) {
        Index idx = new Index(t.name);
        return Scanner.lookup(idx);
    }

}