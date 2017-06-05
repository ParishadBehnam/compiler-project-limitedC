/**
 * Created by afra on 6/5/17.
 */
import java.util.*;
public class Parser {
}
class ParseTable {
    ArrayList<HashMap <String, String>> actionTable;
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

        HashMap<String, Integer> row0g = new HashMap<>();
        HashMap<String, Integer> row2g = new HashMap<>();
        HashMap<String, Integer> row3g = new HashMap<>();
        HashMap<String, Integer> row9g = new HashMap<>();
        HashMap<String, Integer> row11g = new HashMap<>();
        HashMap<String, Integer> row13g = new HashMap<>();
        HashMap<String, Integer> row14g = new HashMap<>();
        HashMap<String, Integer> row15g = new HashMap<>();
        HashMap<String, Integer> row19g = new HashMap<>();
        HashMap<String, Integer> row22g = new HashMap<>();
        HashMap<String, Integer> row30g = new HashMap<>();
        HashMap<String, Integer> row31g = new HashMap<>();
        HashMap<String, Integer> row36g = new HashMap<>();
        HashMap<String, Integer> row37g = new HashMap<>();
        HashMap<String, Integer> row38g = new HashMap<>();
        HashMap<String, Integer> row39g = new HashMap<>();
        HashMap<String, Integer> row40g = new HashMap<>();
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


        row0a.put("int", "r66");
        row0a.put("if", "r66");
        row0a.put("void", "r66");
        row0a.put(";", "r66");
        row0a.put("ID", "r66");
        row0a.put("while", "r66");
        row0a.put("return", "r66");

        row0g.put("Program", 1);
        row0g.put("X7", 2);

        row1a.put("$", "accept");

        row2a.put("int", "s24");
        row2a.put("int", "r77");
        row2a.put("void", "r77");

        row2g.put("DiclarationList", 3);
        row2g.put("Diclaration", 4);
        row2g.put("VarDiclaration", 5);
        row2g.put("FunDeclaration", 7);
        row2g.put("TypeSpecifier", 14);
        row2g.put("X18", 13);

        row3a.put("EOF", "s11");
        row3a.put("int", "r77 s24");
        row3a.put("void", "r77");

        row3g.put("Declaration", 12);
        row3g.put("VarDeclaration", 5);
        row3g.put("FunDeclaration", 7);
        row3g.put("TypeSpecifier", 14);
        row3g.put("X18", 13);

        row4a.put("EOF", "r3");
        row4a.put("int", "r3");
        row4a.put("void", "r3");

        row5a.put("EOF", "r4");
        row5a.put("int", "r4");
        row5a.put("void", "r4");

        row6a.put("ID", "r10");

        row7a.put("EOF", "r5");
        row7a.put("int", "r5");
        row7a.put("void", "r5");

        row8a.put("ID", "r11");

        row9a.put("[", "s15");
        row9a.put(";", "r79");

        row9g.put("X20", 120);

        row10a.put("ID", "s17");

        row11g.put("X19", 16);

        row11a.put("$", "r1");

        row12a.put("EOF", "r2");
        row12a.put("int", "r2");
        row12a.put("void", "r2");

        row13a.put("int", "s6");
        row13a.put("void", "s8");

        row13g.put("FunReturnType", 121);

        row14g.put("ID", 9);

        row15g.put("X21", 18);

        row15a.put("NUM", "r80");

        row16a.put("$", "r1");

        row17a.put("(", "s19");

        row18a.put("NUM", "s26");

        row19a.put("void", "s23");
        row19a.put("int", "s24");

        row19g.put("Params", 20);
        row19g.put("ParamList", 21);
        row19g.put("TypeSpecifier", 25);

        row20a.put(")", "s36");

        row21a.put(",", "s30");
        row21a.put(")", "r12");

        row22a.put(")", "r75");
        row22g.put("X16",29);

        row23a.put(")","r13");

        row24a.put("ID", "r8");

        row25a.put("ID", "s33");

        row26a.put("]", "s27");

        row27a.put(";", "s28");

        row28a.put("EOF", "r7");
        row28a.put("int", "r7");
        row28a.put("void", "r7");
        row28a.put("if", "r7");
        row28a.put("while", "r7");
        row28a.put("return", "r7");
        row28a.put("ID", "r7");
        row28a.put("}", "r7");

