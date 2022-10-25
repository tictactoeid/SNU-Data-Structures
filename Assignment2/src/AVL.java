public class AVL extends BST {
  public AVL() {
    super();
  }

  private int height(Node n) {
    if (n == null) return 0;
    return n.height;
  }

  private Node rotateLeft(Node n) {

    Node newParent = n.rightChild; // new parent
    n.rightChild = newParent.leftChild;
    newParent.leftChild = n;
    //n.rightChild = tmp;

    n.height = Math.max(height(n.leftChild), height(n.rightChild)) + 1;
    newParent.height = Math.max(height(newParent.leftChild), height(newParent.rightChild)) + 1;

    return newParent;

  }

  private Node rotateRight(Node n) {
    Node newParent = n.leftChild;
    n.leftChild = newParent.rightChild;
    newParent.rightChild = n;
    //n.leftChild = tmp;

    n.height = Math.max(height(n.leftChild), height(n.rightChild)) + 1;
    newParent.height = Math.max(height(newParent.leftChild), height(newParent.rightChild)) + 1;
    return newParent;

  }


  public void insert(String key) {
      root = insert(root, key);
  }

  private int balanceFactor(Node n) {
    if (n == null) return 0;
    return height(n.leftChild) - height(n.rightChild);
  }

  public Node insert(Node n, String key) {
    if (n == null) {
      return new Node(key);
    }

    if (key.compareTo(n.key) > 0) {
      n.rightChild = insert(n.rightChild, key);
    } else if (key.compareTo(n.key) < 0) {
      n.leftChild = insert(n.leftChild, key);
    } else {
      n.frequency += 1;
      return n;
    }

    n.height = Math.max(height(n.leftChild), height(n.rightChild)) + 1;
    int balanceFactor = balanceFactor(n);
        if (balanceFactor > 1) { // left
          if (key.compareTo(n.leftChild.key) < 0) { // left
              n = rotateRight(n);
            } else { //lr
                n.leftChild = rotateLeft(n.leftChild);
                n = rotateRight(n);
            }
        } else if (balanceFactor < -1) { // right
            if (key.compareTo(n.rightChild.key) > 0) {
              n = rotateLeft(n);
            } else { // rl
              n.rightChild = rotateRight(n.rightChild);
              n = rotateLeft(n);
            }
        }

    return n;
  }
}


