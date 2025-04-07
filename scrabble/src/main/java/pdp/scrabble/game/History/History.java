package pdp.scrabble.game.History;

import java.util.LinkedList;
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

  public void clear() {
    this.currentNode = this.nullNode;
    this.nullNode.detach();
  }

  /**
   * @return the ID of the current node when the Hist is empty
   */
  private int getEmptyHistNodeID() {
    return this.nullNode.getID();
  }

  /**
   * @return the ID of the current node
   */
  private int getCurrentNodeID() {
    return this.currentNode.getID();
  }

  /**
   * add the action played to the history, will clear all the current redo
   *
   * @param value the action to save
   */
  public void addPlayedAction(T value) {
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
    this.currentNode = next;
    return this.currentNode.getValue();
  }

  public String toSerializedString() {
    LinkedList<T> actions = this.getCurrentActionsList();
    if (actions.isEmpty()) {
      return "";
    }

    StringBuilder serialized = new StringBuilder();
    int prevTurn = -1;
    for (T value : actions) {
      GameAction gameAction = (GameAction) value;
      boolean firstPlayerOfTurn = (prevTurn != gameAction.turn);
      if (firstPlayerOfTurn) {
        // add the beginning of the line
        if (prevTurn != -1) {
          // => not the first turn
          serialized.append(";\n");
        }
        serialized.append(gameAction.turn).append(". ");
      } else {
        // spacing between actions of a gameTurn
        serialized.append(" ");
      }
      // add the action
      serialized.append(gameAction.toSerializedString());
      prevTurn = gameAction.turn;
    }
    return serialized.toString() + ";";
  }

  /***
   * @return the list of all action up to the current point in the history
   *         (included)
   */
  public LinkedList<T> getCurrentActionsList() {
    HistNode<T> node = this.currentNode;
    LinkedList<T> lst = new LinkedList<>();
    while (node != this.nullNode) {
      lst.addFirst(node.getValue());
      node = node.getPrevNode();
    }
    return lst;
  }

  public boolean canUndo() {
    return (this.getCurrentNodeID() != this.getEmptyHistNodeID());
  }
}
