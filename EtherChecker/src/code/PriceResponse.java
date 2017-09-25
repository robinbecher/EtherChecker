package code;

public class PriceResponse {

	Result result;
	Allowance allowance;

	public static class Result {
		Double price;
	}

	public static class Allowance {
		long cost;
		long remaining;
	}
}