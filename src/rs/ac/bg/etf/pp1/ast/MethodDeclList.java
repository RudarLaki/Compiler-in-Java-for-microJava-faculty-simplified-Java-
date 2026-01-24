// generated with ast extension for cup
// version 0.8
// 24/0/2026 18:27:39


package src/rs/ac/bg/etf/pp1.ast;

public class MethodDeclList implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Prefix Prefix;
    private String I2;
    private ParamsList ParamsList;
    private VarDeclList VarDeclList;
    private Statement Statement;

    public MethodDeclList (Prefix Prefix, String I2, ParamsList ParamsList, VarDeclList VarDeclList, Statement Statement) {
        this.Prefix=Prefix;
        if(Prefix!=null) Prefix.setParent(this);
        this.I2=I2;
        this.ParamsList=ParamsList;
        if(ParamsList!=null) ParamsList.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
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

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Prefix!=null) Prefix.accept(visitor);
        if(ParamsList!=null) ParamsList.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Prefix!=null) Prefix.traverseTopDown(visitor);
        if(ParamsList!=null) ParamsList.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Prefix!=null) Prefix.traverseBottomUp(visitor);
        if(ParamsList!=null) ParamsList.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclList(\n");

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

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDeclList]");
        return buffer.toString();
    }
}
