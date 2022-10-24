public class Node {
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
    public void setFrequency(int n) {
        this.frequency = n;
    }

}