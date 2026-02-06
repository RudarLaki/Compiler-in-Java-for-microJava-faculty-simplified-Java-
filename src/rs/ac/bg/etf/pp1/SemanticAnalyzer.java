package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {

	boolean errorDetected = false;
	int printCallCount = 0;
	Obj currentMethod = null;
	Obj currentEnum = null;
	boolean returnFound = false;
	int nVars;
	Obj programScope;
	Struct boolType = new Struct(Struct.Bool);
	Struct enumType = new Struct(Struct.Enum);

	Logger log = Logger.getLogger(getClass());
	private Struct currentType;
	private int constantValue;
	private Struct constantType;
	private boolean mainExists;
	private boolean params = false;
	private int enumCounter;

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
		varObj.setFpPos(1);
		currentMethod.setLevel(currentMethod.getLevel() + 1);
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

		Obj varObj = Tab.insert(Obj.Var, name, currentType);
		varObj.setFpPos(1);
		currentMethod.setLevel(currentMethod.getLevel() + 1);
	}

	@Override
	public void visit(ConstDecl constDecl) {
		String name = constDecl.getI1();
		if (Tab.find(name) != Tab.noObj) {
			report_error("Simbol " + name + " je vec deklarisan", constDecl);
			return;
		}
		if (currentType.getKind() != constantType.getKind()) {
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

	public void visit(MethodName methodName) {
		if (Tab.currentScope().findSymbol(methodName.getI1()) != null) {
			report_error("Vec definisan simbol u istom scope-u " + methodName.getI1(), methodName);
			return;
		}
		if (methodName.getI1().equals("main")) {
			mainExists = true;
		}
		currentMethod = Tab.insert(Obj.Meth, methodName.getI1(), currentType);
		Tab.openScope();
	}

	public void visit(ParamsList_param paramsList_param) {
		params = true;
	}

	public void visit(EnumDecl enumDecl) {
		Tab.chainLocalSymbols(currentEnum);
		Tab.closeScope();
	}
	
	public void visit(EnumName enumName) {
		if (Tab.currentScope().findSymbol(enumName.getI1()) != null) {
			report_error("Vec definisan simbol u istom scope-u " + enumName.getI1(), enumName);
			return;
		}
		currentEnum = Tab.insert(Obj.Type, enumName.getI1(), enumType);
		Tab.openScope();      // scope for enum items
	    enumCounter = 0;
	}

	@Override
	public void visit(IdentEnumItem item) {
	    String name = item.getI1();

	    if (Tab.currentScope().findSymbol(name) != null) {
	        report_error("Enum item " + name + " vec postoji", item);
	        return;
	    }

	    Obj con = Tab.insert(Obj.Con, name, Tab.intType);
	    con.setAdr(enumCounter);

	    enumCounter++;
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

	    enumCounter = value + 1;  // next automatic value
	}


	@Override
	public void visit(VoidPrefix voidPrefix) {
		currentType = Tab.noType;
	}
}