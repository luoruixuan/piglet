package symboltable;
import java.util.*;


public class SymbolTable extends Symbol{
	public Hashtable<String, ClassSymbol> classes;
	public Hashtable<String, Integer> degree;
	String mainclass;
	
	// For type check
	public String presentClass, presentMethod;
	// For PIGLET
	public int default_indent;
	public String ansID = "TEMP 1";
	public String tmpID = "TEMP 2";
    public String stkID = "TEMP 3";
    public String sttID = "TEMP 4";
	int var_id = 5;
	int label_id = 0;
	public Hashtable<VarSymbol, Integer> varID;
	
	// constructer
	public SymbolTable() {
		ClassSymbol O = new ClassSymbol("Object", null);
		mainclass = null;
		classes = new Hashtable<String, ClassSymbol>();
		varID = new Hashtable<VarSymbol, Integer>();
		classes.put("Object", O);
	}
	public void setStatus(String C, String M) {
		presentClass = C;
		presentMethod = M;
	}
	public void setClass(String C) {
		presentClass = C;
	}
	public void setMethod(String M) {
		presentMethod = M;
	}
	
	private VarSymbol getVarSymbol(String var) {
		ClassSymbol cls = classes.get(presentClass);
		MethodSymbol method = cls.cls_method.get(presentMethod);
		if (method.hasVar(var))
			return method.local_var.get(var);
		
		while(!cls.getName().equals("Object")) {
			if (cls.hasVar(var))
				return cls.getVar(var);
			cls = cls.getSuper();
		}
		System.out.println("Using variable \""+var+"\" before defination.");
		System.exit(0);
		return null;
	}
	
	public String getType(String var) {
		VarSymbol varsym = getVarSymbol(var);
		return varsym.getType();
	}
	
	public boolean varIsInitialized(String var) {
		VarSymbol varsym = getVarSymbol(var);
		return varsym.isInitialized();
	}
	
	public void varInitialize(String var) {
		VarSymbol varsym = getVarSymbol(var);
		varsym.initialize();
	}
	
	public void addClass(String C, ClassSymbol cls) {
		if (classes.containsKey(C)) {
			System.out.println("Duplicate class defination "+C);
			System.exit(0);
		}
		classes.put(C, cls);
	}
	
	public boolean hasClasses(String C) {
		if (classes.containsKey(C)) return true;
		if (C.equals("int")) return true;
		if (C.equals("boolean")) return true;
		if (C.equals("int*")) return true;
		return false;
	}
	public ClassSymbol getLca(String C1, String C2) {
		if (!hasClasses(C1)) {
			System.out.println("BUG: No such class symbol: "+C1);
			System.exit(1);
		}
		if (!hasClasses(C2)) {
			System.out.println("BUG: No such class symbol: "+C2);
			System.exit(1);
		}
		
		ClassSymbol cls1, cls2;
		cls1 = classes.containsKey(C1) ? classes.get(C1) : classes.get("Object");
		cls2 = classes.containsKey(C2) ? classes.get(C2) : classes.get("Object");
		while(cls1.cls_depth > cls2.cls_depth) cls1 = cls1.getSuper();
		while(cls1.cls_depth < cls2.cls_depth) cls2 = cls2.getSuper();
		if (cls1.cls_depth != cls2.cls_depth || !cls1.getName().equals(cls2.getName())) {
			System.out.println("BUG: Class "+C1+" and class "+C2+" are not in the same tree!");
			System.exit(1);	
		}
		return cls1;
	}
	
	public boolean isAnsistor(String C1, String C2) {
		if (C1.equals(C2)) return true;
		ClassSymbol lca = getLca(C1, C2);
		return lca.getName().equals(C1);
	}
	
	private MethodSymbol getMethodSymbol(ClassSymbol cls, String M) { 
		while(!cls.getName().equals("Object")) {
			if (cls.cls_method.containsKey(M))
				return cls.cls_method.get(M);
			cls = cls.getSuper();
		}
		System.out.println("Using method \""+M+"\" before defination.");
		System.exit(0);
		return null;
	}
	public boolean checkMethodArgsType(String C, String M, String Arg_types) {
		ClassSymbol cls = classes.get(C);
		MethodSymbol method = getMethodSymbol(cls, M);

		if (Arg_types.equals("")) Arg_types = " ";
		String[] arg_types = Arg_types.split(" ");
		int length = arg_types.length;
		if (length != method.argSize()) return false;
		for (int i = 0; i < length; i++)
			if (!isAnsistor(method.argElementAt(i), arg_types[i])) return false;
		return true;
	}
	public String getMethodType(String C, String M) {
		ClassSymbol cls = classes.get(C);
		MethodSymbol method = getMethodSymbol(cls, M);
		return method.getType();
	}
	
