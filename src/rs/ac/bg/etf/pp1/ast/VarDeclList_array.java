// generated with ast extension for cup
// version 0.8
// 28/0/2026 3:38:26


package rs.ac.bg.etf.pp1.ast;

public class VarDeclList_array extends VarDeclList {

    private Type Type;
    private VarDecl VarDecl;
    private VarDeclListRest VarDeclListRest;

    public VarDeclList_array (Type Type, VarDecl VarDecl, VarDeclListRest VarDeclListRest) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
        this.VarDeclListRest=VarDeclListRest;
        if(VarDeclListRest!=null) VarDeclListRest.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
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
        if(Type!=null) Type.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
        if(VarDeclListRest!=null) VarDeclListRest.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
        if(VarDeclListRest!=null) VarDeclListRest.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        if(VarDeclListRest!=null) VarDeclListRest.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclList_array(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

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
        buffer.append(") [VarDeclList_array]");
        return buffer.toString();
    }
}
