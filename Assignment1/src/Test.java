// Test code for https://www.acmicpc.net/problem/2338
import java.util.Scanner;

// LongIntTest ADT for unbounded integers
import java.util.Arrays;
class LongIntTest {
    //public char[] value;
    public int[] value;
    public boolean isNegative;
    public int length;

    // constructor
    public LongIntTest(String s) {
        if (s.substring(0, 1).equals("-")) {
            this.isNegative = true;
            this.length = s.length()-1;
        }
        else {
            this.isNegative = false;
            this.length = s.length();
        }
        this.value = new int[s.length()]; //new char[s.length()];

        // array is reversed
        // value[0] = s[last]
        for (int i=0; i<this.length; i++) {
            this.value[i] = s.charAt(s.length()-i-1) - '0';
        }

        this.removeLeadingZeros();
    }

    public LongIntTest(int[] value, boolean isNegative, int length) {
        this.value = value;
        this.isNegative = isNegative;
        this.length = length;

        this.removeLeadingZeros();
    }

    // returns 'this' + 'opnd'; Both inputs remain intact.
    public LongIntTest add(LongIntTest opnd) {
        if (this.isNegative && !opnd.isNegative) {
            return opnd.subtract(new LongIntTest(this.value, !this.isNegative, this.length));
        }
        else if (!this.isNegative && opnd.isNegative) {
            return this.subtract(new LongIntTest(opnd.value, !opnd.isNegative, opnd.length));
        }

        LongIntTest result; //= new LongIntTest(null, false, 0);
        int newLength = this.length > opnd.length ? this.length : opnd.length;
        newLength++;
        int carry = 0;
        int tmpResult = 0;

        int[] newValue = new int[newLength];

        for (int i=0; i<newLength; i++) {
            tmpResult = 0;
            if (this.length > i) tmpResult += this.value[i];
            if (opnd.length > i) tmpResult += opnd.value[i];
            tmpResult += carry;

            if (tmpResult > 9){
                carry = 1;
                newValue[i] = tmpResult%10;
            }
            else {
                carry = 0;
                newValue[i] = tmpResult;
            }
        }

        if (!this.isNegative && !opnd.isNegative) {
            result = new LongIntTest(newValue, false, newLength);
        }
        else {
            result = new LongIntTest(newValue, true, newLength);
        }
        result.removeLeadingZeros();
        return result;
    }

    // returns 'this' - 'opnd'; Both inputs remain intact.
    public LongIntTest subtract(LongIntTest opnd) {
        LongIntTest result;

        if (this.isNegative != opnd.isNegative) { // A -(-B) => A + B
            return this.add(new LongIntTest(opnd.value, !opnd.isNegative, opnd.length));
        }

        if (this.compareAbsoluteValueTo(opnd) == 0) return new LongIntTest("0"); // A - A => 0

            // A - B
            // (-A) - (-B)

        else if (this.isNegative && opnd.isNegative) {
            // -A - -B => -A + B => B - A
            LongIntTest a1 = new LongIntTest(this.value, !this.isNegative, this.length);
            LongIntTest a2 = new LongIntTest(opnd.value, !opnd.isNegative, opnd.length);
            result = a2.subtract(a1);
            return result;
        }

        else if (this.compareAbsoluteValueTo(opnd) < 0){ // A < B when A-B
            result = opnd.subtract(this);
            result.isNegative = !result.isNegative;
            return result;
        }

        // A - B

        int carry = 0;
        int tmpResult = 0;

        int[] newValue = new int[this.length];

        for (int i=0; i<this.length; i++) {
            tmpResult = this.value[i];
            if (opnd.length > i) tmpResult -= opnd.value[i];
            tmpResult += carry;
            newValue[i] = (tmpResult % 10);

            if (tmpResult < 0) {
                carry = -1;
                newValue[i] = (10 + tmpResult);
            }
            else {
                carry = 0;
                newValue[i] = tmpResult;
            }
        }
        if (carry < 0) {
            result = new LongIntTest(newValue, true, this.length);
        }
        else {
            result = new LongIntTest(newValue, false, this.length);
        }
        result.removeLeadingZeros();
        return result;
    }
    // returns 'this' * 'opnd'; Both inputs remain intact.
    public LongIntTest multiply(LongIntTest opnd) {
        LongIntTest result = new LongIntTest("0");
        LongIntTest tmpLongIntTest;
        int newLength = this.length + opnd.length + 1;
        int carry = 0;
        int tmpResult = 0;
        int[] tmpValue = new int[newLength];

        for (int j=0; j<opnd.length; j++) {
            tmpLongIntTest = this.multiplyOneDigit(opnd.value[j]);
            tmpLongIntTest.insertTralingZeros(j);
            result = result.add(tmpLongIntTest);
        }

        if (this.isNegative && opnd.isNegative || !this.isNegative && !opnd.isNegative) {
            result.isNegative = false;
        }
        else {
            result.isNegative = true;
        }
        result.removeLeadingZeros();
        return result;
    }

