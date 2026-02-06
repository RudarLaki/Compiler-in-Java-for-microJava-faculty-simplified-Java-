// generated with ast extension for cup
// version 0.8
// 5/1/2026 23:51:26


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclListRest_comma extends ConstDeclListRest {

    private ConstDecl ConstDecl;
    private ConstDeclListRest ConstDeclListRest;

    public ConstDeclListRest_comma (ConstDecl ConstDecl, ConstDeclListRest ConstDeclListRest) {
        this.ConstDecl=ConstDecl;
        if(ConstDecl!=null) ConstDecl.setParent(this);
        this.ConstDeclListRest=ConstDeclListRest;
        if(ConstDeclListRest!=null) ConstDeclListRest.setParent(this);
    }

    public ConstDecl getConstDecl() {
        return ConstDecl;
    }

    public void setConstDecl(ConstDecl ConstDecl) {
        this.ConstDecl=ConstDecl;
    }

    public ConstDeclListRest getConstDeclListRest() {
        return ConstDeclListRest;
    }

    public void setConstDeclListRest(ConstDeclListRest ConstDeclListRest) {
        this.ConstDeclListRest=ConstDeclListRest;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDecl!=null) ConstDecl.accept(visitor);
        if(ConstDeclListRest!=null) ConstDeclListRest.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDecl!=null) ConstDecl.traverseTopDown(visitor);
        if(ConstDeclListRest!=null) ConstDeclListRest.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDecl!=null) ConstDecl.traverseBottomUp(visitor);
        if(ConstDeclListRest!=null) ConstDeclListRest.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclListRest_comma(\n");

        if(ConstDecl!=null)
            buffer.append(ConstDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclListRest!=null)
            buffer.append(ConstDeclListRest.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclListRest_comma]");
        return buffer.toString();
    }
}
