// generated with ast extension for cup
// version 0.8
// 9/1/2026 18:57:41


package rs.ac.bg.etf.pp1.ast;

public class ActParsList implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Expression Expression;
    private ActParsRest ActParsRest;

    public ActParsList (Expression Expression, ActParsRest ActParsRest) {
        this.Expression=Expression;
        if(Expression!=null) Expression.setParent(this);
        this.ActParsRest=ActParsRest;
        if(ActParsRest!=null) ActParsRest.setParent(this);
    }

    public Expression getExpression() {
        return Expression;
    }

    public void setExpression(Expression Expression) {
        this.Expression=Expression;
    }

    public ActParsRest getActParsRest() {
        return ActParsRest;
    }

    public void setActParsRest(ActParsRest ActParsRest) {
        this.ActParsRest=ActParsRest;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expression!=null) Expression.accept(visitor);
        if(ActParsRest!=null) ActParsRest.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expression!=null) Expression.traverseTopDown(visitor);
        if(ActParsRest!=null) ActParsRest.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expression!=null) Expression.traverseBottomUp(visitor);
        if(ActParsRest!=null) ActParsRest.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParsList(\n");

        if(Expression!=null)
            buffer.append(Expression.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsRest!=null)
            buffer.append(ActParsRest.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParsList]");
        return buffer.toString();
    }
}
