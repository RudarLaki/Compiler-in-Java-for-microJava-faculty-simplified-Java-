package rs.ac.bg.etf.pp1;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {
	int mainPC;

	public int getMainPC() {
		return this.mainPC;
	}

	@Override
	public void visit(MethodName methodName) {
		methodName.obj.setAdr(Code.pc);
		if (methodName.getI1().equals("main"))
			this.mainPC = Code.pc;
		Code.put(Code.enter);
		Code.put(methodName.obj.getLevel()); // b1
		Code.put(methodName.obj.getLocalSymbols().size()); // b2
	}

	@Override
	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(PrintExpression_num printExpression_num) {
		Code.loadConst(printExpression_num.getN2());
	}

	public void visit(PrintExpression_non_num node) {
		Code.loadConst(0); // default width
	}

	@Override
	public void visit(Term_array term_array) {
		if (term_array.getMulop() instanceof MulMulop)
			Code.put(Code.mul);
		else if (term_array.getMulop() instanceof DivMulop)
			Code.put(Code.div);
		else
			Code.put(Code.rem);
	}

	@Override
	public void visit(TermList_array termList_array) {
		if (termList_array.getAddop() instanceof PlusAddop)
			Code.put(Code.add);
		else
			Code.put(Code.sub);
	}

	@Override
	public void visit(Factor factor) {
		if (factor instanceof MinusFactor) {
			Code.put(Code.neg);
		}
	}

	///////////////////////////////////// designator statements
	///////////////////////////////////// ///////////////////////////////
	@Override
	public void visit(NonParamsDesignatorStatement nonParamsDesignatorStatement) {
		int offset = nonParamsDesignatorStatement.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);

		if (!nonParamsDesignatorStatement.getDesignator().obj.getType().equals(Tab.noType))
			Code.put(Code.pop);
	}

	@Override
	public void visit(ParamsDesignatorStatement paramsDesignatorStatement) {
		int offset = paramsDesignatorStatement.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);

		if (!paramsDesignatorStatement.getDesignator().obj.getType().equals(Tab.noType))
			Code.put(Code.pop);
	}

	@Override
	public void visit(ExprDesignatorStatement exprDesignatorStatement) {
		Code.store(exprDesignatorStatement.getDesignator().obj);
	}

	@Override
	public void visit(IncDesignatorStatement incDesignatorStatement) {
		if (incDesignatorStatement.getDesignator().obj.getKind() == Obj.Elem)
			Code.put(Code.dup2);
		Code.load(incDesignatorStatement.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(incDesignatorStatement.getDesignator().obj);
	}

	@Override
	public void visit(DecDesignatorStatement decDesignatorStatement) {
		if (decDesignatorStatement.getDesignator().obj.getKind() == Obj.Elem)
			Code.put(Code.dup2);
		Code.load(decDesignatorStatement.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(decDesignatorStatement.getDesignator().obj);
	}

	@Override
	public void visit(DesignatorFactor_e designatorFactor_e) {
		Code.load(designatorFactor_e.getDesignator().obj);
	}

	@Override
	public void visit(DesignatorArrayName designatorArrayName) {
		Code.load(designatorArrayName.obj);
	}
////////////////////////////////// Statements //////////////////////////////////

	@Override
	public void visit(ReturnExpression_non_expr node) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	@Override
	public void visit(ReturnExpression_expr node) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	@Override
	public void visit(PrintStatement printStatement) {
		if (printStatement.getPrintExpression().struct.equals(Tab.charType))
			Code.put(Code.bprint);
		else
			Code.put(Code.print);
	}

	@Override
	public void visit(ReadStatement readStatement) {
		Obj desObj = readStatement.getDesignator().obj;

		if (desObj.getType().equals(Tab.charType))
			Code.put(Code.bread);
		else
			Code.put(Code.read);
		Code.store(desObj);
	}

	@Override
	public void visit(NumberParamFactor numberParamFactor) {
		Code.loadConst(numberParamFactor.getN1());
	}

	@Override
	public void visit(CharParamFactor charParamFactor) {
		Code.loadConst(charParamFactor.getC1());
	}

	@Override
	public void visit(BoolParamFactor boolParamFactor) {
		Code.loadConst(boolParamFactor.getB1());
	}

	@Override
	public void visit(NewParamFactor newParamFactor) {
		Code.put(Code.newarray);
		if (newParamFactor.getType().struct.equals(Tab.charType))
			Code.put(0); // b1
		else
			Code.put(1);
	}

	@Override
	public void visit(DesignatorFactor_func designatorFactor_func) {
		int offset = designatorFactor_func.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}

	@Override
	public void visit(DesignatorActFactor_func designatorActFactor_func) {
		int offset = designatorActFactor_func.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}

	/////////////////////// Conditions ///////////////////////
	private int returnRelOp(Relop relop) {
		if (relop instanceof Relop_eq)
			return Code.eq;
		if (relop instanceof Relop_neq)
			return Code.ne;
		if (relop instanceof Relop_ls)
			return Code.lt;
		if (relop instanceof Relop_ls_eq)
			return Code.le;
		if (relop instanceof Relop_gr)
			return Code.gt;
		if (relop instanceof Relop_gr_eq)
			return Code.ge;
		return 0;
	}

	private Stack<Integer> skipAND = new Stack();
	private Stack<Integer> skipOR = new Stack();
	
	private Stack<Integer> skipTrue = new Stack();
	private Stack<Integer> skipFalse = new Stack();



	@Override
	public void visit(NonRelopCondFact nonRelopCondFact) {
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0);
		skipAND.push(Code.pc - 2);
	}
	@Override
	public void visit(RelopCondFact relopCondFact) {
		Code.putFalseJump(returnRelOp(relopCondFact.getRelop()), 0);
		skipAND.push(Code.pc - 2);
	}
	
	@Override
	public void visit(CondTermHelp CondTermHelp) {
		Code.putJump(0);
		skipOR.push(Code.pc - 2);
		while(!skipAND.empty()) {
			Code.fixup(skipAND.pop());
		}
	}
	@Override
	public void visit(Condition condition) {
		Code.putJump(0);
		skipTrue.push(Code.pc - 2);
		while(!skipOR.empty()) {
			Code.fixup(skipOR.pop());
		}		
	}
	@Override
	public void visit(IfElseStatement_non_else ifElseStatement_non_else) {
		Code.fixup(skipTrue.pop());
	}
	@Override
	public void visit(Else else_) {
		Code.putJump(0);
		skipFalse.push(Code.pc - 2);
		Code.fixup(skipTrue.pop());
	}
	@Override
	public void visit(IfElseStatement ifElseStatement) {
		Code.fixup(skipFalse.pop());
	}
	@Override
	public void visit(IfElseStatement_else ifElseStatement_else) {
		Code.fixup(skipFalse.pop());
	}
	
	//////////////////////////   ternary operator  //////////////////////////
	public void visit(TernaryExpression ternaryExpression) {
		Code.fixup(skipFalse.pop());
	}
	public void visit(Colon colon) {
		Code.putJump(0);
		skipFalse.push(Code.pc - 2);
		Code.fixup(skipTrue.pop());
	}
}