/**
 * Created by afra on 6/5/17.
 */


import java.util.*;

public class Parser {
    ArrayList<Integer> grammarLength = new ArrayList<Integer>
            (Arrays.asList(4, 2, 1, 1, 1, 2, 2, 1, 2, 2, 5, 1, 1, 4, 2, 2, 4, 4,
                    2, 0, 2, 0, 1, 1, 1, 1, 1, 5, 1, 7, 10, 8, 3, 4, 2, 6, 1, 1, 4,
                    1, 4, 4, 4, 1, 1, 1, 4, 1, 1, 1, 3, 1, 2, 2, 5, 1, 0, 3, 1));
    ArrayList<String> grammarLHS = new ArrayList<>(
            Arrays.asList("Program", "DeclarationList", "DeclarationList", "Declaration", "Declaration",
                    "VarDeclaration", "FunDeclaration", "TypeSpecifier", "FunDeclaration", "R1",
                    "R3", "Params", "Params", "ParamList", "ParamList",
                    "Param", "Param", "CompoundStmt", "LocalDeclarations", "LocalDeclarations",
                    "StatementList", "StatementList", "Statement", "Statement", "Statement",
                    "Statement", "Statement", "ExpressionStmt", "ExpressionStmt", "SelectionStmt",
                    "SelectionStmt", "IterationStmt", "ReturnStmt", "ReturnStmt", "Var",
                    "Var", "GenExpression", "GenExpression", "RelExpression", "RelExpression",
                    "RelTerm", "RelTerm", "Expression", "Expression", "AddOp",
                    "AddOp", "Term", "Term", "MulOp", "MulOp",
                    "Factor", "Factor", "Factor", "Factor", "Call",
                    "Args", "Args", "ArgList", "ArgList", "X1",
                    "X2", "X3", "X4", "X5", "X6",
                    "X7", "X8", "X9", "X10", "X11",
                    "X12", "X13", "X14", "X15", "X16",
                    "X17", "X18", "X19", "X20", "X21",
                    "X22", "X23", "X24", "R3", "R2", "Statement", "X25", "X26", "X27", "X28", "Expression", "Term"));

    HashMap<String, ArrayList<String>> follows = new HashMap<>();


    Stack<String> parsStack = new Stack();
    ParseTable parseTable = new ParseTable();
    Token[] codeGenTokens = new Token[4];
    CodeGenerator cg = new CodeGenerator();

    public Parser() {
        for (int i = 0; i < 24; i++) {
            grammarLength.add(0);
        }
        grammarLength.add(2);
        grammarLength.add(7);
        grammarLength.add(6);
        grammarLength.add(0);
        grammarLength.add(0);
        grammarLength.add(0);
        grammarLength.add(0);
        grammarLength.add(4);
        grammarLength.add(4);
        parsStack.push("$");
        parsStack.push("0");

        follows.put("Program", new ArrayList<>(Arrays.asList("$")));
        follows.put("X19", new ArrayList<>(Arrays.asList("$")));
        follows.put("DeclarationList", new ArrayList<>(Arrays.asList("int", "void", "EOF")));
        follows.put("Declaration", new ArrayList<>(Arrays.asList("int", "void", "EOF")));
        follows.put("FunDeclaration", new ArrayList<>(Arrays.asList("int", "void", "EOF")));
        follows.put("R2", new ArrayList<>(Arrays.asList("int", "void", "EOF")));
        follows.put("X23", new ArrayList<>(Arrays.asList("int", "void", "EOF")));
        follows.put("VarDeclaration", new ArrayList<>(Arrays.asList("int", "void", "EOF", ";", "ID", "{", "if", "while", "return", "int", "}")));
        follows.put("R1", new ArrayList<>(Arrays.asList("int", "void", "EOF", ";", "ID", "{", "if", "while", "return", "int", "}")));
        follows.put("R3", new ArrayList<>(Arrays.asList("int", "void", "EOF", ";", "ID", "{", "if", "while", "return", "int", "}")));
        follows.put("LocalDeclarations", new ArrayList<>(Arrays.asList(";", "ID", "{", "if", "while", "return", "int", "}")));
        follows.put("Params", new ArrayList<>(Arrays.asList(")")));
        follows.put("Args", new ArrayList<>(Arrays.asList(")")));
        follows.put("ParamList", new ArrayList<>(Arrays.asList(")", ",")));
        follows.put("X16", new ArrayList<>(Arrays.asList(")", ",")));
        follows.put("ArgList", new ArrayList<>(Arrays.asList(")", ",")));
        follows.put("Param", new ArrayList<>(Arrays.asList(")", ",")));
        follows.put("CompoundStmt", new ArrayList<>(Arrays.asList("int", "void", "EOF", "output", "else", ";", "ID", "{", "if", "while", "return", "}")));
        follows.put("Statement", new ArrayList<>(Arrays.asList("else", ";", "ID", "{", "if", "output", "while", "return", "}")));
        follows.put("X25", new ArrayList<>(Arrays.asList("else", ";", "ID", "{", "if", "output", "while", "return", "}")));
        follows.put("ExpressionStmt", new ArrayList<>(Arrays.asList("else", ";", "output", "ID", "{", "if", "while", "return", "}")));
        follows.put("SelectionStmt", new ArrayList<>(Arrays.asList("else", ";", "ID", "output", "{", "if", "while", "return", "}")));
        follows.put("X8", new ArrayList<>(Arrays.asList("else", ";", "ID", "output", "{", "if", "while", "return", "}")));
        follows.put("X10", new ArrayList<>(Arrays.asList("else", ";", "ID", "output", "{", "if", "while", "return", "}")));
        follows.put("IterationStmt", new ArrayList<>(Arrays.asList("else", ";", "ID", "output", "{", "if", "while", "return", "}")));
        follows.put("X15", new ArrayList<>(Arrays.asList("else", ";", "ID", "output", "{", "if", "while", "return", "}")));
        follows.put("ReturnStmt", new ArrayList<>(Arrays.asList("else", ";", "ID", "{", "output", "if", "while", "return", "}")));
        follows.put("X17", new ArrayList<>(Arrays.asList("else", ";", "ID", "{", "output", "if", "while", "return", "}")));
        follows.put("X7", new ArrayList<>(Arrays.asList("int", "void", ";", "ID", "{", "output", "if", "while", "return")));
        follows.put("StatementList", new ArrayList<>(Arrays.asList(";", "ID", "{", "if", "output", "while", "return", "}")));
        follows.put("Expression", new ArrayList<>(Arrays.asList(")", "<", "==", "+", "-", ",", "&&", ";", "]")));
        follows.put("X26", new ArrayList<>(Arrays.asList(")", "<", "==", "+", "-", ",", "&&", ";", "]")));
        follows.put("Term", new ArrayList<>(Arrays.asList("*", "/", ")", "<", "==", "+", "-", ",", "&&", ";", "]")));
        follows.put("X27", new ArrayList<>(Arrays.asList("*", "/", ")", "<", "==", "+", "-", ",", "&&", ";", "]")));
        follows.put("X28", new ArrayList<>(Arrays.asList("*", "/", ")", "<", "==", "+", "-", ",", "&&", ";", "]")));
        follows.put("X4", new ArrayList<>(Arrays.asList("*", "/", ")", "<", "==", "+", "-", ",", "&&", ";", "]")));
        follows.put("Factor", new ArrayList<>(Arrays.asList("*", "/", ")", "<", "==", "+", "-", ",", "&&", ";", "]")));
        follows.put("X5", new ArrayList<>(Arrays.asList("*", "/", ")", "<", "==", "+", "-", ",", "&&", ";", "]")));
        follows.put("X2", new ArrayList<>(Arrays.asList("=" + "[" + "*", "/", ")", "<", "==", "+", "-", ",", "&&", ";", "]")));
        follows.put("X3", new ArrayList<>(Arrays.asList("=" + "*", "/", ")", "<", "==", "+", "-", ",", "&&", ";", "]")));
        follows.put("Call", new ArrayList<>(Arrays.asList("*", "/", ")", "<", "==", "+", "-", ",", "&&", ";", "]")));
        follows.put("Var", new ArrayList<>(Arrays.asList("*", "/", ")", "<", "==", "+", "-", ",", "&&", ";", "]", "=")));
        follows.put("RelExpression", new ArrayList<>(Arrays.asList("&&", ";", ")")));
        follows.put("X13", new ArrayList<>(Arrays.asList("&&", ";", ")")));
        follows.put("RelTerm", new ArrayList<>(Arrays.asList("&&", ";", ")")));
        follows.put("X11", new ArrayList<>(Arrays.asList("&&", ";", ")")));
        follows.put("X12", new ArrayList<>(Arrays.asList("&&", ";", ")")));
        follows.put("GenExpression", new ArrayList<>(Arrays.asList(";", ")")));
        follows.put("AddOp", new ArrayList<>(Arrays.asList("NUM", "ID", "(")));
        follows.put("MulOp", new ArrayList<>(Arrays.asList("NUM", "ID", "(")));
        follows.put("X1", new ArrayList<>(Arrays.asList(";")));
        follows.put("X6", new ArrayList<>(Arrays.asList("NUM")));
        follows.put("X9", new ArrayList<>(Arrays.asList("else")));
        follows.put("X14", new ArrayList<>(Arrays.asList("(")));
        follows.put("X24", new ArrayList<>(Arrays.asList("(")));
        follows.put("X22", new ArrayList<>(Arrays.asList("(")));
        follows.put("X20", new ArrayList<>(Arrays.asList(";")));
        follows.put("X21", new ArrayList<>(Arrays.asList("NUM")));


    }

