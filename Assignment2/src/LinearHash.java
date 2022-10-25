import java.util.*;

public class LinearHash {
  ArrayList<LinkedList<String>> hashtable;
  int splitindex;
  int hashFunctionValue; // for h_k = MyUtil.ELFhash(s, 2^k)


  public LinearHash(int HTinitSize) {
    this.hashtable = new ArrayList<LinkedList<String>>(HTinitSize);
    this.splitindex = 0;
    this.hashFunctionValue =HTinitSize;

    for (int i=0; i<HTinitSize; i++) {
      hashtable.add(new LinkedList<String>());
    }

  }	// constructor for the Hash table

  public int insertUnique(String word) {
    int hashBase = (int) MyUtil.ELFhash(word, hashFunctionValue);
    int hashExtended = (int) MyUtil.ELFhash(word, hashFunctionValue * 2);

    // cannot use long because of ArrayList idx range
    if (hashBase >= splitindex) {
      if (hashtable.get(hashBase).contains(word)) return -1;

      if (!hashtable.get(hashBase).isEmpty()) { // collision
        hashtable.get(hashBase).add(word);

        LinkedList<String> splitBase = new LinkedList<>();
        hashtable.add(new LinkedList<>());
        for (String s: hashtable.get(splitindex)) {
          if ((int) MyUtil.ELFhash(s, hashFunctionValue*2) == splitindex) splitBase.add(s);
          else {
            hashtable.get((int) MyUtil.ELFhash(s, hashFunctionValue*2)).add(s);
          }
        }
        hashtable.set(splitindex, splitBase);
        splitindex++;
        if (splitindex == this.hashFunctionValue) { // 2^k collisions
          this.hashFunctionValue *= 2;
          this.splitindex = 0;
        }
      }
      else {
        hashtable.get(hashBase).add(word);
      }
      return hashBase;
    }
    else {
      if (hashtable.get(hashExtended).contains(word)) return -1;
      if (!hashtable.get(hashExtended).isEmpty()) { // collision
        hashtable.get(hashExtended).add(word);

        LinkedList<String> splitBase = new LinkedList<>();
        hashtable.add(new LinkedList<>());
        for (String s: hashtable.get(splitindex)) {
          if ((int) MyUtil.ELFhash(s, hashFunctionValue*2) == splitindex) {
            // ith -> ith
            splitBase.add(s);
          }
          else {
            // ith -> i +2^k th
            hashtable.get((int) MyUtil.ELFhash(s, hashFunctionValue*2)).add(s);
          }
        }
        hashtable.set(splitindex, splitBase);
        splitindex++;
        if (splitindex == this.hashFunctionValue) { // 2^k collisions
          this.hashFunctionValue *= 2;
          this.splitindex = 0;
        }
      }
      else {
        hashtable.get(hashExtended).add(word);
      }
      return hashExtended;
    }
  } // insert `word' to the Hash table.

  public int lookup(String word) {
    int hash = (int) MyUtil.ELFhash(word, this.hashFunctionValue);
    int hashExtended = (int) MyUtil.ELFhash(word, this.hashFunctionValue * 2);
    for (String s: hashtable.get(hash)) {
      if (word.equals(s)) {
        return hashtable.get(hash).size();
      }
    }
    if (hashtable.size() > hashExtended) {
      for (String s: hashtable.get(hashExtended)) {
        if (word.equals(s)) {
          return hashtable.get(hashExtended).size();
        }
      }
      return -1 * hashtable.get(hashExtended).size();
    }
    return -1 * hashtable.get(hash).size();
  } // look up `word' in the Hash table.

  public int wordCount() {
    int cnt = 0;
    for (LinkedList<String> entry: hashtable) {
      cnt += entry.size();
    }
    return cnt;
  }
  public int emptyCount() {
    int cnt = 0;
    for (LinkedList<String> entry: hashtable) {
      if (entry.isEmpty()) cnt++;
    }
    return cnt;
  }
  public int size() {
    return hashtable.size();
  }			// 2^k + collisions in the current round
  public void print() {
    for (int i=0; i<hashtable.size(); i++) {
      System.out.print("["+i+":");
      ArrayList<String> entry = new ArrayList(hashtable.get(i));
      Collections.sort(entry);
      for (String s: entry) {
        System.out.print(" "+s);
      }
      System.out.println("]");

    }
  }   	// Print keys in the hash table

}
