// generated with ast extension for cup
// version 0.8
// 5/1/2026 23:51:26


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Prefix Prefix;
    private MethodName MethodName;
    private ParamsList ParamsList;
    private VarDiffDecl VarDiffDecl;
    private StatementList StatementList;

    public MethodDecl (Prefix Prefix, MethodName MethodName, ParamsList ParamsList, VarDiffDecl VarDiffDecl, StatementList StatementList) {
        this.Prefix=Prefix;
        if(Prefix!=null) Prefix.setParent(this);
        this.MethodName=MethodName;
        if(MethodName!=null) MethodName.setParent(this);
        this.ParamsList=ParamsList;
        if(ParamsList!=null) ParamsList.setParent(this);
        this.VarDiffDecl=VarDiffDecl;
        if(VarDiffDecl!=null) VarDiffDecl.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public Prefix getPrefix() {
        return Prefix;
    }

    public void setPrefix(Prefix Prefix) {
        this.Prefix=Prefix;
    }

    public MethodName getMethodName() {
        return MethodName;
    }

    public void setMethodName(MethodName MethodName) {
        this.MethodName=MethodName;
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
        if(MethodName!=null) MethodName.accept(visitor);
        if(ParamsList!=null) ParamsList.accept(visitor);
        if(VarDiffDecl!=null) VarDiffDecl.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Prefix!=null) Prefix.traverseTopDown(visitor);
        if(MethodName!=null) MethodName.traverseTopDown(visitor);
        if(ParamsList!=null) ParamsList.traverseTopDown(visitor);
        if(VarDiffDecl!=null) VarDiffDecl.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Prefix!=null) Prefix.traverseBottomUp(visitor);
        if(MethodName!=null) MethodName.traverseBottomUp(visitor);
        if(ParamsList!=null) ParamsList.traverseBottomUp(visitor);
        if(VarDiffDecl!=null) VarDiffDecl.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(Prefix!=null)
            buffer.append(Prefix.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodName!=null)
            buffer.append(MethodName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
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

        buffer.append(tab);
        buffer.append(") [MethodDecl]");
        return buffer.toString();
    }
}
