package item2;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.codegen.model.decl.Decl;

import Domain.MiniCNode;
import Domain.Program;
import Domain.Args.Arguments;
import Domain.Decl.Declaration;
import Domain.Decl.Function_Declaration;
import Domain.Decl.Local_Declaration;
import Domain.Decl.Local_Variable_Declaration_Array;
import Domain.Decl.Local_Variable_Declaration_Assign;
import Domain.Decl.Variable_Declaration;
import Domain.Decl.Variable_Declaration_Array;
import Domain.Decl.Variable_Declaration_Assign;
import Domain.Expr.ArefAssignNode;
import Domain.Expr.ArefNode;
import Domain.Expr.AssignNode;
import Domain.Expr.BinaryOpNode;
import Domain.Expr.Expression;
import Domain.Expr.FuncallNode;
import Domain.Expr.ParenExpression;
import Domain.Expr.TerminalExpression;
import Domain.Expr.UnaryOpNode;
import Domain.Param.ArrayParameter;
import Domain.Param.Parameter;
import Domain.Param.Parameters;
import Domain.Stmt.Compound_Statement;
import Domain.Stmt.Expression_Statement;
import Domain.Stmt.If_Statement;
import Domain.Stmt.Return_Statement;
import Domain.Stmt.Statement;
import Domain.Stmt.While_Statement;
import Domain.Type_spec.TypeSpecification;
import Domain.Type_spec.TypeSpecification.Type;

public class MiniCAstVisitor extends MiniCBaseVisitor<MiniCNode> {
	
	@Override
	public Program visitProgram(MiniCParser.ProgramContext ctx) {
		// TODO Auto-generated method stub
		List<Declaration> list = new ArrayList<>();
		for(int i = 0; i < ctx.getChildCount(); i++){
			list.add((Declaration) visit(ctx.decl(i)));
		}
		return new Program(list);
	}

	@Override
	public Declaration visitDecl(MiniCParser.DeclContext ctx) {
		// TODO Auto-generated method stub
		return (Declaration) visit(ctx.getChild(0));
	}

	@Override
	public Variable_Declaration visitVar_decl(MiniCParser.Var_declContext ctx) {
		// TODO Auto-generated method stub
		if(isInitialization(ctx)){
			return new Variable_Declaration_Assign((TypeSpecification)visit(ctx.type_spec()),
					ctx.IDENT(), ctx.LITERAL());
		}
		else if(isArrayDecl(ctx)){
			return new Variable_Declaration_Array((TypeSpecification)visit(ctx.type_spec()), ctx.IDENT(), ctx.LITERAL());
		}
		else{
			return new Variable_Declaration((TypeSpecification)visit(ctx.type_spec()), ctx.IDENT());
		}
	}

	@Override
	public TypeSpecification visitType_spec(MiniCParser.Type_specContext ctx) {
		// TODO Auto-generated method stub
		if(ctx.getChild(0) == ctx.VOID()) {
			return new TypeSpecification(Type.VOID);
		} else {
			return new TypeSpecification(Type.INT);
		}
	}

	@Override
	public Function_Declaration visitFun_decl(MiniCParser.Fun_declContext ctx) {
		// TODO Auto-generated method stub
		
		return new Function_Declaration( (TypeSpecification) visit(ctx.type_spec()),
											ctx.IDENT(),
											(Parameters) visit(ctx.params()),
											(Compound_Statement) visit(ctx.compound_stmt()));
	}

	@Override
	public Parameters visitParams(MiniCParser.ParamsContext ctx) {
		// TODO Auto-generated method stub
		if(ctx.getChildCount() == 0){
			return new Parameters();
		}
		else {
			if(ctx.getChild(0) == ctx.VOID()) {
				return new Parameters(new TypeSpecification(Type.VOID));
			} else {
				List<Parameter> list = new ArrayList<>();
				for(int i = 0; i < ctx.param().size(); i++){
					list.add((Parameter)visit(ctx.param(i)));
				}
				return new Parameters(list);
			}
		}
	}

	@Override
	public Parameter visitParam(MiniCParser.ParamContext ctx) {
		// TODO Auto-generated method stub
		if(isArrayParam(ctx)){
			return new ArrayParameter((TypeSpecification)visit(ctx.type_spec()), ctx.IDENT());
		}
		else{
			return new Parameter((TypeSpecification)visit(ctx.type_spec()), ctx.IDENT());
		}
	}

	@Override
	public Statement visitStmt(MiniCParser.StmtContext ctx) {
		// TODO Auto-generated method stub
		return (Statement)visit(ctx.getChild(0));
	}

