// generated with ast extension for cup
// version 0.8
// 9/1/2026 18:57:41


package rs.ac.bg.etf.pp1.ast;

public class Factor implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private UnaryMinus UnaryMinus;
    private PartFactor PartFactor;

    public Factor (UnaryMinus UnaryMinus, PartFactor PartFactor) {
        this.UnaryMinus=UnaryMinus;
        if(UnaryMinus!=null) UnaryMinus.setParent(this);
        this.PartFactor=PartFactor;
        if(PartFactor!=null) PartFactor.setParent(this);
    }

    public UnaryMinus getUnaryMinus() {
        return UnaryMinus;
    }

    public void setUnaryMinus(UnaryMinus UnaryMinus) {
        this.UnaryMinus=UnaryMinus;
    }

    public PartFactor getPartFactor() {
        return PartFactor;
    }

    public void setPartFactor(PartFactor PartFactor) {
        this.PartFactor=PartFactor;
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
        if(UnaryMinus!=null) UnaryMinus.accept(visitor);
        if(PartFactor!=null) PartFactor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(UnaryMinus!=null) UnaryMinus.traverseTopDown(visitor);
        if(PartFactor!=null) PartFactor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(UnaryMinus!=null) UnaryMinus.traverseBottomUp(visitor);
        if(PartFactor!=null) PartFactor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Factor(\n");

        if(UnaryMinus!=null)
            buffer.append(UnaryMinus.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PartFactor!=null)
            buffer.append(PartFactor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Factor]");
        return buffer.toString();
    }
}
