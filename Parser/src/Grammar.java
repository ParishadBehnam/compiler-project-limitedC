/**
 * Created by afra on 6/22/17.
 */
import com.sun.javafx.collections.ArrayListenerHelper;

import java.util.*;

public class Grammar {
     ArrayList<Integer> getLen() {
        ArrayList<Integer>grammarLength = new ArrayList<Integer>
                (Arrays.asList(4, 2, 1, 1, 1, 2, 2, 1, 2, 2, 5, 1, 1, 4, 2, 2, 4, 4,
                        2, 0, 2, 0, 1, 1, 1, 1, 1, 5, 1, 7, 10, 8, 3, 4, 2, 6, 1, 1, 4,
                        1, 4, 4, 4, 1, 1, 1, 4, 1, 1, 1, 3, 1, 2, 2, 5, 1, 0, 3, 1));
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
        return grammarLength;

    }
     ArrayList<String> getLHS() {
        return new ArrayList<>(
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

    }
    public HashMap<String, ArrayList<String>> getFollows() {
        HashMap<String, ArrayList<String>> follows = new HashMap<>();
        follows.put("Program", new ArrayList<>(Collections.singletonList("$")));
        follows.put("X19", new ArrayList<>(Collections.singletonList("$")));
        follows.put("DeclarationList", new ArrayList<>(Arrays.asList("int", "void", "EOF")));
        follows.put("Declaration", new ArrayList<>(Arrays.asList("int", "void", "EOF")));
        follows.put("FunDeclaration", new ArrayList<>(Arrays.asList("int", "void", "EOF")));
        follows.put("R2", new ArrayList<>(Arrays.asList("int", "void", "EOF")));
        follows.put("X23", new ArrayList<>(Arrays.asList("int", "void", "EOF")));
        follows.put("VarDeclaration", new ArrayList<>(Arrays.asList("int", "void", "EOF", ";", "ID", "{", "if", "while", "return", "int", "}")));
        follows.put("R1", new ArrayList<>(Arrays.asList("int", "void", "EOF", ";", "ID", "{", "if", "while", "return", "int", "}")));
        follows.put("R3", new ArrayList<>(Arrays.asList("int", "void", "EOF", ";", "ID", "{", "if", "while", "return", "int", "}")));
        follows.put("LocalDeclarations", new ArrayList<>(Arrays.asList(";", "ID", "{", "if", "while", "return", "int", "}")));
        follows.put("Params", new ArrayList<>(Collections.singletonList(")")));
        follows.put("Args", new ArrayList<>(Collections.singletonList(")")));
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
        follows.put("X1", new ArrayList<>(Collections.singletonList(";")));
        follows.put("X6", new ArrayList<>(Collections.singletonList("NUM")));
        follows.put("X9", new ArrayList<>(Collections.singletonList("else")));
        follows.put("X14", new ArrayList<>(Collections.singletonList("(")));
        follows.put("X24", new ArrayList<>(Collections.singletonList("(")));
        follows.put("X22", new ArrayList<>(Collections.singletonList("(")));
        follows.put("X20", new ArrayList<>(Collections.singletonList(";")));
        follows.put("X21", new ArrayList<>(Collections.singletonList("NUM")));
        return follows;
    }
}
