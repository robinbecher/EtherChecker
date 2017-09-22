package code;

public class Response {

	public Result result;
	public Allowance allowance;
	
	public static class Result{
		Double price;
	}
	
	public static class Allowance{
		long cost;
		long remaining;
	}
}
