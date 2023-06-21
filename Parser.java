package homework10.main;

import java.util.ArrayList;

public class Parser {
    Lexer lexer;
    public Parser(Lexer l){
        lexer = l;
    }

    public AST parse(){//解析入口
        AST ast = parseAsTerm(new ArrayList<>());
        return ast;
    }

    /**
     *解析 term
     * @param ctx
     * @return
     */
    private AST parseAsTerm(ArrayList<String> ctx){
        if(lexer.curIsMatched(TokenType.LAMBDA)){//一整个表达式，继续当作Term
            lexer.nextToken();
            String cur = lexer.curSrc;
            lexer.nextToken();
            if(lexer.curIsMatched(TokenType.DOT)) lexer.nextToken();
            ctx.add(0, cur);
            String value = "" + ctx.indexOf(cur);
            AST body = parseAsTerm(ctx);
            ctx.remove(cur);
            return new Abstraction(new Identifier(cur, value), body);
        } else {//不是变量
            if(lexer.curIsMatched(TokenType.EOF)) return null;
            return parseAsApplication(ctx);
        }
    }

    /**
     *解析 application
     * @param ctx
     * @return
     */
    private AST parseAsApplication(ArrayList<String> ctx){
        //TODO
        Application application = new Application();
        application.setLhs(parseAsAtom(ctx));
        application.setRhs(parseAsAtom(ctx));
        while(true){
            if(application.getRhs() == null){return application.getLhs();}//右树为空，则说明该应用已结束
            else{
                application.setLhs(new Application(application.getLhs(), application.getRhs()));
                application.setRhs(parseAsAtom(ctx));
            }
        }
    }

    /**
     *解析 atom
     * @param ctx
     * @return
     */
    private AST parseAsAtom(ArrayList<String> ctx){
        //TODO
        if(lexer.curIsMatched(TokenType.LPAREN)){
            lexer.nextToken();
            AST term = parseAsTerm(ctx);
            if(lexer.curIsMatched(TokenType.RPAREN) || lexer.curIsMatched(TokenType.EOF)) {lexer.nextToken();return term;}
        } else if (lexer.curIsMatched(TokenType.LCID)){
            String name = lexer.curSrc;
            String value = "" + ctx.indexOf(name);
            lexer.nextToken();
            return new Identifier(name, value);
        }
        return  null;
    }
}
