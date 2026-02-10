// generated with ast extension for cup
// version 0.8
// 10/1/2026 0:30:21


package rs.ac.bg.etf.pp1.ast;

public class NonConditionedForExpression extends ForExpression {

    private EpsilonDesignatorStatement EpsilonDesignatorStatement;
    private EpsilonDesignatorStatement EpsilonDesignatorStatement1;

    public NonConditionedForExpression (EpsilonDesignatorStatement EpsilonDesignatorStatement, EpsilonDesignatorStatement EpsilonDesignatorStatement1) {
        this.EpsilonDesignatorStatement=EpsilonDesignatorStatement;
        if(EpsilonDesignatorStatement!=null) EpsilonDesignatorStatement.setParent(this);
        this.EpsilonDesignatorStatement1=EpsilonDesignatorStatement1;
        if(EpsilonDesignatorStatement1!=null) EpsilonDesignatorStatement1.setParent(this);
    }

    public EpsilonDesignatorStatement getEpsilonDesignatorStatement() {
        return EpsilonDesignatorStatement;
    }

    public void setEpsilonDesignatorStatement(EpsilonDesignatorStatement EpsilonDesignatorStatement) {
        this.EpsilonDesignatorStatement=EpsilonDesignatorStatement;
    }

    public EpsilonDesignatorStatement getEpsilonDesignatorStatement1() {
        return EpsilonDesignatorStatement1;
    }

    public void setEpsilonDesignatorStatement1(EpsilonDesignatorStatement EpsilonDesignatorStatement1) {
        this.EpsilonDesignatorStatement1=EpsilonDesignatorStatement1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EpsilonDesignatorStatement!=null) EpsilonDesignatorStatement.accept(visitor);
        if(EpsilonDesignatorStatement1!=null) EpsilonDesignatorStatement1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EpsilonDesignatorStatement!=null) EpsilonDesignatorStatement.traverseTopDown(visitor);
        if(EpsilonDesignatorStatement1!=null) EpsilonDesignatorStatement1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EpsilonDesignatorStatement!=null) EpsilonDesignatorStatement.traverseBottomUp(visitor);
        if(EpsilonDesignatorStatement1!=null) EpsilonDesignatorStatement1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NonConditionedForExpression(\n");

        if(EpsilonDesignatorStatement!=null)
            buffer.append(EpsilonDesignatorStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EpsilonDesignatorStatement1!=null)
            buffer.append(EpsilonDesignatorStatement1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NonConditionedForExpression]");
        return buffer.toString();
    }
}
