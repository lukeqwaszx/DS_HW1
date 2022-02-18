import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.*;
  
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("[+\\-*]");

    // Fields
    private boolean isNegative = false;
    private byte[] digits = new byte[200];

    // Constructors
    public BigInteger() {
    }

    //Constructor to be deprecated
    public BigInteger(int i) {
        if (i < 0) {
            isNegative = true;
            i *= -1;
        } for (int j = 0; i != 0; j++) {
        digits[j] = (byte) (i % 10);
        i /= 10;
        }
    }

    // Assumes proper inputs
    public BigInteger(String s) {
        if (s.startsWith("-")) {
            isNegative = true;
            s = s.substring(1);
        }
        for (int j = 0; !(s.equals("") || s.equals("-")); j++, s = s.substring(0, s.length() - 1)) {
            digits[j] = (byte) Character.getNumericValue(s.charAt(s.length() - 1));
        }

    }

    // Produce negative
    public BigInteger(BigInteger a) {
        digits = a.digits;
    }

    // Main operations
    public BigInteger add(BigInteger big) {
        if (isNegative == big.isNegative) {
            BigInteger value = primitiveAdd(big);
            value.isNegative = isNegative;
        } else {
            if (isAbsoluteBigger(big)) {
                BigInteger value = primitiveSubtract(big);
                value.isNegative = isNegative;
            } else {
                BigInteger value = big.primitiveSubtract(this);
                value.isNegative = big.isNegative;
            }
        }
    }

    public BigInteger subtract(BigInteger big) {
        BigInteger negation = big.clone();
    }

    public BigInteger multiply(BigInteger big) {

    }

    @Override
    public String toString() {

    }
    // Helper methods
    private BigInteger primitiveAdd(BigInteger big) {
        BigInteger newBig = new BigInteger();
        for (int i = 0, carry = 0; i <= 100; i++) {
            int calc = digits[i] + big.digits[i] + carry;
            carry = calc / 10;
            newBig.digits[i] = (byte) (calc % 10);
        } return newBig;
    }


    private BigInteger primitiveSubtract(BigInteger big) {
        BigInteger newBig = new BigInteger();
        for (int i = 0, carry = 0; i <= 100; i++) {
            if (digits[i] + carry < big.digits[i]) {
                newBig.digits[i] = (byte) (digits[i] + carry + 10 - big.digits[i]);
                carry = -1;
            } else {
                newBig.digits[i] = (byte) (digits[i] + carry - big.digits[i]);
                carry = 0
            }
        } return newBig;
    }

    private boolean isAbsoluteBigger(BigInteger big) {
        int myLength = endIndex();
        int otherLength = big.endIndex();
        if (myLength > otherLength) {
            return true;
        } else if (myLength < otherLength) {
            return true;
        } else {
            for (int i = myLength; i >= 0; i--) {
                if (digits[i] > big.digits[i]) {
                    return true;
                } else if (digits[i] < big.digits[i]) {
                    return false;
                }
            } return false;
        }
    }

    private int endIndex() {
        for (int i = 199; i >= 0; i--) {
            if (digits[i] != 0) {
                return i;
            }
        }
        return -1;
    }
  

    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
        // implement here
        // parse input
        // using regex is allowed
  
        // One possible implementation
        // BigInteger num1 = new BigInteger(arg1);
        // BigInteger num2 = new BigInteger(arg2);
        // BigInteger result = num1.add(num2);
        // return result;
    }
  
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
  
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit) {
            return true;
        }
        else {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
  
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
