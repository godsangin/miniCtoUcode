package item2;
import java.time.format.DecimalStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

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
import Domain.Expr.TerminalExpression;
import Domain.Expr.UnaryOpNode;
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

public class UcodeGenVisitor implements ASTVisitor{
	HashMap<String, Variable> local_decl = new HashMap<String, Variable>();
	HashMap<String, Variable> global_decl = new HashMap<String, Variable>();
	
	HashMap<String, String> operators = new HashMap<String, String>();
	HashMap<String, String> functionReturnVal = new HashMap<String, String>();
	
	Stack<Integer> stack = new Stack<Integer>();
	
	String elevenSpace = "           ";
	String returnValue = "";
	
	int gvCount = 1;
	int lvCount = 0;
	int CJPN = 0;	// jump할 곳을 저장
	
	public UcodeGenVisitor() {
		operators.put(">", "gt");
		operators.put("<", "lt");
		operators.put(">=", "ge");
		operators.put("<=", "le");
		operators.put("==", "eq");
		operators.put("!=", "ne");
		operators.put("||", "and");
		operators.put("&&", "or");
		operators.put("%", "mod");
		operators.put("*", "mult");
		operators.put("/", "div");
		operators.put("-", "sub");
		operators.put("+", "add");
		operators.put("--", "dec");
		operators.put("++", "inc");
	}
	
	@Override
	public void visitProgram(Program node) {
		// TODO Auto-generated method stub
		String decl = "";
		int global_var = 0;
		List<Declaration> decls = node.decls;
		for(int i = 0; i < decls.size(); i++){
			if(!decls.get(i).toString().contains("(")){
				global_var++;
			}
		}
		System.out.println(elevenSpace + "proc " + global_var + " 1 1");
		for(int i = 0; i < decls.size(); i++) {
			if(decls.get(i).toString().contains("(")) {
				((Function_Declaration)decls.get(i)).accept(this);
			}
			else {
				((Variable_Declaration)decls.get(i)).accept(this);
			}
		}
		if ((gvCount - 1) < 0)
			System.out.println(elevenSpace + "bgn 0");
		else
			System.out.println(elevenSpace + "bgn " + (gvCount - 1));
		System.out.println(elevenSpace + "ldp");
		System.out.println(elevenSpace + "call main");
		System.out.println(elevenSpace + "end");
	}

	@Override
	public void visitFunction_Declaration(Function_Declaration node) {
		// TODO Auto-generated method stub
		this.lvCount = 1;
		//this.lvCount++;

		String stmt = "";
		functionReturnVal.put(node.t_node.toString(), returnValue);
		String funcName = node.t_node.toString();
		int spaceLength = 11 - funcName.length();
		String space = "";
		for(int i = 0; i < spaceLength; i++) {
			space += " ";
		}
		stmt += funcName + space;
		stmt += "proc " + (node.params.params.size() + node.compount_stmt.local_decls.size()) + " 2 2";
		System.out.println(stmt);
		node.params.accept(this);
		node.compount_stmt.accept(this);
		if(node.params.type_spec != null) {
			if(node.params.type_spec.toString().equals("void"))
				System.out.println(elevenSpace + "ret");
		}
		else		
		System.out.println(elevenSpace + "end");
	}

	@Override
	public void visitLocal_Declaration(Local_Declaration node) {
		// TODO Auto-generated method stub
		
		String decl = "";
		String var_val = "";
		if(node.toString().contains("=")){ //type_spec IDENT '=' LITERAL ';'
			decl += elevenSpace + "sym 2 " + lvCount + " 1\n";
			var_val = ((Local_Variable_Declaration_Assign)node).rhs.toString();
			decl += elevenSpace + "ldc " + var_val + "\n";
			decl += elevenSpace + "str 2 " + lvCount;
			local_decl.put(node.lhs.toString(), new Variable("2", String.valueOf(lvCount++), "1"));
		} 
		else if(node.toString().contains("[")) {
			var_val = ((Local_Variable_Declaration_Array)node).rhs.toString();
			decl += elevenSpace + "sym 2 " + lvCount + " " + var_val;
			local_decl.put(node.lhs.toString(), 
					new Variable("2", String.valueOf(lvCount),var_val));
			lvCount += Integer.parseInt(var_val);
 		}
		else {
			decl += elevenSpace + "sym 2 " + lvCount + " 1";
			local_decl.put(node.lhs.toString(), new Variable("2", String.valueOf(lvCount++), "1"));
		}
		System.out.println(decl);
	}