	@Override
	public Expression_Statement visitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		// TODO Auto-generated method stub
		return new Expression_Statement((Expression)visit(ctx.expr()));
	}

	@Override
	public While_Statement visitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		// TODO Auto-generated method stub
		return new While_Statement(ctx.WHILE(), (Expression)visit(ctx.expr()), (Statement)visit(ctx.stmt()));
	}

	@Override
	public Compound_Statement visitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		// TODO Auto-generated method stub
		List<Local_Declaration> list1 = new ArrayList<>();
		List<Statement> list2 = new ArrayList<>();
		
		for(int i = 0; i < ctx.local_decl().size(); i++) {
			list1.add((Local_Declaration) visit(ctx.local_decl(i)));
		}
		
		for(int j = 0; j < ctx.stmt().size(); j++) {
			list2.add((Statement) visit(ctx.stmt(j)));
		}
		return new Compound_Statement(list1, list2);
	}

	@Override
	public Local_Declaration visitLocal_decl(MiniCParser.Local_declContext ctx) {
		// TODO Auto-generated method stub
		if(isInitialization(ctx)) {
			return new Local_Variable_Declaration_Assign((TypeSpecification)visit(ctx.type_spec()),
															ctx.IDENT(),
															ctx.LITERAL());
		}
		else if(isArrayDecl(ctx)){
			return new Local_Variable_Declaration_Array((TypeSpecification)visit(ctx.type_spec()),
															ctx.IDENT(),
															ctx.LITERAL());
		}
		else{
			return new Local_Declaration((TypeSpecification)visit(ctx.type_spec()),
															ctx.IDENT());
		}
	}

	@Override
	public If_Statement visitIf_stmt(MiniCParser.If_stmtContext ctx) {
		// TODO Auto-generated method stub
		if(isIfSentence(ctx)) {
			return new If_Statement(ctx.IF(), (Expression) visit(ctx.expr()), (Statement) visit(ctx.stmt(0)));
		} else {
			return new If_Statement(ctx.IF(), (Expression) visit(ctx.expr()), (Statement) visit(ctx.stmt(0)), ctx.ELSE(), (Statement) visit(ctx.stmt(1)));
		}
	}

	@Override
	public Return_Statement visitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		// TODO Auto-generated method stub
		if(ctx.expr() != null) {
			return new Return_Statement(ctx.RETURN(), (Expression) visit(ctx.expr()));
		} else {
			return new Return_Statement(ctx.RETURN());
		}
	}

	@Override
	public Expression visitExpr(MiniCParser.ExprContext ctx) {
		// TODO Auto-generated method stub
		if(isBinaryOperation(ctx)) {
			if(ctx.getChild(1).getText().equals("=")) {
				return new AssignNode(ctx.IDENT(), (Expression) visit(ctx.getChild(2)));
			}
			
			return new BinaryOpNode((Expression) visit(ctx.getChild(0)), ctx.getChild(1).getText(), (Expression)visit(ctx.getChild(2)));
		}
		else if(isUnaryOperation(ctx)) {
			return new UnaryOpNode(ctx.getChild(0).getText(), (Expression) visit(ctx.getChild(1)));
		}
		else if(ctx.getChildCount() == 1){
			if(ctx.getChild(0) == ctx.LITERAL()){
				return new TerminalExpression(ctx.LITERAL());				
			}
			return new TerminalExpression(ctx.IDENT());
		}
		else if(ctx.getChildCount() == 4){
			if(ctx.getChild(2) == ctx.args()) {
				
				return new FuncallNode(ctx.IDENT(), (Arguments)visit(ctx.getChild(2)));
			} else {
				
				return new ArefNode(ctx.IDENT(), (Expression)visit(ctx.getChild(2)));
			}
		}
		else if(ctx.getChildCount() == 6){
			
			return new ArefAssignNode(ctx.IDENT(), (Expression)visit(ctx.getChild(2)), (Expression) visit(ctx.getChild(5)));
		}
		else{
			return new ParenExpression((Expression)visit(ctx.getChild(1)));
		}
	}

	@Override
	public Arguments visitArgs(MiniCParser.ArgsContext ctx) {
		// TODO Auto-generated method stub
		List<Expression> list = new ArrayList<Expression>();
		
		for(int i = 0; i < ctx.expr().size(); i++) {
			list.add((Expression) visit(ctx.expr(i)));
		}
		
		return new Arguments(list);
	}
	
	
	// 아래는 보조 메소드이다.
	
			boolean isBinaryOperation(MiniCParser.ExprContext ctx) {
				return ctx.getChildCount() == 3 && ctx.getChild(1) != ctx.expr().get(0);
				// 자식 3개짜리 expr 중 ‘(‘ expr ’)’를 배제
			}

			boolean isUnaryOperation(MiniCParser.ExprContext ctx) {
				return ctx.getChildCount() == 2;
			}

			boolean isIfSentence(MiniCParser.If_stmtContext ctx) {
				return ctx.stmt().size() == 1;
			}

			boolean isArrayParam(MiniCParser.ParamContext ctx) {
				return ctx.getChildCount() > 2;
			}

			boolean isInitialization(MiniCParser.Var_declContext ctx) {
				return ctx.getChildCount() == 5;
			}

			boolean isArrayDecl(MiniCParser.Var_declContext ctx) {
				return ctx.getChildCount() == 6;
			}

			boolean isInitialization(MiniCParser.Local_declContext ctx) {
				return ctx.getChildCount() == 5;
			}

			boolean isArrayDecl(MiniCParser.Local_declContext ctx) {
				return ctx.getChildCount() == 6;
			}
	
}
