// generated with ast extension for cup
// version 0.8
// 5/1/2026 23:51:26


package rs.ac.bg.etf.pp1.ast;

public class DesignatorList_array extends DesignatorList {

    private DesignatorOp DesignatorOp;
    private DesignatorList DesignatorList;

    public DesignatorList_array (DesignatorOp DesignatorOp, DesignatorList DesignatorList) {
        this.DesignatorOp=DesignatorOp;
        if(DesignatorOp!=null) DesignatorOp.setParent(this);
        this.DesignatorList=DesignatorList;
        if(DesignatorList!=null) DesignatorList.setParent(this);
    }

    public DesignatorOp getDesignatorOp() {
        return DesignatorOp;
    }

    public void setDesignatorOp(DesignatorOp DesignatorOp) {
        this.DesignatorOp=DesignatorOp;
    }

    public DesignatorList getDesignatorList() {
        return DesignatorList;
    }

    public void setDesignatorList(DesignatorList DesignatorList) {
        this.DesignatorList=DesignatorList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorOp!=null) DesignatorOp.accept(visitor);
        if(DesignatorList!=null) DesignatorList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorOp!=null) DesignatorOp.traverseTopDown(visitor);
        if(DesignatorList!=null) DesignatorList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorOp!=null) DesignatorOp.traverseBottomUp(visitor);
        if(DesignatorList!=null) DesignatorList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorList_array(\n");

        if(DesignatorOp!=null)
            buffer.append(DesignatorOp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorList!=null)
            buffer.append(DesignatorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorList_array]");
        return buffer.toString();
    }
}