	@Override
	public void visitVariable_Declaration(Variable_Declaration node) {
		// TODO Auto-generated method stub
		String decl = "";
		String var_val = "";
		if(node.toString().contains("=")){ //type_spec IDENT '=' LITERAL ';'
			decl += elevenSpace + "sym 1 " + gvCount + " 1\n";
			var_val = ((Variable_Declaration_Assign)node).rhs.toString();
			decl += elevenSpace + "ldc " + var_val + "\n";
			decl += elevenSpace + "str 1 " + gvCount;
			global_decl.put(node.lhs.toString(), new Variable("1", String.valueOf(gvCount++), "1"));
		} 
		else if(node.toString().contains("[")) {	
			var_val = ((Variable_Declaration_Array)node).rhs.toString();
			decl += elevenSpace + "sym 1 " + gvCount + " " + var_val;
			global_decl.put(node.lhs.toString(), 
					new Variable("1", String.valueOf(gvCount),var_val));
			gvCount += Integer.parseInt(var_val);
 		} 
		else {
			decl += elevenSpace + "sym 1 " + gvCount + " 1";
			global_decl.put(node.lhs.toString(), new Variable("1", String.valueOf(gvCount++), "1"));
		}
		System.out.println(decl);
	}

	@Override
	public void visitArguments(Arguments node) {
		// TODO Auto-generated method stub
		String args = "";
		List<Expression> exprs = node.exprs;
		if (exprs.size() == 1) {
			if (local_decl.containsKey(exprs.get(0).toString())) {
				args += elevenSpace + "lod " + local_decl.get(exprs.get(0).toString()).getBase() + " "
						+ local_decl.get(exprs.get(0).toString()).getOffset();
			}
			else if (global_decl.containsKey(exprs.get(0).toString())) {
				args += elevenSpace + "lod " + global_decl.get(exprs.get(0).toString()).getBase() + " "
						+ global_decl.get(exprs.get(0).toString()).getBase();
			}
			else {
				args += elevenSpace + "ldc " + exprs.get(0).toString();
			}
		}
		else {
			for (int i = 0; i < exprs.size(); i++) {
				if(i % 2 == 0) {
					if(local_decl.containsKey(exprs.get(i/2).toString())) {
						args += elevenSpace + "lda "
								+ local_decl.get(exprs.get(i/2).toString()).getBase() + " "
								+ local_decl.get(exprs.get(i/2).toString()).getOffset() + "\n";
					}
					else if (global_decl.containsKey(exprs.get(i/2).toString())) {
						args += elevenSpace + "lda "
								+ global_decl.get(exprs.get(i/2).toString()).getBase() + " "
								+ global_decl.get(exprs.get(i/2).toString()).getOffset() + "\n";
					}
					else {
						args += elevenSpace + "ldc " + exprs.get(i/2).toString() + "\n";
					}
				}
			}
		}
		System.out.println(args);
	}

