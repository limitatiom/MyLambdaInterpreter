package homework10.main;

public class Interpreter {
    Parser parser;
    AST astAfterParser;

    public Interpreter(Parser p){
        parser = p;
        astAfterParser = p.parse();
        System.out.println("After parser:"+astAfterParser.toString());
    }

    private  boolean isAbstraction(AST ast){
        return ast instanceof Abstraction;
    }
    private  boolean isApplication(AST ast){
        return ast instanceof Application;
    }
    private  boolean isIdentifier(AST ast){
        return ast instanceof Identifier;
    }

    public AST eval(){
        return evalAST(astAfterParser);
    }


    private  AST evalAST(AST ast) {
        while (true) {
            if (isApplication(ast)) {
                if (isAbstraction(((Application) ast).lhs)) {
                    //((Application) ast).rhs = evalAST(((Application) ast).rhs);
                    ast = substitute(((Abstraction) ((Application) ast).lhs).body, ((Application) ast).rhs);
                } else if (isApplication(((Application) ast).lhs) && !isIdentifier(((Application) ast).rhs)) {
                    ((Application) ast).lhs = evalAST(((Application) ast).lhs);
                    ((Application) ast).rhs = evalAST(((Application) ast).rhs);
                    if (isAbstraction(((Application) ast).lhs)) ast = evalAST(ast);
                    return ast;
                } else if (isApplication(((Application) ast).lhs) && isIdentifier(((Application) ast).rhs)) {
                    ((Application) ast).lhs = evalAST(((Application) ast).lhs);
                    if (isAbstraction(((Application) ast).lhs)) ast = evalAST(ast);
                    return ast;
                } else {
                    ((Application) ast).rhs = evalAST(((Application) ast).rhs);
                    return ast;
                }
            } else if (isAbstraction(ast)) {
                ((Abstraction) ast).body = evalAST(((Abstraction) ast).body);
                return ast;
            }
            else return ast;
        }
    }

    private AST substitute(AST node,AST value){
        return shift(-1,subst(node,shift(1,value,0),0),0);
    }

    private AST subst(AST node, AST value, int depth){
        if(isApplication(node)){
            return new Application(subst(((Application)node).lhs, value, depth), subst(((Application)node).rhs, value, depth));
        } else if(isAbstraction(node)){
            return new Abstraction(((Abstraction)node).param, subst(((Abstraction)node).body, value, depth + 1));
        } else {
            if(depth == ((Identifier)node).getValue()) return shift(depth, value, 0);//返回替换深度的value
            else return node;//返回待被替换的lambda项
        }
    }

    private AST shift(int by, AST node,int from){
        if(isApplication(node)) {
            return new Application(shift(by, ((Application)node).lhs, from), shift(by, ((Application)node).rhs, from));
        } else if (isAbstraction(node)) {
            return new Abstraction(((Abstraction)node).param,  shift(by, ((Abstraction)node).body, from + 1));//从Abstraction的0开始偏移，进入body后因为0已经被偏移过了，所以要转向下一位
        } else {
            return new Identifier(((Identifier)node).name, String.valueOf(((Identifier) node).getValue() + (((Identifier) node).getValue() >= from ? by : 0)));
        }
    }
}
