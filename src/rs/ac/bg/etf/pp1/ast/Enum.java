// generated with ast extension for cup
// version 0.8
// 24/0/2026 18:27:39


package src/rs/ac/bg/etf/pp1.ast;

public class Enum implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String I1;
    private EnumItemsList EnumItemsList;

    public Enum (String I1, EnumItemsList EnumItemsList) {
        this.I1=I1;
        this.EnumItemsList=EnumItemsList;
        if(EnumItemsList!=null) EnumItemsList.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public EnumItemsList getEnumItemsList() {
        return EnumItemsList;
    }

    public void setEnumItemsList(EnumItemsList EnumItemsList) {
        this.EnumItemsList=EnumItemsList;
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
        if(EnumItemsList!=null) EnumItemsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumItemsList!=null) EnumItemsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumItemsList!=null) EnumItemsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Enum(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(EnumItemsList!=null)
            buffer.append(EnumItemsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Enum]");
        return buffer.toString();
    }
}
