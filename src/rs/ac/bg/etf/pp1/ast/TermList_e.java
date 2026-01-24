// generated with ast extension for cup
// version 0.8
// 24/0/2026 18:27:39


package src/rs/ac/bg/etf/pp1.ast;

public class TermList_e extends TermList {

    public TermList_e () {
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
        buffer.append("TermList_e(\n");

        buffer.append(tab);
        buffer.append(") [TermList_e]");
        return buffer.toString();
    }
}
