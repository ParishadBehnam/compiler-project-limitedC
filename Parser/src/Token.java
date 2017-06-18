/**
 * Created by parishad behnam on 6/5/2017.
 */
public class Token {

    public String type;
    public String name;

    public Token() {
        type = null;
        name = null;
    }
    public Token(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public Token(Token t) {
        this.type = t.type;
        this.name = t.name;
    }

}

class Index {

    public String name;
//    public int scope;

    public Index(String name) {
        this.name = name;
//        this.scope = scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Index index = (Index) o;

//        if (scope != index.scope) return false;
        return name.equals(index.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
//        result = 31 * result + scope;
//        return result;
    }
}

class Target{

    public String type;
    public Long address;
    public int dimension;
    public int scope;
    public int paramsNum;
    public boolean isVoid;
    public boolean isParam;
    public int length;

    public Target(String type, Long address, int dimension, int scope) {
        this.type = type;
        this.address = address;
        this.dimension = dimension;
        this.scope = scope;
        this.isParam = false;
    }
}