    public void start() {
        Token t = Scanner.getToken();
        codeGenTokens[0] = t;
        while (true) {
//            System.out.println("token" + t.type);

//            printStack();
//            cg.printStack();
            String res = parseTable.actionTable.get(Integer.parseInt(parsStack.peek())).get(t.type);
            if (res == null) {
//                parsStack.pop();
//                cg.emptyStack();
                System.out.println(Color.ANSI_RED + "PANIC MODE - PARSER" + Color.ANSI_RESET);
                printParsError(t);

                String state = "";
                int firstState = Integer.parseInt(parsStack.peek());
                L1:
                while (parsStack.size() > 0) {
                    state = parsStack.peek();
                    if (!state.matches("\\d+")) {
                        String arg = parsStack.pop();
//                        dummyReduce(arg, firstState, t);
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
//                                    System.out.println(t.type + " " + state + "!!!!!!!!");
                                    dummyReduce(s, firstState, t);

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
//                    return;
                }

            }

            assert res != null;
            if (res.equals("accept")) {
//                CodeGenerator.print();
                return;
            }
//            System.out.println("res" + res);
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
                    cg.generateCode("jmpToFunc", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("Var"))
                    cg.generateCode("Var", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("Param"))
                    cg.generateCode("parameter", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("X25"))
                    cg.generateCode("output", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("X26"))
                    cg.generateCode("X26", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("X27"))
                    cg.generateCode("X27", codeGenTokens);
                if (grammarLHS.get(idx - 1).equals("X28"))
                    cg.generateCode("X28", codeGenTokens);


//                System.out.println(grammarLHS.get(idx - 1) + " " + gotoIdx);
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

    public Token getTokenFromScanner(Token t) {
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

     private void dummyReduce(String lhs, int state, Token t) {
        String tmp = parseTable.actionTable.get(state).get(t.type);
        if (tmp != null && tmp.charAt(0) == 'r') {
//            System.out.println(t.type);
            if (lhs.charAt(0) == 'X') {
                int idx = Integer.parseInt(lhs.substring(1));
                cg.generateCode(lhs, codeGenTokens);
            }
            if (lhs.equals("ArgList"))
                cg.generateCode("ArgList", codeGenTokens);
            if (lhs.equals("Call"))
                cg.generateCode("jmpToFunc", codeGenTokens);
            if (lhs.equals("Var"))
                cg.generateCode("Var", codeGenTokens);
            if (lhs.equals("Param"))
                cg.generateCode("parameter", codeGenTokens);
            if (lhs.equals("X25"))
                cg.generateCode("output", codeGenTokens);

        }
    }
    private void printParsError(Token t) {
        System.out.println(Color.ANSI_BLUE + "Line " + Scanner.line + " Character " + (Scanner.pointer + 1)
                + " : Unexpected token. Allowed Tokens are: " + Color.ANSI_RESET);
        int state = Integer.parseInt(parsStack.peek());
        ArrayList permitted = new ArrayList(parseTable.actionTable.get(state).keySet());
        for (int i = 0; i < permitted.size(); i++) {
            if(i != 0)
                System.out.print(Color.ANSI_BLUE + ", " + permitted.get(i)+ Color.ANSI_RESET);
            else System.out.print(Color.ANSI_BLUE + permitted.get(i)+ Color.ANSI_RESET);
        }
        System.out.println("");
    }

}


class ParseTable {
    ArrayList<HashMap<String, String>> actionTable;
    ArrayList<HashMap<String, Integer>> gotoTable;

    public ParseTable() {
        actionTable = new ArrayList<>();
        gotoTable = new ArrayList<>();

        HashMap<String, String> row0a = new HashMap<>();
        HashMap<String, String> row1a = new HashMap<>();
        HashMap<String, String> row2a = new HashMap<>();
        HashMap<String, String> row3a = new HashMap<>();
        HashMap<String, String> row4a = new HashMap<>();
        HashMap<String, String> row5a = new HashMap<>();
        HashMap<String, String> row6a = new HashMap<>();
        HashMap<String, String> row7a = new HashMap<>();
        HashMap<String, String> row8a = new HashMap<>();
        HashMap<String, String> row9a = new HashMap<>();
        HashMap<String, String> row10a = new HashMap<>();
        HashMap<String, String> row11a = new HashMap<>();
        HashMap<String, String> row12a = new HashMap<>();
        HashMap<String, String> row13a = new HashMap<>();
        HashMap<String, String> row14a = new HashMap<>();
        HashMap<String, String> row15a = new HashMap<>();
        HashMap<String, String> row16a = new HashMap<>();
        HashMap<String, String> row17a = new HashMap<>();
        HashMap<String, String> row18a = new HashMap<>();
        HashMap<String, String> row19a = new HashMap<>();
        HashMap<String, String> row20a = new HashMap<>();
        HashMap<String, String> row21a = new HashMap<>();
        HashMap<String, String> row22a = new HashMap<>();
        HashMap<String, String> row23a = new HashMap<>();
        HashMap<String, String> row24a = new HashMap<>();
        HashMap<String, String> row25a = new HashMap<>();
        HashMap<String, String> row26a = new HashMap<>();
        HashMap<String, String> row27a = new HashMap<>();
        HashMap<String, String> row28a = new HashMap<>();
        HashMap<String, String> row29a = new HashMap<>();
        HashMap<String, String> row30a = new HashMap<>();
        HashMap<String, String> row31a = new HashMap<>();
        HashMap<String, String> row32a = new HashMap<>();
        HashMap<String, String> row33a = new HashMap<>();
        HashMap<String, String> row34a = new HashMap<>();
        HashMap<String, String> row35a = new HashMap<>();
        HashMap<String, String> row36a = new HashMap<>();
        HashMap<String, String> row37a = new HashMap<>();
        HashMap<String, String> row38a = new HashMap<>();
        HashMap<String, String> row39a = new HashMap<>();
        HashMap<String, String> row40a = new HashMap<>();
        HashMap<String, String> row41a = new HashMap<>();
        HashMap<String, String> row42a = new HashMap<>();
        HashMap<String, String> row43a = new HashMap<>();
        HashMap<String, String> row44a = new HashMap<>();
        HashMap<String, String> row45a = new HashMap<>();
        HashMap<String, String> row46a = new HashMap<>();
        HashMap<String, String> row47a = new HashMap<>();
        HashMap<String, String> row48a = new HashMap<>();
        HashMap<String, String> row49a = new HashMap<>();
        HashMap<String, String> row50a = new HashMap<>();
        HashMap<String, String> row51a = new HashMap<>();
        HashMap<String, String> row52a = new HashMap<>();
        HashMap<String, String> row53a = new HashMap<>();
        HashMap<String, String> row54a = new HashMap<>();
        HashMap<String, String> row55a = new HashMap<>();
        HashMap<String, String> row56a = new HashMap<>();
        HashMap<String, String> row57a = new HashMap<>();
        HashMap<String, String> row58a = new HashMap<>();
        HashMap<String, String> row59a = new HashMap<>();
        HashMap<String, String> row60a = new HashMap<>();
        HashMap<String, String> row61a = new HashMap<>();
        HashMap<String, String> row62a = new HashMap<>();
        HashMap<String, String> row63a = new HashMap<>();
        HashMap<String, String> row64a = new HashMap<>();
        HashMap<String, String> row65a = new HashMap<>();
        HashMap<String, String> row66a = new HashMap<>();
        HashMap<String, String> row67a = new HashMap<>();
        HashMap<String, String> row68a = new HashMap<>();
        HashMap<String, String> row69a = new HashMap<>();
        HashMap<String, String> row70a = new HashMap<>();
        HashMap<String, String> row71a = new HashMap<>();
        HashMap<String, String> row72a = new HashMap<>();
        HashMap<String, String> row73a = new HashMap<>();
        HashMap<String, String> row74a = new HashMap<>();
        HashMap<String, String> row75a = new HashMap<>();
        HashMap<String, String> row76a = new HashMap<>();
        HashMap<String, String> row77a = new HashMap<>();
        HashMap<String, String> row78a = new HashMap<>();
        HashMap<String, String> row79a = new HashMap<>();
        HashMap<String, String> row80a = new HashMap<>();
        HashMap<String, String> row81a = new HashMap<>();
        HashMap<String, String> row82a = new HashMap<>();
        HashMap<String, String> row83a = new HashMap<>();
        HashMap<String, String> row84a = new HashMap<>();
        HashMap<String, String> row85a = new HashMap<>();
        HashMap<String, String> row86a = new HashMap<>();
        HashMap<String, String> row87a = new HashMap<>();
        HashMap<String, String> row88a = new HashMap<>();
        HashMap<String, String> row89a = new HashMap<>();
        HashMap<String, String> row90a = new HashMap<>();
        HashMap<String, String> row91a = new HashMap<>();
        HashMap<String, String> row92a = new HashMap<>();
        HashMap<String, String> row93a = new HashMap<>();
        HashMap<String, String> row94a = new HashMap<>();
        HashMap<String, String> row95a = new HashMap<>();
        HashMap<String, String> row96a = new HashMap<>();
        HashMap<String, String> row97a = new HashMap<>();
        HashMap<String, String> row98a = new HashMap<>();
        HashMap<String, String> row99a = new HashMap<>();
        HashMap<String, String> row100a = new HashMap<>();
        HashMap<String, String> row101a = new HashMap<>();
        HashMap<String, String> row102a = new HashMap<>();
        HashMap<String, String> row103a = new HashMap<>();
        HashMap<String, String> row104a = new HashMap<>();
        HashMap<String, String> row105a = new HashMap<>();
        HashMap<String, String> row106a = new HashMap<>();
        HashMap<String, String> row107a = new HashMap<>();
        HashMap<String, String> row108a = new HashMap<>();
        HashMap<String, String> row109a = new HashMap<>();
        HashMap<String, String> row110a = new HashMap<>();
        HashMap<String, String> row111a = new HashMap<>();
        HashMap<String, String> row112a = new HashMap<>();
        HashMap<String, String> row113a = new HashMap<>();
        HashMap<String, String> row114a = new HashMap<>();
        HashMap<String, String> row115a = new HashMap<>();
        HashMap<String, String> row116a = new HashMap<>();
        HashMap<String, String> row117a = new HashMap<>();
        HashMap<String, String> row118a = new HashMap<>();
        HashMap<String, String> row119a = new HashMap<>();
        HashMap<String, String> row120a = new HashMap<>();
        HashMap<String, String> row121a = new HashMap<>();
        HashMap<String, String> row122a = new HashMap<>();
        HashMap<String, String> row123a = new HashMap<>();
        HashMap<String, String> row124a = new HashMap<>();
        HashMap<String, String> row125a = new HashMap<>();
        HashMap<String, String> row126a = new HashMap<>();
        HashMap<String, String> row127a = new HashMap<>();
        HashMap<String, String> row128a = new HashMap<>();
        HashMap<String, String> row129a = new HashMap<>();
        HashMap<String, String> row130a = new HashMap<>();
        HashMap<String, String> row131a = new HashMap<>();
        HashMap<String, String> row132a = new HashMap<>();
        HashMap<String, String> row133a = new HashMap<>();
        HashMap<String, String> row134a = new HashMap<>();
        HashMap<String, String> row135a = new HashMap<>();
        HashMap<String, String> row136a = new HashMap<>();
        HashMap<String, String> row137a = new HashMap<>();
        HashMap<String, String> row138a = new HashMap<>();
        HashMap<String, String> row139a = new HashMap<>();
        HashMap<String, String> row140a = new HashMap<>();
        HashMap<String, String> row141a = new HashMap<>();
        HashMap<String, String> row142a = new HashMap<>();
        HashMap<String, String> row143a = new HashMap<>();
        HashMap<String, String> row144a = new HashMap<>();
        HashMap<String, String> row145a = new HashMap<>();
        HashMap<String, String> row146a = new HashMap<>();
        HashMap<String, String> row147a = new HashMap<>();
        HashMap<String, String> row148a = new HashMap<>();
        HashMap<String, String> row149a = new HashMap<>();
        HashMap<String, String> row150a = new HashMap<>();
        HashMap<String, String> row151a = new HashMap<>();
        HashMap<String, String> row152a = new HashMap<>();
        HashMap<String, String> row153a = new HashMap<>();
        HashMap<String, String> row154a = new HashMap<>();
        HashMap<String, String> row155a = new HashMap<>();


        HashMap<String, Integer> row0g = new HashMap<>();
        HashMap<String, Integer> row1g = new HashMap<>();
        HashMap<String, Integer> row2g = new HashMap<>();
        HashMap<String, Integer> row3g = new HashMap<>();
        HashMap<String, Integer> row4g = new HashMap<>();
        HashMap<String, Integer> row5g = new HashMap<>();
        HashMap<String, Integer> row6g = new HashMap<>();
        HashMap<String, Integer> row7g = new HashMap<>();
        HashMap<String, Integer> row8g = new HashMap<>();
        HashMap<String, Integer> row9g = new HashMap<>();
        HashMap<String, Integer> row10g = new HashMap<>();
        HashMap<String, Integer> row11g = new HashMap<>();
        HashMap<String, Integer> row12g = new HashMap<>();
        HashMap<String, Integer> row13g = new HashMap<>();
        HashMap<String, Integer> row14g = new HashMap<>();
        HashMap<String, Integer> row15g = new HashMap<>();
        HashMap<String, Integer> row16g = new HashMap<>();
        HashMap<String, Integer> row17g = new HashMap<>();
        HashMap<String, Integer> row18g = new HashMap<>();
        HashMap<String, Integer> row19g = new HashMap<>();
        HashMap<String, Integer> row20g = new HashMap<>();
        HashMap<String, Integer> row21g = new HashMap<>();
        HashMap<String, Integer> row22g = new HashMap<>();
        HashMap<String, Integer> row23g = new HashMap<>();
        HashMap<String, Integer> row24g = new HashMap<>();
        HashMap<String, Integer> row25g = new HashMap<>();
        HashMap<String, Integer> row26g = new HashMap<>();
        HashMap<String, Integer> row27g = new HashMap<>();
        HashMap<String, Integer> row28g = new HashMap<>();
        HashMap<String, Integer> row29g = new HashMap<>();
        HashMap<String, Integer> row30g = new HashMap<>();
        HashMap<String, Integer> row31g = new HashMap<>();
        HashMap<String, Integer> row32g = new HashMap<>();
        HashMap<String, Integer> row33g = new HashMap<>();
        HashMap<String, Integer> row34g = new HashMap<>();
        HashMap<String, Integer> row35g = new HashMap<>();
        HashMap<String, Integer> row36g = new HashMap<>();
        HashMap<String, Integer> row37g = new HashMap<>();
        HashMap<String, Integer> row38g = new HashMap<>();
        HashMap<String, Integer> row39g = new HashMap<>();
        HashMap<String, Integer> row40g = new HashMap<>();
        HashMap<String, Integer> row41g = new HashMap<>();
        HashMap<String, Integer> row42g = new HashMap<>();
        HashMap<String, Integer> row43g = new HashMap<>();
        HashMap<String, Integer> row44g = new HashMap<>();
        HashMap<String, Integer> row45g = new HashMap<>();
        HashMap<String, Integer> row46g = new HashMap<>();
        HashMap<String, Integer> row47g = new HashMap<>();
        HashMap<String, Integer> row48g = new HashMap<>();
        HashMap<String, Integer> row49g = new HashMap<>();
        HashMap<String, Integer> row50g = new HashMap<>();
        HashMap<String, Integer> row51g = new HashMap<>();
        HashMap<String, Integer> row52g = new HashMap<>();
        HashMap<String, Integer> row53g = new HashMap<>();
        HashMap<String, Integer> row54g = new HashMap<>();
        HashMap<String, Integer> row55g = new HashMap<>();
        HashMap<String, Integer> row56g = new HashMap<>();
        HashMap<String, Integer> row57g = new HashMap<>();
        HashMap<String, Integer> row58g = new HashMap<>();
        HashMap<String, Integer> row59g = new HashMap<>();
        HashMap<String, Integer> row60g = new HashMap<>();
        HashMap<String, Integer> row61g = new HashMap<>();
        HashMap<String, Integer> row62g = new HashMap<>();
        HashMap<String, Integer> row63g = new HashMap<>();
        HashMap<String, Integer> row64g = new HashMap<>();
        HashMap<String, Integer> row65g = new HashMap<>();
        HashMap<String, Integer> row66g = new HashMap<>();
        HashMap<String, Integer> row67g = new HashMap<>();
        HashMap<String, Integer> row68g = new HashMap<>();
        HashMap<String, Integer> row69g = new HashMap<>();
        HashMap<String, Integer> row70g = new HashMap<>();
        HashMap<String, Integer> row71g = new HashMap<>();
        HashMap<String, Integer> row72g = new HashMap<>();
        HashMap<String, Integer> row73g = new HashMap<>();
        HashMap<String, Integer> row74g = new HashMap<>();
        HashMap<String, Integer> row75g = new HashMap<>();
        HashMap<String, Integer> row76g = new HashMap<>();
        HashMap<String, Integer> row77g = new HashMap<>();
        HashMap<String, Integer> row78g = new HashMap<>();
        HashMap<String, Integer> row79g = new HashMap<>();
        HashMap<String, Integer> row80g = new HashMap<>();
        HashMap<String, Integer> row81g = new HashMap<>();
        HashMap<String, Integer> row82g = new HashMap<>();
        HashMap<String, Integer> row83g = new HashMap<>();
        HashMap<String, Integer> row84g = new HashMap<>();
        HashMap<String, Integer> row85g = new HashMap<>();
        HashMap<String, Integer> row86g = new HashMap<>();
        HashMap<String, Integer> row87g = new HashMap<>();
        HashMap<String, Integer> row88g = new HashMap<>();
        HashMap<String, Integer> row89g = new HashMap<>();
        HashMap<String, Integer> row90g = new HashMap<>();
        HashMap<String, Integer> row91g = new HashMap<>();
        HashMap<String, Integer> row92g = new HashMap<>();
        HashMap<String, Integer> row93g = new HashMap<>();
        HashMap<String, Integer> row94g = new HashMap<>();
        HashMap<String, Integer> row95g = new HashMap<>();
        HashMap<String, Integer> row96g = new HashMap<>();
        HashMap<String, Integer> row97g = new HashMap<>();
        HashMap<String, Integer> row98g = new HashMap<>();
        HashMap<String, Integer> row99g = new HashMap<>();
        HashMap<String, Integer> row100g = new HashMap<>();
        HashMap<String, Integer> row101g = new HashMap<>();
        HashMap<String, Integer> row102g = new HashMap<>();
        HashMap<String, Integer> row103g = new HashMap<>();
        HashMap<String, Integer> row104g = new HashMap<>();
        HashMap<String, Integer> row105g = new HashMap<>();
        HashMap<String, Integer> row106g = new HashMap<>();
        HashMap<String, Integer> row107g = new HashMap<>();
        HashMap<String, Integer> row108g = new HashMap<>();
        HashMap<String, Integer> row109g = new HashMap<>();
        HashMap<String, Integer> row110g = new HashMap<>();
        HashMap<String, Integer> row111g = new HashMap<>();
        HashMap<String, Integer> row112g = new HashMap<>();
        HashMap<String, Integer> row113g = new HashMap<>();
        HashMap<String, Integer> row114g = new HashMap<>();
        HashMap<String, Integer> row115g = new HashMap<>();
        HashMap<String, Integer> row116g = new HashMap<>();
        HashMap<String, Integer> row117g = new HashMap<>();
        HashMap<String, Integer> row118g = new HashMap<>();
        HashMap<String, Integer> row119g = new HashMap<>();
        HashMap<String, Integer> row120g = new HashMap<>();
        HashMap<String, Integer> row121g = new HashMap<>();
        HashMap<String, Integer> row122g = new HashMap<>();
        HashMap<String, Integer> row123g = new HashMap<>();
        HashMap<String, Integer> row124g = new HashMap<>();
        HashMap<String, Integer> row125g = new HashMap<>();
        HashMap<String, Integer> row126g = new HashMap<>();
        HashMap<String, Integer> row127g = new HashMap<>();
        HashMap<String, Integer> row128g = new HashMap<>();
        HashMap<String, Integer> row129g = new HashMap<>();
        HashMap<String, Integer> row130g = new HashMap<>();
        HashMap<String, Integer> row131g = new HashMap<>();
        HashMap<String, Integer> row132g = new HashMap<>();
        HashMap<String, Integer> row133g = new HashMap<>();
        HashMap<String, Integer> row134g = new HashMap<>();
        HashMap<String, Integer> row135g = new HashMap<>();
        HashMap<String, Integer> row136g = new HashMap<>();
        HashMap<String, Integer> row137g = new HashMap<>();
        HashMap<String, Integer> row138g = new HashMap<>();
        HashMap<String, Integer> row139g = new HashMap<>();
        HashMap<String, Integer> row140g = new HashMap<>();
        HashMap<String, Integer> row141g = new HashMap<>();
        HashMap<String, Integer> row142g = new HashMap<>();
        HashMap<String, Integer> row143g = new HashMap<>();
        HashMap<String, Integer> row144g = new HashMap<>();
        HashMap<String, Integer> row145g = new HashMap<>();
        HashMap<String, Integer> row146g = new HashMap<>();
        HashMap<String, Integer> row147g = new HashMap<>();
        HashMap<String, Integer> row148g = new HashMap<>();
        HashMap<String, Integer> row149g = new HashMap<>();
        HashMap<String, Integer> row150g = new HashMap<>();
        HashMap<String, Integer> row151g = new HashMap<>();
        HashMap<String, Integer> row152g = new HashMap<>();
        HashMap<String, Integer> row153g = new HashMap<>();
        HashMap<String, Integer> row154g = new HashMap<>();
        HashMap<String, Integer> row155g = new HashMap<>();


        row0a.put("int", "r66");
        row0a.put("void", "r66");
        row0a.put("if", "r66");
        row0a.put("output", "r66");
        row0a.put(";", "r66");
        row0a.put("ID", "r66");
        row0a.put("{", "r66");
        row0a.put("while", "r66");
        row0a.put("return", "r66");
        row0g.put("Program", 1);
        row0g.put("X7", 2);

        row1a.put("$", "accept");

        row2g.put("DeclarationList", 3);
        row2g.put("Declaration", 4);
        row2g.put("VarDeclaration", 5);
        row2g.put("FunDeclaration", 7);
        row2a.put("int", "s13");
        row2a.put("void", "s8");

        row3a.put("EOF", "s11");
        row3a.put("int", "s13");
        row3a.put("void", "s8");
        row3g.put("Declaration", 12);
        row3g.put("VarDeclaration", 5);
        row3g.put("FunDeclaration", 7);

        row4a.put("EOF", "r3");
        row4a.put("int", "r3");
        row4a.put("void", "r3");

        row5a.put("EOF", "r4");
        row5a.put("int", "r4");
        row5a.put("void", "r4");


        row7a.put("EOF", "r5");
        row7a.put("int", "r5");
        row7a.put("void", "r5");

        row8a.put("ID", "s134");
        row8g.put("R2", 143);

        row9g.put("X20", 136);
        row9g.put("R3", 135);
        row9a.put("[", "s138");
        row9a.put(";", "r79");

        row10a.put("(", "s86");

        row11g.put("X19", 16);
        row11a.put("$", "r78");

        row12a.put("EOF", "r2");
        row12a.put("int", "r2");
        row12a.put("void", "r2");

        row13a.put("ID", "s131");
        row13g.put("R1", 132);
        row13g.put("R2", 133);

        row14g.put("R1", 132);
        row14a.put("ID", "s9");

        /*row15g.put("X21", 18);
        row15a.put("NUM", "r80");
*/
        row16a.put("$", "r1");

        row17a.put("(", "s19");

//        row18a.put("NUM", "s26");

        row19a.put("void", "s23");
        row19a.put("int", "s24");
        row19g.put("Params", 20);
        row19g.put("Param", 22);
        row19g.put("ParamList", 21);
        row19g.put("TypeSpecifier", 25);

        row20a.put(")", "s36");

        row21a.put(",", "s30");
        row21a.put(")", "r12");

        row22a.put(")", "r75");
        row22a.put(",", "r75");
        row22g.put("X16", 29);

        row23a.put(")", "r13");

        row24a.put("ID", "r8");

        row25a.put("ID", "s33");

//        row26a.put("]", "s27");

//        row27a.put(";", "s28");

        /*row28a.put("EOF", "r7");
        row28a.put("int", "r7");
        row28a.put("void", "r7");
        row28a.put("if", "r7");
        row28a.put("while", "r7");
        row28a.put("return", "r7");
        row28a.put("ID", "r7");
        row28a.put("}", "r7");
        row28a.put(";", "r7");
        row28a.put("{", "r7");*/

        row29a.put(")", "r15");
        row29a.put(",", "r15");

        row30a.put("int", "s24");
        row30g.put("Param", 31);
        row30g.put("TypeSpecifier", 25);

        row31a.put(")", "r75");
        row31a.put(",", "r75");
        row31g.put("X16", 32);

        row32a.put(")", "r14");
        row32a.put(",", "r14");

        row33a.put(")", "r16");
        row33a.put(",", "r16");
        row33a.put("[", "s34");

        row34a.put("]", "s35");

        row35a.put(")", "r17");
        row35a.put(",", "r17");

        row36a.put("{", "s37");
        row36g.put("CompoundStmt", 39);

        row37a.put("int", "r20");
        row37a.put("}", "r20");
        row37a.put("{", "r20");
        row37a.put(";", "r20");
        row37a.put("if", "r20");
        row37a.put("while", "r20");
        row37a.put("ID", "r20");
        row37a.put("return", "r20");
        row37a.put("output", "r20");
        row37g.put("LocalDeclarations", 38);

        row38g.put("StatementList", 40);
        row38g.put("VarDeclaration", 41);
        row38a.put("int", "s14");
        row38a.put(";", "r22");
        row38a.put("ID", "r22");
        row38a.put("output", "r22");
        row38a.put("{", "r22");
        row38a.put("}", "r22");
        row38a.put("if", "r22");
        row38a.put("while", "r22");
        row38a.put("return", "r22");

        row39a.put("EOF", "r82");
        row39a.put("int", "r82");
        row39a.put("void", "r82");
        row39g.put("X23", 123);

        row40a.put("{", "s37");
        row40a.put(";", "s49");
        row40a.put("if", "s52");
        row40a.put("}", "s42");
        row40a.put("while", "s54");
        row40a.put("return", "s53");
        row40a.put("output", "s144");
        row40a.put("ID", "s68");
        row40g.put("Statement", 46);
        row40g.put("ExpressionStmt", 43);
        row40g.put("CompoundStmt", 47);
        row40g.put("SelectionStmt", 44);
        row40g.put("IterationStmt", 48);
        row40g.put("ReturnStmt", 45);
        row40g.put("Var", 50);

        row41a.put("int", "r19");
        row41a.put("}", "r19");
        row41a.put("if", "r19");
        row41a.put("while", "r19");
        row41a.put("return", "r19");
        row41a.put("ID", "r19");
        row41a.put("{", "r19");
        row41a.put(";", "r19");
        row41a.put("output", "r19");

        row42a.put(";", "r18");
        row42a.put("ID", "r18");
        row42a.put("output", "r18");
        row42a.put("{", "r18");
        row42a.put("if", "r18");
        row42a.put("else", "r18");
        row42a.put("while", "r18");
        row42a.put("return", "r18");
        row42a.put("int", "r18");
        row42a.put("void", "r18");
        row42a.put("EOF", "r18");
        row42a.put("}", "r18");

        row43a.put(";", "r23");
        row43a.put("output", "r23");
        row43a.put("ID", "r23");
        row43a.put("{", "r23");
        row43a.put("if", "r23");
        row43a.put("while", "r23");
        row43a.put("return", "r23");
        row43a.put("else", "r23");
        row43a.put("}", "r23");

        row44a.put(";", "r25");
        row44a.put("output", "r25");
        row44a.put("ID", "r25");
        row44a.put("{", "r25");
        row44a.put("if", "r25");
        row44a.put("while", "r25");
        row44a.put("return", "r25");
        row44a.put("else", "r25");
        row44a.put("}", "r25");

        row45a.put(";", "r27");
        row45a.put("output", "r27");
        row45a.put("ID", "r27");
        row45a.put("{", "r27");
        row45a.put("if", "r27");
        row45a.put("while", "r27");
        row45a.put("return", "r27");
        row45a.put("else", "r27");
        row45a.put("}", "r27");

        row46a.put(";", "r21");
        row46a.put("output", "r21");
        row46a.put("ID", "r21");
        row46a.put("{", "r21");
        row46a.put("if", "r21");
        row46a.put("while", "r21");
        row46a.put("return", "r21");
        row46a.put("}", "r21");

        row47a.put(";", "r24");
        row47a.put("output", "r24");
        row47a.put("ID", "r24");
        row47a.put("{", "r24");
        row47a.put("if", "r24");
        row47a.put("while", "r24");
        row47a.put("else", "r24");
        row47a.put("return", "r24");
        row47a.put("}", "r24");

        row48a.put(";", "r26");
        row48a.put("output", "r26");
        row48a.put("ID", "r26");
        row48a.put("{", "r26");
        row48a.put("if", "r26");
        row48a.put("while", "r26");
        row48a.put("else", "r26");
        row48a.put("return", "r26");
        row48a.put("}", "r26");

        row49a.put(";", "r29");
        row49a.put("output", "r29");
        row49a.put("ID", "r29");
        row49a.put("{", "r29");
        row49a.put("if", "r29");
        row49a.put("while", "r29");
        row49a.put("else", "r29");
        row49a.put("return", "r29");
        row49a.put("}", "r29");

        row50a.put("=", "s55");

//        row51a.put("ID", "s56");

        row52a.put("(", "s76");

        row53a.put("(", "s75");
        row53a.put("ID", "s68");
        row53a.put(";", "s58");
        row53a.put("NUM", "r65");
        row53g.put("GenExpression", 59);
        row53g.put("RelExpression", 60);
        row53g.put("Expression", 61);
        row53g.put("RelTerm", 62);
        row53g.put("Term", 63);
        row53g.put("Factor", 64);
        row53g.put("Var", 65);
        row53g.put("Call", 66);
        row53g.put("X6", 67);

        row54g.put("X14", 124);
        row54a.put("(", "r73");

        row55g.put("Expression", 71);
        row55g.put("Term", 63);
        row55g.put("Factor", 64);
        row55g.put("Var", 65);
        row55g.put("Call", 66);
        row55g.put("X6", 67);
        row55a.put("ID", "s68");
        row55a.put("(", "s75");
        row55a.put("NUM", "r65");

        row56a.put("=", "r35");
        row56a.put("*", "r35");
        row56a.put("/", "r35");
        row56a.put(",", "r35");
        row56a.put(")", "r35");
        row56a.put("+", "r35");
        row56a.put("-", "r35");
        row56a.put("<", "r35");
        row56a.put("&&", "r35");
        row56a.put(";", "r35");
        row56a.put("==", "r35");
        row56a.put("]", "r35");
        row56a.put("[", "s57");

        row57g.put("Expression", 87);
        row57g.put("Term", 63);
        row57g.put("Factor", 64);
        row57g.put("Var", 65);
        row57g.put("Call", 66);
        row57g.put("X6", 67);
        row57a.put("(", "s75");
        row57a.put("ID", "s68");
        row57a.put("NUM", "r65");

        row58g.put("X17", 72);
        row58a.put("while", "r76");
        row58a.put("output", "r76");
        row58a.put("ID", "r76");
        row58a.put(";", "r76");
        row58a.put("{", "r76");
        row58a.put("if", "r76");
        row58a.put("return", "r76");
        row58a.put("else", "r76");
        row58a.put("}", "r76");

        row59a.put(";", "s73");

        row60a.put("&&", "s82");
        row60a.put(";", "r37");
        row60a.put(")", "r37");

        row61a.put("+", "s79");
        row61a.put("-", "s150");
        row61a.put("==", "s80");
        row61a.put("<", "s81");
        row61a.put(";", "r38");
        row61a.put(")", "r38");

        row62a.put("&&", "r40");
        row62a.put(";", "r40");
        row62a.put(")", "r40");

        row63a.put(",", "r44");
        row63a.put(")", "r44");
        row63a.put("+", "r44");
        row63a.put("-", "r44");
        row63a.put("<", "r44");
        row63a.put("&&", "r44");
        row63a.put(";", "r44");
        row63a.put("==", "r44");
        row63a.put("]", "r44");
        row63a.put("*", "s83");
        row63a.put("/", "s151");

        row64a.put(",", "r48");
        row64a.put(")", "r48");
        row64a.put("+", "r48");
        row64a.put("*", "r48");
        row64a.put("/", "r48");
        row64a.put("-", "r48");
        row64a.put("<", "r48");
        row64a.put("&&", "r48");
        row64a.put(";", "r48");
        row64a.put("==", "r48");
        row64a.put("]", "r48");

        row65a.put(",", "r52");
        row65a.put(")", "r52");
        row65a.put("+", "r52");
        row65a.put("*", "r52");
        row65a.put("/", "r52");
        row65a.put("-", "r52");
        row65a.put("<", "r52");
        row65a.put("&&", "r52");
        row65a.put(";", "r52");
        row65a.put("==", "r52");
        row65a.put("]", "r52");

        row66g.put("X5", 69);
        row66a.put(",", "r64");
        row66a.put(")", "r64");
        row66a.put("+", "r64");
        row66a.put("*", "r64");
        row66a.put("/", "r64");
        row66a.put("-", "r64");
        row66a.put("<", "r64");
        row66a.put("&&", "r64");
        row66a.put(";", "r64");
        row66a.put("==", "r64");
        row66a.put("]", "r64");

        row67a.put("NUM", "s70");

        row68a.put(",", "r61");
        row68a.put("=", "r61");
        row68a.put(")", "r61");
        row68a.put("+", "r61");
        row68a.put("*", "r61");
        row68a.put("/", "r61");
        row68a.put("-", "r61");
        row68a.put("<", "r61");
        row68a.put("&&", "r61");
        row68a.put(";", "r61");
        row68a.put("==", "r61");
        row68a.put("]", "r61");
        row68a.put("[", "r61");
        row68a.put("(", "r83");
        row68g.put("X2", 56);
        row68g.put("X24", 10);

        row69a.put(",", "r53");
        row69a.put(")", "r53");
        row69a.put("+", "r53");
        row69a.put("*", "r53");
        row69a.put("/", "r53");
        row69a.put("-", "r53");
        row69a.put("<", "r53");
        row69a.put("&&", "r53");
        row69a.put(";", "r53");
        row69a.put("]", "r53");
        row69a.put("==", "r53");

        row70a.put(",", "r54");
        row70a.put(")", "r54");
        row70a.put("+", "r54");
        row70a.put("*", "r54");
        row70a.put("/", "r54");
        row70a.put("-", "r54");
        row70a.put("<", "r54");
        row70a.put("&&", "r54");
        row70a.put(";", "r54");
        row70a.put("==", "r54");
        row70a.put("]", "r54");

        row71g.put("X1", 113);
        row71a.put("+", "s79");
        row71a.put("-", "s150");
        row71a.put(";", "r60");

        row72a.put(";", "r33");
        row72a.put("ID", "r33");
        row72a.put("{", "r33");
        row72a.put("if", "r33");
        row72a.put("output", "r33");
        row72a.put("while", "r33");
        row72a.put("return", "r33");
        row72a.put("else", "r33");
        row72a.put("}", "r33");

        row73g.put("X17", 74);
        row73a.put(";", "r76");
        row73a.put("output", "r76");
        row73a.put("ID", "r76");
        row73a.put("{", "r76");
        row73a.put("if", "r76");
        row73a.put("while", "r76");
        row73a.put("return", "r76");
        row73a.put("else", "r76");
        row73a.put("}", "r76");

        row74a.put(";", "r34");
        row74a.put("output", "r34");
        row74a.put("ID", "r34");
        row74a.put("{", "r34");
        row74a.put("if", "r34");
        row74a.put("while", "r34");
        row74a.put("return", "r34");
        row74a.put("else", "r34");
        row74a.put("}", "r34");

        row75g.put("Expression", 90);
        row75g.put("Term", 63);
        row75g.put("Factor", 64);
        row75g.put("Var", 65);
        row75g.put("Call", 66);
        row75g.put("X6", 67);
        row75a.put("(", "s75");
        row75a.put("ID", "s68");
        row75a.put("NUM", "r65");

        row76g.put("GenExpression", 92);
        row76g.put("RelExpression", 60);
        row76g.put("RelTerm", 62);
        row76g.put("Expression", 61);
        row76g.put("Term", 63);
        row76g.put("Factor", 64);
        row76g.put("Var", 65);
        row76g.put("Call", 66);
        row76g.put("X6", 67);
        row76a.put("(", "s75");
        row76a.put("ID", "s68");
        row76a.put("NUM", "r65");

        row77a.put("NUM", "r45");
        row77a.put("ID", "r45");
        row77a.put("(", "r45");

        row78a.put("NUM", "r46");
        row78a.put("ID", "r46");
        row78a.put("(", "r46");

        row79g.put("Term", 95);
        row79g.put("Factor", 64);
        row79a.put("(", "s75");
        row79g.put("Var", 65);
        row79g.put("Call", 66);
        row79g.put("X6", 67);
        row79a.put("ID", "s68");
        row79a.put("NUM", "r65");

        row150g.put("Term", 152);
        row150g.put("Factor", 64);
        row150a.put("(", "s75");
        row150g.put("Var", 65);
        row150g.put("Call", 66);
        row150g.put("X6", 67);
        row150a.put("ID", "s68");
        row150a.put("NUM", "r65");

        row80g.put("Term", 63);
        row80g.put("Expression", 94);
        row80g.put("Factor", 64);
        row80g.put("Var", 65);
        row80g.put("Call", 66);
        row80g.put("X6", 67);
        row80a.put("(", "s75");
        row80a.put("ID", "s68");
        row80a.put("NUM", "r65");

        row81g.put("Term", 63);
        row81g.put("Expression", 93);
        row81g.put("Factor", 64);
        row81g.put("Var", 65);
        row81g.put("Call", 66);
        row81g.put("X6", 67);
        row81a.put("(", "s75");
        row81a.put("ID", "s68");
        row81a.put("NUM", "r65");

        row82g.put("Term", 63);
        row82g.put("Expression", 98);
        row82g.put("RelTerm", 96);
        row82g.put("Factor", 64);
        row82a.put("(", "s75");
        row82g.put("Var", 65);
        row82g.put("Call", 66);
        row82g.put("X6", 67);
        row82a.put("ID", "s68");
        row82a.put("NUM", "r65");

        row83g.put("Factor", 99);
        row83g.put("Var", 65);
        row83g.put("Call", 66);
        row83g.put("X6", 67);
        row83a.put("(", "s75");
        row83a.put("ID", "s68");
        row83a.put("NUM", "r65");

        row151g.put("Factor", 154);
        row151g.put("Var", 65);
        row151g.put("Call", 66);
        row151g.put("X6", 67);
        row151a.put("(", "s75");
        row151a.put("ID", "s68");
        row151a.put("NUM", "r65");

        row84a.put("ID", "r49");
        row84a.put("NUM", "r49");
        row84a.put("(", "r49");

        row85a.put("ID", "r50");
        row85a.put("NUM", "r50");
        row85a.put("(", "r50");

        row86g.put("Term", 63);
        row86g.put("Args", 101);
        row86g.put("ArgList", 103);
        row86g.put("Expression", 104);
        row86g.put("Factor", 64);
        row86g.put("Var", 65);
        row86g.put("Call", 66);
        row86g.put("X6", 67);
        row86a.put("(", "s75");
        row86a.put("ID", "s68");
        row86a.put("NUM", "r65");
        row86a.put(")", "r57");

        row87a.put("+", "s79");
        row87a.put("-", "s150");
        row87a.put("]", "s88");

        row88a.put(",", "r62");
        row88a.put("=", "r62");
        row88a.put(")", "r62");
        row88a.put("+", "r62");
        row88a.put("*", "r62");
        row88a.put("/", "r62");
        row88a.put("-", "r62");
        row88a.put("<", "r62");
        row88a.put("&&", "r62");
        row88a.put(";", "r62");
        row88a.put("==", "r62");
        row88a.put("]", "r62");
        row88g.put("X3", 89);

        row89a.put(",", "r36");
        row89a.put("=", "r36");
        row89a.put(")", "r36");
        row89a.put("+", "r36");
        row89a.put("*", "r36");
        row89a.put("/", "r36");
        row89a.put("-", "r36");
        row89a.put("<", "r36");
        row89a.put("&&", "r36");
        row89a.put(";", "r36");
        row89a.put("==", "r36");
        row89a.put("]", "r36");

        row90a.put("+", "s79");
        row90a.put("-", "s150");
        row90a.put(")", "s91");

        row91a.put(",", "r51");
        row91a.put(")", "r51");
        row91a.put("+", "r51");
        row91a.put("*", "r51");
        row91a.put("/", "r51");
        row91a.put("-", "r51");
        row91a.put("<", "r51");
        row91a.put("&&", "r51");
        row91a.put(";", "r51");
        row91a.put("==", "r51");
        row91a.put("]", "r51");

        row92a.put(")", "s108");

        row93g.put("X12", 105);
        row93a.put("+", "s79");
        row93a.put("-", "s150");
        row93a.put("&&", "r71");
        row93a.put(";", "r71");
        row93a.put(")", "r71");

        row94g.put("X11", 106);
        row94a.put("+", "s79");
        row94a.put("-", "s150");
        row94a.put("&&", "r70");
        row94a.put(";", "r70");
        row94a.put(")", "r70");

        row95a.put(",", "r63");
        row95a.put(")", "r63");
        row95a.put("+", "r63");
        row95a.put("-", "r63");
        row95a.put("<", "r63");
        row95a.put("&&", "r63");
        row95a.put(";", "r63");
        row95a.put("==", "r63");
        row95a.put("]", "r63");
        row95a.put("*", "s83");
        row95a.put("/", "s151");
        row95g.put("X4", 107);

        row152a.put(",", "r88");
        row152a.put(")", "r88");
        row152a.put("+", "r88");
        row152a.put("-", "r88");
        row152a.put("<", "r88");
        row152a.put("&&", "r88");
        row152a.put(";", "r88");
        row152a.put("==", "r88");
        row152a.put("]", "r88");
        row152a.put("*", "s83");
        row152a.put("/", "s151");
        row152g.put("X26", 153);

        row96g.put("X13", 97);
        row96a.put("&&", "r72");
        row96a.put(";", "r72");
        row96a.put(")", "r72");

        row97a.put("&&", "r39");
        row97a.put(";", "r39");
        row97a.put(")", "r39");

        row98a.put("+", "s79");
        row98a.put("-", "s150");
        row98a.put("==", "s80");
        row98a.put("<", "s81");

        row99g.put("X27", 100);
        row99a.put(",", "r89");
        row99a.put(")", "r89");
        row99a.put("+", "r89");
        row99a.put("-", "r89");
        row99a.put("<", "r89");
        row99a.put("&&", "r89");
        row99a.put(";", "r89");
        row99a.put("==", "r89");
        row99a.put("]", "r89");
        row99a.put("*", "r89");
        row99a.put("/", "r89");

        row154g.put("X28", 155);
        row154a.put(",", "r90");
        row154a.put(")", "r90");
        row154a.put("+", "r90");
        row154a.put("-", "r90");
        row154a.put("<", "r90");
        row154a.put("&&", "r90");
        row154a.put(";", "r90");
        row154a.put("==", "r90");
        row154a.put("]", "r90");
        row154a.put("*", "r90");
        row154a.put("/", "r90");

        row100a.put(",", "r47");
        row100a.put(")", "r47");
        row100a.put("+", "r47");
        row100a.put("-", "r47");
        row100a.put("<", "r47");
        row100a.put("&&", "r47");
        row100a.put(";", "r47");
        row100a.put("==", "r47");
        row100a.put("]", "r47");
        row100a.put("*", "r47");
        row100a.put("/", "r47");

        row155a.put(",", "r92");
        row155a.put(")", "r92");
        row155a.put("+", "r92");
        row155a.put("-", "r92");
        row155a.put("<", "r92");
        row155a.put("&&", "r92");
        row155a.put(";", "r92");
        row155a.put("==", "r92");
        row155a.put("]", "r92");
        row155a.put("*", "r92");
        row155a.put("/", "r92");

        row101a.put(")", "s102");

        row102a.put(",", "r55");
        row102a.put(")", "r55");
        row102a.put("+", "r55");
        row102a.put("-", "r55");
        row102a.put("<", "r55");
        row102a.put("&&", "r55");
        row102a.put(";", "r55");
        row102a.put("==", "r55");
        row102a.put("]", "r55");
        row102a.put("*", "r55");
        row102a.put("/", "r55");

        row103a.put(",", "s118");
        row103a.put(")", "r56");

        row104a.put("+", "s79");
        row104a.put("-", "s150");
        row104a.put(",", "r59");
        row104a.put(")", "r59");

        row105a.put("&&", "r42");
        row105a.put(";", "r42");
        row105a.put(")", "r42");

        row106a.put("&&", "r41");
        row106a.put(";", "r41");
        row106a.put(")", "r41");

        row107a.put(",", "r43");
        row107a.put(")", "r43");
        row107a.put("+", "r43");
        row107a.put("-", "r43");
        row107a.put("<", "r43");
        row107a.put("&&", "r43");
        row107a.put(";", "r43");
        row107a.put("==", "r43");
        row107a.put("]", "r43");

        row153a.put(",", "r91");
        row153a.put(")", "r91");
        row153a.put("+", "r91");
        row153a.put("-", "r91");
        row153a.put("<", "r91");
        row153a.put("&&", "r91");
        row153a.put(";", "r91");
        row153a.put("==", "r91");
        row153a.put("]", "r91");


        row108a.put("int", "r66");
        row108a.put("void", "r66");
        row108a.put("if", "r66");
        row108a.put(";", "r66");
        row108a.put("while", "r66");
        row108a.put("output", "r66");
        row108a.put("return", "r66");
        row108a.put("ID", "r66");
        row108a.put("{", "r66");
        row108g.put("X7", 109);

        row109g.put("ExpressionStmt", 43);
        row109g.put("SelectionStmt", 44);
        row109g.put("ReturnStmt", 45);
        row109g.put("Var", 50);
        row109g.put("IterationStmt", 48);
        row109g.put("CompoundStmt", 47);
        row109g.put("Statement", 110);
        row109a.put("if", "s52");
        row109a.put("while", "s54");
        row109a.put("return", "s53");
        row109a.put(";", "s49");
        row109a.put("{", "s37");
        row109a.put("ID", "s68");
        row109a.put("output", "s144");

        row110a.put(";", "r67");
        row110a.put("ID", "r67");
        row110a.put("output", "r67");
        row110a.put("{", "r67");
        row110a.put("}", "r67");
        row110a.put("if", "r67");
        row110a.put("while", "r67");
        row110a.put("return", "r67");
        row110a.put("else", "r68");
        row110g.put("X9", 112);
        row110g.put("X8", 111);

        row111a.put(";", "r30");
        row111a.put("ID", "r30");
        row111a.put("{", "r30");
        row111a.put("if", "r30");
        row111a.put("while", "r30");
        row111a.put("output", "r30");
        row111a.put("return", "r30");
        row111a.put("else", "r30");
        row111a.put("}", "r30");

        row112a.put("else", "s115");

        row113a.put(";", "s114");

        row114a.put(";", "r28");
        row114a.put("ID", "r28");
        row114a.put("output", "r28");
        row114a.put("{", "r28");
        row114a.put("if", "r28");
        row114a.put("while", "r28");
        row114a.put("return", "r28");
        row114a.put("else", "r28");
        row114a.put("}", "r28");

        row115g.put("ExpressionStmt", 43);
        row115g.put("SelectionStmt", 44);
        row115g.put("ReturnStmt", 45);
        row115g.put("Var", 50);
        row115g.put("IterationStmt", 48);
        row115g.put("CompoundStmt", 47);
        row115g.put("Statement", 116);
        row115a.put("if", "s52");
        row115a.put("while", "s54");
        row115a.put("return", "s53");
        row115a.put(";", "s49");
        row115a.put("{", "s37");
        row115a.put("ID", "s68");
        row115a.put("output", "s144");

        row116g.put("X10", 117);
        row116a.put(";", "r69");
        row116a.put("output", "r69");
        row116a.put("ID", "r69");
        row116a.put("{", "r69");
        row116a.put("if", "r69");
        row116a.put("while", "r69");
        row116a.put("return", "r69");
        row116a.put("else", "r69");
        row116a.put("}", "r69");

        row117a.put(";", "r31");
        row117a.put("ID", "r31");
        row117a.put("{", "r31");
        row117a.put("}", "r31");
        row117a.put("if", "r31");
        row117a.put("while", "r31");
        row117a.put("return", "r31");
        row117a.put("output", "r31");
        row117a.put("else", "r31");

        row118g.put("Term", 63);
        row118g.put("Expression", 119);
        row118g.put("Factor", 64);
        row118g.put("Var", 65);
        row118g.put("Call", 66);
        row118g.put("X6", 67);
        row118a.put("(", "s75");
        row118a.put("ID", "s68");
        row118a.put("NUM", "r65");

        row119a.put("+", "s79");
        row119a.put("-", "s150");
        row119a.put(",", "r58");
        row119a.put(")", "r58");

        row120a.put(";", "s122");

       /* row121g.put("X22", 10);
        row121a.put("ID", "r81");*/

        row122a.put("EOF", "r6");
        row122a.put("int", "r6");
        row122a.put("void", "r6");
        row122a.put("if", "r6");
        row122a.put("while", "r6");
        row122a.put("return", "r6");
        row122a.put("ID", "r6");
        row122a.put(";", "r6");
        row122a.put("}", "r6");
        row122a.put("{", "r6");
        row122a.put("output", "r6");

        row123a.put("EOF", "r85");
        row123a.put("int", "r85");
        row123a.put("void", "r85");

        row124a.put("(", "s125");

        row125a.put("(", "s75");
        row125a.put("ID", "s68");
        row125a.put("NUM", "r65");
        row125g.put("RelExpression", 60);
        row125g.put("Expression", 61);
        row125g.put("RelTerm", 62);
        row125g.put("Term", 63);
        row125g.put("Factor", 64);
        row125g.put("Var", 65);
        row125g.put("Call", 66);
        row125g.put("X6", 67);
        row125g.put("GenExpression", 126);

        row126a.put(")", "s127");

        row127g.put("X7", 128);
        row127a.put("int", "r66");
        row127a.put("if", "r66");
        row127a.put("void", "r66");
        row127a.put(";", "r66");
        row127a.put("while", "r66");
        row127a.put("return", "r66");
        row127a.put("ID", "r66");
        row127a.put("output", "r66");
        row127a.put("{", "r66");

        row128g.put("ExpressionStmt", 43);
        row128g.put("SelectionStmt", 44);
        row128g.put("ReturnStmt", 45);
        row128g.put("Var", 50);
        row128g.put("IterationStmt", 48);
        row128g.put("CompoundStmt", 47);
        row128g.put("Statement", 129);
        row128a.put("if", "s52");
        row128a.put("while", "s54");
        row128a.put("return", "s53");
        row128a.put("output", "s144");
        row128a.put(";", "s49");
        row128a.put("{", "s37");
        row128a.put("ID", "s68");

        row129g.put("X15", 130);
        row129a.put(";", "r74");
        row129a.put("output", "r74");
        row129a.put("ID", "r74");
        row129a.put("{", "r74");
        row129a.put("if", "r74");
        row129a.put("while", "r74");
        row129a.put("return", "r74");
        row129a.put("else", "r74");
        row129a.put("}", "r74");

        row130a.put(";", "r32");
        row130a.put("output", "r32");
        row130a.put("ID", "r32");
        row130a.put("{", "r32");
        row130a.put("if", "r32");
        row130a.put("while", "r32");
        row130a.put("return", "r32");
        row130a.put("else", "r32");
        row130a.put("}", "r32");

        row131g.put("R3", 135);
        row131g.put("X20", 136);
        row131g.put("X22", 17);
        row131a.put("[", "s138");
        row131a.put(";", "r79");
        row131a.put("(", "r81");

        row132a.put("int", "r6");
        row132a.put("void", "r6");
        row132a.put("EOF", "r6");
        row132a.put(";", "r6");
        row132a.put("ID", "r6");
        row132a.put("{", "r6");
        row132a.put("}", "r6");
        row132a.put("if", "r6");
        row132a.put("while", "r6");
        row132a.put("return", "r6");
        row132a.put("output", "r6");

        row133a.put("int", "r7");
        row133a.put("void", "r7");
        row133a.put("EOF", "r7");

        row134g.put("X22", 17);
        row134a.put("(", "r81");

        row135a.put("int", "r10");
        row135a.put("void", "r10");
        row135a.put("EOF", "r10");
        row135a.put("ID", "r10");
        row135a.put(";", "r10");
        row135a.put("}", "r10");
        row135a.put("{", "r10");
        row135a.put("if", "r10");
        row135a.put("while", "r10");
        row135a.put("return", "r10");
        row135a.put("output", "r10");

        row136a.put(";", "s137");

        row137a.put("int", "r84");
        row137a.put("void", "r84");
        row137a.put("EOF", "r84");
        row137a.put("ID", "r84");
        row137a.put(";", "r84");
        row137a.put("{", "r84");
        row137a.put("}", "r84");
        row137a.put("if", "r84");
        row137a.put("while", "r84");
        row137a.put("return", "r84");
        row137a.put("output", "r84");

        row138g.put("X21", 139);
        row138a.put("NUM", "r80");

        row139a.put("NUM", "s140");

        row140a.put("]", "s141");

        row141a.put(";", "s142");

        row142a.put("int", "r11");
        row142a.put("void", "r11");
        row142a.put("EOF", "r11");
        row142a.put("ID", "r11");
        row142a.put(";", "r11");
        row142a.put("{", "r11");
        row142a.put("}", "r11");
        row142a.put("if", "r11");
        row142a.put("while", "r11");
        row142a.put("return", "r11");
        row142a.put("output", "r11");

        row143a.put("int", "r9");
        row143a.put("void", "r9");
        row143a.put("EOF", "r9");

        row144a.put("(", "s145");

        row145a.put("(", "s75");
        row145a.put("ID", "s68");
        row145a.put("NUM", "r65");
        row145g.put("GenExpression", 146);
        row145g.put("Term", 63);
        row145g.put("Factor", 64);
        row145g.put("Var", 65);
        row145g.put("Call", 66);
        row145g.put("X6", 67);
        row145g.put("Expression", 61);
        row145g.put("RelExpression", 60);
        row145g.put("RelTerm", 62);

        row146a.put(")", "s149");

        row147a.put("else", "r87");
        row147a.put("output", "r87");
        row147a.put(";", "r87");
        row147a.put("ID", "r87");
        row147a.put("{", "r87");
        row147a.put("if", "r87");
        row147a.put("while", "r87");
        row147a.put("return", "r87");
        row147a.put("}", "r87");
        row147g.put("X25", 148);

        row148a.put("else", "r86");
        row148a.put("output", "r86");
        row148a.put(";", "r86");
        row148a.put("ID", "r86");
        row148a.put("{", "r86");
        row148a.put("if", "r86");
        row148a.put("while", "r86");
        row148a.put("return", "r86");
        row148a.put("}", "r86");

        row149a.put(";", "s147");

        actionTable.add(row0a);
        actionTable.add(row1a);
        actionTable.add(row2a);
        actionTable.add(row3a);
        actionTable.add(row4a);
        actionTable.add(row5a);
        actionTable.add(row6a);
        actionTable.add(row7a);
        actionTable.add(row8a);
        actionTable.add(row9a);
        actionTable.add(row10a);
        actionTable.add(row11a);
        actionTable.add(row12a);
        actionTable.add(row13a);
        actionTable.add(row14a);
        actionTable.add(row15a);
        actionTable.add(row16a);
        actionTable.add(row17a);
        actionTable.add(row18a);
        actionTable.add(row19a);
        actionTable.add(row20a);
        actionTable.add(row21a);
        actionTable.add(row22a);
        actionTable.add(row23a);
        actionTable.add(row24a);
        actionTable.add(row25a);
        actionTable.add(row26a);
        actionTable.add(row27a);
        actionTable.add(row28a);
        actionTable.add(row29a);
        actionTable.add(row30a);
        actionTable.add(row31a);
        actionTable.add(row32a);
        actionTable.add(row33a);
        actionTable.add(row34a);
        actionTable.add(row35a);
        actionTable.add(row36a);
        actionTable.add(row37a);
        actionTable.add(row38a);
        actionTable.add(row39a);
        actionTable.add(row40a);
        actionTable.add(row41a);
        actionTable.add(row42a);
        actionTable.add(row43a);
        actionTable.add(row44a);
        actionTable.add(row45a);
        actionTable.add(row46a);
        actionTable.add(row47a);
        actionTable.add(row48a);
        actionTable.add(row49a);
        actionTable.add(row50a);
        actionTable.add(row51a);
        actionTable.add(row52a);
        actionTable.add(row53a);
        actionTable.add(row54a);
        actionTable.add(row55a);
        actionTable.add(row56a);
        actionTable.add(row57a);
        actionTable.add(row58a);
        actionTable.add(row59a);
        actionTable.add(row60a);
        actionTable.add(row61a);
        actionTable.add(row62a);
        actionTable.add(row63a);
        actionTable.add(row64a);
        actionTable.add(row65a);
        actionTable.add(row66a);
        actionTable.add(row67a);
        actionTable.add(row68a);
        actionTable.add(row69a);
        actionTable.add(row70a);
        actionTable.add(row71a);
        actionTable.add(row72a);
        actionTable.add(row73a);
        actionTable.add(row74a);
        actionTable.add(row75a);
        actionTable.add(row76a);
        actionTable.add(row77a);
        actionTable.add(row78a);
        actionTable.add(row79a);
        actionTable.add(row80a);
        actionTable.add(row81a);
        actionTable.add(row82a);
        actionTable.add(row83a);
        actionTable.add(row84a);
        actionTable.add(row85a);
        actionTable.add(row86a);
        actionTable.add(row87a);
        actionTable.add(row88a);
        actionTable.add(row89a);
        actionTable.add(row90a);
        actionTable.add(row91a);
        actionTable.add(row92a);
        actionTable.add(row93a);
        actionTable.add(row94a);
        actionTable.add(row95a);
        actionTable.add(row96a);
        actionTable.add(row97a);
        actionTable.add(row98a);
        actionTable.add(row99a);
        actionTable.add(row100a);
        actionTable.add(row101a);
        actionTable.add(row102a);
        actionTable.add(row103a);
        actionTable.add(row104a);
        actionTable.add(row105a);
        actionTable.add(row106a);
        actionTable.add(row107a);
        actionTable.add(row108a);
        actionTable.add(row109a);
        actionTable.add(row110a);
        actionTable.add(row111a);
        actionTable.add(row112a);
        actionTable.add(row113a);
        actionTable.add(row114a);
        actionTable.add(row115a);
        actionTable.add(row116a);
        actionTable.add(row117a);
        actionTable.add(row118a);
        actionTable.add(row119a);
        actionTable.add(row120a);
        actionTable.add(row121a);
        actionTable.add(row122a);
        actionTable.add(row123a);
        actionTable.add(row124a);
        actionTable.add(row125a);
        actionTable.add(row126a);
        actionTable.add(row127a);
        actionTable.add(row128a);
        actionTable.add(row129a);
        actionTable.add(row130a);
        actionTable.add(row131a);
        actionTable.add(row132a);
        actionTable.add(row133a);
        actionTable.add(row134a);
        actionTable.add(row135a);
        actionTable.add(row136a);
        actionTable.add(row137a);
        actionTable.add(row138a);
        actionTable.add(row139a);
        actionTable.add(row140a);
        actionTable.add(row141a);
        actionTable.add(row142a);
        actionTable.add(row143a);
        actionTable.add(row144a);
        actionTable.add(row145a);
        actionTable.add(row146a);
        actionTable.add(row147a);
        actionTable.add(row148a);
        actionTable.add(row149a);
        actionTable.add(row150a);
        actionTable.add(row151a);
        actionTable.add(row152a);
        actionTable.add(row153a);
        actionTable.add(row154a);
        actionTable.add(row155a);


        gotoTable.add(row0g);
        gotoTable.add(row1g);
        gotoTable.add(row2g);
        gotoTable.add(row3g);
        gotoTable.add(row4g);
        gotoTable.add(row5g);
        gotoTable.add(row6g);
        gotoTable.add(row7g);
        gotoTable.add(row8g);
        gotoTable.add(row9g);
        gotoTable.add(row10g);
        gotoTable.add(row11g);
        gotoTable.add(row12g);
        gotoTable.add(row13g);
        gotoTable.add(row14g);
        gotoTable.add(row15g);
        gotoTable.add(row16g);
        gotoTable.add(row17g);
        gotoTable.add(row18g);
        gotoTable.add(row19g);
        gotoTable.add(row20g);
        gotoTable.add(row21g);
        gotoTable.add(row22g);
        gotoTable.add(row23g);
        gotoTable.add(row24g);
        gotoTable.add(row25g);
        gotoTable.add(row26g);
        gotoTable.add(row27g);
        gotoTable.add(row28g);
        gotoTable.add(row29g);
        gotoTable.add(row30g);
        gotoTable.add(row31g);
        gotoTable.add(row32g);
        gotoTable.add(row33g);
        gotoTable.add(row34g);
        gotoTable.add(row35g);
        gotoTable.add(row36g);
        gotoTable.add(row37g);
        gotoTable.add(row38g);
        gotoTable.add(row39g);
        gotoTable.add(row40g);
        gotoTable.add(row41g);
        gotoTable.add(row42g);
        gotoTable.add(row43g);
        gotoTable.add(row44g);
        gotoTable.add(row45g);
        gotoTable.add(row46g);
        gotoTable.add(row47g);
        gotoTable.add(row48g);
        gotoTable.add(row49g);
        gotoTable.add(row50g);
        gotoTable.add(row51g);
        gotoTable.add(row52g);
        gotoTable.add(row53g);
        gotoTable.add(row54g);
        gotoTable.add(row55g);
        gotoTable.add(row56g);
        gotoTable.add(row57g);
        gotoTable.add(row58g);
        gotoTable.add(row59g);
        gotoTable.add(row60g);
        gotoTable.add(row61g);
        gotoTable.add(row62g);
        gotoTable.add(row63g);
        gotoTable.add(row64g);
        gotoTable.add(row65g);
        gotoTable.add(row66g);
        gotoTable.add(row67g);
        gotoTable.add(row68g);
        gotoTable.add(row69g);
        gotoTable.add(row70g);
        gotoTable.add(row71g);
        gotoTable.add(row72g);
        gotoTable.add(row73g);
        gotoTable.add(row74g);
        gotoTable.add(row75g);
        gotoTable.add(row76g);
        gotoTable.add(row77g);
        gotoTable.add(row78g);
        gotoTable.add(row79g);
        gotoTable.add(row80g);
        gotoTable.add(row81g);
        gotoTable.add(row82g);
        gotoTable.add(row83g);
        gotoTable.add(row84g);
        gotoTable.add(row85g);
        gotoTable.add(row86g);
        gotoTable.add(row87g);
        gotoTable.add(row88g);
        gotoTable.add(row89g);
        gotoTable.add(row90g);
        gotoTable.add(row91g);
        gotoTable.add(row92g);
        gotoTable.add(row93g);
        gotoTable.add(row94g);
        gotoTable.add(row95g);
        gotoTable.add(row96g);
        gotoTable.add(row97g);
        gotoTable.add(row98g);
        gotoTable.add(row99g);
        gotoTable.add(row100g);
        gotoTable.add(row101g);
        gotoTable.add(row102g);
        gotoTable.add(row103g);
        gotoTable.add(row104g);
        gotoTable.add(row105g);
        gotoTable.add(row106g);
        gotoTable.add(row107g);
        gotoTable.add(row108g);
        gotoTable.add(row109g);
        gotoTable.add(row110g);
        gotoTable.add(row111g);
        gotoTable.add(row112g);
        gotoTable.add(row113g);
        gotoTable.add(row114g);
        gotoTable.add(row115g);
        gotoTable.add(row116g);
        gotoTable.add(row117g);
        gotoTable.add(row118g);
        gotoTable.add(row119g);
        gotoTable.add(row120g);
        gotoTable.add(row121g);
        gotoTable.add(row122g);
        gotoTable.add(row123g);
        gotoTable.add(row124g);
        gotoTable.add(row125g);
        gotoTable.add(row126g);
        gotoTable.add(row127g);
        gotoTable.add(row128g);
        gotoTable.add(row129g);
        gotoTable.add(row130g);
        gotoTable.add(row131g);
        gotoTable.add(row132g);
        gotoTable.add(row133g);
        gotoTable.add(row134g);
        gotoTable.add(row135g);
        gotoTable.add(row136g);
        gotoTable.add(row137g);
        gotoTable.add(row138g);
        gotoTable.add(row139g);
        gotoTable.add(row140g);
        gotoTable.add(row141g);
        gotoTable.add(row142g);
        gotoTable.add(row143g);
        gotoTable.add(row144g);
        gotoTable.add(row145g);
        gotoTable.add(row146g);
        gotoTable.add(row147g);
        gotoTable.add(row148g);
        gotoTable.add(row149g);
        gotoTable.add(row150g);
        gotoTable.add(row151g);
        gotoTable.add(row152g);
        gotoTable.add(row153g);
        gotoTable.add(row154g);
        gotoTable.add(row155g);
    }

}
