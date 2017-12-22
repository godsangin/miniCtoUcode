package Domain.Expr;

import org.antlr.v4.runtime.tree.TerminalNode;

import Domain.Args.Arguments;

public class FuncallNode extends Expression{
	public TerminalNode t_node;
	public Arguments args;
	
	public FuncallNode(TerminalNode t_node, Arguments args) {
		super();
		this.t_node = t_node;
		this.args = args;
	}
	
	@Override
	public String toString(){
		return t_node.toString() + "(" + args.toString() +")";
	}
}
