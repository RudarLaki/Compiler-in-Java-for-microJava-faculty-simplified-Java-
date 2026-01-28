// generated with ast extension for cup
// version 0.8
// 28/0/2026 3:38:26


package rs.ac.bg.etf.pp1.ast;

public class EpsilonDesignatorStatement_e extends EpsilonDesignatorStatement {

    public EpsilonDesignatorStatement_e () {
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
        buffer.append("EpsilonDesignatorStatement_e(\n");

        buffer.append(tab);
        buffer.append(") [EpsilonDesignatorStatement_e]");
        return buffer.toString();
    }
}
