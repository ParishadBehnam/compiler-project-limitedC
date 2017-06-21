/**
 * Created by afra on 6/5/17.
 */
public class Main {
    public static void main(String[] args) {
        Scanner myScanner = new Scanner();

        Parser p = new Parser();
        try {
            p.start();
        } catch (Exception e) {
            System.out.println("panic failed, code generated till now:");
        } finally {
            CodeGenerator.print();
        }
    }
}
