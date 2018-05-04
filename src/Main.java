import syntaxtree.*;
import symboltable.*;
import java.io.FileInputStream;
import translate.TranslateVisitor;
import java.util.*;

public class Main{
	public static void main(String args[]) {
		//AutoTest tester = new AutoTest();
		//tester.run(15);
		//Scanner s = new Scanner(System.in);
		
		SymbolTable ST = new SymbolTable();
		BuildSymbolTableVisitor V = new BuildSymbolTableVisitor();
		try {
			FileInputStream in = new FileInputStream(args[0]);
			//String input_file = s.next();
			//FileInputStream in = new FileInputStream(input_file);
			Node root = new MiniJavaParser(in).Goal();
			root.accept(V, ST);
			ST.createSymbolTree();
			TranslateVisitor TV = new TranslateVisitor();
			root.accept(TV, ST);
			System.out.println("Accpet.");
		}catch (ParseException e){
			e.printStackTrace();
		}catch (TokenMgrError e){
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
}