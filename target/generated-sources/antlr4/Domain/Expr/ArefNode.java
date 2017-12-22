package Domain.Expr;

import org.antlr.v4.runtime.tree.TerminalNode;

public class ArefNode extends Expression{
	public TerminalNode t_node;
	public Expression expr;
	
	public ArefNode(TerminalNode t_node, Expression expr) {
		super();
		this.t_node = t_node;
		this.expr = expr;
	}
	
	@Override
	public String toString(){
		return t_node.toString() + "[" + expr.toString() + "]";
	}
}
