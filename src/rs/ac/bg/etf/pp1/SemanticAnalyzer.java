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
	public int nVars;
	private boolean mainExists = false;
	private boolean inFor = false;
	private boolean inSwitch = false;

	private Obj chrObj;

	private Obj ordObj;

	private Obj lenObj;

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
		chrObj = Tab.find("chr");
		ordObj = Tab.find("ord");
		lenObj = Tab.find("len");
		Tab.openScope();
	}

	@Override
	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();
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
		type.struct = currentType;
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
		if (currentMethod.getName().equals("main") && currentMethod.getLevel() != 0) {
			report_error("Metoda 'main' ne sme imati parametre", methodDecl);
			return;
		}
		if (currentMethod.getType() != Tab.noType && returnFound == false) {
			report_error("Metoda '" + currentMethod.getName() + "' nije void i nema return iskaz", methodDecl);
			return;
		}
		Tab.chainLocalSymbols(currentMethod);

		Tab.closeScope();

		currentMethod = null;
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
		methodName.obj = currentMethod;
		params = true;
		returnFound = false;
		Tab.openScope();
	}

	@Override
	public void visit(ParamsList_e paramsList_e) {
		params = false;
	}

	public void visit(ParamsList_epsilon paramsList_epsilon) {
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
		if (value < 0) {
			report_error("Enum vrednost ne sme biti negativna", item);
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
		if (desObj == chrObj || desObj == ordObj || desObj == lenObj) {
			report_error("Standardna metoda '" + desObj.getName() + "' mora biti pozvana sa parametrom",
					designatorFactor_e);
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
		if (desObj == chrObj || desObj == ordObj || desObj == lenObj) {
			// Ovo je poziv standardne metode bez parametara - greška
			report_error("Standardna metoda '" + desObj.getName() + "' zahteva parametar", designatorFactor_func);
			designatorFactor_func.struct = Tab.noType;
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
	public void visit(MinusFactor minusFactor) {
		if (!minusFactor.getFactor().struct.compatibleWith(Tab.intType)) {
			report_error("Operator '-' može se primeniti samo na celobrojne vrednosti", minusFactor);
			minusFactor.struct = Tab.noType;
			return;
		}
		minusFactor.struct = Tab.intType;
		return;
	}

	@Override
	public void visit(DesignatorActFactor_func designatorActFactor_func) {

		Obj desObj = designatorActFactor_func.getDesignator().obj;

		if (desObj.getKind() != Obj.Meth) {
			report_error("Poziv funkcije zahteva identifikator metode", designatorActFactor_func);
			actualParamTypes.clear();
			designatorActFactor_func.struct = Tab.noType;
			return;
		}
		String methodName = desObj.getName();
		if (desObj == chrObj || desObj == ordObj || desObj == lenObj) {
			checkStandardMethodCall(desObj, designatorActFactor_func);
			designatorActFactor_func.struct = desObj.getType();
		}
		boolean bool = checkFunctionCall(desObj, designatorActFactor_func);
		if (bool)
			designatorActFactor_func.struct = designatorActFactor_func.getDesignator().obj.getType();
		else
			designatorActFactor_func.struct = Tab.noType;
		return;
	}

	@Override
	public void visit(Term_e term_e) {
		term_e.struct = term_e.getFactor().struct;
	}

	@Override
	public void visit(Term_array term_array) {
		Struct term = term_array.getTerm().struct;
		Struct factor = term_array.getFactor().struct;
		if (!term.compatibleWith(Tab.intType) || !factor.compatibleWith(Tab.intType)) {
			report_error("Operandi moraju biti celobrojni tipovi", term_array);
			term_array.struct = Tab.noType;
			return;
		}
		term_array.struct = Tab.intType;
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
	public void visit(SimpleDesignator simpleDesignator) {
		Obj varObj = Tab.find(simpleDesignator.getI1());
		if (varObj == Tab.noObj) {
			report_error("Nedeklarisan identifikator: '" + simpleDesignator.getI1() + "'", simpleDesignator);
			simpleDesignator.obj = Tab.noObj;
			return;
		}
		simpleDesignator.obj = varObj;
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
	public void visit(ArrayDesignator arrayDesignator) {
		Obj arrObj = arrayDesignator.getDesignatorArrayName().obj;
		if (arrObj == Tab.noObj) {
			arrayDesignator.obj = Tab.noObj;
		} else if (!arrayDesignator.getExpression().struct.compatibleWith(Tab.intType)) {
			report_error("Indeks niza mora biti celobrojne vrednosti", arrayDesignator);
			arrayDesignator.obj = Tab.noObj;
		} else {
			arrayDesignator.obj = new Obj(Obj.Elem, arrObj.getName() + "[$]", arrObj.getType().getElemType());
		}
	}

	@Override
	public void visit(DotDesignator dotDesignator) {
		Obj desObj = dotDesignator.getDesignator().obj;
		DotDesignatorOp dotDes = dotDesignator.getDotDesignatorOp();

		if (desObj.equals(Tab.noObj)) {
			report_error("Nevalidan designator", dotDesignator);
			dotDesignator.obj = Tab.noObj;
			return;
		}

		if (dotDes instanceof LengthDesignatorOp) {
			if (desObj.getType().getKind() != Struct.Array) {
				report_error("Atribut '.length' je dostupan samo za nizove", dotDes);
				dotDesignator.obj = Tab.noObj;
				return;
			}
			dotDesignator.obj = new Obj(Obj.Var, "length", Tab.intType);
		} else if (dotDes instanceof IdentDesignatorOp) {
			if (desObj.getType().getKind() != Struct.Enum) {
				report_error("Pristup članovima je dozvoljen samo za enumeracije", dotDes);
				dotDesignator.obj = Tab.noObj;
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
				dotDesignator.obj = Tab.noObj;
				return;
			}
			dotDesignator.obj = enumVal;
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
	public void visit(Condition_OR condition_or) {
		Struct cond = condition_or.getConditionHelp().struct;
		Struct condTerm = condition_or.getCondTermHelp().struct;

		if (!condTerm.equals(boolType) || !cond.equals(boolType)) {
			report_error("Operandi logičkih operatora moraju biti logičkog tipa", condition_or);
			condition_or.struct = Tab.noType;
			return;
		}
		condition_or.struct = boolType;
	}

	@Override
	public void visit(CondTerm_AND condTerm_and) {
		Struct condFact = condTerm_and.getCondFact().struct;
		Struct condTerm = condTerm_and.getCondTerm().struct;

		if (!condFact.compatibleWith(boolType) || !condTerm.compatibleWith(boolType)) {
			report_error("Operandi logičkih operatora moraju biti logičkog tipa", condTerm_and);
			condTerm_and.struct = Tab.noType;
			return;
		}
		condTerm_and.struct = boolType;
	}
	
	@Override
	public void visit(Condition condition) {
		condition.struct = condition.getConditionHelp().struct;
	}

	@Override
	public void visit(Condition_e condition_e) {
		condition_e.struct = condition_e.getCondTermHelp().struct;
	}

	@Override
	public void visit(CondTerm_e condTerm_e) {
		condTerm_e.struct = condTerm_e.getCondFact().struct;
	}

	@Override
	public void visit(CondTermHelp condTermHelp) {
		condTermHelp.struct = condTermHelp.getCondTerm().struct;
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
			if (!(relop instanceof Relop_eq) && !(relop instanceof Relop_neq)) {
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
	public void visit(IfElseStatement_non_else ifStatement) {
		Struct cond = ifStatement.getCondition().struct;
		if (!cond.compatibleWith(boolType)) {
			report_error("Uslov u 'if' naredbi mora biti logičkog tipa", ifStatement);
		}
	}

	@Override
	public void visit(IfElseStatement_else ifElseUnmatched) {
		Struct cond = ifElseUnmatched.getCondition().struct;
		if (!cond.compatibleWith(boolType)) {
			report_error("Uslov u 'if' naredbi mora biti logičkog tipa", ifElseUnmatched);
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
		if (desObj == chrObj || desObj == ordObj || desObj == lenObj) {
			checkStandardMethodCall(desObj, paramsDesignatorStatement);
			actualParamTypes.clear();
			return;
		}

		checkFunctionCall(desObj, paramsDesignatorStatement);
		actualParamTypes.clear();
	}

	@Override
	public void visit(NonParamsDesignatorStatement nonParamsDesignatorStatement) {
		Obj desObj = nonParamsDesignatorStatement.getDesignator().obj;
		if (desObj.getKind() != Obj.Meth) {
			report_error("Poziv funkcije zahteva identifikator metode", nonParamsDesignatorStatement);
			actualParamTypes.clear();
			return;
		}
		String methodName = desObj.getName();
		if (desObj == chrObj || desObj == ordObj || desObj == lenObj) {
			// Standardne metode uvek zahtevaju parametar
			report_error("Standardna metoda '" + desObj.getName() + "' zahteva parametar",
					nonParamsDesignatorStatement);
		}

		checkFunctionCall(desObj, nonParamsDesignatorStatement);
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
	public void visit(ActPars_e actPars_e) {
		Struct expr = actPars_e.getExpression().struct;
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

		for (int i = 0; i < actualParamTypes.size(); i++) {
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

	////////////////////// provera za standardne metode
	////////////////////// //////////////////////////////
	private void checkStandardMethodCall(Obj methodObj, SyntaxNode errorNode) {
		String methodName = methodObj.getName();

		if (actualParamTypes.size() != 1) {
			report_error("Standardna metoda '" + methodName + "' zahteva tačno jedan parametar", errorNode);
			return;
		}

		Struct paramType = actualParamTypes.get(0);

		switch (methodName) {
		case "ord":
			if (!paramType.compatibleWith(Tab.charType)) {
				report_error("Metoda 'ord' zahteva parametar tipa char", errorNode);
			}
			break;

		case "chr":
			if (!paramType.compatibleWith(Tab.intType)) {
				report_error("Metoda 'chr' zahteva parametar tipa int", errorNode);
			}
			break;

		case "len":
			if (paramType.getKind() != Struct.Array) {
				report_error("Metoda 'len' zahteva parametar tipa niz", errorNode);
			}
			break;
		}
	}

	private Struct getStandardMethodReturnType(String methodName) {
		switch (methodName) {
		case "ord":
			return Tab.intType;
		case "chr":
			return Tab.charType;
		case "len":
			return Tab.intType;
		default:
			return Tab.noType;
		}
	}
}