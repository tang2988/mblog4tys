package mblog.core.persist.utils;

public class QueryRules {

	
	private String name;
	private Object val;
	private String op;//eq like
	
	public static String OP_EQ="eq";
	public static String OP_LE="le";
	public static String OP_GE="ge";
	public static String OP_LT="lt";
	public static String OP_GT="gt";
	public static String OP_LIKE="like";
	public static String OP_NE="ne";
	
		
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
