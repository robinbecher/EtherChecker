package code;

class StringManipulator {
    static String trimETHValue(String result) {
        boolean lessThanOne = false;
        while (result.length() < 18) {
            result = result.concat("0");
            lessThanOne = true;
        }

        if (lessThanOne) {
            result = result.substring(0, 1) + "." + result.substring(1, result.length());
        } else {
            String base = result.substring(0, result.length() - 18);
            String mantissa = result.substring(result.length() - 18, result.length());

            base = groupDigits(Long.valueOf(base));
            mantissa = removeTrailingZeroes(mantissa);

            if (!mantissa.equals("")) {
                result = base + "." + mantissa;
            } else {
                result = base;
            }
        }

        return result;
    }

    private static String groupDigits(Long number) {

        String text = String.valueOf(number);
        StringBuilder newText = new StringBuilder();
        int end = text.length();

        for (int i = 0; i <= end / 3; i++) {
            if (text.length() < 3) {
                newText.insert(0, text);
            } else {
                newText.insert(0, "," + text.substring(text.length() - 3, text.length()));
                text = text.substring(0, text.length() - 3);
            }
        }

        if (newText.toString().startsWith(",")) {
            newText = new StringBuilder(newText.substring(1));
        }
        return newText.toString();
    }

    private static String removeTrailingZeroes(String mantissa) {
        String newMantissa = mantissa;
        boolean done = false;
        while (!done) {
            if (newMantissa.endsWith("0")) {
                newMantissa = newMantissa.substring(0, newMantissa.length() - 1);
            } else {
                done = true;
            }
        }

        return newMantissa;
    }
}
