import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

public class MiniCVisitor2 extends MiniCBaseVisitor<String>{
	StringBuffer nesting = new StringBuffer();
	

	@Override
	public String visitProgram(MiniCParser.ProgramContext ctx) {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < ctx.getChildCount(); i++) {
			buf.append(visit(ctx.decl(i)) + "\n");
		}
		
		return buf + "";
	}
	

	@Override
	public String visitDecl(MiniCParser.DeclContext ctx) {
		// TODO Auto-generated method stub
		return visit(ctx.getChild(0));
	}

	@Override
	public String visitVar_decl(MiniCParser.Var_declContext ctx) {
		// TODO Auto-generated method stub
		String type = null, id = null, literal = null;
		if(isInitialization(ctx)){
			type = ctx.type_spec().getText();
			id = ctx.IDENT().getText();
			literal = ctx.LITERAL().getText();
			return type + " " + id + " = " + literal + ";";
		}
		else if(isArrayDecl(ctx)){
			type = ctx.type_spec().getText();
			id = ctx.IDENT().getText();
			literal = ctx.LITERAL().getText();
			return type + " " + id + "[" + literal + "];";
		}
		else{
			type = ctx.type_spec().getText();
			id = ctx.IDENT().getText();
			return type + " " + id + ";";
		}
	}

	@Override
	public String visitType_spec(MiniCParser.Type_specContext ctx) {
		// TODO Auto-generated method stub
		return visit(ctx.getChild(0));
	}

	@Override
	public String visitFun_decl(MiniCParser.Fun_declContext ctx) {
		// TODO Auto-generated method stub
		String type = ctx.type_spec().getText();
		String id = ctx.IDENT().getText();
		String params = visit(ctx.params());
		String stmt = visit(ctx.compound_stmt());
		return type + " " + id + "(" + params + ")\n" + stmt;
	}

	@Override
	public String visitParams(MiniCParser.ParamsContext ctx) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		if(ctx.getChildCount() > 0){
			sb.append(visit(ctx.getChild(0)));
		}
		for(int i = 1; i < ctx.param().size() ; i++){
			sb.append(", " + visit(ctx.param(i)));
		}
		return sb.toString();
	}

	@Override
	public String visitParam(MiniCParser.ParamContext ctx) {
		// TODO Auto-generated method stub
		String type = ctx.type_spec().getText();
		String id = ctx.IDENT().getText();
		if(isArrayParam(ctx)){
			return type + " " + id + "[]";
		}
		else{
			return type + " " + id;
		}
	}

	@Override
	public String visitStmt(MiniCParser.StmtContext ctx) {
		// TODO Auto-generated method stub
		return visit(ctx.getChild(0));
	}

	@Override
	public String visitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		// TODO Auto-generated method stub
		String expr = visit(ctx.expr());
		return expr + ";";
	}

	@Override
	public String visitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		// TODO Auto-generated method stub
		String expr = visit(ctx.expr());
		String stmt = visit(ctx.stmt());
		if(!(ctx.stmt().getChild(0) instanceof MiniCParser.Compound_stmtContext)){
			stmt = nesting + "...." + stmt + "\n";
			return "while (" + expr + ")\n" + nesting + "{\n" + stmt + nesting + "}";
		}
		else{
			return "while (" + expr + ")\n" + stmt;
		}
	}

	@Override
	public String visitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		nesting.append("....");
		for(int i = 1; i < ctx.getChildCount() - 1; i++){
			sb.append(nesting + visit(ctx.getChild(i)) + "\n");
		}
		nesting.delete(0, 4);
		return nesting + "{\n" + sb + nesting + "} ";
	}

	@Override
	public String visitLocal_decl(MiniCParser.Local_declContext ctx) {
		// TODO Auto-generated method stub
		String type = null, id = null, literal = null;
		if(isInitialization(ctx)){
			type = ctx.type_spec().getText();
			id = ctx.IDENT().getText();
			literal = ctx.LITERAL().getText();
			return type + " " + id + " = " + literal + ";";
		}
		else if(isArrayDecl(ctx)){
			type = ctx.type_spec().getText();
			id = ctx.IDENT().getText();
			literal = ctx.LITERAL().getText();
			return type + " " + id + "[" + literal + "];";
		}
		else{
			type = ctx.type_spec().getText();
			id = ctx.IDENT().getText();
			return type + " " + id + ";";
		}
	}

	@Override
	public String visitIf_stmt(MiniCParser.If_stmtContext ctx) {
		// TODO Auto-generated method stub
		String expr = visit(ctx.expr());
		String stmt1 = visit(ctx.stmt(0)), stmt2 = null;
		if(isIfSentence(ctx)){
			if(!(ctx.stmt(0).getChild(0) instanceof MiniCParser.Compound_stmtContext)){
				stmt1 = nesting + "...." + stmt1 + "\n";
				return "if (" + expr + ")\n" + nesting + "{\n" + stmt1 + nesting + "}";
			}
			else{
				return "if (" + expr + ")\n" + stmt1;
			}
		}
		else{
			stmt2 = visit(ctx.stmt(1));
			if(!(ctx.stmt(0).getChild(0) instanceof MiniCParser.Compound_stmtContext)){
				stmt1 = nesting + "...." + stmt1 + "\n";
				stmt1 = "if (" + expr + ")\n" + nesting + "{\n" + stmt1 + nesting + "}";
			}
			else if(!(ctx.stmt(1).getChild(0) instanceof MiniCParser.Compound_stmtContext)){
				stmt2 = nesting + "...." + stmt2 + "\n";
				stmt2 = "else\n" + nesting + "{\n" + stmt2 + nesting + "}";
				return "if (" + expr + ")\n" + stmt1 + stmt2;
			}
			else{
				return "if (" + expr + ")\n" + stmt1 + "else\n" + stmt2;	
			}
			
			if(!(ctx.stmt(1).getChild(0) instanceof MiniCParser.Compound_stmtContext)){
				stmt2 = nesting + "...." + stmt2 + "\n";
				stmt2 = "else\n" + nesting + "{\n" + stmt2 + nesting + "}";
				return stmt1 + stmt2;
			}
			else{
				return stmt1 + "else\n" + stmt2;
			}
		}
	}

	@Override
	public String visitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		// TODO Auto-generated method stub
		if(ctx.expr() != null){
			return "return " + visit(ctx.expr()) + ";";
 		}
		else{
			return "return;";
		}
	}

	@Override
	public String visitExpr(MiniCParser.ExprContext ctx) {
		// TODO Auto-generated method stub
		String s1 = null, s2 = null, op = null;
		if(isBinaryOperation(ctx)){
			if(ctx.getChild(1).getText().equals("=")){
				return ctx.getChild(0).getText() + " = " + visit(ctx.getChild(2));
			}
			s1 = visit(ctx.getChild(0));
			s2 = visit(ctx.getChild(2));
			op = ctx.getChild(1).getText();
			return s1 + " " + op + " " + s2;
		}
		else if(isUnaryOperation(ctx)){
			s1 = visit(ctx.getChild(1));
			op = ctx.getChild(0).getText();
			return op + s1;
		}
		else if(ctx.getChildCount() == 1){
			return ctx.getChild(0).getText();
		}
		else if(ctx.getChildCount() == 4){
			s1 = ctx.IDENT().getText();
			if(ctx.getChild(2) == ctx.args()) {
				s2 = visit(ctx.getChild(2));
				return s1 + "(" + s2 + ")";
			} else {
				s2 = visit(ctx.getChild(2));
				return s1 + "[" + s2 + "]";
			}
		}
		else if(ctx.getChildCount() == 6){
			s1 = ctx.IDENT().getText();
			s2 = visit(ctx.getChild(2));
			String s3 = visit(ctx.getChild(5));
			return s1 + "[" + s2 + "] = " + s3;
		}
		else{
			
			s1 = visit(ctx.getChild(1));
			return "(" + s1 + ")";
		}
	}

	@Override
	public String visitArgs(MiniCParser.ArgsContext ctx) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		if(ctx.expr().size() > 0){
			sb.append(visit(ctx.expr(0)));
		}
		for(int i = 1; i < ctx.expr().size(); i++){
			sb.append(", " + visit(ctx.expr(i)));
		}
		return sb.toString();
	}
	
	// 아래는 보조 메소드이다.
		boolean isBinaryOperation(MiniCParser.ExprContext ctx) {
			return ctx.getChildCount() == 3 && ctx.getChild(1) != ctx.expr();
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