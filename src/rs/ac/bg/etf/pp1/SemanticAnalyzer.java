package rs.ac.bg.etf.pp1;

import java.util.*;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {

	// ====== Logger ======
	Logger log = Logger.getLogger(getClass());

	// ====== Global State ======
	private boolean errorDetected = false;
	private Obj programScope;

	// ====== Type Information ======
	private Struct currentType;
	private Struct boolType = Tab.find("bool").getType();
	private Struct enumType = new Struct(Struct.Enum);

	// ====== Method Context ======
	private Obj currentMethod = null;
	private boolean returnFound = false;
	private boolean params = false;
	private List<Struct> actualParamTypes = new ArrayList<>();

	// ====== Enum Context ======
	private Obj currentEnum = null;
	private int enumValueCounter = 0;

	// ====== Constant Context ======
	private Struct constantType;
	private int constantValue;

	// ====== Switch Context ======
	private Set<Integer> switchCaseValues = new HashSet<>();

	// ====== Counters & Statistics ======
	private int nVars;
	private int printCallCount = 0;
	private boolean mainExists = false;
	private boolean inFor = false;
	private boolean inSwitch = false;

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public boolean passed() {
		return !errorDetected;
	}

	/*
	 * public static final int Con = 0, Var = 1, Type = 2, Meth = 3, Fld = 4,
	 * Elem=5, Prog = 6;
	 */

	@Override
	public void visit(ProgramName programName) {
		programScope = Tab.insert(Obj.Prog, programName.getI1(), Tab.noType);
		Tab.openScope();
	}

	@Override
	public void visit(Program program) {
		Tab.chainLocalSymbols(programScope);
		Tab.closeScope();
		if (!mainExists)
			report_error("nije definisana metoda main", program);

	}
	/*
	 * noType = new Struct(Struct.None), intType = new Struct(Struct.Int), charType
	 * = new Struct(Struct.Char), nullType = new Struct(Struct.Class);
	 */

	@Override
	public void visit(Type type) {

		Obj typeObj = Tab.find(type.getI1());
		if (typeObj == Tab.noObj) {
			report_error("nepostojeci tip podataka " + type.getI1(), type);
			currentType = Tab.noType;
		} else if (typeObj.getKind() != Obj.Type) {
			report_error("neadekvatan tip podataka " + type.getI1(), type);
			currentType = Tab.noType;
		} else {
			currentType = typeObj.getType();
		}
	}

	@Override
	public void visit(IdentVarDecl identVarDecl) {
		String name = identVarDecl.getI1();

		if (Tab.currentScope().findSymbol(name) != null) {
			report_error("Simbol " + name + " je vec deklarisan", identVarDecl);
			return;
		}

		if (currentType == Tab.noType) {
			report_error("Neispravan tip promenljive " + name, identVarDecl);
			return;
		}
		if (currentMethod == null) {
			Tab.insert(Obj.Var, name, currentType);
			return;
		}

		/* else these are params of method */
		Obj varObj = Tab.insert(Obj.Var, name, currentType);
		if (params) {
			// Ovo je parametar metode
			varObj.setFpPos(currentMethod.getLevel());
			currentMethod.setLevel(currentMethod.getLevel() + 1);
		}
	}

	@Override
	public void visit(ArrayVarDecl arrayVarDecl) {
		String name = arrayVarDecl.getI1();

		if (Tab.currentScope().findSymbol(name) != null) {
			report_error("Simbol " + name + " je vec deklarisan", arrayVarDecl);
			return;
		}

		if (currentType == Tab.noType) {
			report_error("Neispravan tip promenljive " + name, arrayVarDecl);
			return;
		}

		if (currentMethod == null) {
			Tab.insert(Obj.Var, name, new Struct(Struct.Array, currentType));
			return;
		}

		/* else these are params of method */

		Obj varObj = Tab.insert(Obj.Var, name, new Struct(Struct.Array, currentType));
		if (params) {
			// Ovo je parametar metode
			varObj.setFpPos(currentMethod.getLevel());
			currentMethod.setLevel(currentMethod.getLevel() + 1);
		}

	}

	@Override
	public void visit(ConstDecl constDecl) {
		String name = constDecl.getI1();
		if (Tab.find(name) != Tab.noObj) {
			report_error("Simbol " + name + " je vec deklarisan", constDecl);
			return;
		}
		if (!constantType.assignableTo(currentType)) {
			report_error("Neispravan tip promenljive " + name + constantType.getKind() + currentType.getKind(),
					constDecl);
			return;
		}

		Obj conObj = Tab.insert(Obj.Con, name, currentType);
		conObj.setAdr(constantValue);

	}

	@Override
	public void visit(Constant_num constant_num) {
		constantValue = constant_num.getN1();
		constantType = Tab.intType;

	}

	@Override
	public void visit(Constant_char constant_char) {
		constantValue = constant_char.getC1();
		constantType = Tab.charType;
	}

	@Override
	public void visit(Constant_bool constant_bool) {
		constantValue = constant_bool.getB1();
		constantType = boolType;
	}

	@Override
	public void visit(MethodDecl methodDecl) {
		if (currentMethod.getName().equals("main") && currentMethod.getType() != Tab.noType) {
			report_error("Main metoda ne moze da ima type razlicit od void", methodDecl);
			return;
		}
		if (currentMethod.getName().equals("main") && params) {
			report_error("Main metoda ne moze da ima parametre", methodDecl);
			return;
		}
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();

		currentMethod = null;
		params = false;
	}

	@Override
	public void visit(MethodName methodName) {
		if (Tab.currentScope().findSymbol(methodName.getI1()) != null) {
			report_error("Vec definisan simbol u istom scope-u " + methodName.getI1(), methodName);
			return;
		}
		if (methodName.getI1().equals("main")) {
			mainExists = true;
		}
		currentMethod = Tab.insert(Obj.Meth, methodName.getI1(), currentType);
		currentMethod.setLevel(0);
		params = true;
		Tab.openScope();
	}

	@Override
	public void visit(ParamsList_param paramsList_param) {
		params = false;
	}

	@Override
	public void visit(ParamsList_e paramsList_e) {
		params = false;
	}

	@Override
	public void visit(EnumDecl enumDecl) {
		Tab.chainLocalSymbols(currentEnum);
		Tab.closeScope();
	}

	@Override
	public void visit(EnumName enumName) {
		if (Tab.currentScope().findSymbol(enumName.getI1()) != null) {
			report_error("Vec definisan simbol u istom scope-u " + enumName.getI1(), enumName);
			return;
		}
		currentEnum = Tab.insert(Obj.Type, enumName.getI1(), enumType);
		Tab.openScope(); // scope for enum items
		enumValueCounter = 0;
	}

	@Override
	public void visit(IdentEnumItem item) {
		String name = item.getI1();

		if (Tab.currentScope().findSymbol(name) != null) {
			report_error("Enum item " + name + " vec postoji", item);
			return;
		}

		Obj con = Tab.insert(Obj.Con, name, Tab.intType);
		con.setAdr(enumValueCounter);

		enumValueCounter++;
	}

	@Override
	public void visit(AssignEnumItem item) {
		String name = item.getI1();
		int value = item.getN2();

		if (Tab.currentScope().findSymbol(name) != null) {
			report_error("Enum item " + name + " vec postoji", item);
			return;
		}

		Obj con = Tab.insert(Obj.Con, name, Tab.intType);
		con.setAdr(value);

		enumValueCounter = value + 1; // next automatic value
	}

	@Override
	public void visit(VoidPrefix voidPrefix) {
		currentType = Tab.noType;
	}

	//////////////// Context analyzer //////////////////////
	// PartFactor
	@Override
	public void visit(NumberParamFactor numberParamFactor) {
		numberParamFactor.struct = Tab.intType;
	}

	@Override
	public void visit(CharParamFactor charParamFactor) {
		charParamFactor.struct = Tab.charType;
	}

	@Override
	public void visit(BoolParamFactor boolParamFactor) {
		boolParamFactor.struct = boolType;
	}

	@Override
	public void visit(DesignatorFactor_e designatorFactor_e) {
		designatorFactor_e.struct = designatorFactor_e.getDesignator().obj.getType();
	}

	@Override
	public void visit(DesignatorFactor_func designatorFactor_func) {
		designatorFactor_func.struct = designatorFactor_func.getDesignator().obj.getType();
	}

	@Override
	public void visit(DesignatorFactor_param_func designatorFactor_param_func) {
		designatorFactor_param_func.struct = designatorFactor_param_func.getDesignator().obj.getType();
	}

	@Override
	public void visit(NewParamFactor newParamFactor) {
		if (!newParamFactor.getExpression().struct.compatibleWith(Tab.intType)) {
			report_error("Duzina niza mora da bude int", newParamFactor);
			newParamFactor.struct = Tab.noType;
			return;
		}
		if (currentType.equals(Tab.noType)) {
			report_error("Neodgovarajuci tip", newParamFactor);
			newParamFactor.struct = Tab.noType;
			return;
		}
		newParamFactor.struct = new Struct(Struct.Array, currentType);
	}

	@Override
	public void visit(ExpParamFactor expParamFactor) {
		expParamFactor.struct = expParamFactor.getExpression().struct;
	}

	@Override
	public void visit(Factor factor) {

		if (factor.getUnaryMinus() instanceof UnaryMinus_m) {
			if (factor.getPartFactor().struct.compatibleWith(Tab.intType)) {
				factor.struct = Tab.intType;
				return;
			} else {
				report_error("Negacija ne int vrednosti", factor);
				factor.struct = Tab.noType;
				return;
			}
		}
		factor.struct = factor.getPartFactor().struct;
	}

	@Override
	public void visit(FactorList_e factorList_e) {
		factorList_e.struct = factorList_e.getFactor().struct;
	}

	@Override
	public void visit(FactorList_array factorList_array) {
		Struct facList = factorList_array.getFactorList().struct;
		Struct fac = factorList_array.getFactor().struct;
		if (facList.compatibleWith(Tab.intType) && fac.compatibleWith(Tab.intType)) {
			factorList_array.struct = Tab.intType;
			return;
		}
		report_error("tipovi nisu kompatibilni za " + facList + " i " + fac, factorList_array);
		factorList_array.struct = Tab.noType;
		return;

	}

	@Override
	public void visit(Term term) {
		term.struct = term.getFactorList().struct;
	}

	@Override
	public void visit(TermList_e termList_e) {
		termList_e.struct = termList_e.getTerm().struct;
	}

	@Override
	public void visit(TermList_array termList_array) {
		Struct tl = termList_array.getTerm().struct;
		Struct tl_array = termList_array.getTermList().struct;
		if (tl.compatibleWith(Tab.intType) && tl_array.compatibleWith(Tab.intType)) {
			termList_array.struct = Tab.intType;
			return;
		}
		report_error("Neadekvatan tip promenljive " + tl.getKind() + " i " + tl_array.getKind(), termList_array);
		termList_array.struct = Tab.noType;
		return;
	}

	@Override
	public void visit(Designator_var designator_var) {
		Obj varObj = Tab.find(designator_var.getI1());
		if (varObj == Tab.noObj) {
			report_error("nedefisinisana promenljiva " + designator_var.getI1(), designator_var);
			designator_var.obj = Tab.noObj;
			return;
		}
		if (!(varObj.getKind() == Obj.Var || varObj.getKind() == Obj.Con || varObj.getKind() == Obj.Elem
				|| varObj.getKind() == Obj.Meth
				|| (varObj.getKind() == Obj.Type && varObj.getType().getKind() == Struct.Enum))) {
			report_error("Neadekvatan tip promenljiveeeeeeee " + designator_var.getI1(), designator_var);
			designator_var.obj = Tab.noObj;
			return;
		}
		designator_var.obj = varObj;
	}

	@Override
	public void visit(DesignatorArrayName designatorArrayName) {
		Obj arrObj = Tab.find(designatorArrayName.getI1());
		if (arrObj == Tab.noObj) {
			report_error("nedefisinisana promenljiva " + designatorArrayName.getI1(), designatorArrayName);
			designatorArrayName.obj = Tab.noObj;
			return;
		}
		if (arrObj.getKind() != Obj.Var || arrObj.getType().getKind() != Struct.Array) {
			report_error("Neadekvatan tip promenljiveeee " + designatorArrayName.getI1(), designatorArrayName);
			designatorArrayName.obj = Tab.noObj;
			return;
		}
		designatorArrayName.obj = arrObj;
		return;
	}

	@Override
	public void visit(Designator_elem designator_elem) {
		Obj arrObj = designator_elem.getDesignatorArrayName().obj;
		if (arrObj == Tab.noObj)
			designator_elem.obj = Tab.noObj;
		else if (!designator_elem.getExpression().struct.compatibleWith(Tab.intType)) {
			report_error("indeks mora da bude int tipa", designator_elem);
			designator_elem.obj = Tab.noObj;
		} else {
			designator_elem.obj = new Obj(Obj.Elem, arrObj.getName() + "[$]", arrObj.getType().getElemType()); // nejasno
		}
	}

	@Override
	public void visit(Designator_nest designator_nest) {

		Obj desObj = designator_nest.getDesignator().obj;
		DotDesignatorOp dotDes = designator_nest.getDotDesignatorOp();

		if (desObj.equals(Tab.noObj)) {
			report_error("Nepostojeci designator", designator_nest);
			designator_nest.obj = Tab.noObj;
			return;
		}

		if (dotDes instanceof LengthDesignatorOp) {
			if (desObj.getType().getKind() != Struct.Array) {
				report_error(".length je dozvoljen samo nad nizom", dotDes);
				designator_nest.obj = Tab.noObj;
				return;
			}
			designator_nest.obj = new Obj(Obj.Var, "length", Tab.intType);
		} else if (dotDes instanceof IdentDesignatorOp) {
			if (desObj.getType().getKind() != Struct.Enum) {
				report_error("Pristup clanu dozvoljen samo za enum", dotDes);
				designator_nest.obj = Tab.noObj;
				return;
			}
			IdentDesignatorOp identOp = (IdentDesignatorOp) dotDes;
			String enumMemberName = identOp.getI1();

			Obj enumVal = Tab.noObj;

			for (Obj elem : desObj.getLocalSymbols()) {
				if (elem.getName().equals(enumMemberName)) {
					enumVal = elem;
					break;
				}
			}
			if (enumVal.equals(Tab.noObj)) {
				report_error("Enum nema trazeni clan: " + enumMemberName, dotDes);
				designator_nest.obj = Tab.noObj;
				return;
			}
			designator_nest.obj = enumVal;
		}
	}

	@Override
	public void visit(NonTerExpression nonTerExpression) {
		nonTerExpression.struct = nonTerExpression.getNonTernaryExpression().struct;
	}

	@Override
	public void visit(TerExpression terExpression) {
		terExpression.struct = terExpression.getTernaryExpression().struct;
	}

	@Override
	public void visit(NonTernaryExpression nonTernaryExpression) {
		nonTernaryExpression.struct = nonTernaryExpression.getTermList().struct;
	}

	@Override
	public void visit(TernaryExpression ternaryExpression) {
		Struct cond = ternaryExpression.getCondition().struct;
		Struct expr1 = ternaryExpression.getExpression().struct;
		Struct expr2 = ternaryExpression.getExpression1().struct;

		if (!cond.compatibleWith(boolType)) {
			report_error("Condition mora da bude tipa bool", ternaryExpression);
			ternaryExpression.struct = Tab.noType;
			return;
		}
		if (!expr1.compatibleWith(expr2)) {
			report_error("Expr1 i Expr2 moraju da budu istog tipa", ternaryExpression);
			ternaryExpression.struct = Tab.noType;
			return;
		}
		ternaryExpression.struct = expr1;
	}

	@Override
	public void visit(Condition condition) {
		Struct condTerm = condition.getCondTerm().struct;
		Struct condRest = condition.getConditionRest().struct;

		if (condTerm.equals(Tab.noType) || condRest.compatibleWith(Tab.noType)) {
			condition.struct = Tab.noType;
			return;
		}

		if (!condTerm.compatibleWith(boolType) || !condRest.compatibleWith(boolType)) {
			report_error("CondTerm ili CondRest su tipa razlicitog od bool! 499", condition);
			condition.struct = Tab.noType;
			return;
		}
		condition.struct = boolType;
	}

	@Override
	public void visit(CondTerm condTerm) {
		Struct condFact = condTerm.getCondFact().struct;
		Struct condRest = condTerm.getCondFactRest().struct;

		if (condFact.equals(Tab.noType) || condRest.compatibleWith(Tab.noType)) {
			condTerm.struct = Tab.noType;
			return;
		}

		if (!condFact.compatibleWith(boolType) || !condRest.compatibleWith(boolType)) {
			report_error("CondTerm ili CondRest su tipa razlicitog od bool! 457", condTerm);
			condTerm.struct = Tab.noType;
			return;
		}
		condTerm.struct = boolType;
	}

	@Override
	public void visit(ConditionRest_array conditionRest_array) {
		Struct condTerm = conditionRest_array.getCondTerm().struct;
		Struct condRest = conditionRest_array.getConditionRest().struct;
		if (!condTerm.compatibleWith(boolType) || !condRest.compatibleWith(boolType)) {
			report_error("CondTerm ili CondRest su tipa razlicitog od bool! 470", conditionRest_array);
			conditionRest_array.struct = Tab.noType;
			return;
		}
		conditionRest_array.struct = boolType;
	}

	@Override
	public void visit(CondFactRest_array condFactRest_array) {
		Struct condFact = condFactRest_array.getCondFact().struct;
		Struct condRest = condFactRest_array.getCondFactRest().struct;
		if (!condFact.compatibleWith(boolType) || (!(condFactRest_array.getCondFactRest() instanceof CondFactRest_e)
				&& !condRest.compatibleWith(boolType))) {
			report_error("CondTerm ili CondRest su tipa razlicitog od bool!", condFactRest_array);
			condFactRest_array.struct = Tab.noType;
			return;
		}
		condFactRest_array.struct = boolType;
	}

	@Override
	public void visit(ConditionRest_e conditionRest_e) {
		conditionRest_e.struct = boolType;
	}

	@Override
	public void visit(CondFactRest_e condFactRest_e) {
		condFactRest_e.struct = boolType;
	}

	@Override
	public void visit(NonRelopCondFact nonRelopCondFact) {
		Struct condFact = nonRelopCondFact.getNonTernaryExpression().struct;
		if (!condFact.compatibleWith(boolType)) {
			report_error("condFact je tipa razlicitog od bool! 494" + condFact.getKind(), nonRelopCondFact);
			nonRelopCondFact.struct = Tab.noType;
			return;
		}
		nonRelopCondFact.struct = boolType;
	}

	@Override
	public void visit(RelopCondFact relopCondFact) {
		Struct nte1 = relopCondFact.getNonTernaryExpression().struct;
		Struct nte2 = relopCondFact.getNonTernaryExpression1().struct;

		if (nte1.equals(Tab.noType) || nte2.compatibleWith(Tab.noType)) {
			relopCondFact.struct = Tab.noType;
			return;
		}

		if (!nte1.compatibleWith(nte2)) {
			report_error("nte1 i nte2 su razlicitog tipa!", relopCondFact);
			relopCondFact.struct = Tab.noType;
			return;
		}
		if (nte1.getKind() == Struct.Array) {
			Relop relop = relopCondFact.getRelop();
			if (!(relop instanceof EqRelop) && !(relop instanceof NeqRelop)) {
				report_error("dva arraya mozes da poredis samo sa eq ili neq!", relopCondFact);
				relopCondFact.struct = Tab.noType;
				return;
			}
			relopCondFact.struct = boolType;
			return;
		}
		relopCondFact.struct = boolType;
		return;
	}
/////////////////////////////////////// statements //////////////////////////////////////////

	@Override
	public void visit(ReturnStatement returnStatement) {
		returnFound = true;
		Struct ret = returnStatement.getReturnExpression().struct;
		if (returnStatement.getReturnExpression() instanceof ReturnExpression_non_expr
				&& !currentMethod.getType().equals(Tab.noType)) {
			report_error("Return vrednost se ne poklapa sa return tipom funkcije", returnStatement);
			return;
		}
		if (!ret.compatibleWith(currentMethod.getType())) {
			report_info(ret.getKind() + " " + currentMethod.getType().getKind(), returnStatement);
			report_error("Return vrednost se ne poklapa sa return tipom funkcije " + currentMethod.getName(),
					returnStatement);
			return;
		}
	}

	@Override
	public void visit(ReturnExpression_expr returnExpression_expr) {
		returnExpression_expr.struct = returnExpression_expr.getExpression().struct;
	}

	@Override
	public void visit(ReturnExpression_non_expr returnExpression_non_expr) {
		returnExpression_non_expr.struct = Tab.noType;
	}

	@Override
	public void visit(IfElseStatement ifElseStatement) {
		Struct cond = ifElseStatement.getCondition().struct;
		if (!cond.compatibleWith(boolType)) {

		}
	}

	@Override
	public void visit(ExprDesignatorStatement exprDesignatorStatement) {
		Struct expr = exprDesignatorStatement.getExpression().struct;
		Obj desig = exprDesignatorStatement.getDesignator().obj;

		if (expr.equals(Tab.noType) || desig.equals(Tab.noObj))
			return;

		if (!(desig.getKind() == Obj.Var || desig.getKind() == Obj.Elem || desig.getKind() == Obj.Fld)) {
			report_error("ne mozes objektu ovog tipa dodeliti vrednost!", exprDesignatorStatement);
			return;
		}
		if (!desig.getType().equals(expr)) {
			report_error("Razliciti tipovi, ne moze se dodeliti!", exprDesignatorStatement);
			return;
		}
	}

	@Override
	public void visit(ConditionedForExpression conditionedForExpression) {
		Struct cond = conditionedForExpression.getCondition().struct;
		if (cond.equals(Tab.noType))
			return;

		if (!cond.compatibleWith(boolType)) {
			report_error("Condition mora biti bool!", conditionedForExpression);
			return;
		}
	}

	@Override
	public void visit(SwitchStatement switchStatement) {
		Struct expr = switchStatement.getExpression().struct;
		if (!expr.compatibleWith(Tab.intType)) {
			report_error("Expression u switchu mora biti int tipa!", switchStatement);
			return;
		}

		if (switchCaseValues != null) {
			switchCaseValues.clear();
			switchCaseValues = null;
		}
		inSwitch = false;
	}

	@Override
	public void visit(SwitchCases_s switchCases_s) {
		if (switchCaseValues == null) {
			switchCaseValues = new HashSet<>();
		}
		int num = switchCases_s.getN1();
		if (switchCaseValues.contains(num)) {
			report_error("Switch case mora da bude jedinstven broj!", switchCases_s);
			return;
		}
		switchCaseValues.add(num);
	}

	@Override
	public void visit(ParamsDesignatorStatement paramsDesignatorStatement) {
		Obj desObj = paramsDesignatorStatement.getDesignator().obj;
		ActPars actPars = paramsDesignatorStatement.getActPars();
		if (desObj == Tab.noObj) {
			return;
		}
		if (desObj.getKind() != Obj.Meth) {
			report_error("Iskorisceni designator nije funkcija!", paramsDesignatorStatement);
			actualParamTypes.clear();
			return;
		}
		int actualParamCount = actualParamTypes.size();
		Obj meth = Tab.find(desObj.getName());
		if (meth.getLevel() != actualParamCount) {
			report_error("pogresan broj parametara u pozivu funkcije " + meth.getName() + " " + meth.getLevel() + " i "
					+ actualParamCount, actPars);
			actualParamTypes.clear();
			return;
		}
		Collection<Obj> formalParams = desObj.getLocalSymbols();
		Iterator<Obj> formalIt = formalParams.iterator();

		for (int i = actualParamTypes.size() - 1; i >= 0; i--) {
			Struct tempParamActual = actualParamTypes.get(i);
			Obj tempParamFormal = formalIt.next();
			if (!tempParamActual.compatibleWith(tempParamFormal.getType())) {
				report_error("Tip parametra u pozivu se ne poklapa sa tipom u funckiji!", actPars);
				actualParamTypes.clear();
				return;
			}
		}
	}

	@Override
	public void visit(PrintStatement printStatement) {
		Struct expr = printStatement.getPrintExpression().struct;
		if (!(expr.compatibleWith(Tab.charType) || (expr.compatibleWith(Tab.intType))
				|| (expr.compatibleWith(boolType)))) {
			report_error("prvi deo printa mora da bude tipa bool, int ili char", printStatement);
			return;
		}
	}

	@Override
	public void visit(ContinueStatement continueStatement) {
		if (!inFor) {
			report_error("Mogucnost koriscenja continue je samo unutar for petlje!", continueStatement);
			return;
		}
	}

	@Override
	public void visit(BreakStatement breakStatement) {
		if (!(inFor || inSwitch)) {
			report_error("Mogucnost koriscenja break je samo unutar for petlje ili switch statementa!", breakStatement);
			return;
		}
	}

	@Override
	public void visit(IncDesignatorStatement incDesignatorStatement) {
		Obj desObj = incDesignatorStatement.getDesignator().obj;
		if (!(desObj.getKind() == Obj.Var || desObj.getKind() == Obj.Elem)) {
			report_error("Operator ++ moze se koristiti samo na varijabli ili elementu niza", incDesignatorStatement);
			return;
		}
		if (desObj.getType() != Tab.intType) {
			report_error("Operator ++ moze se koristiti samo na elementu tipa int", incDesignatorStatement);
			return;
		}
	}

	@Override
	public void visit(DecDesignatorStatement decDesignatorStatement) {
		Obj desObj = decDesignatorStatement.getDesignator().obj;
		if (!(desObj.getKind() == Obj.Var || desObj.getKind() == Obj.Elem)) {
			report_error("Operator -- moze se koristiti samo na varijabli ili elementu niza", decDesignatorStatement);
			return;
		}
		if (desObj.getType() != Tab.intType) {
			report_error("Operator -- moze se koristiti samo na elementu tipa int", decDesignatorStatement);
			return;
		}
	}

	@Override
	public void visit(ReadStatement readStatement) {
		Obj desObj = readStatement.getDesignator().obj;
		if(!(desObj.getKind() == Obj.Var ||  desObj.getKind() == Obj.Elem)) {
			report_error("Moguce je procitati samo varijablu ili element niza", readStatement);
			return;
		}
		if(!(desObj.getType().compatibleWith(Tab.intType) ||  desObj.getType().compatibleWith(Tab.charType) || desObj.getType().compatibleWith(boolType))) {
			report_error("Moguce je procitati samo element tipa int, char ili bool!", readStatement);
			return;
		}
	}

	@Override
	public void visit(ActParsList actParsList) {
		Struct expr = actParsList.getExpression().struct;
		actualParamTypes.add(expr);
		return;
	}

	@Override
	public void visit(ActParsRest_array actParsRest_array) {
		Struct expr = actParsRest_array.getExpression().struct;
		actualParamTypes.add(expr);
		return;
	}

	@Override
	public void visit(DesigStatement desigStatement) {
		actualParamTypes.clear();
	}

	@Override
	public void visit(PrintExpression_num printExpression_num) {
		printExpression_num.struct = printExpression_num.getExpression().struct;
	}

	@Override
	public void visit(PrintExpression_non_num printExpression_non_num) {
		printExpression_non_num.struct = printExpression_non_num.getExpression().struct;
	}

	@Override
	public void visit(ForStatement forStatement) {
		inFor = false;
	}

	@Override
	public void visit(ForWord forWord) {
		inFor = true;
	}

	@Override
	public void visit(SwitchWord switchWord) {
		inFor = true;
	}
}