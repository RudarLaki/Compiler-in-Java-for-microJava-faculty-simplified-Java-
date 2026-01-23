// generated with ast extension for cup
// version 0.8
// 23/0/2026 9:13:17


package src/rs/ac/bg/etf/pp1.ast;

public class ConstDeclListComa implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ConstDeclListComa ConstDeclListComa;
    private ConstDecl ConstDecl;

    public ConstDeclListComa (ConstDeclListComa ConstDeclListComa, ConstDecl ConstDecl) {
        this.ConstDeclListComa=ConstDeclListComa;
        if(ConstDeclListComa!=null) ConstDeclListComa.setParent(this);
        this.ConstDecl=ConstDecl;
        if(ConstDecl!=null) ConstDecl.setParent(this);
    }

    public ConstDeclListComa getConstDeclListComa() {
        return ConstDeclListComa;
    }

    public void setConstDeclListComa(ConstDeclListComa ConstDeclListComa) {
        this.ConstDeclListComa=ConstDeclListComa;
    }

    public ConstDecl getConstDecl() {
        return ConstDecl;
    }

    public void setConstDecl(ConstDecl ConstDecl) {
        this.ConstDecl=ConstDecl;
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
        if(ConstDeclListComa!=null) ConstDeclListComa.accept(visitor);
        if(ConstDecl!=null) ConstDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclListComa!=null) ConstDeclListComa.traverseTopDown(visitor);
        if(ConstDecl!=null) ConstDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclListComa!=null) ConstDeclListComa.traverseBottomUp(visitor);
        if(ConstDecl!=null) ConstDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclListComa(\n");

        if(ConstDeclListComa!=null)
            buffer.append(ConstDeclListComa.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDecl!=null)
            buffer.append(ConstDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclListComa]");
        return buffer.toString();
    }
}
