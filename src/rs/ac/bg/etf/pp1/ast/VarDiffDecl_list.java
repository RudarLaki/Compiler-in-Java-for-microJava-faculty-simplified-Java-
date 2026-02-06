// generated with ast extension for cup
// version 0.8
// 5/1/2026 23:51:26


package rs.ac.bg.etf.pp1.ast;

public class VarDiffDecl_list extends VarDiffDecl {

    private VarDeclList VarDeclList;
    private VarDiffDecl VarDiffDecl;

    public VarDiffDecl_list (VarDeclList VarDeclList, VarDiffDecl VarDiffDecl) {
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.VarDiffDecl=VarDiffDecl;
        if(VarDiffDecl!=null) VarDiffDecl.setParent(this);
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public VarDiffDecl getVarDiffDecl() {
        return VarDiffDecl;
    }

    public void setVarDiffDecl(VarDiffDecl VarDiffDecl) {
        this.VarDiffDecl=VarDiffDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(VarDiffDecl!=null) VarDiffDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(VarDiffDecl!=null) VarDiffDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(VarDiffDecl!=null) VarDiffDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDiffDecl_list(\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDiffDecl!=null)
            buffer.append(VarDiffDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDiffDecl_list]");
        return buffer.toString();
    }
}
