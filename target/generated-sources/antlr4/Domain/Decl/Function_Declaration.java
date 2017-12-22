package Domain.Decl;

import org.antlr.v4.runtime.tree.TerminalNode;

import Domain.Param.Parameters;
import Domain.Stmt.Compound_Statement;
import Domain.Type_spec.TypeSpecification;
import item2.ASTVisitor;

public class Function_Declaration extends Declaration{
	public TypeSpecification type;
	public TerminalNode t_node;
	public Parameters params;
	public Compound_Statement compount_stmt;

	public Function_Declaration(TypeSpecification type, TerminalNode t_node, Parameters params, Compound_Statement compound_stmt) {
		super();
		this.type = type;
		this.t_node = t_node;
		this.params = params;
		this.compount_stmt = compound_stmt;
	}
	
	@Override
	public String toString(){
		return type.toString() + " " + t_node.toString() + "(" + params.toString() + ")"
				+ "\n" + compount_stmt.toString();
	}

	@Override
	public void accept(ASTVisitor v) {
		// TODO Auto-generated method stub
		v.visitFunction_Declaration(this);
	}
}
