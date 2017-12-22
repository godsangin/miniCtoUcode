package Domain.Param;

import java.util.List;

import Domain.MiniCNode;
import Domain.Type_spec.TypeSpecification;
import item2.ASTVisitor;

public class Parameters extends MiniCNode{
	public String str = null;
	public List<Parameter> params;
	public TypeSpecification type_spec;
	
	public Parameters() {
		this.str = "";
	}
	
	public Parameters(TypeSpecification type_spec){
		this.type_spec = type_spec;
	}
	
	public Parameters(List<Parameter> params){
		this.params = params;
	}

	@Override
	public String toString(){
		return str != null ? str 
		: type_spec != null ? type_spec.toString() 
		: params.stream().map(t -> ", " + t.toString())
						 .skip(1)
						 .reduce(params.get(0).toString(), (acc, param) -> acc + param);
	}

	@Override
	public void accept(ASTVisitor v) {
		// TODO Auto-generated method stub
		v.visitParameters(this);
	}
}
