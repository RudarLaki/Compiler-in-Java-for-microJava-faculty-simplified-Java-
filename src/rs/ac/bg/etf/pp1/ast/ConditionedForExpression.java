// generated with ast extension for cup
// version 0.8
// 24/0/2026 18:27:39


package src/rs/ac/bg/etf/pp1.ast;

public class ConditionedForExpression extends ForExpression {

    private EpsilonDesignatorStatement EpsilonDesignatorStatement;
    private Condition Condition;
    private EpsilonDesignatorStatement EpsilonDesignatorStatement1;

    public ConditionedForExpression (EpsilonDesignatorStatement EpsilonDesignatorStatement, Condition Condition, EpsilonDesignatorStatement EpsilonDesignatorStatement1) {
        this.EpsilonDesignatorStatement=EpsilonDesignatorStatement;
        if(EpsilonDesignatorStatement!=null) EpsilonDesignatorStatement.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.EpsilonDesignatorStatement1=EpsilonDesignatorStatement1;
        if(EpsilonDesignatorStatement1!=null) EpsilonDesignatorStatement1.setParent(this);
    }

    public EpsilonDesignatorStatement getEpsilonDesignatorStatement() {
        return EpsilonDesignatorStatement;
    }

    public void setEpsilonDesignatorStatement(EpsilonDesignatorStatement EpsilonDesignatorStatement) {
        this.EpsilonDesignatorStatement=EpsilonDesignatorStatement;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
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
        if(Condition!=null) Condition.accept(visitor);
        if(EpsilonDesignatorStatement1!=null) EpsilonDesignatorStatement1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EpsilonDesignatorStatement!=null) EpsilonDesignatorStatement.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(EpsilonDesignatorStatement1!=null) EpsilonDesignatorStatement1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EpsilonDesignatorStatement!=null) EpsilonDesignatorStatement.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(EpsilonDesignatorStatement1!=null) EpsilonDesignatorStatement1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConditionedForExpression(\n");

        if(EpsilonDesignatorStatement!=null)
            buffer.append(EpsilonDesignatorStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EpsilonDesignatorStatement1!=null)
            buffer.append(EpsilonDesignatorStatement1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConditionedForExpression]");
        return buffer.toString();
    }
}
