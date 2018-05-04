package symboltable;
import syntaxtree.*;
import visitor.GJVoidDepthFirst;

public class BuildSymbolTableVisitor extends GJVoidDepthFirst<Symbol>{

	   private String parseType(Type n) {
		   Node c = n.f0.choice;
		   String t = c.getClass().toString();
		   if (t.equals("class syntaxtree.ArrayType")) return "int*";
		   if (t.equals("class syntaxtree.Identifier")) return ((Identifier)c).f0.toString();
		   if (t.equals("class syntaxtree.IntegerType")) return ((IntegerType)c).f0.toString();
		   if (t.equals("class syntaxtree.BooleanType")) return ((BooleanType)c).f0.toString();
		   return null;
	   }
	
	
	   //public void visit(NodeList n, Symbol argu);
	   //public void visit(NodeListOptional n, Symbol argu);
	   //public void visit(NodeOptional n, Symbol argu);
	   //public void visit(NodeSequence n, Symbol argu);
	   //public void visit(NodeToken n, Symbol argu);

	   //
	   // User-generated visitor methods below
	   //

	   /**
	    * f0 -> MainClass()
	    * f1 -> ( TypeDeclaration() )*
	    * f2 -> <EOF>
	    */
	   //public void visit(Goal n, Symbol argu);

	   /**
	    * f0 -> "class"
	    * f1 -> Identifier()
	    * f2 -> "{"
	    * f3 -> "public"
	    * f4 -> "static"
	    * f5 -> "void"
	    * f6 -> "main"
	    * f7 -> "("
	    * f8 -> "String"
	    * f9 -> "["
	    * f10 -> "]"
	    * f11 -> Identifier()
	    * f12 -> ")"
	    * f13 -> "{"
	    * f14 -> ( VarDeclaration() )*
	    * f15 -> ( Statement() )*
	    * f16 -> "}"
	    * f17 -> "}"
	    */
	   public void visit(MainClass n, Symbol argu) {
		   SymbolTable ST = (SymbolTable)argu;
		   ClassSymbol C = new ClassSymbol(n.f1.f0.toString(), "Object");
		   MethodSymbol M = new MethodSymbol("main", "void");
		   M.addArg(new VarSymbol(n.f11.f0.toString(), "String*", true));
		   ST.mainclass = n.f1.f0.toString();
		   n.f14.accept(this, (Symbol)M);
		   C.addMethod(M);
		   ST.addClass(n.f1.f0.toString(), C);
	   }

	   /**
	    * f0 -> ClassDeclaration()
	    *       | ClassExtendsDeclaration()
	    */
	   //public void visit(TypeDeclaration n, Symbol argu);

	   /**
	    * f0 -> "class"
	    * f1 -> Identifier()
	    * f2 -> "{"
	    * f3 -> ( VarDeclaration() )*
	    * f4 -> ( MethodDeclaration() )*
	    * f5 -> "}"
	    */
	   public void visit(ClassDeclaration n, Symbol argu) {
		   SymbolTable ST = (SymbolTable)argu;
		   String name = n.f1.f0.toString();
		   ClassSymbol C = new ClassSymbol(name, "Object");
		   n.f3.accept(this, (Symbol)C);
		   n.f4.accept(this, (Symbol)C);
		   ST.addClass(name, C);
	   }

	   /**
	    * f0 -> "class"
	    * f1 -> Identifier()
	    * f2 -> "extends"
	    * f3 -> Identifier()
	    * f4 -> "{"
	    * f5 -> ( VarDeclaration() )*
	    * f6 -> ( MethodDeclaration() )*
	    * f7 -> "}"
	    */
	   public void visit(ClassExtendsDeclaration n, Symbol argu) {
		   SymbolTable ST = (SymbolTable)argu;
		   String name = n.f1.f0.toString();
		   ClassSymbol C = new ClassSymbol(name, n.f3.f0.toString());
		   n.f5.accept(this, (Symbol)C);
		   n.f6.accept(this, (Symbol)C);
		   ST.addClass(name, C);
	   }

	   /**
	    * f0 -> Type()
	    * f1 -> Identifier()
	    * f2 -> ";"
	    */
	   public void visit(VarDeclaration n, Symbol argu) {
		   if (argu.getClass().toString().equals("class symboltable.ClassSymbol")) {
			   ((ClassSymbol)argu).addVar(new VarSymbol(n.f1.f0.toString(), parseType(n.f0), true));
		   }
		   else {
			   ((MethodSymbol)argu).addVar(new VarSymbol(n.f1.f0.toString(), parseType(n.f0), false));
		   }
	   }

	   /**
	    * f0 -> "public"
	    * f1 -> Type()
	    * f2 -> Identifier()
	    * f3 -> "("
	    * f4 -> ( FormalParameterList() )?
	    * f5 -> ")"
	    * f6 -> "{"
	    * f7 -> ( VarDeclaration() )*
	    * f8 -> ( Statement() )*
	    * f9 -> "return"
	    * f10 -> Expression()
	    * f11 -> ";"
	    * f12 -> "}"
	    */
	   public void visit(MethodDeclaration n, Symbol argu) {
		   String name = n.f2.f0.toString();
		   MethodSymbol M = new MethodSymbol(name, parseType(n.f1));
		   n.f4.accept(this, (Symbol)M);
		   n.f7.accept(this, (Symbol)M);
		   ((ClassSymbol)argu).addMethod(M);
	   }

	   /**
	    * f0 -> FormalParameter()
	    * f1 -> ( FormalParameterRest() )*
	    */
	   // public void visit(FormalParameterList n, Symbol argu);
	   
