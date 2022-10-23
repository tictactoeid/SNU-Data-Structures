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
  }

}


public class BST { // Binary Search Tree implementation
  ArrayList<Node> nodes;
  Node root;
  public BST() {
    nodes = new ArrayList<>();
    root = null;
  }

  public void insert(String key) {
    if (root == null) {
      root = new Node(key);
      nodes.add(root);
    }
    else {
      insert(root, key);
    }
  }

  public Node insert(Node n, String key) {
    // current node n
    if (n == null) {
      n = new Node(key);
      nodes.add(n);
      return n;
    }

    if (n.key.equals(key)) {
      n.frequency += 1;
      return n;
    }
    else if (n.key.compareTo(key) > 0) { // n.key > key; ex) B > A
      n.rightChild = insert(n.rightChild, key);
    }
    else {
      n.leftChild = insert(n.leftChild, key);
    }
    return n;
  }

  public boolean find(String key) {
    return find(root, key);
  }

  public boolean find(Node n, String key) {
    if (n == null) {
      return false;
    }
    n.accessCount += 1;
    if (n.key.equals(key)) {
      return true;
    }
    else if (n.key.compareTo(key) > 0) {
      return find(n.rightChild, key);
    }
    else {
      return find(n.leftChild, key);
    }
  }

  public int size() {
    return this.nodes.size();
  }

  public int sumFreq() {
    int sum = 0;
    for (Node n: nodes) {
      sum += n.frequency;
    }
    return sum;
  }
  public int sumProbes() {
    int sum = 0;
    for (Node n: nodes) {
      sum += n.accessCount;
    }
    return sum;
  }
  public void resetCounters() {
    for (Node n: nodes) {
      n.accessCount = 0;
      n.frequency = 1;
    }
  }

  public void print() {
    printInOrder(root);
  }

  public void printInOrder(Node n) {
    // left - root - right
    if (n == null) return;
    if (n.leftChild == null) {
      System.out.println("["+n.key+":"+n.frequency+":"+n.accessCount+"]");
      printInOrder(n.rightChild);
    }
    printInOrder(n.leftChild);
  }

}

