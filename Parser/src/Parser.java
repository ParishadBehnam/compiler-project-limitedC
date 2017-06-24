/**
 * Created by afra on 6/5/17.
 */


import java.util.*;

public class Parser {
    private ArrayList<Integer> grammarLength;
    private ArrayList<String> grammarLHS;
    private HashMap<String, ArrayList<String>> follows;
    private Stack<String> parsStack;
    private ParseTable parseTable;
    private Token[] codeGenTokens;
    private CodeGenerator cg;

    Parser() {
        Grammar limitedC = new Grammar();
        grammarLength = limitedC.getLen();
        grammarLHS = limitedC.getLHS();
        follows = limitedC.getFollows();

        parsStack = new Stack();
        parseTable = new ParseTable();
        codeGenTokens = new Token[4];
        cg = new CodeGenerator();

        parsStack.push("$");
        parsStack.push("0");

    }

    void start() throws Exception {
        Token t = Scanner.getToken();
        codeGenTokens[0] = t;
        while (true) {
            String res = parseTable.actionTable.get(Integer.parseInt(parsStack.peek())).get(t.type);
            if (res == null) {
                System.out.println(Color.ANSI_RED + "PANIC MODE - PARSER" + Color.ANSI_RESET);
                printParsError();

                String state = "";
                int firstState = Integer.parseInt(parsStack.peek());
                L1:
                while (parsStack.size() > 0) {
                    state = parsStack.peek();
                    if (!state.matches("\\d+")) {
                        parsStack.pop();
                        continue;
                    }

                    int stateNum = Integer.parseInt(state);
                    if (parseTable.gotoTable.get(stateNum).size() > 0) {

                        HashMap<String, Integer> target = parseTable.gotoTable.get(stateNum);
                        Set<String> NT = target.keySet();
                        String lastToken = "";
                        while (!(t.type.equals("$") && lastToken.equals("$"))) {
                            lastToken = t.type;
                            t = getTokenFromScanner(t);
                            for (String s : NT) {
                                if (follows.get(s).contains(t.type)) {
//                                    dummyReduce(s, firstState, t);
                                    parsStack.push(s);
                                    parsStack.push(Integer.toString(target.get(s)));
                                    break L1;
                                }


                            }
                        }
                        break L1;
                    } else parsStack.pop();
                }

                res = parseTable.actionTable.get(Integer.parseInt(parsStack.peek())).get(t.type);
                if (res == null) {
                    cg.gc(codeGenTokens);
                }

            }

            assert res != null;
            if (res.equals("accept")) {
                return;
            }
            if (res.charAt(0) == 's') {
                parsStack.push(t.type);
                int state = Integer.parseInt(res.substring(1));
                parsStack.push(Integer.toString(state));
                Token tmp = new Token(t.type, t.name);

                t = getTokenFromScanner(t);

                if (t.type.equals("$") && tmp.type.equals("$")) return;
            } else if (res.charAt(0) == 'r') {
                int idx = Integer.parseInt(res.substring(1));
                int len = 2 * grammarLength.get(idx - 1);

                for (int i = 0; i < len; i++)
                    parsStack.pop();

                int gotoIdx = Integer.parseInt(parsStack.peek());
                parsStack.push(grammarLHS.get(idx - 1));


                if (idx >= 60 && idx < 84) cg.generateCode(grammarLHS.get(idx - 1), codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("ArgList"))
                    cg.generateCode("ArgList", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("Call"))
                    cg.generateCode("Call", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("Var"))
                    cg.generateCode("Var", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("Param"))
                    cg.generateCode("parameter", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("X25"))
                    cg.generateCode("X25", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("X26"))
                    cg.generateCode("X26", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("X27"))
                    cg.generateCode("X27", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("SelectionStmt"))
                    cg.generateCode("stmt", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("IterationStmt"))
                    cg.generateCode("stmt", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("X28"))
                    cg.generateCode("X28", codeGenTokens);


                parsStack.push(Integer.toString(parseTable.gotoTable.get(gotoIdx).get(grammarLHS.get(idx - 1))));

            }
        }
    }

    public void printStack() {
        Stack<String> copiedStack = (Stack<String>) parsStack.clone();
        for (int i = 0; i < parsStack.size(); i++) {
            System.out.print(copiedStack.pop() + " ");
        }
        System.out.println("");
        System.out.println("=====");
    }

    private Token getTokenFromScanner(Token t) {
        Token toRet;
        if (codeGenTokens[2] != null)
            codeGenTokens[3] = new Token(codeGenTokens[2]);
        if (codeGenTokens[1] != null)
            codeGenTokens[2] = new Token(codeGenTokens[1]);
        codeGenTokens[1] = new Token(t);
        toRet = Scanner.getToken();
        codeGenTokens[0] = new Token(toRet);
        return toRet;
    }

    private void dummyReduce(String lhs, int state, Token t) throws Exception {
        String tmp = parseTable.actionTable.get(state).get(t.type);
        if (tmp != null && tmp.charAt(0) == 'r') {
            if (lhs.charAt(0) == 'X') {
                int idx = Integer.parseInt(lhs.substring(1));
                cg.generateCode(lhs, codeGenTokens);
            }
            if (lhs.equals("ArgList"))
                cg.generateCode("ArgList", codeGenTokens);
            if (lhs.equals("Call"))
                cg.generateCode("Call", codeGenTokens);
            if (lhs.equals("Var"))
                cg.generateCode("Var", codeGenTokens);
            if (lhs.equals("Param"))
                cg.generateCode("parameter", codeGenTokens);
            if (lhs.equals("SelectionStmt"))
                cg.generateCode("stmt", codeGenTokens);
            if (lhs.equals("IterationStmt"))
                cg.generateCode("stmt", codeGenTokens);
            if (lhs.equals("X28"))
                cg.generateCode("X28", codeGenTokens);

        }
    }

    private void printParsError() {
        System.out.println(Color.ANSI_BLUE + "Line " + Scanner.line + " Character " + (Scanner.pointer + 1)
                + " : Unexpected token. Allowed Tokens are: " + Color.ANSI_RESET);
        int state = Integer.parseInt(parsStack.peek());
        ArrayList<String> permitted = new ArrayList<>();
        permitted.addAll(parseTable.actionTable.get(state).keySet());

        for (int i = 0; i < permitted.size(); i++) {
            if (i != 0)
                System.out.print(Color.ANSI_BLUE + ", " + permitted.get(i) + Color.ANSI_RESET);
            else System.out.print(Color.ANSI_BLUE + permitted.get(i) + Color.ANSI_RESET);
        }
        System.out.println("");
    }

}



