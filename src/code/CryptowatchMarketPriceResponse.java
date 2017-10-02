package code;

@SuppressWarnings("unused")
class CryptowatchMarketPriceResponse {

    Result result;
    Allowance allowance;

    @SuppressWarnings("unused")
    public static class Result {
        Double price;
    }

    @SuppressWarnings("unused")
    static class Allowance {
        long cost;
        long remaining;
    }
}
