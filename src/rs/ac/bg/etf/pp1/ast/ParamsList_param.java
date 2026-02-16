// generated with ast extension for cup
// version 0.8
// 16/1/2026 13:59:10


package rs.ac.bg.etf.pp1.ast;

public class ParamsList_param extends ParamsList {

    private Type Type;
    private VarDecl VarDecl;
    private ParamsList ParamsList;

    public ParamsList_param (Type Type, VarDecl VarDecl, ParamsList ParamsList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
        this.ParamsList=ParamsList;
        if(ParamsList!=null) ParamsList.setParent(this);
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

    public ParamsList getParamsList() {
        return ParamsList;
    }

    public void setParamsList(ParamsList ParamsList) {
        this.ParamsList=ParamsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
        if(ParamsList!=null) ParamsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
        if(ParamsList!=null) ParamsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        if(ParamsList!=null) ParamsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ParamsList_param(\n");

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

        if(ParamsList!=null)
            buffer.append(ParamsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ParamsList_param]");
        return buffer.toString();
    }
}
