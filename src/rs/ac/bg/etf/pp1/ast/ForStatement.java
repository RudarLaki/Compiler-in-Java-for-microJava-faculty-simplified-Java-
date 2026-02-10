// generated with ast extension for cup
// version 0.8
// 10/1/2026 0:30:21


package rs.ac.bg.etf.pp1.ast;

public class ForStatement extends MatchedStatement {

    private ForWord ForWord;
    private ForExpression ForExpression;
    private MatchedStatement MatchedStatement;

    public ForStatement (ForWord ForWord, ForExpression ForExpression, MatchedStatement MatchedStatement) {
        this.ForWord=ForWord;
        if(ForWord!=null) ForWord.setParent(this);
        this.ForExpression=ForExpression;
        if(ForExpression!=null) ForExpression.setParent(this);
        this.MatchedStatement=MatchedStatement;
        if(MatchedStatement!=null) MatchedStatement.setParent(this);
    }

    public ForWord getForWord() {
        return ForWord;
    }

    public void setForWord(ForWord ForWord) {
        this.ForWord=ForWord;
    }

    public ForExpression getForExpression() {
        return ForExpression;
    }

    public void setForExpression(ForExpression ForExpression) {
        this.ForExpression=ForExpression;
    }

    public MatchedStatement getMatchedStatement() {
        return MatchedStatement;
    }

    public void setMatchedStatement(MatchedStatement MatchedStatement) {
        this.MatchedStatement=MatchedStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ForWord!=null) ForWord.accept(visitor);
        if(ForExpression!=null) ForExpression.accept(visitor);
        if(MatchedStatement!=null) MatchedStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForWord!=null) ForWord.traverseTopDown(visitor);
        if(ForExpression!=null) ForExpression.traverseTopDown(visitor);
        if(MatchedStatement!=null) MatchedStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForWord!=null) ForWord.traverseBottomUp(visitor);
        if(ForExpression!=null) ForExpression.traverseBottomUp(visitor);
        if(MatchedStatement!=null) MatchedStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForStatement(\n");

        if(ForWord!=null)
            buffer.append(ForWord.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForExpression!=null)
            buffer.append(ForExpression.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MatchedStatement!=null)
            buffer.append(MatchedStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForStatement]");
        return buffer.toString();
    }
}
