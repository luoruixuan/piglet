package symboltable;
import java.util.*;

public class MethodSymbol extends Symbol {
	String method_type;
	Vector<String> args_type;
	Hashtable<String, VarSymbol> local_var;
	
	MethodSymbol()
	{
		args_type = new Vector<String>();
		local_var = new Hashtable<String, VarSymbol>();
	}
	MethodSymbol(String name, String ret_type)
	{
		args_type = new Vector<String>();
		local_var = new Hashtable<String, VarSymbol>();
		setName(name);
		setType(ret_type);
	}
	
	void setType(String ret_type) { method_type = ret_type; }
	String getType() { return method_type; }
	
	void addArg(VarSymbol var)
	{
		if (local_var.containsKey(var.getName())) {
			System.out.println("Variable "+var.getName()+" duplicate defination.");
			System.exit(0);
		}
		args_type.addElement(var.getType());
		local_var.put(var.getName(), var);
	}
	public Enumeration<String> argElements() { return args_type.elements(); }
	public String argElementAt(int i) { return args_type.elementAt(i); }
	public int argSize() { return args_type.size(); }
	
	public void addVar(VarSymbol var) { 
		if (local_var.containsKey(var.getName())) {
			System.out.println("Variable "+var.getName()+" duplicate defination.");
			System.exit(0);
		}
		local_var.put(var.getName(), var); 
	}
	public Enumeration<VarSymbol> varElements() { return local_var.elements(); }
	public boolean hasVar(String var) {
		return local_var.containsKey(var);
	}
	
	// just for debug
	public String toString() {
		String ret = method_type + " " + sym_name + "(";
		Enumeration<String> i = argElements();
		while (i.hasMoreElements()) {
			ret = ret + i.nextElement() + ", ";
		}
		ret = ret.substring(0,ret.length())+")\n{\n";
		Enumeration<VarSymbol> j = varElements();
		while (j.hasMoreElements()) {
			ret = ret + j.nextElement() + "\n";
		}
		return ret+"}";
	}
}