	@Override
	public void visitExpression(Expression node) {
		// TODO Auto-generated method stub
		String expr = "";
		if(node instanceof TerminalExpression) {
			if(local_decl.containsKey(((TerminalExpression)node).t_node.toString())){
				Variable v = local_decl.get(((TerminalExpression)node).t_node.toString());
				expr = " " + v.getBase() + " " + v.getOffset();
			}
			else{
			}
		}
		else if(node instanceof ArefNode) {
			String t_node = ((ArefNode)node).t_node.toString();
			String expr_node = ((ArefNode)node).expr.toString();
			
			if (local_decl.containsKey(expr_node))
				expr += "\n" + elevenSpace + "lod " + local_decl.get(expr_node).getBase()
						+ " " + local_decl.get(expr_node).getOffset();
			else if (global_decl.containsKey(expr_node)) {
				expr += "\n" + elevenSpace + "lod "
						+ global_decl.get(expr_node).getBase() + " "
						+ global_decl.get(expr_node).getOffset();
				;
			} else {
				expr += "\n" + elevenSpace + "ldc " + expr_node;
			}
			if (local_decl.containsKey(t_node))
				expr += "\n" + elevenSpace + "lda " + local_decl.get(t_node).getBase()
						+ " " + local_decl.get(t_node).getOffset();
			else if (global_decl.containsKey(t_node)) {
				expr += "\n" + elevenSpace + "lda "
						+ global_decl.get(t_node).getBase() + " "
						+ global_decl.get(t_node).getOffset();
			}
			expr += "\n" + elevenSpace + "add ";
		}
		else if(node instanceof FuncallNode) {
			((FuncallNode)node).args.accept(this);
			String t_node = ((FuncallNode)node).t_node.toString();
			expr += elevenSpace + "ldp" + "\n";
			((FuncallNode)node).args.accept(this);
			expr += elevenSpace + "call " + t_node;
		}
		else if(node instanceof UnaryOpNode) {
			String expr_node = ((UnaryOpNode)node).expr.toString();
			if(global_decl.containsKey(expr_node)) {
				expr += elevenSpace + "lod " + global_decl.get(expr_node).getBase() + " "
						+ global_decl.get(expr_node).getOffset() + "\n";
				expr += elevenSpace + operators.get(((UnaryOpNode)node).op.toString()) + "\n";
				expr += elevenSpace + "str " + global_decl.get(expr_node).getBase() + " "
						+ global_decl.get(expr_node).getOffset();
			}
			else if (local_decl.containsKey(expr_node)) {				
				expr += elevenSpace + "lod " + local_decl.get(expr_node).getBase() + " "
						+ local_decl.get(expr_node).getOffset() + "\n";
				expr += elevenSpace + operators.get(((UnaryOpNode)node).op.toString()) + "\n";
				expr += elevenSpace + "str " + local_decl.get(expr_node).getBase() + " "
						+ local_decl.get(expr_node).getOffset();
			}
		}
		else if(node instanceof BinaryOpNode) {
			String lhs_expr = ((BinaryOpNode)node).lhs.toString();
			String rhs_expr = ((BinaryOpNode)node).rhs.toString();
			if (global_decl.containsKey(lhs_expr)) {
				expr += elevenSpace + "lod "
						+ global_decl.get(lhs_expr).getBase() + " "
						+ global_decl.get(lhs_expr).getOffset() + "\n";
			} 
			else if (local_decl.containsKey(lhs_expr)) {
				expr += elevenSpace + "lod "
						+ local_decl.get(lhs_expr).getBase() + " "
						+ local_decl.get(lhs_expr).getOffset() + "\n";
			}
			else {				
				expr += elevenSpace + "ldc " + lhs_expr + "\n";
			}
			if (global_decl.containsKey(rhs_expr)) {
				expr += elevenSpace + "lod "
						+ global_decl.get(rhs_expr).getBase() + " "
						+ global_decl.get(rhs_expr).getOffset() + "\n";
			} 
			else if (local_decl.containsKey(rhs_expr)) {
				expr += elevenSpace + "lod "
						+ local_decl.get(rhs_expr).getBase() + " "
						+ local_decl.get(rhs_expr).getOffset() + "\n";
			}
			else {
				expr += elevenSpace + "ldc " + rhs_expr + "\n";				
			}
			expr += elevenSpace + operators.get(((BinaryOpNode)node).op.toString());
		}
		else if (node instanceof AssignNode) {
			String t_node = ((AssignNode)node).t_node.toString();
			String expr_node = ((AssignNode)node).expr.toString();
			if (((AssignNode)node).expr instanceof FuncallNode) {
				((AssignNode)node).expr.accept(this);
				if (global_decl.containsKey(t_node)) {
					expr += elevenSpace + "str "
							+ global_decl.get(t_node).getBase() + " "
							+ global_decl.get(t_node).getOffset() + "\n";
				} else if (local_decl.containsKey(t_node))
					expr += "\n" + elevenSpace + "str "
							+ local_decl.get(t_node).getBase() + " "
							+ local_decl.get(t_node).getOffset() + "\n";
			}
			else if(((AssignNode)node).expr instanceof ArefNode) {
				((AssignNode)node).expr.accept(this);
				if (global_decl.containsKey(t_node)) {
					expr += elevenSpace + "str "
							+ global_decl.get(t_node).getBase() + " "
							+ global_decl.get(t_node).getOffset();
				} else if (local_decl.containsKey(t_node))
					expr += elevenSpace + "str "
							+ local_decl.get(t_node).getBase() + " "
							+ local_decl.get(t_node).getOffset() + "\n";
			}
		}
		else if(node instanceof ArefAssignNode) {
			String t_node = ((ArefAssignNode)node).t_node.toString();
			String lhs_expr = ((ArefAssignNode)node).lhs.toString();
			String rhs_expr = ((ArefAssignNode)node).rhs.toString();
		
			if (local_decl.containsKey(lhs_expr))
				expr += elevenSpace + "lod " + local_decl.get(lhs_expr).getBase()
						+ " " + local_decl.get(lhs_expr).getOffset() + "\n";
			else if (global_decl.containsKey(lhs_expr)) {
				expr += elevenSpace + "lod " + global_decl.get(lhs_expr).getBase()
						+ " " + global_decl.get(lhs_expr).getOffset() + "\n";
			} 
			else {
				expr += elevenSpace + "ldc " + lhs_expr + "\n";
			}
			if (local_decl.containsKey(t_node)) {				
				expr += elevenSpace + "lda " + local_decl.get(t_node).getBase()
						+ " " + local_decl.get(t_node).getOffset() + "\n";
			}
			else if (global_decl.containsKey(t_node)) {
				expr += elevenSpace + "lda " + global_decl.get(t_node).getBase()
						+ " " + global_decl.get(t_node).getOffset() + "\n";
			}
			expr += elevenSpace + "add "  + "\n";
			if (local_decl.containsKey(rhs_expr)) {
				expr += elevenSpace + "lod " + local_decl.get(rhs_expr).getBase()
						+ " " + local_decl.get(rhs_expr).getOffset() + "\n";				
			}
			else if (global_decl.containsKey(rhs_expr)) {
				expr += elevenSpace + "lod " + global_decl.get(rhs_expr).getBase()
						+ " " + global_decl.get(rhs_expr).getOffset() + "\n";
			} 
			else {
				expr += elevenSpace + "ldc " + rhs_expr + "\n";

			}
			expr += elevenSpace + "sti ";
		}
		System.out.println(expr);
	}

