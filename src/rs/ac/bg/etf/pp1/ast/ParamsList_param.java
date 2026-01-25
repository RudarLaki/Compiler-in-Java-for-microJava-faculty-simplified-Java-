// generated with ast extension for cup
// version 0.8
// 25/0/2026 18:11:18


package src/rs/ac/bg/etf/pp1.ast;

public class ParamsList_param extends ParamsList {

    private Param Param;
    private ParamsListRest ParamsListRest;

    public ParamsList_param (Param Param, ParamsListRest ParamsListRest) {
        this.Param=Param;
        if(Param!=null) Param.setParent(this);
        this.ParamsListRest=ParamsListRest;
        if(ParamsListRest!=null) ParamsListRest.setParent(this);
    }

    public Param getParam() {
        return Param;
    }

    public void setParam(Param Param) {
        this.Param=Param;
    }

    public ParamsListRest getParamsListRest() {
        return ParamsListRest;
    }

    public void setParamsListRest(ParamsListRest ParamsListRest) {
        this.ParamsListRest=ParamsListRest;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Param!=null) Param.accept(visitor);
        if(ParamsListRest!=null) ParamsListRest.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Param!=null) Param.traverseTopDown(visitor);
        if(ParamsListRest!=null) ParamsListRest.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Param!=null) Param.traverseBottomUp(visitor);
        if(ParamsListRest!=null) ParamsListRest.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ParamsList_param(\n");

        if(Param!=null)
            buffer.append(Param.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ParamsListRest!=null)
            buffer.append(ParamsListRest.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ParamsList_param]");
        return buffer.toString();
    }
}
