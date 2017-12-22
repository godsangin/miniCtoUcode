package Domain.Expr;

public class BinaryOpNode extends Expression{	
	public Expression lhs;
	public Expression rhs;
	public String op;
	
	public BinaryOpNode(Expression lhs, String op, Expression rhs){
		super();
		this.lhs = lhs;
		this.op = op;
		this.rhs = rhs;
	}
	
	@Override
	public String toString(){
		return lhs.toString() + " " + op + " " + rhs.toString();
	}
}