    // computes 1453*3, 1453*4, 1453*5, ...
    // opnd should be one digit number
    // only considers positive * positive
    public LongIntTest multiplyOneDigit(int opnd) {
        LongIntTest result; //= new LongIntTest(null, false, 0);
        int newLength = this.length + 1;
        int carry = 0;
        int tmpResult = 0;

        int[] newValue = new int[newLength];

        for (int i=0; i<this.length; i++) {
            tmpResult = this.value[i] * opnd + carry;
            carry = tmpResult / 10;
            newValue[i] = tmpResult % 10;
        }
        newValue[this.length] = carry;

        result = new LongIntTest(newValue, false, newLength);
        result.removeLeadingZeros();
        return result;
    }

    // removes leading zeros
    public void removeLeadingZeros() {
        if (this.length == 1) {
            if (this.value[0] == 0) {
                this.isNegative = false; // -0
                return;
            }
        }

        int tmp = this.length;

        for (int i=length-1; i>=0; i--){
            if (this.value[i] != 0 || tmp == 1) {
                break;
            }
            else {
                tmp--;
            }
        }

        this.value = Arrays.copyOf(this.value, tmp);
        this.length = tmp;

        if (this.length == 1) {
            if (this.value[0] == 0) {
                this.isNegative = false; // -0
            }
        }
    }

    public void insertTralingZeros(int n){
        if (n<=0) return;
        int newLength = this.length + n;
        int[] newValue = new int[newLength];
        for (int i=0; i<n; i++) {
            newValue[i] = 0;
        }
        for (int j=0; j<this.length; j++) {
            newValue[n+j] = this.value[j];
        }
        this.value = newValue;
        this.length = newLength;
    }

    // returns positive integer if this > opnd, negative if this < opnd, zero if this == opnd.
    // considers only absolute values, does not care about signs
    public int compareAbsoluteValueTo(LongIntTest opnd) {
        if (this.length != opnd.length) return this.length - opnd.length;
        else {
            for (int i=this.length-1; i>=0; i--) {
                if (this.value[i] != opnd.value[i]) return this.value[i] - opnd.value[i];
            }
        }
        return 0;
    }

    // print the value of 'this' element to the standard output.
    public void print() {
        if (this.isNegative){
            if (this.length!=1 || this.value[0]!=0) {
                System.out.print("-");
            }
        }
        for (int i=length-1; i>=0; i--){
            System.out.print(this.value[i]);
        }
    }

}


public class Test {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String str;
        str = scan.nextLine();
        LongIntTest a1 = new LongIntTest(str);
        str = scan.nextLine();
        LongIntTest a2 = new LongIntTest(str);

        LongIntTest b1 = a1.add(a2);
        LongIntTest b2 = a1.subtract(a2);
        LongIntTest b3 = a1.multiply(a2);
        b1.print();
        System.out.println();
        b2.print();
        System.out.println();
        b3.print();
    }
}

