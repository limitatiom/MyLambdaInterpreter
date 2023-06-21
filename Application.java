package homework10.main;

public class Application extends AST{
    AST lhs;//左树
    AST rhs;//右树

    Application(AST l, AST s){
        lhs = l;
        rhs = s;
    }

    public Application() {}

    public String toString(){
        if (lhs == null) return rhs.toString();
        else if(rhs == null) return lhs.toString();
        else if(lhs!= null && rhs!= null) return "("+lhs.toString()+" "+rhs.toString()+")";
        else return "";
    }

    void setLhs(AST lhs){
        this.lhs = lhs;
    }

    void setRhs(AST rhs){
        this.rhs = rhs;
    }

    AST getLhs(){
        return lhs;
    }

    AST getRhs(){
        return rhs;
    }

    @Override
    public boolean equals(AST ast) {
        if (ast instanceof Application) {
            if (this.lhs.equals(((Application) ast).lhs) && this.rhs.equals(((Application) ast).rhs))
                return true;
            else
                return false;
        }else
            return false;
    }

}
