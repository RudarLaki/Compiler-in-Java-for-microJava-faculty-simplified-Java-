// generated with ast extension for cup
// version 0.8
// 16/1/2026 13:59:10


package rs.ac.bg.etf.pp1.ast;

public class EnumItemsRest_e extends EnumItemsRest {

    public EnumItemsRest_e () {
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
        buffer.append("EnumItemsRest_e(\n");

        buffer.append(tab);
        buffer.append(") [EnumItemsRest_e]");
        return buffer.toString();
    }
}
