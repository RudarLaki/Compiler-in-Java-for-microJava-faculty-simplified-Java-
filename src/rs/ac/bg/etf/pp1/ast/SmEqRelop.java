// generated with ast extension for cup
// version 0.8
// 25/0/2026 18:11:19


package src/rs/ac/bg/etf/pp1.ast;

public class SmEqRelop extends Relop {

    public SmEqRelop () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SmEqRelop(\n");

        buffer.append(tab);
        buffer.append(") [SmEqRelop]");
        return buffer.toString();
    }
}