	   /**
	    * f0 -> Type()
	    * f1 -> Identifier()
	    */
	   public void visit(FormalParameter n, Symbol argu) {
		   MethodSymbol M = (MethodSymbol)argu;
		   M.addArg(new VarSymbol(n.f1.f0.toString(), parseType(n.f0), true));
	   }

	   /**
	    * f0 -> ","
	    * f1 -> FormalParameter()
	    */
	   //public void visit(FormalParameterRest n, Symbol argu);

	   /**
	    * f0 -> ArrayType()
	    *       | BooleanType()
	    *       | IntegerType()
	    *       | Identifier()
	    */
	   //public void visit(Type n, Symbol argu);

	   /**
	    * f0 -> "int"
	    * f1 -> "["
	    * f2 -> "]"
	    */
	   //public void visit(ArrayType n, Symbol argu);

	   /**
	    * f0 -> "boolean"
	    */
	   //public void visit(BooleanType n, Symbol argu);

	   /**
	    * f0 -> "int"
	    */
	   //public void visit(IntegerType n, Symbol argu);

	   /**
	    * f0 -> Block()
	    *       | AssignmentStatement()
	    *       | ArrayAssignmentStatement()
	    *       | IfStatement()
	    *       | WhileStatement()
	    *       | PrintStatement()
	    */
	   //public void visit(Statement n, Symbol argu);

	   /**
	    * f0 -> "{"
	    * f1 -> ( Statement() )*
	    * f2 -> "}"
	    */
	   //public void visit(Block n, Symbol argu);

	   /**
	    * f0 -> Identifier()
	    * f1 -> "="
	    * f2 -> Expression()
	    * f3 -> ";"
	    */
	   //public void visit(AssignmentStatement n, Symbol argu);

	   /**
	    * f0 -> Identifier()
	    * f1 -> "["
	    * f2 -> Expression()
	    * f3 -> "]"
	    * f4 -> "="
	    * f5 -> Expression()
	    * f6 -> ";"
	    */
	   //public void visit(ArrayAssignmentStatement n, Symbol argu);

	   /**
	    * f0 -> "if"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> Statement()
	    * f5 -> "else"
	    * f6 -> Statement()
	    */
	   //public void visit(IfStatement n, Symbol argu);

	   /**
	    * f0 -> "while"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> Statement()
	    */
	   //public void visit(WhileStatement n, Symbol argu);

	   /**
	    * f0 -> "System.out.println"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> ";"
	    */
	   //public void visit(PrintStatement n, Symbol argu);

	   /**
	    * f0 -> AndExpression()
	    *       | CompareExpression()
	    *       | PlusExpression()
	    *       | MinusExpression()
	    *       | TimesExpression()
	    *       | ArrayLookup()
	    *       | ArrayLength()
	    *       | MessageSend()
	    *       | PrimaryExpression()
	    */
	   //public void visit(Expression n, Symbol argu);

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "&&"
	    * f2 -> PrimaryExpression()
	    */
	   //public void visit(AndExpression n, Symbol argu);

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "<"
	    * f2 -> PrimaryExpression()
	    */
	   //public void visit(CompareExpression n, Symbol argu);

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "+"
	    * f2 -> PrimaryExpression()
	    */
	   //public void visit(PlusExpression n, Symbol argu);

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "-"
	    * f2 -> PrimaryExpression()
	    */
	   //public void visit(MinusExpression n, Symbol argu);

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "*"
	    * f2 -> PrimaryExpression()
	    */
	   //public void visit(TimesExpression n, Symbol argu);

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "["
	    * f2 -> PrimaryExpression()
	    * f3 -> "]"
	    */
	   //public void visit(ArrayLookup n, Symbol argu);

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> "length"
	    */
	   //public void visit(ArrayLength n, Symbol argu);

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> Identifier()
	    * f3 -> "("
	    * f4 -> ( ExpressionList() )?
	    * f5 -> ")"
	    */
	   //public void visit(MessageSend n, Symbol argu);

	   /**
	    * f0 -> Expression()
	    * f1 -> ( ExpressionRest() )*
	    */
	   //public void visit(ExpressionList n, Symbol argu);

	   /**
	    * f0 -> ","
	    * f1 -> Expression()
	    */
	   //public void visit(ExpressionRest n, Symbol argu);

	   /**
	    * f0 -> IntegerLiteral()
	    *       | TrueLiteral()
	    *       | FalseLiteral()
	    *       | Identifier()
	    *       | ThisExpression()
	    *       | ArrayAllocationExpression()
	    *       | AllocationExpression()
	    *       | NotExpression()
	    *       | BracketExpression()
	    */
	   //public void visit(PrimaryExpression n, Symbol argu);

	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   //public void visit(IntegerLiteral n, Symbol argu);

	   /**
	    * f0 -> "true"
	    */
	   //public void visit(TrueLiteral n, Symbol argu);

	   /**
	    * f0 -> "false"
	    */
	   //public void visit(FalseLiteral n, Symbol argu);

	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   //public void visit(Identifier n, Symbol argu);

	   /**
	    * f0 -> "this"
	    */
	   //public void visit(ThisExpression n, Symbol argu);

	   /**
	    * f0 -> "new"
	    * f1 -> "int"
	    * f2 -> "["
	    * f3 -> Expression()
	    * f4 -> "]"
	    */
	   //public void visit(ArrayAllocationExpression n, Symbol argu);

	   /**
	    * f0 -> "new"
	    * f1 -> Identifier()
	    * f2 -> "("
	    * f3 -> ")"
	    */
	   //public void visit(AllocationExpression n, Symbol argu);

	   /**
	    * f0 -> "!"
	    * f1 -> Expression()
	    */
	   //public void visit(NotExpression n, Symbol argu);

	   /**
	    * f0 -> "("
	    * f1 -> Expression()
	    * f2 -> ")"
	    */
	   //public void visit(BracketExpression n, Symbol argu);
}