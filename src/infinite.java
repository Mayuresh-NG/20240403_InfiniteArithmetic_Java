import java.util.ArrayList;
import java.util.List;

class infinite {
    /** An internal member List to contain the digits of the Infinite Integer. */
    private List<Integer> internalList = new ArrayList<>();

    public infinite(Object inputObject) {
        if (inputObject instanceof Integer) {
            System.out.println("You sent a number");
            int inputNumber = (int) inputObject;
            if (inputNumber < 0)
                throw new IllegalArgumentException("Please provide only positive number");

            // initialize the member list
            String numberStr = Integer.toString(inputNumber);
            for (char c : numberStr.toCharArray()) {
                internalList.add(Character.getNumericValue(c));
            }
            System.out.println(internalList);
        } else if (inputObject instanceof String) {
            System.out.println("You sent a String");
            String inputString = (String) inputObject;
            if (!inputString.matches("\\d+")) {
                throw new IllegalArgumentException("Please provide a valid number string");
            }

            // initialize the member list
            for (char c : inputString.toCharArray()) {
                internalList.add(Character.getNumericValue(c));
            }
            System.out.println(internalList);
        } else if (inputObject instanceof int[]) {
            System.out.println("You sent an array");
            int[] inputArray = (int[]) inputObject;
            for (int num : inputArray) {
                if (num < 0)
                    throw new IllegalArgumentException("Invalid array. Please provide an array of positive integers.");
                internalList.add(num);
            }
            System.out.println(internalList);
        } else if (inputObject instanceof Object) {
            System.out.println("You sent an Object");

            // TODO: Check if this object has getInternalList() and make a deep copy
            // and assign it to local internalList
        } else {
            System.out.println("You sent some bullshit!");
            throw new IllegalArgumentException("Constructor of InfiniteNumber does not support this data type " + inputObject.getClass());
        }
    }

    /** Helper method to return the representation of this Infinite Precision */
    private String getNumberAsString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int digit : list) {
            sb.append(digit);
        }
        return sb.toString();
    }

    public String add(infinite toAdd) {
        List<Integer> res = toAdd.internalList;
        List<Integer> result = new ArrayList<>();

        int carry = 0;
        int maxLength = Math.max(internalList.size(), res.size());

        for (int i = 0; i < maxLength; i++) {
            int num1 = i < internalList.size() ? internalList.get(internalList.size() - 1 - i) : 0;
            int num2 = i < res.size() ? res.get(res.size() - 1 - i) : 0;

            int sum = num1 + num2 + carry;
            result.add(0, sum % 10);
            carry = sum / 10;
        }

        if (carry > 0) {
            result.add(0, carry);
        }

        return getNumberAsString(result);
    }

    public String sub(infinite toSub) {
        List<Integer> res = toSub.internalList;
        List<Integer> result = new ArrayList<>();

        int borrow = 0;
        int maxLength = Math.max(internalList.size(), res.size());

        for (int i = 0; i < maxLength; i++) {
            int num1 = i < internalList.size() ? internalList.get(internalList.size() - 1 - i) : 0;
            int num2 = i < res.size() ? res.get(res.size() - 1 - i) : 0;

            // Adjust num1 if borrow is needed
            num1 -= borrow;

            if (num1 < num2) {
                // If num1 is smaller than num2, add 10 to num1 and set borrow flag
                num1 += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }

            int diff = num1 - num2;
            result.add(0, diff);
        }

        // Remove leading zeros
        while (result.size() > 1 && result.get(0) == 0) {
            result.remove(0);
        }

        // If borrow is set, result is negative
        if (borrow == 1) {
            result.set(0, -result.get(0));
        }

        // Remove leading zeros again after possible sign adjustment
        while (result.size() > 1 && result.get(0) == 0) {
            result.remove(0);
        }

        return getNumberAsString(result);
    }

    public String mul(infinite toMul) {
        List<Integer> res = toMul.internalList;
        List<Integer> result = new ArrayList<>();

        // Calculate the length of the result array
        int len1 = internalList.size();
        int len2 = res.size();
        int lenResult = len1 + len2;

        // Initialize result array with zeros
        for (int i = 0; i < lenResult; i++) {
            result.add(0);
        }

        // Perform multiplication digit by digit
        for (int i = len1 - 1; i >= 0; i--) {
            int carry = 0;
            int num1 = internalList.get(i);

            for (int j = len2 - 1; j >= 0; j--) {
                int num2 = res.get(j);
                int product = num1 * num2 + result.get(i + j + 1) + carry;
                result.set(i + j + 1, product % 10);
                carry = product / 10;
            }

            result.set(i, carry);
        }

        // Remove leading zeros
        while (result.size() > 1 && result.get(0) == 0) {
            result.remove(0);
        }

        return getNumberAsString(result);
    }

    public int div(infinite toDiv) {
        List<Integer> arr1 = internalList;
        List<Integer> arr2 = toDiv.internalList;

        // Convert the two lists to integers
        int dividend = convertListToNumber(arr1);
        int divisor = convertListToNumber(arr2);

        // Handle division by zero
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }

        // Perform division
        int quotient = dividend / divisor;

        return quotient;
    }

    // Helper method to convert a list of digits to an integer
    private int convertListToNumber(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int digit : list) {
            sb.append(digit);
        }
        return Integer.parseInt(sb.toString());
    }

    public static void main(String[] args) {
        infinite ob1 = new infinite(306);
        infinite ob2 = new infinite(206);

        System.out.println("Addition is " + ob1.add(ob2));
        System.out.println("Subtraction is " + ob1.sub(ob2));
        System.out.println("Multiplication is " + ob1.mul(ob2));
        System.out.println("Division is " + ob1.div(ob2));
    }
}
