// generated with ast extension for cup
// version 0.8
// 10/1/2026 0:30:21


package rs.ac.bg.etf.pp1.ast;

public class ParamsListRest_param extends ParamsListRest {

    private Type Type;
    private VarDecl VarDecl;
    private ParamsListRest ParamsListRest;

    public ParamsListRest_param (Type Type, VarDecl VarDecl, ParamsListRest ParamsListRest) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
        this.ParamsListRest=ParamsListRest;
        if(ParamsListRest!=null) ParamsListRest.setParent(this);
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

    public ParamsListRest getParamsListRest() {
        return ParamsListRest;
    }

    public void setParamsListRest(ParamsListRest ParamsListRest) {
        this.ParamsListRest=ParamsListRest;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
        if(ParamsListRest!=null) ParamsListRest.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
        if(ParamsListRest!=null) ParamsListRest.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        if(ParamsListRest!=null) ParamsListRest.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ParamsListRest_param(\n");

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

        if(ParamsListRest!=null)
            buffer.append(ParamsListRest.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ParamsListRest_param]");
        return buffer.toString();
    }
}
