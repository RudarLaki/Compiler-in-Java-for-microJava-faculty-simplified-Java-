// generated with ast extension for cup
// version 0.8
// 23/0/2026 9:13:17


package src/rs/ac/bg/etf/pp1.ast;

public class VarDeclListComa implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private VarDeclListComa VarDeclListComa;
    private VarDecl VarDecl;

    public VarDeclListComa (VarDeclListComa VarDeclListComa, VarDecl VarDecl) {
        this.VarDeclListComa=VarDeclListComa;
        if(VarDeclListComa!=null) VarDeclListComa.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
    }

    public VarDeclListComa getVarDeclListComa() {
        return VarDeclListComa;
    }

    public void setVarDeclListComa(VarDeclListComa VarDeclListComa) {
        this.VarDeclListComa=VarDeclListComa;
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
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
        if(VarDeclListComa!=null) VarDeclListComa.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclListComa!=null) VarDeclListComa.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclListComa!=null) VarDeclListComa.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclListComa(\n");

        if(VarDeclListComa!=null)
            buffer.append(VarDeclListComa.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListComa]");
        return buffer.toString();
    }
}
