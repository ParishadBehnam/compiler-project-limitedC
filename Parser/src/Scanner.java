import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by parishad behnam on 6/5/2017.
 */
public class Scanner {

    public static ArrayList<HashMap<Index, Target>> symbolTable;

    private static boolean inDeclaration;
    private static int isSingle;
    private static boolean isError;
    private static int pointer;
    private static String input;
    private static java.util.Scanner scanner;
    private static ArrayList<Character> singles;
    private static Token token;

    public Scanner() {
        pointer = -1;
        inDeclaration = false;
        isSingle = 1;
        isError = false;
        singles = new ArrayList<Character>(Arrays.asList(',', ';', '*', '<', '(', ')', '[', ']', '{', '}', '=', '&', '/', '+', '-'));
        symbolTable = new ArrayList<>();
        symbolTable.add(new HashMap<Index, Target>());

        try {
            scanner = new java.util.Scanner(new File("/Users/afra/University/Compiler/[قختثزف/compiler-limitedC/Parser/src/file.txt"));
//            scanner = new java.util.Scanner(new File("C:\\Users\\parishad behnam\\IdeaProjects\\compiler-limitedC\\Parser\\src\\file.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        input = getInput(scanner);

    }

    private static String getInput(java.util.Scanner scanner) {
        String s = scanner.next() + " ";
        return s;
    }

    public static void incScope() {
        symbolTable.add(new HashMap<Index, Target>());
    }

    public static void decScope() {
        symbolTable.remove(symbolTable.size() - 1);
    }

    public static Target lookup(Index index) {

        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            if (symbolTable.get(i).containsKey(index))
                return symbolTable.get(i).get(index);
        }
        return null;
    }


    public static void main(String[] args) {

        Scanner myScanner = new Scanner();
        for (int i = 0; i < 30; i++) {
            Scanner.getToken();
            System.out.println("type: " + token.type + " name: " + token.name);
        }
    }

    public static Token getToken() {
        HashMap<Index, Target> partial;
        token = new Token();
        match(0, "");
        if (token.type != null && token.type.equals("ID")) {
            Index index = new Index(token.name);
            Target target = new Target("", null, 0, symbolTable.size());
            partial = symbolTable.get(symbolTable.size() - 1);
            if (!partial.containsKey(index)) {
                if (inDeclaration) {
                    partial.put(index, target);
                } else if (symbolTable.size() == 1 || !symbolTable.get(0).containsKey(index))
                    partial.put(index, target);
            }
            inDeclaration = false;
        }
        return token;
    }

