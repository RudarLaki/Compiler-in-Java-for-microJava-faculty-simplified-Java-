// generated with ast extension for cup
// version 0.8
// 24/0/2026 18:27:39


package src/rs/ac/bg/etf/pp1.ast;

public class ReturnExpression_expr extends ReturnExpression {

    private Expression Expression;

    public ReturnExpression_expr (Expression Expression) {
        this.Expression=Expression;
        if(Expression!=null) Expression.setParent(this);
    }

    public Expression getExpression() {
        return Expression;
    }

    public void setExpression(Expression Expression) {
        this.Expression=Expression;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expression!=null) Expression.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expression!=null) Expression.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expression!=null) Expression.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ReturnExpression_expr(\n");

        if(Expression!=null)
            buffer.append(Expression.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ReturnExpression_expr]");
        return buffer.toString();
    }
}
