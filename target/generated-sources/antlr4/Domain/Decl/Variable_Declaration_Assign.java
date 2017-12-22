package Domain.Decl;

import org.antlr.v4.runtime.tree.TerminalNode;

import Domain.Type_spec.TypeSpecification;

public class Variable_Declaration_Assign extends Variable_Declaration{
	public TerminalNode rhs;
	
	public Variable_Declaration_Assign(TypeSpecification type, TerminalNode lhs, TerminalNode rhs){
		super(type,lhs);
		this.rhs = rhs;
	}
	
	@Override
	public String toString(){
		return type.toString() + " " + lhs.getText() + " = " + rhs.getText() + ";";
	}
}
