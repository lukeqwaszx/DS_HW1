import java.io.BufferedReader;
import java.io.InputStreamReader;
//  import java.util.regex.*;

/*  Made on Feb 20, 2022.
    The thing I regretted during my design is
    that I could have made the BigInteger final.
    Since there's no motivation to modify the BigInteger
    object, I did not need to risk having unintended
    side effects. Also, if the BigInteger was constant
    I could keep the length of the byte array as a field,
    not as a method to call. As the number was used across
    various functions, keeping it as a variable would up
    the performance quite a bit.

    Also, naming skills still need much improvement.

    One thing I also noticed is the importance of the classes
    I took on Fall 2021. Chunking the codes into separate functions
    was noticeably easy, and the importance of Discrete mathematics
    course could be seen in modelling the problem.

    Overall, the code contains improved design and
    single responsibility principle was adhered to.
    It is quite a step-up from my former project,
    and I am proud for it. */
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";
    // public static final Pattern EXPRESSION_PATTERN = Pattern.compile("[+\\-*]");

    // Fields
    private boolean isNegative = false;
    private byte[] digits = new byte[201];

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
        } else if (s.startsWith("+")) {
            s = s.substring(1);
        }
        for (int j = 0; !(s.equals("") || s.equals("-")); j++, s = s.substring(0, s.length() - 1)) {
            digits[j] = (byte) Character.getNumericValue(s.charAt(s.length() - 1));
        }
    }

    // Produce clone
    public BigInteger(BigInteger a) {
        digits = a.digits;
        isNegative = a.isNegative;
    }

    // Main operations
    public BigInteger add(BigInteger big) {
        BigInteger value;
        if (isNegative == big.isNegative) {
            value = primitiveAdd(big);
            value.isNegative = isNegative;
        } else if (isAbsoluteBigger(big)) {
            value = primitiveSubtract(big);
            value.isNegative = isNegative;
        }
        else {
                value = big.primitiveSubtract(this);
                value.isNegative = big.isNegative;
            }
        return value;
    }

    public BigInteger subtract(BigInteger big) {
        BigInteger negation = new BigInteger(big);
        negation.isNegative = !(big.isNegative);
        return add(negation);
    }

    public BigInteger multiply(BigInteger big) {
        BigInteger value = new BigInteger();
        for (int i = 0; i <= big.endIndex(); i++) {
            value = value.primitiveAdd(this.primitiveMultiply(big.digits[i], i));
        }
        if (isNegative != big.isNegative) {
            value.isNegative = true;
        }
        return value;
    }

    @Override
    public String toString() {
        int myLength = endIndex();
        if (myLength == -1) {
            return "0";
        }
        StringBuilder builder = new StringBuilder();
        if (isNegative) {
            builder.append("-");
        }
        for (int i = myLength; i >= 0; i--) {
            builder.append(digits[i]);
        }
        return builder.toString();
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
                carry = 0;
            }
        } return newBig;
    }

    private BigInteger primitiveMultiply(int factor, int power) {
        BigInteger newBig = new BigInteger();
        int myLength = endIndex();
        for (int i = 0, carry = 0; i <= myLength + 1; i++) {
            int result = digits[i] * factor + carry;
            carry = result / 10;
            newBig.digits[i + power] = (byte) (result % 10);
        }
        return newBig;
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
        input = input.replace(" ", "");
        int operatorIndex = findOperatorIndex(input);
        BigInteger one = new BigInteger(input.substring(0, operatorIndex));
        BigInteger two = new BigInteger(input.substring(operatorIndex + 1));
        char operator = input.charAt(operatorIndex);
        return switch (operator) {
            case '+' -> one.add(two);
            case '-' -> one.subtract(two);
            case '*' -> one.multiply(two);
            default -> null;
        };
    }

    static int findOperatorIndex(String input) {
        for (int i = 1; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return i;
            }
        }
        return -1;
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
