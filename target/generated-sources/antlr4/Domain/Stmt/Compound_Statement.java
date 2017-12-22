package Domain.Stmt;

import java.util.List;

import Domain.Decl.Local_Declaration;
import item2.ASTVisitor;

public class Compound_Statement extends Statement {
	public List<Local_Declaration> local_decls;
	public List<Statement> stmts;

	public Compound_Statement(List<Local_Declaration> local_decls, List<Statement> stmts) {
		super();
		this.local_decls = local_decls;
		this.stmts = stmts;
	}

	@Override
	public String toString(){
		return "{\n" + local_decls.stream().map(t -> t.toString() + "\n").reduce("",(acc, decl) -> acc + decl)
				+ stmts.stream().map(t -> t.toString() + "\n").reduce("",(acc, decl) -> acc + decl) + "}";
	}

	@Override
	public void accept(ASTVisitor v) {
		// TODO Auto-generated method stub
		v.visitCompound_Statement(this);
	}
}
