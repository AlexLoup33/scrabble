package pdp.scrabble.game;

public enum Direction {
  Down,
  Right;

  public Direction getOppositDirection() {
    return (this == Down) ? Right : Down;
  }
}
