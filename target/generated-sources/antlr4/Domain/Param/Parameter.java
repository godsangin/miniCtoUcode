package Domain.Param;

import org.antlr.v4.runtime.tree.TerminalNode;

import Domain.MiniCNode;
import Domain.Type_spec.TypeSpecification;
import item2.ASTVisitor;

public class Parameter extends MiniCNode{
	public TypeSpecification type;
	public TerminalNode t_node;

	public Parameter(TypeSpecification type, TerminalNode t_node){
		this.type = type;
		this.t_node = t_node;
	}

	@Override
	public String toString(){
		return type.toString() + " " + t_node.toString();
	}

	@Override
	public void accept(ASTVisitor v) {
		// TODO Auto-generated method stub
		v.visitParameter(this);
	}
}
