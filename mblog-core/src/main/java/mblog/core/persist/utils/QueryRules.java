package mblog.core.persist.utils;

public class QueryRules {

	
	private String name;
	private Object val;
	private String op;//eq like
	
	
	public QueryRules( String name,Object val,String op) {
		this.name = name;
		this.val = val;
		this.op =op;
	}
	
	public QueryRules( String name,Object val) {
		this.name = name;
		this.val = val;
		this.op ="eq";
	}
	
	public QueryRules() {
		this.op ="eq";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getVal() {
		return val;
	}
	public void setVal(Object val) {
		this.val = val;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	
	@Override
	public String toString() {
		return "["+op+"]"+name+"="+val;
	}
}
