// generated with ast extension for cup
// version 0.8
// 23/0/2026 9:13:17


package src/rs/ac/bg/etf/pp1.ast;

public class VarDeclList implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private VarDecl VarDecl;
    private VarDeclListComa VarDeclListComa;

    public VarDeclList (Type Type, VarDecl VarDecl, VarDeclListComa VarDeclListComa) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
        this.VarDeclListComa=VarDeclListComa;
        if(VarDeclListComa!=null) VarDeclListComa.setParent(this);
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

    public VarDeclListComa getVarDeclListComa() {
        return VarDeclListComa;
    }

    public void setVarDeclListComa(VarDeclListComa VarDeclListComa) {
        this.VarDeclListComa=VarDeclListComa;
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
        if(Type!=null) Type.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
        if(VarDeclListComa!=null) VarDeclListComa.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
        if(VarDeclListComa!=null) VarDeclListComa.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        if(VarDeclListComa!=null) VarDeclListComa.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclList(\n");

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

        if(VarDeclListComa!=null)
            buffer.append(VarDeclListComa.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclList]");
        return buffer.toString();
    }
}
