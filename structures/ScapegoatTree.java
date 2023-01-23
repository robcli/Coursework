package structures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class ScapegoatTree<T extends Comparable<T>> extends BinarySearchTree<T> {
  private int upperBound;
  private final double twoThird = ((double) 2)/3;

  @Override
  public void add(T t) {
    upperBound++;
    super.add(t);
    if (this.height() > Math.log(upperBound) / Math.log(1.5)) {
      BSTNode<T> scapegoat = scapegoatFinder(findDeepestNode(this.getRoot()), new double[upperBound+1], 0);
      Iterator<T> preorderIter = preorderIterator(scapegoat);
      BinarySearchTree<T> scapeTree = new BinarySearchTree<T>();
      while(preorderIter.hasNext()){
        scapeTree.add(preorderIter.next());
      }
      scapeTree.balance();
      insertScapeTree(scapeTree, scapegoat);
    }
  }

  private Iterator<T> preorderIterator(BSTNode<T> subRoot){
    Queue<T> queue = new LinkedList<T>();
    preorderTraversal(queue, subRoot);
    return queue.iterator();
  }

  private BSTNode<T> findDeepestNode(BSTNode<T> root){
    Queue<BSTNode<T>> queue = new LinkedList<BSTNode<T>>();
    BSTNode<T> temp = null;
    queue.add(root);
    while(!queue.isEmpty()){
      temp = queue.remove();
      if(temp.getLeft() != null)
        queue.add(temp.getLeft());
      if(temp.getRight() != null)
        queue.add(temp.getRight());
    }
    return temp;
  }

  private BSTNode<T> scapegoatFinder(BSTNode<T> node, double[] memo, int index){
    if (index == 0){
      memo[index] = subtreeSize(node);
      index++;
    } 
    if (node.getParent() == null)
      return node;
    memo[index] = subtreeSize(node.getParent());
    if(memo[index] != 0){
      if(memo[index-1] / memo[index] > twoThird)
        return node.getParent();
    }
    return scapegoatFinder(node.getParent(), memo, index+1);
  }

  private void insertScapeTree(BinarySearchTree<T> scapeTree, BSTNode<T> scapegoat){
    BSTNode<T> parent = scapegoat.getParent();
    if (parent == null){
      this.setRoot(scapeTree.getRoot());
      scapeTree.setRoot(this.getRoot());
    }
    else if (scapegoat == parent.getLeft()){
      parent.setLeft(scapeTree.getRoot());
      scapeTree.setRoot(this.getRoot());
    }
    else{
      parent.setRight(scapeTree.getRoot());
      scapeTree.setRoot(this.getRoot());
    }
  }


  @Override
  public boolean remove(T element) {
    boolean result = super.remove(element);
    if (upperBound > 2*size()){
      this.balance();
      upperBound = size();
    }
    return result;
  }

  public static void main(String[] args) {
    BSTInterface<Integer> tree = new ScapegoatTree<Integer>();
    BSTInterface<Character> charTree = new BinarySearchTree<Character>();
    /*
    You can test your Scapegoat tree here.
    for (String r : new String[] {"0", "1", "2", "3", "4"}) {
      tree.add(r);
      System.out.println(toDotFormat(tree.getRoot()));
    }
    */

    for (char i = 'a'; i < 'f'; i++) {
			charTree.add(i);
		}
		charTree.balance();
		BinarySearchTree<Character> comparisonTree = new BinarySearchTree<Character>();
		for (char i = 'a'; i < 'f'; i++) {
			comparisonTree.add(i);
		}
  }
}
