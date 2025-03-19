package pdp.scrabble.game.History;

import java.nio.file.Path;

import pdp.scrabble.errorsHandling.CustomError;
import pdp.scrabble.errorsHandling.ErrorType;
import pdp.scrabble.game.GameModel;

public class History<T> {
    private int nextNodeID;
    private HistNode<T> currentNode;
    private final HistNode<T> nullNode;
    private int lastSavedID;

    public History() {
        this.nextNodeID = 0;
        this.nullNode = new HistNode<>(this.giveNextID(), null, null);
        this.currentNode = this.nullNode;
        this.lastSavedID = -1; // never saved
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
     * add this checkpoint to the history,
     * will clear all the current redo
     * 
     * @param value the checkpoint to save
     */
    public void addCheckpoint(T value) {
        this.currentNode = this.currentNode.newNext(value, this.giveNextID());
    }

    /**
     * @return the checkpoint undone
     * @throws CustomError if there is no history available
     */
    public T undoOne() {
        HistNode<T> prev = this.currentNode.getPrevNode();
        if (prev == null) {
            throw new CustomError(
                    ErrorType.NoHistoryAvailable, "can't undo",
                    "no history to undo from currentNode: " + currentNode.getID());
        }
        T value = this.currentNode.getValue();
        this.currentNode = prev;
        return value;
    }

    /**
     * @return the checkpoint redone
     * @throws CustomError if there is no history available
     */
    public T redoOne() {
        HistNode<T> next = this.currentNode.getNextNode();
        if (next == null) {
            throw new CustomError(
                    ErrorType.NoHistoryAvailable, "can't redo",
                    "no history to redo from currentNode: " + currentNode.getID());
        }
        T value = this.currentNode.getValue();
        this.currentNode = next;
        return value;
    }

    public void saveCurrentToFile(Path filePath) {
        // get current value and serialize it
        // save it to the file
        // update this.lastSavedID with getCurrentNodeID
        throw new UnsupportedOperationException("Unimplemented method 'saveToFile'");
    }

    public void loadModelFromFile(Path filePath, GameModel gm) {
        // load from the file and deserialize it
        // loadGameModel from the loaded checkPoint
        // clear the history (the hist of a game isn't saved)
        throw new UnsupportedOperationException("Unimplemented method 'loadModelFromFile'");
    }

}
