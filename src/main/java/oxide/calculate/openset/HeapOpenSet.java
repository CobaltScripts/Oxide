package oxide.calculate.openset;

import java.util.Arrays;

import oxide.calculate.PathNode;

public class HeapOpenSet {

  private static final int DEFAULT_CAPACITY = 1024;

  private PathNode[] array;
  private int size;

  public HeapOpenSet() {
    this(DEFAULT_CAPACITY);
  }

  public HeapOpenSet(final int capacity) {
    array = new PathNode[capacity];
  }

  public void add(final PathNode n) {
    if (isFull()) {
      array = Arrays.copyOf(array, array.length << 1);
    }

    size++;
    array[size] = n;
    n.setHeapPosition(size);
    update(n);
  }

  public void update(final PathNode n) {
    int index = n.getHeapPosition();

    while (index > 1) {
      final int parentIndex = index >>> 1;
      final PathNode parent = array[parentIndex];

      if (compare(parent, n) <= 0) {
        break;
      }

      array[index] = parent;
      array[parentIndex] = n;
      n.setHeapPosition(parentIndex);
      parent.setHeapPosition(index);
      index = parentIndex;
    }
  }

  public PathNode poll() {
    final PathNode node = array[1];
    final PathNode n = array[size];

    array[1] = n;
    array[size] = null;
    n.setHeapPosition(1);
    node.setHeapPosition(-1);
    size--;

    if (size < 2) {
      return node;
    }

    int index = 1;

    while (true) {
      int childIndex = index << 1;

      if (childIndex > size) {
        break;
      }

      PathNode child = array[childIndex];
      double childCost = child.getTotalCost();

      if (childIndex < size) {
        final PathNode rightChild = array[childIndex + 1];
        final double rightCost = rightChild.getTotalCost();

        if (childCost > rightCost) {
          childIndex++;
          child = rightChild;
          childCost = rightCost;
        }
      }

      if (compare(n, child) <= 0) {
        break;
      }

      array[index] = child;
      array[childIndex] = n;
      n.setHeapPosition(childIndex);
      child.setHeapPosition(index);
      index = childIndex;
    }

    return node;
  }

  public boolean isFull() {
    return size >= array.length - 1;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  private static int compare(final PathNode first, final PathNode second) {
    final int cost = Double.compare(first.getTotalCost(), second.getTotalCost());

    if (cost != 0) {
      return cost;
    }

    return Integer.compare(first.getTurns(), second.getTurns());
  }
}
