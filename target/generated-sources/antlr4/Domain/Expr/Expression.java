package Domain.Expr;

import Domain.MiniCNode;
import item2.ASTVisitor;

public class Expression extends MiniCNode {

	@Override
	public void accept(ASTVisitor v) {
		// TODO Auto-generated method stub
		v.visitExpression(this);
	}
}