	public void createSymbolTree() {
		Enumeration<ClassSymbol> i = classes.elements();
		while(i.hasMoreElements()) {
			ClassSymbol temp = i.nextElement();
			if (temp.getName().equals("Object")) continue;
			ClassSymbol Super = classes.get(temp.cls_super_name);
			temp.setSuper(Super);
		}
		
		i = classes.elements();
		while(i.hasMoreElements()) {
			ClassSymbol temp = i.nextElement();
			if (!temp.TryAccessObject()) {
				System.out.println("Cyclic inheritance found in class \""+temp.getName()+"\"");
				System.exit(0);
			}
		}
		
		i = classes.elements();
		while(i.hasMoreElements()) {
			ClassSymbol temp = i.nextElement();
			if (!temp.CheckOverLoading()) {
				System.out.println("Function overloaded.");
				System.exit(0);
			}
		}
	}
	
	// To PIGLET
	public void pigletMainInit() {
		System.out.println("MOVE "+stkID+" HALLOCATE 512");
	}
	
	public String getMethodName() {
		ClassSymbol cls = classes.get(presentClass);
		MethodSymbol method = getMethodSymbol(cls, presentMethod);
		return "METHOD_"+cls.getName()+"_"+method.getName()+"[1]";
	}

	public String getMethodName(String class_name, String method_name) {
		ClassSymbol cls = classes.get(class_name);
		MethodSymbol method = getMethodSymbol(cls, method_name);
		return "METHOD_"+cls.getName()+"_"+method.getName()+"[1]";
    }
	
	public void println(String s) {
		for (int i = 0; i < default_indent; i++)
			s = " " + s;
		System.out.println(s);
	}
	
	public void addIndent(int x) {
		default_indent += x;
		if (default_indent < 0) {
			System.out.println("BUG: invalid indent after add: "+default_indent);
			System.exit(1);
		}
	}
	
	public void setIndent(int x) {
		default_indent = x;
		if (default_indent < 0) {
			System.out.println("BUG: invalid indent when set: "+default_indent);
			System.exit(1);
		}
	}
	
	public String getID(String var) {
		VarSymbol varsym = getVarSymbol(var);
		int ans = -1;
		
		if(varID.containsKey(varsym))
			ans = varID.get(varsym); 
		else {
			ans = var_id;
			varID.put(varsym, var_id);
			var_id++;
		}
		
		return "TEMP "+ans;
	}
	
	public boolean isMember(String var) {
		ClassSymbol cls = classes.get(presentClass);
		return cls.hasVar(var);
	}

	public void push(String reg) {
        println("HSTORE "+stkID+" 0 "+reg);
        println("MOVE "+stkID+" 4");
	}
	public void pop(String reg) {
        println("HLOAD "+reg+" "+stkID+" 0");
        println("MOVE "+stkID+" -4");
	}
	
	public String getLabel() {
		label_id++;
		return "JUMP_LABEL_"+label_id;
	}
	
	public void save() {
		ClassSymbol cls = classes.get(presentClass);
		MethodSymbol method= getMethodSymbol(cls, presentMethod);
		Enumeration<VarSymbol> i = method.varElements();
		
		while(i.hasMoreElements()) {
			VarSymbol var = i.nextElement();
			push(getID(var.getName()));
		}
	}
	
	public void call(String cls, String method, String explst) {
        String func = getMethodName(cls, method);
		ClassSymbol cls_sym = classes.get(cls);
		MethodSymbol method_sym = getMethodSymbol(cls_sym, method);
		
		pop("TEMP 0");
		for (int i = method_sym.argSize()-1; i >= 0; --i)
			pop(getID(method_sym.args_var.get(i).getName()));

	    println("CALL "+func+" ( TEMP 0 )");
	}
	
	public void restore() {
		ClassSymbol cls = classes.get(presentClass);
		MethodSymbol method= getMethodSymbol(cls, presentMethod);
		Vector<String> var_lst = new Vector<String>();
		Enumeration<VarSymbol> i = method.varElements();
		
		while(i.hasMoreElements()) {
			VarSymbol var = i.nextElement();
			var_lst.addElement(var.getName());
		}
		
		for (int idx = var_lst.size()-1; idx >= 0; --idx)
			pop(getID(var_lst.get(idx)));
	}

	public int sizeof(String class_name) {
		ClassSymbol cls = classes.get(class_name);
		return cls.sizeof();
	}
	
	public int getOffset(String var) {
		ClassSymbol cls = classes.get(presentClass);
		if (!cls.hasVar(var)) {
			System.out.println("BUG: invalid var symbol "+var+" in class: "+presentClass);
			System.exit(1);
		}
		return cls.getOffset(var);
	}
	
	// just for debug
	public String toString() {
		String ret = "";
		Enumeration<ClassSymbol> i = classes.elements();
		while(i.hasMoreElements()){
			ClassSymbol C = i.nextElement();
			ret = ret + C + "\n";
		}
		return ret;
	}

}

