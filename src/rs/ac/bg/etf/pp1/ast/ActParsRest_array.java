// generated with ast extension for cup
// version 0.8
// 9/1/2026 18:57:41


package rs.ac.bg.etf.pp1.ast;

public class ActParsRest_array extends ActParsRest {

    private Expression Expression;
    private ActParsRest ActParsRest;

    public ActParsRest_array (Expression Expression, ActParsRest ActParsRest) {
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
        buffer.append("ActParsRest_array(\n");

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
        buffer.append(") [ActParsRest_array]");
        return buffer.toString();
    }
}
