// generated with ast extension for cup
// version 0.8
// 9/1/2026 18:57:41


package rs.ac.bg.etf.pp1.ast;

public class SwitchStatement extends MatchedStatement {

    private SwitchWord SwitchWord;
    private Expression Expression;
    private SwitchCases SwitchCases;

    public SwitchStatement (SwitchWord SwitchWord, Expression Expression, SwitchCases SwitchCases) {
        this.SwitchWord=SwitchWord;
        if(SwitchWord!=null) SwitchWord.setParent(this);
        this.Expression=Expression;
        if(Expression!=null) Expression.setParent(this);
        this.SwitchCases=SwitchCases;
        if(SwitchCases!=null) SwitchCases.setParent(this);
    }

    public SwitchWord getSwitchWord() {
        return SwitchWord;
    }

    public void setSwitchWord(SwitchWord SwitchWord) {
        this.SwitchWord=SwitchWord;
    }

    public Expression getExpression() {
        return Expression;
    }

    public void setExpression(Expression Expression) {
        this.Expression=Expression;
    }

    public SwitchCases getSwitchCases() {
        return SwitchCases;
    }

    public void setSwitchCases(SwitchCases SwitchCases) {
        this.SwitchCases=SwitchCases;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SwitchWord!=null) SwitchWord.accept(visitor);
        if(Expression!=null) Expression.accept(visitor);
        if(SwitchCases!=null) SwitchCases.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SwitchWord!=null) SwitchWord.traverseTopDown(visitor);
        if(Expression!=null) Expression.traverseTopDown(visitor);
        if(SwitchCases!=null) SwitchCases.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SwitchWord!=null) SwitchWord.traverseBottomUp(visitor);
        if(Expression!=null) Expression.traverseBottomUp(visitor);
        if(SwitchCases!=null) SwitchCases.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SwitchStatement(\n");

        if(SwitchWord!=null)
            buffer.append(SwitchWord.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expression!=null)
            buffer.append(Expression.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SwitchCases!=null)
            buffer.append(SwitchCases.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SwitchStatement]");
        return buffer.toString();
    }
}
