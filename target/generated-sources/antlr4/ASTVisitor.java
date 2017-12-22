
import Domain.Program;
import Domain.Args.Arguments;
import Domain.Decl.Declaration;
import Domain.Expr.Expression;
import Domain.Param.Parameter;
import Domain.Stmt.Statement;
import Domain.Type_spec.TypeSpecification;


public interface ASTVisitor {
	public void visitProgram(Program node);
	public void visitDeclaration(Declaration node);
	public void visitArguments(Arguments node);
	public void visitExpression(Expression node);
	public void visitParameter(Parameter node);
	public void visitStatement(Statement node);
	public void visitType_spec(TypeSpecification node);
	
}
