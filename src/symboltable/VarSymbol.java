package symboltable;

public class VarSymbol extends Symbol {
	String var_type;
	boolean initialized;
	
	public VarSymbol ()
	{
	}
	public VarSymbol (String name, String type, boolean init)
	{
		setName(name);
		var_type = type;
		initialized = init;
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
	
	public String toString() {return var_type + ": "+ sym_name;}
}
