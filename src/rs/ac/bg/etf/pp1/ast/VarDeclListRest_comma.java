// generated with ast extension for cup
// version 0.8
// 25/0/2026 18:11:18


package src/rs/ac/bg/etf/pp1.ast;

public class VarDeclListRest_comma extends VarDeclListRest {

    private VarDecl VarDecl;
    private VarDeclListRest VarDeclListRest;

    public VarDeclListRest_comma (VarDecl VarDecl, VarDeclListRest VarDeclListRest) {
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
        this.VarDeclListRest=VarDeclListRest;
        if(VarDeclListRest!=null) VarDeclListRest.setParent(this);
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public VarDeclListRest getVarDeclListRest() {
        return VarDeclListRest;
    }

    public void setVarDeclListRest(VarDeclListRest VarDeclListRest) {
        this.VarDeclListRest=VarDeclListRest;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDecl!=null) VarDecl.accept(visitor);
        if(VarDeclListRest!=null) VarDeclListRest.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
        if(VarDeclListRest!=null) VarDeclListRest.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        if(VarDeclListRest!=null) VarDeclListRest.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclListRest_comma(\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListRest!=null)
            buffer.append(VarDeclListRest.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListRest_comma]");
        return buffer.toString();
    }
}
