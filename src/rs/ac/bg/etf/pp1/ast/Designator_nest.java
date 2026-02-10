// generated with ast extension for cup
// version 0.8
// 10/1/2026 0:30:21


package rs.ac.bg.etf.pp1.ast;

public class Designator_nest extends Designator {

    private Designator Designator;
    private DotDesignatorOp DotDesignatorOp;

    public Designator_nest (Designator Designator, DotDesignatorOp DotDesignatorOp) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DotDesignatorOp=DotDesignatorOp;
        if(DotDesignatorOp!=null) DotDesignatorOp.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DotDesignatorOp getDotDesignatorOp() {
        return DotDesignatorOp;
    }

    public void setDotDesignatorOp(DotDesignatorOp DotDesignatorOp) {
        this.DotDesignatorOp=DotDesignatorOp;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DotDesignatorOp!=null) DotDesignatorOp.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DotDesignatorOp!=null) DotDesignatorOp.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DotDesignatorOp!=null) DotDesignatorOp.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Designator_nest(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DotDesignatorOp!=null)
            buffer.append(DotDesignatorOp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Designator_nest]");
        return buffer.toString();
    }
}
