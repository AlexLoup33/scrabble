package pdp.scrabble.game.History;

class HistNode<T> {
  private int nodeID;
  private HistNode<T> prev;
  private HistNode<T> next;
  private T value;

  public HistNode(int nodeID, T value, HistNode<T> prev) {
    this.nodeID = nodeID;
    this.value = value;
    this.prev = prev;
    this.next = null;
  }

  public T getValue() {
    return this.value;
  }

  public HistNode<T> getPrevNode() {
    return this.prev;
  }

  public HistNode<T> getNextNode() {
    return this.next;
  }

  public int getID() {
    return this.nodeID;
  }

  public HistNode<T> newNext(T val, int nextNodeID) {
    HistNode<T> newNode = new HistNode<T>(nextNodeID, val, this);
    // detech this.next.prev (for safety only)
    if (this.next != null) {
      this.next.prev = null;
    }
    this.next = newNode;
    return newNode;
  }
}
