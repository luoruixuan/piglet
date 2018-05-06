package symboltable;

public class VarSymbol extends Symbol {
	String var_type;
	boolean initialized;
	// for PIGLET
	int var_id; 
	
	public VarSymbol ()
	{
		var_id = -1; 
	}
	public VarSymbol (String name, String type, boolean init)
	{
		setName(name);
		var_type = type;
		initialized = init;
		var_id = -1; 
	}
	
	public void setType(String type)
	{
		var_type = type;
	}
	public String getType()
	{
		return var_type;
	}
	public boolean isInitialized()
	{
		return initialized;
	}
	public void initialize()
	{
		initialized = true;
	}
	
	// for PIGLET
	public void setID(int id) { var_id = id; }
	public int getID() { return var_id; }
	
	public String toString() {return var_type + ": "+ sym_name;}
}
