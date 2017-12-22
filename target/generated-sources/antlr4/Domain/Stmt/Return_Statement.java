package Domain.Stmt;

import java.util.Optional;

import org.antlr.v4.runtime.tree.TerminalNode;

import Domain.Expr.Expression;
import item2.ASTVisitor;

public class Return_Statement extends Statement{
	public TerminalNode return_node;
	public Expression expr;
	
	public Return_Statement(TerminalNode return_node){
		super();
		this.return_node = return_node;
	}
	
	public Return_Statement(TerminalNode return_node, Expression expr){
		this(return_node);
		this.expr = expr;
	}

	@Override
	public String toString(){
		Optional<Expression> result = Optional.ofNullable(expr);
		return result.map(t -> return_node.getText() + " " + expr.toString() + ";")
				.orElse(return_node.getText() + ";");
	}

	@Override
	public void accept(ASTVisitor v) {
		// TODO Auto-generated method stub
		v.visitReturn_Statement(this);
	}
}
