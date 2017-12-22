package Domain.Type_spec;

import Domain.MiniCNode;
import item2.ASTVisitor;

public class TypeSpecification extends MiniCNode {
	public final Type type;

	public enum Type {
		VOID, INT
	}

	public TypeSpecification(Type type) {
		this.type = type;
	}
	
	@Override
	public String toString(){
		return type.toString().toLowerCase();
	}

	@Override
	public void accept(ASTVisitor v) {
		// TODO Auto-generated method stub
		v.visitType_spec(this);
	}
}
