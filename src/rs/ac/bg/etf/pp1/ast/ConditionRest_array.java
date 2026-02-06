// generated with ast extension for cup
// version 0.8
// 5/1/2026 23:51:26


package rs.ac.bg.etf.pp1.ast;

public class ConditionRest_array extends ConditionRest {

    private CondTerm CondTerm;
    private ConditionRest ConditionRest;

    public ConditionRest_array (CondTerm CondTerm, ConditionRest ConditionRest) {
        this.CondTerm=CondTerm;
        if(CondTerm!=null) CondTerm.setParent(this);
        this.ConditionRest=ConditionRest;
        if(ConditionRest!=null) ConditionRest.setParent(this);
    }

    public CondTerm getCondTerm() {
        return CondTerm;
    }

    public void setCondTerm(CondTerm CondTerm) {
        this.CondTerm=CondTerm;
    }

    public ConditionRest getConditionRest() {
        return ConditionRest;
    }

    public void setConditionRest(ConditionRest ConditionRest) {
        this.ConditionRest=ConditionRest;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondTerm!=null) CondTerm.accept(visitor);
        if(ConditionRest!=null) ConditionRest.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondTerm!=null) CondTerm.traverseTopDown(visitor);
        if(ConditionRest!=null) ConditionRest.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondTerm!=null) CondTerm.traverseBottomUp(visitor);
        if(ConditionRest!=null) ConditionRest.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConditionRest_array(\n");

        if(CondTerm!=null)
            buffer.append(CondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionRest!=null)
            buffer.append(ConditionRest.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConditionRest_array]");
        return buffer.toString();
    }
}