	@Override
	public void visitParameter(Parameter node) {
		// TODO Auto-generated method stub
		String param = "";
		param += elevenSpace + "sym 2 " + lvCount + " 1";
		local_decl.put(node.t_node.toString(), new Variable("2", String.valueOf(lvCount++), "1"));
		System.out.println(param);
	}

	@Override
	public void visitParameters(Parameters node) {
		// TODO Auto-generated method stub
		String params = "";
		//List<Parameter> params = node.params;
		if(node.params != null) {			
			for(int i = 0; i < node.params.size(); i++) {
				if(i % 2 == 0) {
					node.params.get(i/2).accept(this);
				}
			}
		}
		else return;
	}

	@Override
	public void visitCompound_Statement(Compound_Statement node) {
		// TODO Auto-generated method stub
		for(int i = 0; i < node.local_decls.size(); i++) {
			node.local_decls.get(i).accept(this);
		}
		for(int i = 0; i < node.stmts.size(); i++) {
			node.stmts.get(i).accept(this);
		}
	}

	@Override
	public void visitExpression_Statement(Expression_Statement node) {
		// TODO Auto-generated method stub
		node.expr.accept(this);
	}

	@Override
	public void visitIf_Statement(If_Statement node) {
		// TODO Auto-generated method stub
		String stmt = "";
		node.expr.accept(this);
		stmt += elevenSpace + "jp $$" + CJPN;
		node.if_stmt.accept(this);

		System.out.println(stmt);
		stack.push(CJPN);
		if (node.else_stmt != null) {
			stmt = "";
			stmt += "$$" + CJPN + "        " + "nop\n";
			stmt += elevenSpace + "fjp $$" + ++CJPN;
			System.out.println(stmt);
			node.else_stmt.accept(this);
		}
		
	}

	@Override
	public void visitReturn_Statement(Return_Statement node) {
		// TODO Auto-generated method stub
		if(node.expr != null){
			System.out.print(elevenSpace + "push");
			node.expr.accept(this);
			System.out.println(elevenSpace + "retv");	
		}
		else{
			System.out.println(elevenSpace + "ret");
			}
	}

	@Override
	public void visitWhile_Statement(While_Statement node) {
		// TODO Auto-generated method stub
		String stmt = "";
		if (stack.isEmpty()) {
			stmt += "$$" + CJPN + "        " + "nop";
		}
		else {
			stmt += "$$" + stack.peek() + "        " + "nop";
			if (stack.pop() != CJPN) {
				stmt = "";
				stmt += "$$" + CJPN + "        " + "nop";
			}
		}
		System.out.println(stmt);
		
		node.expr.accept(this);
		stmt = elevenSpace + "fjp $$" + ++CJPN;
		System.out.println(stmt);
		node.stmt.accept(this);
		stmt = elevenSpace + "ujp $$" + (CJPN -1);
		System.out.println(stmt);
		
		int temp = CJPN;
		
		stack.push(temp);
	}

	@Override
	public void visitType_spec(TypeSpecification node) {
		// TODO Auto-generated method stub
		String type = "";
		type += node.type.toString();
	}

	
}

