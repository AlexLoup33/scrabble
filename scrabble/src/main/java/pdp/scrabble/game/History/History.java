package pdp.scrabble.game.History;

import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.actions.*;

public class History<T> {
  private int nextNodeID;
  private HistNode<T> currentNode;
  private final HistNode<T> nullNode;

  public History() {
    this.nextNodeID = 0;
    this.nullNode = new HistNode<>(this.giveNextID(), null, null);
    this.currentNode = this.nullNode;
  }

  /**
   * @return return an ID for a new node (update the nextID)
   */
  private int giveNextID() {
    int nodeID = this.nextNodeID;
    this.nextNodeID++;
    return nodeID;
  }

  /**
   * @return the ID of the current node when the Hist is empty
   */
  public int getEmptyHistNodeID() {
    return this.nullNode.getID();
  }

  /**
   * @return the ID of the current node
   */
  public int getCurrentNodeID() {
    return this.currentNode.getID();
  }

  /**
   * add the action played to the history, will clear all the current redo
   *
   * @param value the action to save
   */
  public void addPlayerdAction(T value) {
    this.currentNode = this.currentNode.newNext(value, this.giveNextID());
  }

  /**
   * @return the action to undo
   * @throws CustomError (NoHistoryAvailable) if there is no history available
   */
  public T undoOne() {
    HistNode<T> prev = this.currentNode.getPrevNode();
    if (prev == null) {
      throw new CustomError(
          ErrorType.NoHistoryAvailable,
          "can't undo",
          "no history to undo from currentNode: " + currentNode.getID());
    }
    T value = this.currentNode.getValue();
    this.currentNode = prev;
    return value;
  }

  /**
   * @return the action to redo
   * @throws CustomError (NoHistoryAvailable) if there is no history available
   */
  public T redoOne() {
    HistNode<T> next = this.currentNode.getNextNode();
    if (next == null) {
      throw new CustomError(
          ErrorType.NoHistoryAvailable,
          "can't redo",
          "no history to redo from currentNode: " + currentNode.getID());
    }
    T value = this.currentNode.getValue();
    this.currentNode = next;
    return value;
  }

  public String toSerializedString() {
    HistNode<T> cur = this.currentNode;
    // get to first node, we build the string in insertion order
    // because it is annoying to get decent String building performance in reverse
    while (cur.getPrevNode() != nullNode) {
      cur = cur.getPrevNode();
    }

    int curTurn = 0;
    StringBuilder serialized = new StringBuilder();
    while (cur != null) {
      GameAction curAction = (GameAction) cur.getValue();
      boolean first = (curTurn - curAction.turn) != 0;
      curTurn = first ? curAction.turn : curTurn;
      String toAppend = curAction.toSerializedString();
      String lineBefore = "";
      if (first && curTurn != 1) {
        serialized.deleteCharAt(serialized.length() - 1);
        lineBefore = ";\n";
      }
      if (!first) toAppend = toAppend.substring(toAppend.indexOf('.') + 2); // removing ". "
      serialized.append(lineBefore + toAppend + " ");
      cur = cur.getNextNode();
    }
    if (curTurn != 0) {
      serialized.deleteCharAt(serialized.length() - 1);
    }
    return serialized.toString() + ";";
  }
}
