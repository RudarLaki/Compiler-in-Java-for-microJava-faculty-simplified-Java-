// generated with ast extension for cup
// version 0.8
// 28/0/2026 3:38:26


package rs.ac.bg.etf.pp1.ast;

public class MethodDeclList_vdl extends MethodDeclList {

    private Prefix Prefix;
    private String I2;
    private ParamsList ParamsList;
    private VarDiffDecl VarDiffDecl;
    private StatementList StatementList;
    private MethodDeclList MethodDeclList;

    public MethodDeclList_vdl (Prefix Prefix, String I2, ParamsList ParamsList, VarDiffDecl VarDiffDecl, StatementList StatementList, MethodDeclList MethodDeclList) {
        this.Prefix=Prefix;
        if(Prefix!=null) Prefix.setParent(this);
        this.I2=I2;
        this.ParamsList=ParamsList;
        if(ParamsList!=null) ParamsList.setParent(this);
        this.VarDiffDecl=VarDiffDecl;
        if(VarDiffDecl!=null) VarDiffDecl.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
        this.MethodDeclList=MethodDeclList;
        if(MethodDeclList!=null) MethodDeclList.setParent(this);
    }

    public Prefix getPrefix() {
        return Prefix;
    }

    public void setPrefix(Prefix Prefix) {
        this.Prefix=Prefix;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public ParamsList getParamsList() {
        return ParamsList;
    }

    public void setParamsList(ParamsList ParamsList) {
        this.ParamsList=ParamsList;
    }

    public VarDiffDecl getVarDiffDecl() {
        return VarDiffDecl;
    }

    public void setVarDiffDecl(VarDiffDecl VarDiffDecl) {
        this.VarDiffDecl=VarDiffDecl;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public MethodDeclList getMethodDeclList() {
        return MethodDeclList;
    }

    public void setMethodDeclList(MethodDeclList MethodDeclList) {
        this.MethodDeclList=MethodDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Prefix!=null) Prefix.accept(visitor);
        if(ParamsList!=null) ParamsList.accept(visitor);
        if(VarDiffDecl!=null) VarDiffDecl.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
        if(MethodDeclList!=null) MethodDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Prefix!=null) Prefix.traverseTopDown(visitor);
        if(ParamsList!=null) ParamsList.traverseTopDown(visitor);
        if(VarDiffDecl!=null) VarDiffDecl.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Prefix!=null) Prefix.traverseBottomUp(visitor);
        if(ParamsList!=null) ParamsList.traverseBottomUp(visitor);
        if(VarDiffDecl!=null) VarDiffDecl.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclList_vdl(\n");

        if(Prefix!=null)
            buffer.append(Prefix.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(ParamsList!=null)
            buffer.append(ParamsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDiffDecl!=null)
            buffer.append(VarDiffDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclList!=null)
            buffer.append(MethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDeclList_vdl]");
        return buffer.toString();
    }
}
