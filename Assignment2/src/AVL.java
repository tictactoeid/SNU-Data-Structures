public class AVL extends BST
{
  public AVL() {
    super();
  }

  private int height(Node n){
    if (n == null) return 0;
    return n.height;
  }

  private void updateHeight(Node n) {
    n.height = 1 + Math.max(height(n.leftChild), height(n.rightChild));
  }

  private Node rotateLeft(Node n) {
    Node newParent = n.rightChild; // new parent
    n.rightChild = newParent.leftChild;
    newParent.leftChild = n;

    updateHeight(n); // update new child first
    updateHeight(newParent); // and new parent
    return newParent;
  }

  private Node rotateRight(Node n) {
    Node newParent = n.leftChild;
    n.leftChild = newParent.rightChild;
    newParent.rightChild = n;

    updateHeight(n);
    updateHeight(newParent);
    return newParent;
  }

  private Node rotateLeftRight(Node n) {
    n.leftChild = rotateLeft(n.leftChild);
    return rotateRight(n);
  }

  private Node rotateRightLeft(Node n) {
    n.rightChild = rotateRight(n.rightChild);
    return rotateLeft(n);
  }

  public void insert(String key) {
    if (root == null) {
      root = new Node(key);
    }
    else {
      insert(root, key);
    }
  }

  private int balanceFactor(Node n) {
    if (n == null) return 0;
    return height(n.leftChild) - height(n.rightChild);
  }

  public Node insert(Node n, String key) {
    //System.out.println(key);
    // insert
    if (n == null) {
      n = new Node(key);
      // System.out.println(key);
      // nodes.add(n);
      return n;
    }

    if (key.equals(n.key)) {
      n.frequency += 1;
      return n;
    }
    else if (key.compareTo(n.key) > 0) {
      n.rightChild = insert(n.rightChild, key);
    }
    else if (key.compareTo(n.key) < 0){
      n.leftChild = insert(n.leftChild, key);
    }
    updateHeight(n);

    // rotate
    if (key.compareTo(n.key) > 0) {
      if (balanceFactor(n) <= -2) {
        if (key.compareTo(n.rightChild.key) > 0) {
          n = rotateLeft(n);
          // TODO: build fails when execute rotateLeft() ?
        } else if (key.compareTo(n.rightChild.key) < 0) {
          n = rotateRightLeft(n);
        }
      }
    }
    else if (key.compareTo(n.key) < 0) {
      if (balanceFactor(n) >= 2) {
        if (key.compareTo(n.leftChild.key) < 0) {
          n = rotateRight(n);
        } else if (key.compareTo(n.leftChild.key) > 0) {
          n = rotateLeftRight(n);
        }
      }
    }

    //updateHeight(n);
    return n;
  }
 
}

