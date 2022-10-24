public class AVL extends BST {
    public AVL() {
        super();
    }

    int height(Node root) {
        if (root == null)
            return 0;

        return root.height;
    }

    int findHeight() {
        return height(root);
    }

    int findHeightFrom(String value) {
        Node node = search(root, value);
        if (node == null)
            return -1;

        return node.height;
    }

    Node search(Node root, String value) {
        if (root == null)
            return null;
        else {
            if (value.equals(root.key))
                return root;
            else if (value.compareTo(root.key) < 0)
                return search(root.leftChild, value);
            else
                return search(root.rightChild, value);
        }
    }
    int max(int one, int two) {
        return (one > two) ? one : two;
    }

    Node rightRotate(Node root) {
        Node rootLeftChild = root.leftChild;
        root.leftChild = rootLeftChild.rightChild;
        rootLeftChild.rightChild = root;

        root.height = max(height(root.leftChild), height(root.rightChild)) + 1;
        rootLeftChild.height = max(height(rootLeftChild.leftChild), height(rootLeftChild.rightChild)) + 1;

        return rootLeftChild;
    }

    Node leftRotate(Node root) {
        Node rootRightChild = root.rightChild;
        root.rightChild = rootRightChild.leftChild;
        rootRightChild.leftChild = root;

        root.height = max(height(root.leftChild), height(root.rightChild)) + 1;
        rootRightChild.height = max(height(rootRightChild.leftChild), height(rootRightChild.rightChild)) + 1;

        return rootRightChild;
    }

    Node insertNode(Node root, String value) {
        if (root == null)
            root = new Node(value);
        else {
            if (value.compareTo(root.key) < 0)
                root.leftChild = insertNode(root.leftChild, value);
            else if (value.equals(root.key)) {
                root.frequency += 1;
                return root;
            }
            else
                root.rightChild = insertNode(root.rightChild, value);
        }

        root.height = max(height(root.leftChild), height(root.rightChild)) + 1;

        int balanceFactor = height(root.leftChild) - height(root.rightChild);

        if (balanceFactor > 1) {
            // either left-left case or left-right case
            if (value.compareTo(root.key) < 0) {
                // left-left case
                root = rightRotate(root);
            } else if (value.compareTo(root.key) > 0) {// 원래 else 였음
                // left-right case
                root.leftChild = leftRotate(root.leftChild);
                root = rightRotate(root);
            }
        } else if (balanceFactor < -1) {
            // either right-right case or right-left case
            if (value.compareTo(root.key) > 0) {
                // right-right case
                root = leftRotate(root);
            } else if (value.compareTo(root.key) < 0) {
                // right-left case
                root.rightChild = rightRotate(root.rightChild);
                root = leftRotate(root);
            }
        }

        return root;
    }

    public void insert(String value) {
        root = insertNode(root, value);
    }

    void inorder(Node root) {
        if (root != null) {
            inorder(root.leftChild);
            System.out.print(root.key + " ");
            inorder(root.rightChild);
        }
    }

    void inorderTraversal() {
        inorder(root);
        System.out.println();
    }

    void preorder(Node root) {
        if (root != null) {
            System.out.print(root.key + " ");
            preorder(root.leftChild);
            preorder(root.rightChild);
        }
    }

    void preorderTraversal() {
        preorder(root);
        System.out.println();
    }
}



/*
public class AVL extends BST
{
  public AVL() {
    super();
  }

  private int height(Node n){
    if (n == null) return 0;
    return n.height;
  }

  private Node rotateLeft(Node n) {
    Node oldRoot = new Node(n.key);
    oldRoot.rightChild = n.rightChild;
    oldRoot.leftChild = n.leftChild;

    Node newParent = oldRoot.rightChild; // new parent
    oldRoot.rightChild = newParent.leftChild;
    newParent.leftChild = oldRoot;

    oldRoot.height = Math.max(height(oldRoot.leftChild), height(oldRoot.rightChild)) + 1;
    newParent.height = Math.max(height(newParent.leftChild), height(newParent.rightChild)) + 1;

    return newParent;
  }

  private Node rotateRight(Node n) {
    Node oldRoot = new Node(n.key);
    oldRoot.rightChild = n.rightChild;
    oldRoot.leftChild = n.leftChild;

    Node newParent = oldRoot.leftChild;
    oldRoot.leftChild = newParent.rightChild;
    newParent.rightChild = oldRoot;

    oldRoot.height = Math.max(height(oldRoot.leftChild), height(oldRoot.rightChild)) + 1;
    newParent.height = Math.max(height(newParent.leftChild), height(newParent.rightChild)) + 1;
    return newParent;
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
    else if (key.compareTo(n.key) > 0) { // TODO
      n.rightChild = insert(n.rightChild, key);
    }
    else if (key.compareTo(n.key) < 0) {
      n.leftChild = insert(n.leftChild, key);
    }
    n.height = Math.max(height(root.leftChild), height(root.rightChild)) + 1;

    // rotate
    /*if (balanceFactor(n) >= 2) {
        if (key.compareTo(n.leftChild.key) < 0) {
            return rotateRight(n);
        }
        else if (key.compareTo(n.leftChild.key) > 0) {
            n.leftChild = rotateLeft(n.leftChild);
            return rotateRight(n);
        }
    }
    else if (balanceFactor(n) <= -2) {
        if (key.compareTo(n.rightChild.key) < 0) {
            n.rightChild = rotateRight(n.rightChild);
            return rotateLeft(n);
        }
        else if (key.compareTo(n.rightChild.key) > 0) {
            return rotateLeft(n);
        }
    }

      // use this
   int bf = balanceFactor(n);
   if (bf == 2) {
        if (balanceFactor(n.leftChild) == 1) {
            return rotateRight(n);
        }
        else if (balanceFactor(n.leftChild) == -1) {
            n.leftChild = rotateLeft(n.leftChild);
            return rotateRight(n);
        }
    }
    else if (bf == -2) {
        if (balanceFactor(n.rightChild) == -1) {
            return rotateLeft(n);
        }
        else if (balanceFactor(n.rightChild) == 1) {
            n.rightChild = rotateRight(n.rightChild);
            return rotateLeft(n);
        }
    }


    /* if (key.compareTo(n.key) > 0) {
      if (balanceFactor(n) == -2) {
        if (key.compareTo(n.key) > 0) {
          n = rotateLeft(n);
          // TODO: build fails when execute rotateLeft() ?
          // not works and don't know why.. maybe duplicated keys causes some problem
        }
        else if (key.compareTo(n.key) < 0) { // n.rightChild.key
            n.rightChild = rotateRight(n.rightChild);
            n = rotateLeft(n);
        }
      }
    }

    else if (key.compareTo(n.key) < 0) {
      if (balanceFactor(n) == 2) {
        if (key.compareTo(n.key) < 0) { // n.leftChild.key
          n = rotateRight(n);
        }
        else if (key.compareTo(n.key) > 0) { // n.leftChild.key
            n.leftChild = rotateLeft(n.leftChild);
            n = rotateRight(n);
        }
      }
    }

    //updateHeight(n);
    return n;
  }
 
}
*/