    private static void match(int state, String tokenstr) {
        String errorMessage = "";
        boolean finished = false;
        char ch = ' ';
        pointer++;

        if (pointer >= input.length() && !scanner.hasNext()) {
            token = new Token("$", "");
            input = "";
            pointer = 0;
            return;
        }

        if ((state == 7 || state == 8) && pointer >= input.length()) {
            input = getInput(scanner);
            isSingle = 0;
            pointer = 0;
            ch = input.charAt(pointer);
        } else if (pointer == input.length() - 1) {
            ch = input.charAt(pointer);
            finished = true;
        } else if (pointer >= input.length()) {
            finished = true;
            input = getInput(scanner);
            ch = input.charAt(0);
            pointer = 0;
        } else
            ch = input.charAt(pointer);

//        System.out.println("state " + state + " : " + isSingle + " " + ch);
        if (state == 0) {
            if (ch == ' ') {
                match(state, tokenstr);
                isSingle = 0;
            } else if (Character.isLetter(ch)) {
                match(1, tokenstr + ch);
                isSingle = 0;
            } else if (Character.isDigit(ch)) {
                match(3, tokenstr + ch);
                isSingle = 0;
            } else if (ch == '+' || ch == '-') {
                isSingle++;
                match(2, tokenstr + ch);
            } else if (ch == '=') {
                isSingle++;
                match(4, tokenstr + ch);
            } else if (ch == '&') {
                isSingle++;
                match(5, tokenstr + ch);
            } else if (ch == '/') {
                isSingle++;
                match(6, tokenstr + ch);
            } else if (singles.contains(ch)) {
                if (ch == '*' || ch == ';' || ch == '<' || ch == ',' || ch == '[' || ch == '(' || ch == '{')
                    isSingle++;
                else
                    isSingle = 0;
                token = new Token(tokenstr + ch, "");
                return;
            }
        } else if (state == 1) {
            if (Character.isLetterOrDigit(ch)) {
                match(state, tokenstr + ch);
            } else if (ch == '+' | ch == '-' || singles.contains(ch)) {
                pointer--;
                isSingle = 0;
                token = new Token(findType(tokenstr), tokenstr);
                return;
            } else if (finished) {
                isSingle = 0;
                if (ch == ' ') {
                    token = new Token(findType(tokenstr), tokenstr);
                    return;
                }
                token = new Token(findType(tokenstr + ch), tokenstr + ch);
                return;
            } else {
                errorMessage = "your ID (" + tokenstr + ch + ") includes invalid characters.";
                isError = true;
            }
        } else if (state == 2) {
            if (ch == '-' || ch == '+') {
                pointer--;
                isSingle = 1;
                token = new Token(tokenstr, "");
                return;
            } else if (singles.contains(ch) || Character.isLetter(ch)) {
                if (isSingle >= 2) {
                    isSingle = 1;
                    token = new Token(tokenstr + ch, "");
                    return;
                } else {
                    pointer--;
                    isSingle = 1;
                    token = new Token(tokenstr, "");
                    return;
                }
            } else if (Character.isDigit(ch) && isSingle >= 2) {
                match(3, tokenstr + ch);
            } else if (Character.isDigit(ch) && isSingle == 1) {
                pointer--;
                isSingle = 1;
                token = new Token(tokenstr, "");
                return;
            } else if (finished) {
                if (ch == ' ') {
                    token = new Token(tokenstr, "");
                    return;
                }
                token = new Token("NUM", tokenstr + ch);
                return;
            } else {
                errorMessage = "invalid character (" + ch + ") is found";
                isError = true;
            }
        } else if (state == 3) {
            if (Character.isDigit(ch)) {
                match(state, tokenstr + ch);
            } else if (ch == '+' | ch == '-' || singles.contains(ch)) {
                pointer--;
                isSingle = 0;
                token = new Token("NUM", tokenstr);
                return;
            } else if (ch == '.') {
                match(3, tokenstr + ch);
            } else if (finished) {
                if (ch == ' ') {
                    token = new Token("NUM", tokenstr);
                    return;
                }
                token = new Token("NUM", tokenstr + ch);
                return;
            } else {
                errorMessage = "invalid number (" + tokenstr + ch + ") is found";
                isError = true;
            }
        } else if (state == 4) {
            if (ch == '=') {
                token = new Token("==", "");
                return;
            } else if (Character.isLetterOrDigit(ch) || singles.contains(ch)) {
                pointer--;
                token = new Token("=", "");
                return;
            } else if (finished) {
                token = new Token("=", "");
                return;
            }
        } else if (state == 5) {
            if (ch == '&') {
                token = new Token("&&", "");
                return;
            }
        } else if (state == 6) {
            if (ch == '*') {
                match(7, "");
            } else if (Character.isLetterOrDigit(ch) || singles.contains(ch)) {
                pointer--;
                token = new Token("/", "");
                return;
            } else if (finished) {
                token = new Token("/", "");
                return;
            }
        } else if (state == 7) {
            if (ch == '*') {
                match(8, "");
            } else {
                match(7, "");
            }
        } else if (state == 8) {
            if (ch == '/') {
                match(0, "");
            } else {
                match(7, "");
            }
        }
        if (isError) {
            isError = false;
            System.out.println("ERROR: " + errorMessage);
            match(0, "");
        }
    }

    private static String findType(String str) {
        if (str.equals("if"))
            return "if";
        if (str.equals("else"))
            return "else";
        if (str.equals("output"))
            return "output";
        if (str.equals("void"))
            return "void";
        if (str.equals("int")) {
            inDeclaration = true;
            return "int";
        }
        if (str.equals("while"))
            return "while";
        if (str.equals("EOF"))
            return "EOF";
        if (str.equals("return"))
            return "return";
        return "ID";
    }
}
