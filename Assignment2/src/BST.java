import java.util.ArrayList;

class Node {
  String key;
  Node leftChild;
  Node rightChild;
  int frequency;
  int accessCount;
  int height; // for AVL, not used in BST
  public Node(String key){
    this.key = key;
    this.leftChild = null;
    this.rightChild = null;
    this.frequency = 1;
    this.accessCount = 0;
    this.height = 1;
  }

  @Override
  public boolean equals(Object n) {
    if (!(n instanceof Node)) return false;
    return this.key.equals(((Node) n).key);
    /*if (this.key.equals(((Node) n).key)) {
      System.out.println("true");
      return true;
    }

    System.out.println("false");
    return false;
    //
    */

  }

}


public class BST { // Binary Search Tree implementation
  // ArrayList<Node> nodes;
  Node root;
  public BST() {
    // nodes = new ArrayList<>();
    root = null;
  }

  public void insert(String key) {
    if (root == null) {
      root = new Node(key);
      // nodes.add(root);
    }
    else {
      insert(root, key);
      // if (!nodes.contains(n)) nodes.add(n);

      // duplicate node?
    }
  }

  public Node insert(Node n, String key) {
    // current node n
    if (n == null) {
      n = new Node(key);
      // nodes.add(n);
      return n;
    }

    if (n.key.equals(key)) {
      n.frequency += 1;
      return n;
    }
    else if (key.compareTo(n.key) > 0) { // key > n.key; ex) B > A => key goes to right
      n.rightChild = insert(n.rightChild, key);
    }
    else {
      n.leftChild = insert(n.leftChild, key);
    }
    // nodes.add(n);
    return n;
  }

  public boolean find(String key) {
    if (find(root, key) == null) return false;
    return true;
    // return find(root, key);
  }

  public Node find(Node n, String key) { // public void find
    if (n == null) {
      return null;
    }
    n.accessCount += 1;
    if (key.equals(n.key)) {
      return n;
    }
    else if (key.compareTo(n.key) > 0) {
      return find(n.rightChild, key);
    }
    else {
      return find(n.leftChild, key);
    }
  }

  public int size() {
    // return this.nodes.size();
    return size(root);
  }
  public int size(Node n) {
    if (n == null) return 0;
    return 1 + size(n.leftChild) + size(n.rightChild);
  }

  public int sumFreq() {
    /*int sum = 0;
    for (Node n: nodes) {
      sum += n.frequency;
    }
    return sum;*/
    return sumFreq(root);
  }
  public int sumFreq(Node n) {
    if (n == null) return 0;
    return n.frequency + sumFreq(n.leftChild) + sumFreq(n.rightChild);
  }
  public int sumProbes() {
    /*int sum = 0;
    for (Node n: nodes) {
      sum += n.accessCount;
    }
    return sum;*/
    return sumProbes(root);
  }
  public int sumProbes(Node n) {
    if (n == null) return 0;
    return n.accessCount + sumProbes(n.leftChild) + sumProbes(n.rightChild);
  }
  public void resetCounters() {
    resetCounters(root);
    /*for (Node n: nodes) {
      n.accessCount = 0;
      n.frequency = 1;
    }*/
  }

  public void resetCounters(Node n) {
    if (n==null) return;
    resetCounters(n.leftChild);
    n.accessCount = 0;
    n.frequency = 1;
    resetCounters(n.rightChild);
  }

  public void print() {
    printInOrder(root);
  }

  public void printInOrder(Node n) {
    // left - root - right
    if (n != null) {
      printInOrder(n.leftChild);
      System.out.println("["+n.key+":"+n.frequency+":"+n.accessCount+"]");
      printInOrder(n.rightChild);
    }
    /* if (n.leftChild == null) {
      System.out.println("["+n.key+":"+n.frequency+":"+n.accessCount+"]");
      printInOrder(n.rightChild);
    }
    printInOrder(n.leftChild); */


  }

  public void printTree() {
    printTree(root, "", true);
  }
  private void printTree(Node currPtr, String indent, boolean last) {
    if (currPtr != null) {
      System.out.print(indent);
      if (last) {
        System.out.print("R----");
        indent += "   ";
      } else {
        System.out.print("L----");
        indent += "|  ";
      }
      System.out.println(currPtr.key);
      printTree(currPtr.leftChild, indent, false);
      printTree(currPtr.rightChild, indent, true);
    }
  }

}

