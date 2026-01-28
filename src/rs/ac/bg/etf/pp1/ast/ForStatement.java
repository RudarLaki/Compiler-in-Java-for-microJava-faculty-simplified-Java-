// generated with ast extension for cup
// version 0.8
// 28/0/2026 3:38:26


package rs.ac.bg.etf.pp1.ast;

public class ForStatement extends MatchedStatement {

    private ForExpression ForExpression;
    private MatchedStatement MatchedStatement;

    public ForStatement (ForExpression ForExpression, MatchedStatement MatchedStatement) {
        this.ForExpression=ForExpression;
        if(ForExpression!=null) ForExpression.setParent(this);
        this.MatchedStatement=MatchedStatement;
        if(MatchedStatement!=null) MatchedStatement.setParent(this);
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
        if(ForExpression!=null) ForExpression.accept(visitor);
        if(MatchedStatement!=null) MatchedStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForExpression!=null) ForExpression.traverseTopDown(visitor);
        if(MatchedStatement!=null) MatchedStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForExpression!=null) ForExpression.traverseBottomUp(visitor);
        if(MatchedStatement!=null) MatchedStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForStatement(\n");

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