        row29a.put(")", "r15");

        row30a.put("int", "s24");
        row30g.put("Param", 31);
        row30g.put("TypeSpecifier", 25);

        row31a.put(")", "r75");
        row31g.put("X16", 32);

        row32a.put(")", "r14");

        row33a.put(")", "r16");
        row33a.put("[", "s34");

        row34a.put("]", "s35");

        row35a.put(")", "r17");

        row36a.put("{", "s37");
        row36g.put("CompoundStmt", 39);

        row37a.put("int", "r20");
        row37a.put("}", "r20");
        row37a.put(";", "r20");
        row37a.put("if", "r20");
        row37a.put("while", "r20");
        row37a.put("ID", "r20");
        row37a.put("return", "r20");
        row37g.put("LocalDeclarations", 38);

        row38g.put("StatementList", 40);
        row38g.put("VarDeclarations", 41);
        row38g.put("TypeSpecifier", 14);
        row38a.put("int", "s24");
        row38a.put(";", "r22");
        row38a.put("ID", "r22");
        row38a.put("{", "r22");
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

        row40g.put("Statement", 46);
        row40g.put("ExpressionStmt", 43);
        row40g.put("CompoundStmt", 47);
        row40g.put("SelectionStmt", 44);
        row40g.put("ReturnStmt", 45);
        row40g.put("Var", 50);
        row40g.put("X2", 51);
        row40g.put("X2", 51);
        row40a.put("ID", "r61");

        row41a.put("int", "r19");
        row41a.put("}", "r19");
        row41a.put("if", "r19");
        row41a.put("while", "r19");
        row41a.put("return", "r19");
        row41a.put("ID", "r19");
        row41a.put("}", "r19");
        row41a.put(";", "r19");

        row42a.put(";", "r18");
        row42a.put("ID", "r18");
        row42a.put("{", "r18");
        row42a.put("if", "r18");
        row42a.put("while", "r18");
        row42a.put("return", "r18");

        row43a.put(";", "r23");
        row43a.put("ID", "r23");
        row43a.put("{", "r23");
        row43a.put("if", "r23");
        row43a.put("while", "r23");
        row43a.put("return", "r23");

        row44a.put(";", "r25");
        row44a.put("ID", "r25");
        row44a.put("{", "r25");
        row44a.put("if", "r25");
        row44a.put("while", "r25");
        row44a.put("return", "r25");

        row45a.put(";", "r27");
        row45a.put("ID", "r27");
        row45a.put("{", "r27");
        row45a.put("if", "r27");
        row45a.put("while", "r27");
        row45a.put("return", "r27");

        row46a.put(";", "r21");
        row46a.put("ID", "r21");
        row46a.put("{", "r21");
        row46a.put("if", "r21");
        row46a.put("while", "r21");
        row46a.put("return", "r21");

        row47a.put(";", "r24");
        row47a.put("ID", "r24");
        row47a.put("{", "r24");
        row47a.put("if", "r24");
        row47a.put("while", "r24");
        row47a.put("return", "r24");

        row48a.put(";", "r26");
        row48a.put("ID", "r26");
        row48a.put("{", "r26");
        row48a.put("if", "r26");
        row48a.put("while", "r26");
        row48a.put("return", "r26");

        row49a.put(";", "r29");
        row49a.put("ID", "r29");
        row49a.put("{", "r29");
        row49a.put("if", "r29");
        row49a.put("while", "r29");
        row49a.put("return", "r29");

        row50a.put("=", "s55");

        row51a.put("ID", "s56");

        row52a.put("(", "s76");

        row53a.put("(", "s75");
        row53a.put("ID", "r61 s68");
        row53a.put(";", "s58");
        row53a.put("NUM", "r65");
        row53g.put("RelExpression", 60);
        row53g.put("Expression", 61);
        row53g.put("RelTerm", 62);
        row53g.put("Term", 63);
        row53g.put("Factor", 64);
        row53g.put("Var", 65);
        row53g.put("Call", 66);
        row53g.put("X6", 67);
        row53g.put("X7", 51);
        row53g.put("GenExpression", 61);

        actionTable.add(new HashMap<String, String>());
    }

}
