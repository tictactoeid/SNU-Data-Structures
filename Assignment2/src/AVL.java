public class AVL extends BST
{
  public AVL() { }

  private int height(Node n){
    if (n==null) return -1;
    return n.height;
  }

  private void updateHeights(Node n) {
    if (height(n.leftChild) > height(n.rightChild)) {
      n.height = 1+height(n.leftChild);
    }
    else {
      n.height = 1+height(n.rightChild);
    }

    //1+max(l, r) => faster?
  }

  private Node rotateLeft(Node n) {

  }

  private Node rotateRight(Node n) {

  }

  private Node rotateLeftRight(Node n) {

  }

  private Node rotateRightLeft(Node n) {

  }

  public void insert(String key) { }
 
}

