package Domain.Stmt;

import Domain.Expr.Expression;
import item2.ASTVisitor;

public class Expression_Statement extends Statement{
	public Expression expr;

	public Expression_Statement(Expression expr) {
		super();
		this.expr = expr;
	}
	
	@Override
	public String toString(){
		return expr.toString() + ";";
	}

	@Override
	public void accept(ASTVisitor v) {
		// TODO Auto-generated method stub
		v.visitExpression_Statement(this);;
	}
}
