package Domain.Decl;

import org.antlr.v4.runtime.tree.TerminalNode;

import Domain.MiniCNode;
import Domain.Type_spec.TypeSpecification;
import item2.ASTVisitor;

public class Local_Declaration extends MiniCNode{
	public TypeSpecification type;
	public TerminalNode lhs;
	
	public Local_Declaration(TypeSpecification type, TerminalNode lhs) {
		this.type = type;
		this.lhs = lhs;
	}
	
	@Override
	public String toString(){
		return type.toString() + " " + lhs.getText() + ";";
	}

	@Override
	public void accept(ASTVisitor v) {
		// TODO Auto-generated method stub
		v.visitLocal_Declaration(this);
	}
}
