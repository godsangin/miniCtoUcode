package item2;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import Domain.Program;

public class MiniCTest {
	public static void main(String[] args) throws Exception {
		MiniCLexer lexer = new MiniCLexer(new ANTLRFileStream("test.c"));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MiniCParser parser = new MiniCParser(tokens);
		ParseTree tree = parser.program();
		
		MiniCAstVisitor visitor = new MiniCAstVisitor();
		visitor.visit(tree).accept(new UcodeGenVisitor());
//		Program program = (Program) visitor.visit(tree);
//		System.out.println(program.toString());
	}

}
