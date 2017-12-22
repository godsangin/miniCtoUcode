package Domain.Expr;

public class ParenExpression extends Expression{
	Expression expr;
	
	public ParenExpression(Expression expr){
		this.expr = expr;
	}
	
	@Override
	public String toString(){
		return "(" + expr.toString() + ")";
	}
}
