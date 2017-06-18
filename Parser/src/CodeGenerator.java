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
    static boolean errorSeen = false;

    public CodeGenerator() {
        display[0] = 100;
        display[1] = 104;
        PB.add("(JP, " + (PB.size() + 2) + ")");
        PB.add("");
        PB.add("(ASSIGN, #400, " + display[0] + ")");
    }

    public static void print() {
        if(!errorSeen)
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
        try {
            paramsNum++;
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
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }

    }

    private void output(Token[] tokens) {
        try {

//            System.out.println("output");
//            printStack();
            String exp = SS.pop();
            PB.add("(PRINT, " + exp + ")");
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void jpFunc(Token[] tokens) {
        try {
            int argsNum = argsStack.peek();
            if (argsNum != 0) {
                System.out.println(argsNum);
                System.out.println("SEMANTIC ERROR: arguments' number of function " + calleeToken.name + " don't match!");
                System.exit(0);
            }
            argsStack.pop();
            int s = PB.size() + 7;
            PB.add("(ADD, " + display[1] + ", #" + (lastMainMemory) + ", " + lastTmpMemory + ")");
            PB.add("(ASSIGN, #" + s + ", @" + lastTmpMemory + ")");
            lastTmpMemory += 4;
            Target t = getTarget(calleeToken);
            PB.add("(ADD, " + display[1] + ", #" + (8 + 4 * t.paramsNum) + ", " + lastTmpMemory + ")");
            PB.add("(ASSIGN, " + display[1] + ", @" + lastTmpMemory + ")");
            lastTmpMemory += 4;
            PB.add("(ADD, " + display[1] + ", #" + (12 + 4 * t.paramsNum) + ", " + lastTmpMemory + ")");
            PB.add("(ASSIGN, " + lastTmpMemory + ", " + display[1] + ")");
            lastTmpMemory += 4;

            PB.add("(JP, " + t.address + ")");
            PB.add("(ADD, " + display[1] + ", #" + (lastMainMemory + 4) + ", " + lastTmpMemory + ")");
            PB.add("(ASSIGN, @" + lastTmpMemory + ", " + (lastTmpMemory + 4) + ")");
            lastTmpMemory += 8;
            SS.push(Long.toString(lastTmpMemory - 4));

//            printStack();
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }


    }

    private void args(Token[] tokens) {
        try {
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
            argsNum--;
            argsStack.push(argsNum);
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void op(Token[] tokens) {
        try {
            SS.push(tokens[1].type);
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void funcEnd(Token[] tokens) {
        try {
            Target target = getTarget(funcToken);
            if (!returnSeen) {
                if (funcToken.name.equals("main") && !getTarget(funcToken).isVoid) {
                    System.out.println("SEMANTIC ERROR: return type of function main should be void");
                    System.exit(0);
                }
                if (funcToken.name.equals("main") && paramsNum != 0) {
                    System.out.println("SEMANTIC ERROR: argument of function main should be void");
                    System.exit(0);
                }
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
//                System.out.println("targeeeeet" + target.paramsNum);
                paramsNum = 0;
            }
            returnSeen = false;
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void funcSetup(Token[] tokens) {
        try {
            int i = Integer.parseInt(SS.peek());
            PB.set(i, "(JP, " + PB.size() + ")"); //main

            paramTypes.put(tokens[1].name, new ArrayList<String>());

            Target target = getTarget(tokens[1]);
            if (!target.type.equals("")) {
                System.out.println("SEMANTIC ERROR: duplicate declaration of " + tokens[1].name);
                System.exit(0);
            }
            target.address = (long) PB.size();
            target.type = "function";
            target.scope = Scanner.symbolTable.size();
            target.paramsNum = paramsNum;
            target.isVoid = tokens[2].name.equals("void");

            if (tokens[1].name.equals("main"))
                PB.add("(ASSIGN, #" + (display[1] + 4) + ", " + display[1] + ")");

            oldLastMainMemory = lastMainMemory;
            lastMainMemory = 0;
            funcToken = tokens[1];
            Scanner.incScope();
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void arrayMemory(Token[] tokens) {
        try {
            Index idx = new Index(tokens[2].name);
            Target target = Scanner.lookup(idx);
//            System.out.println(target.type);
//        if (!target.type.equals("")) {
//            System.out.println("SEMANTIC ERROR: duplicate declaration of " + tokens[2].name);
//            System.exit(0);
//        }
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

        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void varMemory(Token[] tokens) {
        try {
            Index idx = new Index(tokens[1].name);
            Target target = Scanner.lookup(idx);
//            System.out.println(target.type);

            target.address = lastMainMemory;
            target.scope = Scanner.symbolTable.size();
            target.dimension = 0;
            target.type = "int";
            lastMainMemory += 4;
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }

    }

    public void gc(Token[] tokens) {
        try {
            SS.pop();
            PB.set(1, "(JP, " + PB.size() + ")");
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    public void emptyStack() {
        try {
            while (SS.size() > 1)
                SS.pop();
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void jpMain(Token[] tokens) {

    }

    private void callee(Token[] tokens) {
        try {
            returnSeen = true;
            Scanner.decScope();
            lastMainMemory = oldLastMainMemory;
            if (funcToken.name.equals("main") && paramsNum != 0) {
                System.out.println("SEMANTIC ERROR: argument of function main should be void");
                System.exit(0);
            }
            if (!tokens[2].name.equals("return")) {
                if (funcToken.name.equals("main") && !getTarget(funcToken).isVoid) {
                    System.out.println("SEMANTIC ERROR: return type of function main should be void");
                    System.exit(0);
                }
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
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void paramAssign(Token[] tokens) {
        try {
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
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void loop(Token[] tokens) {
        try {

            int i = Integer.parseInt(SS.pop());
            String exp = SS.pop();
            int label = Integer.parseInt(SS.pop());
            PB.add("(JP, " + label + ")");
            PB.set(i, "(JPF, " + exp + ", " + PB.size() + ")");
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void label(Token[] tokens) {
        try {
            SS.push(Integer.toString(PB.size()));
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void and(Token[] tokens) {
        try {
            String var2 = SS.pop();
            String var1 = SS.pop();

            PB.add("(AND, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
            SS.push(Long.toString(lastTmpMemory));
            lastTmpMemory += 4;
//        System.out.println(PB.get(PB.size() - 1) + "____");
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void lt(Token[] tokens) {
        try {
            String var2 = SS.pop();
            String var1 = SS.pop();

            PB.add("(LT, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
            SS.push(Long.toString(lastTmpMemory));
            lastTmpMemory += 4;
//        System.out.println(PB.get(PB.size() - 1) + "____");
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void eq(Token[] tokens) {
        try {
            String var2 = SS.pop();
            String var1 = SS.pop();

            PB.add("(EQ, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
            SS.push(Long.toString(lastTmpMemory));
            lastTmpMemory += 4;
            System.out.println(lastTmpMemory - 4 + "hehe");
//        System.out.println(PB.get(PB.size() - 1) + "____");
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void jp(Token[] tokens) {
        try {
            int i = Integer.parseInt(SS.pop());
            PB.set(i, "(JP, " + PB.size() + ")");
//        System.out.println(PB.get(i) + "^^^^");
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void jpfSave(Token[] tokens) {
        try {
            int i = Integer.parseInt(SS.pop());
            String exp = SS.pop();
            PB.add("");
            PB.set(i, "(JPF, " + exp + ", " + PB.size() + ")");
            SS.push(Integer.toString(PB.size() - 1));
//        System.out.println(PB.get(i) + "^^^^");
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void jpf(Token[] tokens) {
        try {
//            printStack();
            int i = Integer.parseInt(SS.pop());
            String exp = SS.pop();
            PB.set(i, "(JPF, " + exp + ", " + PB.size() + ")");
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void save(Token[] tokens) {
        try {
            PB.add("");
            SS.push(Integer.toString(PB.size() - 1));
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void pidNum(Token[] tokens) {
        try {
            if (tokens[0].name.matches("\\d+\\.\\d+")) {
                System.out.println("SEMANTIC ERROR: type mismatched. int expected");
                Double d = Double.parseDouble(tokens[0].name);
                tokens[0].name = d.intValue() + "";
            }
//            System.out.println("pid" + tokens[0].name);
            SS.push("#" + tokens[0].name);
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void caller(Token[] tokens) {
        try {
            Target target = getTarget(tokens[1]);
            calleeToken = tokens[1];
            argsStack.push(target.paramsNum);
            System.out.println("argsssssss caller" + target.type);
            if (target.type.equals("")) {
                System.out.println("SEMANTIC ERROR: function " + tokens[1].name + " hasn't been declared");
                System.exit(0);
            }

        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void add(Token[] tokens) {
        try {
            String var2 = SS.pop();
            String var1 = SS.pop();

            PB.add("(ADD, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");

            SS.push(Long.toString(lastTmpMemory));
            lastTmpMemory += 4;
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void sub(Token[] tokens) {
        try {
            String var2 = SS.pop();
            String var1 = SS.pop();

            PB.add("(SUB, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");

            SS.push(Long.toString(lastTmpMemory));
            lastTmpMemory += 4;
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void mul(Token[] tokens) {
        try {
            String var2 = SS.pop();
            String var1 = SS.pop();

            PB.add("(MULT, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");

            SS.push(Long.toString(lastTmpMemory));
            lastTmpMemory += 4;
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void div(Token[] tokens) {
        try {
            String var2 = SS.pop();
            String var1 = SS.pop();

            PB.add("(DIV, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");

            SS.push(Long.toString(lastTmpMemory));
            lastTmpMemory += 4;
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

//    private void operation(Token[] tokens) {
//        String var2 = SS.pop();
//        String op = SS.pop();
//        String var1 = SS.pop();
//
//        System.out.println(var1 + " heeh " + var2);
//
//        switch (op) {
//            case "+":
//                PB.add("(ADD, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
//                break;
//            case "-":
//                PB.add("(SUB, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
//                break;
//            case "*":
//                PB.add("(MULT, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
//                break;
//            case "/":
//                PB.add("(DIV, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
//                break;
//            default:
//                PB.add("(ADD, " + var1 + ", " + var2 + ", " + Long.toString(lastTmpMemory) + ")");
//                break;
//        }
//
////        System.out.println(op + PB.get(PB.size() - 1) + "____");
//
//        SS.push(Long.toString(lastTmpMemory));
//        lastTmpMemory += 4;
//
//    }

    private void arrayPid(Token[] tokens) {
        try {
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
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void pid(Token[] tokens) {
        try {
        Target t = getTarget(tokens[1]);
        if (t.type.equals("")) {
            System.out.println("SEMANTIC ERROR: ID " + tokens[1].name + " hasn't been declared");
            System.exit(0);
        }
        PB.add("(ADD, " + display[t.scope - 1] + ", #" + t.address + ", " + lastTmpMemory + ")");
        SS.push("@" + Long.toString(lastTmpMemory));
        IDsStack.push(t);
        lastTmpMemory += 4;

    }catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        } }

    private void pidPop(Token[] tokens) {
        try {
            IDsStack.pop();
        }catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private void assign(Token[] tokens) {
        try {
            String exp = SS.pop();
            String var = SS.pop();
//        System.out.println("(ASSIGN, " + exp + ", " + var + "____");
//        System.out.println("size " + PB.size());
            PB.add("(ASSIGN, " + exp + ", " + var + ")");
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
        }
    }

    private Target getTarget(Token t) {
        try {
            Index idx = new Index(t.name);
            return Scanner.lookup(idx);
        } catch (Exception e) {
            System.out.println("Panic mode failed, genarated code till now:");
            print();
            return null;
        }
    }

//    private Target getTargetByAddress(Long addr) {
//        HashSet<Index> keys;
//        for (HashMap<Index, Target> map : Scanner.symbolTable) {
//            keys = new HashSet<>(map.keySet());
//        }
//        return Scanner.lookup(idx);
//    }

}
