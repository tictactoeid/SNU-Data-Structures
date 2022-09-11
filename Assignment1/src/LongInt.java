// LongInt ADT for unbounded integers
import java.util.Arrays;

public class LongInt {
  //public char[] value;
  public int[] value;
  public boolean isNegative;
  public int length;

  // constructor
  public LongInt(String s) {
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

  public LongInt(int[] value, boolean isNegative, int length) {
    this.value = value;
    this.isNegative = isNegative;
    this.length = length;

    this.removeLeadingZeros();
  }

  // returns 'this' + 'opnd'; Both inputs remain intact.
  public LongInt add(LongInt opnd) {
    if (this.isNegative && !opnd.isNegative) {
      return opnd.subtract(new LongInt(this.value, !this.isNegative, this.length));
    }
    else if (!this.isNegative && opnd.isNegative) {
      return this.subtract(new LongInt(opnd.value, !opnd.isNegative, opnd.length));
    }

    LongInt result; //= new LongInt(null, false, 0);
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
      result = new LongInt(newValue, false, newLength);
    }
    else {
      result = new LongInt(newValue, true, newLength);
    }
    result.removeLeadingZeros();
    return result;
  }

  // returns 'this' - 'opnd'; Both inputs remain intact.
  public LongInt subtract(LongInt opnd) {
    LongInt result;

    if (this.isNegative != opnd.isNegative) { // A -(-B) => A + B
      return this.add(new LongInt(opnd.value, !opnd.isNegative, opnd.length));
    }
    if (this.compareAbsoluteValueTo(opnd) == 0) return new LongInt("0"); // A - A => 0
    else if (this.compareAbsoluteValueTo(opnd) < 0){ // -A+B => -(A-B) when A>B
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
      result = new LongInt(newValue, true, this.length);
    }
    else {
      result = new LongInt(newValue, false, this.length);
    }
    result.removeLeadingZeros();
    return result;
  }
  // returns 'this' * 'opnd'; Both inputs remain intact.
  public LongInt multiply(LongInt opnd) {
    LongInt result = new LongInt("0");
    LongInt tmpLongInt;
    int newLength = this.length + opnd.length + 1;
    int carry = 0;
    int tmpResult = 0;
    int[] tmpValue = new int[newLength];

    for (int j=0; j<opnd.length; j++) {
      tmpLongInt = this.multiplyOneDigit(opnd.value[j]);
      tmpLongInt.insertTralingZeros(j);
      result = result.add(tmpLongInt);
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
  public LongInt multiplyOneDigit(int opnd) {
    LongInt result; //= new LongInt(null, false, 0);
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

    result = new LongInt(newValue, false, newLength);
    result.removeLeadingZeros();
    return result;
  }

  // removes leading zeros
  public void removeLeadingZeros() {
    if (this.length == 1) return;

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
  public int compareAbsoluteValueTo(LongInt opnd) {
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
      System.out.print("-");
    }
    for (int i=length-1; i>=0; i--){
      System.out.print(this.value[i]);
    }
  }

}

