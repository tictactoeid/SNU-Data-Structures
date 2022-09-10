// LongInt ADT for unbounded integers
import java.util.Arrays;

public class LongInt {
  //public char[] value;
  public int[] value;
  public boolean isNegative;
  public int length;
  // TODO: 00000011과 같이 여러 0으로 시작하는 경우 고려해야?

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
    LongInt result; //= new LongInt(null, false, 0);
    int newLength = this.length > opnd.length ? this.length : opnd.length;
    newLength++;
    int carry = 0;
    int tmpResult = 0;

    int[] newValue = new int[newLength];

    if (!this.isNegative && !opnd.isNegative) {
      for (int i=0; i<newLength; i++) {
        tmpResult = 0;
        if (this.value.length > i) tmpResult += this.value[i];
        if (opnd.value.length > i) tmpResult += opnd.value[i];
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
      result = new LongInt(newValue, false, newLength);
    }
    else if (this.isNegative && opnd.isNegative) {
      for (int i=0; i<newLength; i++) {
        tmpResult = 0;
        if (this.value.length > i) tmpResult += this.value[i];
        if (opnd.value.length > i) tmpResult += opnd.value[i];
        tmpResult += carry;

        newValue[i] = (tmpResult % 10);

        if (tmpResult > 9){
          carry = 1;
          tmpResult -= 10;
        }
        else {
          carry = 0;
        }
        newValue[i] = tmpResult;
      }
      result = new LongInt(newValue, true, newLength);
    }

    else if (this.isNegative){
      for (int i=0; i<newLength; i++) {
        tmpResult = 0;
        if (opnd.value.length <= i && this.value.length <= i) break;

        if (opnd.value.length > i) tmpResult += opnd.value[i];
        if (this.value.length > i) tmpResult -= this.value[i];
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
        result = new LongInt(newValue, true, newLength);
      }
      else {
        result = new LongInt(newValue, false, newLength);
      }

    }

    else {
      for (int i=0; i<newLength; i++) {
        tmpResult = 0;
        if (opnd.value.length <= i && this.value.length <= i) break;
        if (this.value.length > i) tmpResult += this.value[i];
        if (opnd.value.length > i) tmpResult -= opnd.value[i];
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
        result = new LongInt(newValue, true, newLength);
      }
      else {
        result = new LongInt(newValue, false, newLength);
      }
    }

    result.removeLeadingZeros();
    return result;
  }

  // returns 'this' - 'opnd'; Both inputs remain intact.
  public LongInt subtract(LongInt opnd) {
    LongInt newOpnd = new LongInt(opnd.value, !opnd.isNegative, opnd.length);
    return this.add(newOpnd);
  }

  // returns 'this' * 'opnd'; Both inputs remain intact.
  public LongInt multiply(LongInt opnd) {
    LongInt result = new LongInt("0");
    LongInt tmpLongInt;
    int newLength = this.length + opnd.length;
    int carry = 0;
    int tmpResult = 0;
    int[] tmpValue = new int[newLength];

    for (int i=0; i<this.length; i++) {
      tmpResult = 0;
      carry = 0;
      for (int k=0; k<i; k++) {
        tmpValue[k] = 0;
      }
      for (int j=0; j<opnd.length; j++) {
        tmpResult = this.value[i] * opnd.value[j] + carry;
        carry = tmpResult / 10;
        tmpResult %= 10;
        tmpValue[i+j] = tmpResult;
      }
      if (carry != 0) {
        tmpValue[this.length + opnd.length - 1] = carry;
      }
      tmpLongInt = new LongInt(tmpValue, false, newLength);
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

  // print the value of 'this' element to the standard output.
  public void print() {
    //this.removeLeadingZeros();
    if (this.isNegative){
      System.out.print("-");
    }
    for (int i=length-1; i>=0; i--){
      System.out.print(this.value[i]);
    }
  }

}

