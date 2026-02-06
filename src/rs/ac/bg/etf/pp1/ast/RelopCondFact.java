// generated with ast extension for cup
// version 0.8
// 5/1/2026 23:51:26


package rs.ac.bg.etf.pp1.ast;

public class RelopCondFact extends CondFact {

    private NonTernaryExpression NonTernaryExpression;
    private Relop Relop;
    private NonTernaryExpression NonTernaryExpression1;

    public RelopCondFact (NonTernaryExpression NonTernaryExpression, Relop Relop, NonTernaryExpression NonTernaryExpression1) {
        this.NonTernaryExpression=NonTernaryExpression;
        if(NonTernaryExpression!=null) NonTernaryExpression.setParent(this);
        this.Relop=Relop;
        if(Relop!=null) Relop.setParent(this);
        this.NonTernaryExpression1=NonTernaryExpression1;
        if(NonTernaryExpression1!=null) NonTernaryExpression1.setParent(this);
    }

    public NonTernaryExpression getNonTernaryExpression() {
        return NonTernaryExpression;
    }

    public void setNonTernaryExpression(NonTernaryExpression NonTernaryExpression) {
        this.NonTernaryExpression=NonTernaryExpression;
    }

    public Relop getRelop() {
        return Relop;
    }

    public void setRelop(Relop Relop) {
        this.Relop=Relop;
    }

    public NonTernaryExpression getNonTernaryExpression1() {
        return NonTernaryExpression1;
    }

    public void setNonTernaryExpression1(NonTernaryExpression NonTernaryExpression1) {
        this.NonTernaryExpression1=NonTernaryExpression1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(NonTernaryExpression!=null) NonTernaryExpression.accept(visitor);
        if(Relop!=null) Relop.accept(visitor);
        if(NonTernaryExpression1!=null) NonTernaryExpression1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(NonTernaryExpression!=null) NonTernaryExpression.traverseTopDown(visitor);
        if(Relop!=null) Relop.traverseTopDown(visitor);
        if(NonTernaryExpression1!=null) NonTernaryExpression1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(NonTernaryExpression!=null) NonTernaryExpression.traverseBottomUp(visitor);
        if(Relop!=null) Relop.traverseBottomUp(visitor);
        if(NonTernaryExpression1!=null) NonTernaryExpression1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("RelopCondFact(\n");

        if(NonTernaryExpression!=null)
            buffer.append(NonTernaryExpression.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Relop!=null)
            buffer.append(Relop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(NonTernaryExpression1!=null)
            buffer.append(NonTernaryExpression1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [RelopCondFact]");
        return buffer.toString();
    }
}
