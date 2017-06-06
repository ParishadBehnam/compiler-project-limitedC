import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by parishad behnam on 6/5/2017.
 */
public class Scanner {

    public static HashMap<Index, Target> symbolTable;
    public static Stack<Integer> scopeStack;

    private static boolean inDeclaration;
    private static boolean isError;
    private static int pointer;
    private static String input;
    private static int maxScope;
    private static BufferedReader br;
        private static java.util.Scanner scanner;
    private static ArrayList<Character> singles;
    private static Token token;

    public Scanner() {
        maxScope = 0;
        pointer = -1;
        inDeclaration = false;
        isError = false;
        singles = new ArrayList<Character>(Arrays.asList(',', ';', '*', '<', '(', ')', '[', ']', '{', '}', '=', '&', '/'));
        symbolTable = new HashMap<>();
        scopeStack = new Stack<>();

        URL url = getClass().getResource("file.txt");
        try {
//            scanner = new java.util.Scanner(new File(url.getPath()));
            scanner = new java.util.Scanner(new File("/Users/afra/University/Compiler/Final Project/compiler-limitedC/Parser/src/file.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        input = getInput(scanner);

    }

    private static String getInput(java.util.Scanner scanner) {
        String s = scanner.next() + " ";
//        System.out.println(s);
        return s;
    }

    public static void incScope() {
        maxScope++;
        scopeStack.push(maxScope);
    }

    public static void decScope() {
        scopeStack.pop();
    }


    public static void main(String[] args) {

        Scanner myScanner = new Scanner();

        for (int i = 0; i < 20; i++) {
            Scanner.getToken();
            System.out.println("type: "+token.type+" name: "+token.name);
        }
    }

    public static Token getToken() {
        token = new Token();
        match(0, "");
        if (token.type != null && token.type.equals("ID")) {
            Index index1 = new Index(token.name, maxScope);
            Index index2 = new Index(token.name, 0);
            Target target = new Target("", 0, 0, maxScope);
            if (!symbolTable.containsKey(index1)) {
                if (inDeclaration) {
                    symbolTable.put(index1, target);
                } else if (maxScope == 0 || !symbolTable.containsKey(index2))
                    symbolTable.put(index1, target);
            }
            inDeclaration = false;
        }
        return token;
    }

    private static void match(int state, String tokenstr) {
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
        if (state == 0) {
            if (ch == ' ') {
                match(state, tokenstr);
            } else if (Character.isLetter(ch)) {
                match(1, tokenstr + ch);
            } else if (Character.isDigit(ch)) {
                match(3, tokenstr + ch);
            } else if (ch == '+' || ch == '-') {
                match(2, tokenstr + ch);
            } else if (ch == '=') {
                match(4, tokenstr + ch);
            } else if (ch == '&') {
                match(5, tokenstr + ch);
            } else if (ch == '/') {
                match(6, tokenstr + ch);
            } else if (singles.contains(ch)) {
                token = new Token(tokenstr + ch, "");
                return;
            }
        } else if (state == 1) {
            if (Character.isLetterOrDigit(ch)) {
                match(state, tokenstr + ch);
            } else if (ch == '+' | ch == '-' || singles.contains(ch)) {
                pointer--;
                token = new Token(findType(tokenstr), tokenstr);
                return;
            } else if (finished) {
                if (ch == ' '){
                    token = new Token(findType(tokenstr), tokenstr);
                    return;
                }
                token = new Token(findType(tokenstr+ch), tokenstr + ch);
                return;
            } else {
                isError = true;
            }
        } else if (state == 2) {
            if (Character.isDigit(ch)) {
                match(3, tokenstr + ch);
            } else if (ch == '-' || ch == '+' || singles.contains(ch) || Character.isLetter(ch)) {
                pointer--;
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
                isError = true;
            }
        } else if (state == 3) {
            if (Character.isDigit(ch)) {
                match(state, tokenstr + ch);
            } else if (ch == '+' | ch == '-' || singles.contains(ch)) {
                pointer--;
                token = new Token("NUM", tokenstr);
                return;
            } else if (finished) {
                if (ch == ' ') {
                    token = new Token("NUM", tokenstr);
                    return;
                }
                token = new Token("NUM", tokenstr + ch);
                return;
            } else {
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
            //TODO
            isError = false;
            System.out.println("ERROR");
            input = getInput(scanner);
            pointer = -1;
            match(0, "");
        }
    }

    private static String findType(String str) {
        if (str.equals("if"))
            return "if";
        if (str.equals("else"))
            return "else";
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
