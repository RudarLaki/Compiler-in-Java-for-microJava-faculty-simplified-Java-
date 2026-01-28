// generated with ast extension for cup
// version 0.8
// 28/0/2026 3:38:26


package rs.ac.bg.etf.pp1.ast;

public class EnumItemsRest_comma extends EnumItemsRest {

    private EnumItem EnumItem;
    private EnumItemsRest EnumItemsRest;

    public EnumItemsRest_comma (EnumItem EnumItem, EnumItemsRest EnumItemsRest) {
        this.EnumItem=EnumItem;
        if(EnumItem!=null) EnumItem.setParent(this);
        this.EnumItemsRest=EnumItemsRest;
        if(EnumItemsRest!=null) EnumItemsRest.setParent(this);
    }

    public EnumItem getEnumItem() {
        return EnumItem;
    }

    public void setEnumItem(EnumItem EnumItem) {
        this.EnumItem=EnumItem;
    }

    public EnumItemsRest getEnumItemsRest() {
        return EnumItemsRest;
    }

    public void setEnumItemsRest(EnumItemsRest EnumItemsRest) {
        this.EnumItemsRest=EnumItemsRest;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumItem!=null) EnumItem.accept(visitor);
        if(EnumItemsRest!=null) EnumItemsRest.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumItem!=null) EnumItem.traverseTopDown(visitor);
        if(EnumItemsRest!=null) EnumItemsRest.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumItem!=null) EnumItem.traverseBottomUp(visitor);
        if(EnumItemsRest!=null) EnumItemsRest.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumItemsRest_comma(\n");

        if(EnumItem!=null)
            buffer.append(EnumItem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumItemsRest!=null)
            buffer.append(EnumItemsRest.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumItemsRest_comma]");
        return buffer.toString();
    }
}
