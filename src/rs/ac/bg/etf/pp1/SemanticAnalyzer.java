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

	@Override
	public void visit(Type type) {
		Obj typeObj = Tab.find(type.getI1());
		if (typeObj == Tab.noObj) {
			report_error("Tip '" + type.getI1() + "' nije definisan", type);
			currentType = Tab.noType;
		} else if (typeObj.getKind() != Obj.Type) {
			report_error("'" + type.getI1() + "' nije tip", type);
			currentType = Tab.noType;
		} else {
			currentType = typeObj.getType();
		}
	}

	@Override
	public void visit(IdentVarDecl identVarDecl) {
		String name = identVarDecl.getI1();

		if (Tab.currentScope().findSymbol(name) != null) {
			report_error("Promenljiva '" + name + "' je vec deklarisana", identVarDecl);
			return;
		}

		if (currentType == Tab.noType) {
			report_error("Nevalidan tip za promenljivu '" + name + "'", identVarDecl);
			return;
		}
		if (currentMethod == null) {
			Tab.insert(Obj.Var, name, currentType);
			return;
		}

		Obj varObj = Tab.insert(Obj.Var, name, currentType);
		if (params) {
			varObj.setFpPos(currentMethod.getLevel());
			currentMethod.setLevel(currentMethod.getLevel() + 1);
		}
	}

	@Override
	public void visit(ArrayVarDecl arrayVarDecl) {
		String name = arrayVarDecl.getI1();

		if (Tab.currentScope().findSymbol(name) != null) {
			report_error("Niz '" + name + "' je vec deklarisan", arrayVarDecl);
			return;
		}

		if (currentType == Tab.noType) {
			report_error("Nevalidan tip za niz '" + name + "'", arrayVarDecl);
			return;
		}

		if (currentMethod == null) {
			Tab.insert(Obj.Var, name, new Struct(Struct.Array, currentType));
			return;
		}

		Obj varObj = Tab.insert(Obj.Var, name, new Struct(Struct.Array, currentType));
		if (params) {
			varObj.setFpPos(currentMethod.getLevel());
			currentMethod.setLevel(currentMethod.getLevel() + 1);
		}
	}

	@Override
	public void visit(ConstDecl constDecl) {
		String name = constDecl.getI1();
		if (Tab.find(name) != Tab.noObj) {
			report_error("Konstanta '" + name + "' je vec deklarisana", constDecl);
			return;
		}
		if (!constantType.assignableTo(currentType)) {
			report_error("Tip konstante '" + name + "' (" + constantType.getKind()
					+ ") nije kompatibilan sa deklarisanim tipom (" + currentType.getKind() + ")", constDecl);
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
			report_error("Metoda 'main' mora imati povratni tip void", methodDecl);
			return;
		}
		if (currentMethod.getName().equals("main") && params) {
			report_error("Metoda 'main' ne sme imati parametre", methodDecl);
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
			report_error("Metoda '" + methodName.getI1() + "' je vec deklarisana u trenutnom opsegu", methodName);
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
			report_error("Enumeracija '" + enumName.getI1() + "' je vec deklarisana", enumName);
			return;
		}
		currentEnum = Tab.insert(Obj.Type, enumName.getI1(), enumType);
		Tab.openScope();
		enumValueCounter = 0;
	}

	@Override
	public void visit(IdentEnumItem item) {
		String name = item.getI1();

		if (Tab.currentScope().findSymbol(name) != null) {
			report_error("Član enumeracije '" + name + "' vec postoji", item);
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
			report_error("Član enumeracije '" + name + "' vec postoji", item);
			return;
		}

		Obj con = Tab.insert(Obj.Con, name, Tab.intType);
		con.setAdr(value);
		enumValueCounter = value + 1;
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
		Obj desObj = designatorFactor_e.getDesignator().obj;
		if (desObj.getKind() == Obj.Meth) {
			report_error("Nije moguce dodeljivanje metode: " + desObj.getName(), designatorFactor_e);
			designatorFactor_e.struct = Tab.noType;
			return;
		}
		designatorFactor_e.struct = designatorFactor_e.getDesignator().obj.getType();
	}

	@Override
	public void visit(DesignatorFactor_func designatorFactor_func) {

		Obj desObj = designatorFactor_func.getDesignator().obj;

		if (desObj.getKind() != Obj.Meth) {
			report_error("Poziv funkcije zahteva identifikator metode", designatorFactor_func);
			actualParamTypes.clear();
			designatorFactor_func.struct = Tab.noType;
			return;
		}
		boolean bool = checkFunctionCall(desObj, designatorFactor_func);
		if (bool)
			designatorFactor_func.struct = designatorFactor_func.getDesignator().obj.getType();
		else
			designatorFactor_func.struct = Tab.noType;
		return;
	}

	@Override
	public void visit(ExpParamFactor expParamFactor) {
		expParamFactor.struct = expParamFactor.getExpression().struct;
	}

	@Override
	public void visit(FactorList_e factorList_e) {
		factorList_e.struct = factorList_e.getFactor().struct;
	}

	@Override
	public void visit(NewParamFactor newParamFactor) {
		if (!newParamFactor.getExpression().struct.compatibleWith(Tab.intType)) {
			report_error("Dužina niza mora biti celobrojna vrednost", newParamFactor);
			newParamFactor.struct = Tab.noType;
			return;
		}
		if (currentType.equals(Tab.noType)) {
			report_error("Neodgovarajući tip za alokaciju niza", newParamFactor);
			newParamFactor.struct = Tab.noType;
			return;
		}
		newParamFactor.struct = new Struct(Struct.Array, currentType);
	}

	@Override
	public void visit(Factor factor) {
		if (factor.getUnaryMinus() instanceof UnaryMinus_m) {
			if (!factor.getPartFactor().struct.compatibleWith(Tab.intType)) {
				report_error("Operator '-' može se primeniti samo na celobrojne vrednosti", factor);
				factor.struct = Tab.noType;
				return;
			}
			factor.struct = Tab.intType;
			return;
		}
		factor.struct = factor.getPartFactor().struct;
	}

	@Override
	public void visit(FactorList_array factorList_array) {
		Struct facList = factorList_array.getFactorList().struct;
		Struct fac = factorList_array.getFactor().struct;
		if (!facList.compatibleWith(Tab.intType) || !fac.compatibleWith(Tab.intType)) {
			report_error("Operandi moraju biti celobrojni tipovi", factorList_array);
			factorList_array.struct = Tab.noType;
			return;
		}
		factorList_array.struct = Tab.intType;
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
		if (!tl.compatibleWith(Tab.intType) || !tl_array.compatibleWith(Tab.intType)) {
			report_error("Operandi moraju biti celobrojni tipovi", termList_array);
			termList_array.struct = Tab.noType;
			return;
		}
		termList_array.struct = Tab.intType;
	}

	@Override
	public void visit(Designator_var designator_var) {
		Obj varObj = Tab.find(designator_var.getI1());
		if (varObj == Tab.noObj) {
			report_error("Nedeklarisan identifikator: '" + designator_var.getI1() + "'", designator_var);
			designator_var.obj = Tab.noObj;
			return;
		}
		designator_var.obj = varObj;
	}

	@Override
	public void visit(DesignatorArrayName designatorArrayName) {
		Obj arrObj = Tab.find(designatorArrayName.getI1());
		if (arrObj == Tab.noObj) {
			report_error("Nedeklarisan niz: '" + designatorArrayName.getI1() + "'", designatorArrayName);
			designatorArrayName.obj = Tab.noObj;
			return;
		}
		if (arrObj.getKind() != Obj.Var || arrObj.getType().getKind() != Struct.Array) {
			report_error("'" + designatorArrayName.getI1() + "' nije niz", designatorArrayName);
			designatorArrayName.obj = Tab.noObj;
			return;
		}
		designatorArrayName.obj = arrObj;
	}

	@Override
	public void visit(Designator_elem designator_elem) {
		Obj arrObj = designator_elem.getDesignatorArrayName().obj;
		if (arrObj == Tab.noObj) {
			designator_elem.obj = Tab.noObj;
		} else if (!designator_elem.getExpression().struct.compatibleWith(Tab.intType)) {
			report_error("Indeks niza mora biti celobrojne vrednosti", designator_elem);
			designator_elem.obj = Tab.noObj;
		} else {
			designator_elem.obj = new Obj(Obj.Elem, arrObj.getName() + "[$]", arrObj.getType().getElemType());
		}
	}

	@Override
	public void visit(Designator_nest designator_nest) {
		Obj desObj = designator_nest.getDesignator().obj;
		DotDesignatorOp dotDes = designator_nest.getDotDesignatorOp();

		if (desObj.equals(Tab.noObj)) {
			report_error("Nevalidan designator", designator_nest);
			designator_nest.obj = Tab.noObj;
			return;
		}

		if (dotDes instanceof LengthDesignatorOp) {
			if (desObj.getType().getKind() != Struct.Array) {
				report_error("Atribut '.length' je dostupan samo za nizove", dotDes);
				designator_nest.obj = Tab.noObj;
				return;
			}
			designator_nest.obj = new Obj(Obj.Var, "length", Tab.intType);
		} else if (dotDes instanceof IdentDesignatorOp) {
			if (desObj.getType().getKind() != Struct.Enum) {
				report_error("Pristup članovima je dozvoljen samo za enumeracije", dotDes);
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
				report_error("Enumeracija nema član: '" + enumMemberName + "'", dotDes);
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
			report_error("Uslov u ternarnom izrazu mora biti logičkog tipa", ternaryExpression);
			ternaryExpression.struct = Tab.noType;
			return;
		}
		if (!expr1.compatibleWith(expr2)) {
			report_error("Oba izraza u ternarnom izrazu moraju imati isti tip", ternaryExpression);
			ternaryExpression.struct = Tab.noType;
			return;
		}
		ternaryExpression.struct = expr1;
	}

	@Override
	public void visit(Condition condition) {
		Struct condTerm = condition.getCondTerm().struct;
		Struct condRest = condition.getConditionRest().struct;

		if (!condTerm.equals(boolType) || !condRest.equals(boolType)) {
			report_error("Operandi logičkih operatora moraju biti logičkog tipa", condition);
			condition.struct = Tab.noType;
			return;
		}
		condition.struct = boolType;
	}

	@Override
	public void visit(CondTerm condTerm) {
		Struct condFact = condTerm.getCondFact().struct;
		Struct condRest = condTerm.getCondFactRest().struct;

		if (!condFact.compatibleWith(boolType) || !condRest.compatibleWith(boolType)) {
			report_error("Operandi logičkih operatora moraju biti logičkog tipa", condTerm);
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
			report_error("Operandi logičkih operatora moraju biti logičkog tipa", conditionRest_array);
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
			report_error("Operandi logičkih operatora moraju biti logičkog tipa", condFactRest_array);
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
			report_error("Uslov mora biti logičkog tipa", nonRelopCondFact);
			nonRelopCondFact.struct = Tab.noType;
			return;
		}
		nonRelopCondFact.struct = boolType;
	}

	@Override
	public void visit(RelopCondFact relopCondFact) {
		Struct nte1 = relopCondFact.getNonTernaryExpression().struct;
		Struct nte2 = relopCondFact.getNonTernaryExpression1().struct;

		if (!nte1.compatibleWith(nte2)) {
			report_error("Operandi relacijskog operatora moraju imati isti tip", relopCondFact);
			relopCondFact.struct = Tab.noType;
			return;
		}
		if (nte1.getKind() == Struct.Array) {
			Relop relop = relopCondFact.getRelop();
			if (!(relop instanceof EqRelop) && !(relop instanceof NeqRelop)) {
				report_error("Nizove je moguće porediti samo operatorima '==' i '!='", relopCondFact);
				relopCondFact.struct = Tab.noType;
				return;
			}
			relopCondFact.struct = boolType;
			return;
		}
		relopCondFact.struct = boolType;
	}
	/////////////////////////////////////// statements
	/////////////////////////////////////// ////////////////////////////////////////////

	@Override
	public void visit(ReturnStatement returnStatement) {
		returnFound = true;
		Struct ret = returnStatement.getReturnExpression().struct;
		if (currentMethod == null) {
			report_error("Return statemement se mora nalaziti unutar metode ili funckije", returnStatement);
			return;
		}
		if (returnStatement.getReturnExpression() instanceof ReturnExpression_non_expr
				&& !currentMethod.getType().equals(Tab.noType)) {
			report_error("Metoda '" + currentMethod.getName() + "' mora vraćati vrednost", returnStatement);
			return;
		}
		if (!ret.compatibleWith(currentMethod.getType())) {
			report_error("Povratna vrednost se ne poklapa sa tipom metode '" + currentMethod.getName() + "'",
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
			report_error("Uslov u 'if' naredbi mora biti logičkog tipa", ifElseStatement);
			return;
		}
	}

	@Override
	public void visit(ExprDesignatorStatement exprDesignatorStatement) {
		Struct expr = exprDesignatorStatement.getExpression().struct;
		Obj desig = exprDesignatorStatement.getDesignator().obj;
		if (desig.getType() == Tab.noType || expr == Tab.noType)
			return;

		if (!(desig.getKind() == Obj.Var || desig.getKind() == Obj.Elem || desig.getKind() == Obj.Fld)) {
			report_error("Dodela vrednosti nije dozvoljena ovom tipu objekta", exprDesignatorStatement);
			return;
		}
		if (!expr.assignableTo(desig.getType())) {
			report_error("Tipovi u dodeli nisu kompatibilni", exprDesignatorStatement);
			return;
		}
	}

	@Override
	public void visit(ConditionedForExpression conditionedForExpression) {
		Struct cond = conditionedForExpression.getCondition().struct;
		if (!cond.compatibleWith(boolType)) {
			report_error("Uslov u 'for' petlji mora biti logičkog tipa", conditionedForExpression);
			return;
		}
	}

	@Override
	public void visit(SwitchStatement switchStatement) {
		Struct expr = switchStatement.getExpression().struct;
		if (!expr.compatibleWith(Tab.intType)) {
			report_error("Izraz u 'switch' naredbi mora biti celobrojnog tipa", switchStatement);
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
			report_error("Vrednost case-a (" + num + ") nije jedinstvena u switch naredbi", switchCases_s);
			return;
		}
		switchCaseValues.add(num);
	}

	@Override
	public void visit(ParamsDesignatorStatement paramsDesignatorStatement) {
		Obj desObj = paramsDesignatorStatement.getDesignator().obj;
		if (desObj.getKind() != Obj.Meth) {
			report_error("Poziv funkcije zahteva identifikator metode", paramsDesignatorStatement);
			actualParamTypes.clear();
			return;
		}

		checkFunctionCall(desObj, paramsDesignatorStatement);
		actualParamTypes.clear();
	}

	@Override
	public void visit(PrintStatement printStatement) {
		Struct expr = printStatement.getPrintExpression().struct;
		if (expr == Tab.noType)
			return;
		if (!(expr.compatibleWith(Tab.charType) || expr.compatibleWith(Tab.intType) || expr.compatibleWith(boolType))) {
			report_error("Funkcija 'print' podržava samo tipove: int, char i bool", printStatement);
			return;
		}
	}

	@Override
	public void visit(ContinueStatement continueStatement) {
		if (!inFor) {
			report_error("Naredba 'continue' je dozvoljena samo unutar 'for' petlje", continueStatement);
			return;
		}
	}

	@Override
	public void visit(BreakStatement breakStatement) {
		if (!(inFor || inSwitch)) {
			report_error("Naredba 'break' je dozvoljena samo unutar 'for' petlje ili 'switch' naredbe", breakStatement);
			return;
		}
	}

	@Override
	public void visit(IncDesignatorStatement incDesignatorStatement) {
		Obj desObj = incDesignatorStatement.getDesignator().obj;
		if (!(desObj.getKind() == Obj.Var || desObj.getKind() == Obj.Elem)) {
			report_error("Operator '++' se može primeniti samo na promenljivu ili element niza",
					incDesignatorStatement);
			return;
		}
		if (desObj.getType() != Tab.intType) {
			report_error("Operator '++' zahteva operand celobrojnog tipa", incDesignatorStatement);
			return;
		}
	}

	@Override
	public void visit(DecDesignatorStatement decDesignatorStatement) {
		Obj desObj = decDesignatorStatement.getDesignator().obj;
		if (!(desObj.getKind() == Obj.Var || desObj.getKind() == Obj.Elem)) {
			report_error("Operator '--' se može primeniti samo na promenljivu ili element niza",
					decDesignatorStatement);
			return;
		}
		if (desObj.getType() != Tab.intType) {
			report_error("Operator '--' zahteva operand celobrojnog tipa", decDesignatorStatement);
			return;
		}
	}

	@Override
	public void visit(ReadStatement readStatement) {
		Obj desObj = readStatement.getDesignator().obj;
		if (!(desObj.getKind() == Obj.Var || desObj.getKind() == Obj.Elem)) {
			report_error("Funkcija 'read' može čitati samo promenljive ili elemente niza", readStatement);
			return;
		}
		if (!(desObj.getType().compatibleWith(Tab.intType) || desObj.getType().compatibleWith(Tab.charType)
				|| desObj.getType().compatibleWith(boolType))) {
			report_error("Funkcija 'read' podržava samo tipove: int, char i bool", readStatement);
			return;
		}
	}

	@Override
	public void visit(ActPars_params actPars_params) {
		Struct expr = actPars_params.getExpression().struct;
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
		Struct expr = printExpression_num.getExpression().struct;
		if (expr == Tab.noType)
			return;
		if (!(expr.compatibleWith(Tab.charType) || expr.compatibleWith(Tab.intType) || expr.compatibleWith(boolType))) {
			report_error("Funkcija 'print' podržava samo tipove: int, char i bool", printExpression_num);
			return;
		}
		printExpression_num.struct = printExpression_num.getExpression().struct;
	}

	@Override
	public void visit(PrintExpression_non_num printExpression_non_num) {

		Struct expr = printExpression_non_num.getExpression().struct;
		if (expr == Tab.noType)
			return;
		if (!(expr.compatibleWith(Tab.charType) || expr.compatibleWith(Tab.intType) || expr.compatibleWith(boolType))) {
			report_error("Funkcija 'print' podržava samo tipove: int, char i bool", printExpression_non_num);
			return;
		}
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
		inSwitch = true;
	}

////////////////////////////////// Helping funcitons //////////////////////////////

	private boolean checkFunctionCall(Obj methodObj, SyntaxNode errorNode) {
		if (!validateMethodObject(methodObj, errorNode))
			return false;
		if (!validateParameterCount(methodObj, errorNode))
			return false;
		if (!validateParameterTypes(methodObj, errorNode))
			return false;
		return true;
	}

	private boolean validateMethodObject(Obj methodObj, SyntaxNode errorNode) {
		if (methodObj.getKind() != Obj.Meth) {
			report_error("'" + methodObj.getName() + "' nije funkcija", errorNode);
			return false;
		}
		return true;
	}

	private boolean validateParameterCount(Obj methodObj, SyntaxNode errorNode) {
		if (methodObj.getLevel() != actualParamTypes.size()) {
			report_error(String.format("Pogrešan broj argumenata za funkciju '%s' (očekivano: %d, dobijeno: %d)",
					methodObj.getName(), methodObj.getLevel(), actualParamTypes.size()), errorNode);
			return false;
		}
		return true;
	}

	private boolean validateParameterTypes(Obj methodObj, SyntaxNode errorNode) {
		Iterator<Obj> formalIt = methodObj.getLocalSymbols().iterator();

		for (int i = actualParamTypes.size() - 1; i >= 0; i--) {
			Struct actualType = actualParamTypes.get(i);
			Struct formalType = formalIt.next().getType();

			if (!actualType.compatibleWith(formalType)) {
				report_error("Tip argumenta se ne poklapa sa tipom parametra u funkciji '" + methodObj.getName() + "'",
						errorNode);
				return false;
			}
		}
		return true;
	}
}