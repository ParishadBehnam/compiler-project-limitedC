/**
 * Created by afra on 6/5/17.
 */
import java.util.*;
public class Parser {
}
class ParseTable {
    ArrayList<HashMap <String, String>> actionTable;
    ArrayList<HashMap<String, String>> gotoTable;

    public ParseTable() {
        actionTable = new ArrayList<>();
        gotoTable = new ArrayList<>();

        HashMap<String, String> row0 = new HashMap<>();

        actionTable.add(new HashMap<String, String>());
    }

}
