package pdp.scrabble.game;

/*Direction to place a word. just vertical or horizontal for the scrabble */
public enum Direction {
  Down,
  Right;

  public Direction getOppositDirection() {
    return (this == Down) ? Right : Down;
  }
}
