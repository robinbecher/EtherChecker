package code;

class PriceResponse {

	Result result;
	Allowance allowance;

	public static class Result {
		Double price;
	}

	static class Allowance {
		long cost;
		long remaining;
	}
}
