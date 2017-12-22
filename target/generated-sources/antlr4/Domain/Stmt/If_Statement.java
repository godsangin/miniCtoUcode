package Domain.Stmt;


import java.util.Optional;

import org.antlr.v4.runtime.tree.TerminalNode;

import Domain.Expr.Expression;
import item2.ASTVisitor;

public class If_Statement extends Statement{
	public TerminalNode ifnode;
	public TerminalNode elsenode;
	public Expression expr;
	public Statement if_stmt;
	public Statement else_stmt;
	
	public If_Statement(TerminalNode ifnode, Expression expr,  Statement if_stmt){
		super();
		this.ifnode = ifnode;
		this.expr = expr;
		this.if_stmt = if_stmt;
	}
	
	public If_Statement(TerminalNode ifnode, Expression expr,  Statement if_stmt, TerminalNode elsenode, Statement else_stmt){
		this(ifnode, expr, if_stmt);
		this.elsenode = elsenode;
		this.else_stmt = else_stmt;
	}
	
	@Override
	public String toString(){
		Optional<TerminalNode> result = Optional.ofNullable(elsenode);
		return result.map(t -> ifnode.getText() + "(" + expr.toString() + ")\n" + if_stmt.toString() +"\n"+ elsenode.getText() + "\n" + else_stmt.toString())
		.orElse(ifnode.getText() + "(" + expr.toString() + ")\n" + if_stmt.toString());
	}

	@Override
	public void accept(ASTVisitor v) {
		// TODO Auto-generated method stub
		v.visitIf_Statement(this);
	}
}
