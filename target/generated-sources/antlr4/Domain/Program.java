package Domain;

import java.util.List;

import Domain.Decl.Declaration;
import item2.ASTVisitor;

public class Program extends MiniCNode{
	public List<Declaration> decls;
	
	public Program(List<Declaration> decls){
		this.decls = decls;
	}
	
	@Override
	public String toString(){
		return decls.stream()
					.map(t -> t.toString() + "\n")
					.reduce("", (acc, decl) -> acc + decl);
	}

	@Override
	public void accept(ASTVisitor v) {
		// TODO Auto-generated method stub
		v.visitProgram(this);
	}

	
}
