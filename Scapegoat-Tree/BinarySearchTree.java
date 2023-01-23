package structures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<T extends Comparable<T>> implements BSTInterface<T> {
  protected BSTNode<T> root;

  public boolean isEmpty() {
    return root == null;
  }

  public int size() {
    return subtreeSize(root);
  }

  protected int subtreeSize(BSTNode<T> node) {
    if (node == null) {
      return 0;
    } else {
      return 1 + subtreeSize(node.getLeft()) + subtreeSize(node.getRight());
    }
  }

  public boolean contains(T t) {
    if (t == null)
      throw new NullPointerException();
    return (recursiveSearcher(t, root) != null);
  }

  private BSTNode<T> recursiveSearcher(T key, BSTNode<T> node){
    if (node == null)
      return null;
    if (node.getData().equals(key))
      return node;
    if (node.getData().compareTo(key) > 0)
      return recursiveSearcher(key, node.getLeft());
    return recursiveSearcher(key, node.getRight());
  }

  public boolean remove(T t) {
    if (t == null) {
      throw new NullPointerException();
    }
    boolean result = contains(t);
    if (result) {
      root = removeFromSubtree(root, t);
    }
    return result;
  }

  private BSTNode<T> removeFromSubtree(BSTNode<T> node, T t) {
    // node must not be null
    int result = t.compareTo(node.getData());
    if (result < 0) {
      node.setLeft(removeFromSubtree(node.getLeft(), t));
      return node;
    } else if (result > 0) {
      node.setRight(removeFromSubtree(node.getRight(), t));
      return node;
    } else { // result == 0
      if (node.getLeft() == null) {
        return node.getRight();
      } else if (node.getRight() == null) {
        return node.getLeft();
      } else { // neither child is null
        T predecessorValue = getHighestValue(node.getLeft());
        node.setLeft(removeRightmost(node.getLeft()));
        node.setData(predecessorValue);
        return node;
      }
    }
  }

  private T getHighestValue(BSTNode<T> node) {
    // node must not be null
    if (node.getRight() == null) {
      return node.getData();
    } else {
      return getHighestValue(node.getRight());
    }
  }

  private BSTNode<T> removeRightmost(BSTNode<T> node) {
    // node must not be null
    if (node.getRight() == null) {
      return node.getLeft();
    } else {
      node.setRight(removeRightmost(node.getRight()));
      return node;
    }
  }

  public T get(T t) {
    if (t == null)
      throw new NullPointerException();
    BSTNode<T> node = recursiveSearcher(t, root);
    return (node == null) ? null : node.getData();
  }


  public void add(T t) {
    if (t == null) {
      throw new NullPointerException();
    }
    root = addToSubtree(root, new BSTNode<T>(t, null, null));
  }

  protected BSTNode<T> addToSubtree(BSTNode<T> node, BSTNode<T> toAdd) {
    if (node == null) {
      return toAdd;
    }
    int result = toAdd.getData().compareTo(node.getData());
    if (result <= 0) {
      node.setLeft(addToSubtree(node.getLeft(), toAdd));
    } else {
      node.setRight(addToSubtree(node.getRight(), toAdd));
    }
    return node;
  }

  @Override
  public T getMinimum() {
    BSTNode<T> node = root;
    while(node != null && node.getLeft() != null){
      node = node.getLeft();
    }
    return (isEmpty()) ? null : node.getData();
  }


  @Override
  public T getMaximum() {
    BSTNode<T> node = root;
    while(node != null && node.getRight() != null){
      node = node.getRight();
    }
    return (isEmpty()) ? null : node.getData();
  }


  @Override
  public int height() {
    return recursiveHeight(root)-1;
  }

  private int recursiveHeight(BSTNode<T> node){
    if (node == null)
      return 0;
    return 1 + Math.max(recursiveHeight(node.getLeft()), recursiveHeight(node.getRight()));
  }


  public Iterator<T> preorderIterator() {
    Queue<T> queue = new LinkedList<T>();
    preorderTraversal(queue, root);
    return queue.iterator();
  }

  protected void preorderTraversal(Queue<T> queue, BSTNode<T> node) {
    if (node != null){
      queue.add(node.getData());
      preorderTraversal(queue, node.getLeft());
      preorderTraversal(queue, node.getRight());
    }
  }


  public Iterator<T> inorderIterator() {
    Queue<T> queue = new LinkedList<T>();
    inorderTraverse(queue, root);
    return queue.iterator();
  }


  private void inorderTraverse(Queue<T> queue, BSTNode<T> node) {
    if (node != null) {
      inorderTraverse(queue, node.getLeft());
      queue.add(node.getData());
      inorderTraverse(queue, node.getRight());
    }
  }

  public Iterator<T> postorderIterator() {
    Queue<T> queue = new LinkedList<T>();
    postorderTraversal(queue, root);
    return queue.iterator();
  }

  private void postorderTraversal(Queue<T> queue, BSTNode<T> node){
    if (node != null){
      postorderTraversal(queue, node.getLeft());
      postorderTraversal(queue, node.getRight());
      queue.add(node.getData());
    }
  }


  @Override
  public boolean equals(BSTInterface<T> other) {
    if (other == null)
      throw new NullPointerException();
    Iterator<T> thisIter = this.preorderIterator();
    Iterator<T> otherIter = other.preorderIterator();
    while(thisIter.hasNext() && otherIter.hasNext()){
      if (!thisIter.next().equals(otherIter.next()))
        return false;
    }
    return (thisIter.hasNext() || otherIter.hasNext()) ? false : true;
  }


  @Override
  public boolean sameValues(BSTInterface<T> other) {
    if (other == null)
      throw new NullPointerException();
    Iterator<T> thisIter = this.inorderIterator();
    while(thisIter.hasNext()){
      if(!other.contains(thisIter.next()))
        return false;
    }
    Iterator<T> otherIter = other.inorderIterator();
    while(otherIter.hasNext()){
      if(!this.contains(otherIter.next()))
        return false;
    }
    return true;
  }

  @Override
  public boolean isBalanced() {
    if (isEmpty())
      return true;
    int h = this.height();
    int n = this.size();
    return (Math.pow(2, h) <= n && n < Math.pow(2, h+1)) ? true : false;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void balance() {
    Iterator<T> inorderIter = this.inorderIterator();
    int size = size();
    T[] values = (T[]) new Comparable[size];
    for (int i = 0; i < size; i ++){
      values[i] = inorderIter.next();
    }
    this.root = balanceHelper(values, 0, size-1, new BinarySearchTree<T>());
  }

  private BSTNode<T> balanceHelper(T[] values, int low, int high, BinarySearchTree<T> tree){
    if (high == -1)
      return tree.root;
    if (low == high)
      tree.add(values[low]);
    else if (low+1 == high){
      tree.add(values[low]);
      tree.add(values[high]);
    }
    else{
      int mid = (low+high)/2;
      tree.add(values[mid]);
      balanceHelper(values, low, mid-1, tree);
      balanceHelper(values, mid+1, high, tree);
    }
    return tree.root;
  }


  @Override
  public BSTNode<T> getRoot() {
    // DO NOT MODIFY
    return root;
  }

  protected void setRoot(BSTNode<T> newRoot) {
    this.root = newRoot;
  }

  public static <T extends Comparable<T>> String toDotFormat(BSTNode<T> root) {
    // header
    int count = 0;
    String dot = "digraph G { \n";
    dot += "graph [ordering=\"out\"]; \n";
    // iterative traversal
    Queue<BSTNode<T>> queue = new LinkedList<BSTNode<T>>();
    queue.add(root);
    BSTNode<T> cursor;
    while (!queue.isEmpty()) {
      cursor = queue.remove();
      if (cursor.getLeft() != null) {
        // add edge from cursor to left child
        dot += cursor.getData().toString() + " -> " + cursor.getLeft().getData().toString() + ";\n";
        queue.add(cursor.getLeft());
      } else {
        // add dummy node
        dot += "node" + count + " [shape=point];\n";
        dot += cursor.getData().toString() + " -> " + "node" + count + ";\n";
        count++;
      }
      if (cursor.getRight() != null) {
        // add edge from cursor to right child
        dot +=
            cursor.getData().toString() + " -> " + cursor.getRight().getData().toString() + ";\n";
        queue.add(cursor.getRight());
      } else {
        // add dummy node
        dot += "node" + count + " [shape=point];\n";
        dot += cursor.getData().toString() + " -> " + "node" + count + ";\n";
        count++;
      }
    }
    dot += "};";
    return dot;
  }

  public static void main(String[] args) {
    for (String r : new String[] {"a", "b", "c", "d", "e", "f", "g"}) {
      BSTInterface<String> tree = new BinarySearchTree<String>();
      for (String s : new String[] {"d", "b", "a", "c", "f", "e", "g"}) {
        tree.add(s);
      }
      Iterator<String> iterator = tree.inorderIterator();
      while (iterator.hasNext()) {
        System.out.print(iterator.next());
      }
      System.out.println();
      iterator = tree.preorderIterator();
      while (iterator.hasNext()) {
        System.out.print(iterator.next());
      }
      System.out.println();
      iterator = tree.postorderIterator();
      while (iterator.hasNext()) {
        System.out.print(iterator.next());
      }
      System.out.println();

      System.out.println(tree.remove(r));

      iterator = tree.inorderIterator();
      while (iterator.hasNext()) {
        System.out.print(iterator.next());
      }
      System.out.println();
    }

    BSTInterface<String> tree = new BinarySearchTree<String>();
    for (String r : new String[] {"a", "b", "c", "d", "e", "f", "g"}) {
      tree.add(r);
    }
    System.out.println(toDotFormat(tree.getRoot()));
    System.out.println(tree.size());
    System.out.println(tree.height());
    System.out.println(tree.isBalanced());
    tree.balance();
    System.out.println(tree.size());
    System.out.println(tree.height());
    System.out.println(tree.isBalanced());
  }
}
