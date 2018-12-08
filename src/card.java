import java.util.Random;

public class card {
  protected int value;
  protected int suit;
  private final int max_val = 13;
  private final int max_suit = 4;

  protected void deal() {
    Random rand = new Random();
    value = rand.nextInt(max_val) + 2;
    suit = rand.nextInt(max_suit) + 1;
  }
  //displays value of card, must be done before suit
  protected void display() {
    if (value < 11)
      System.out.print("\n" + value);
    else {
      switch (value) {
        case 11: System.out.print("\nJack"); break;
        case 12: System.out.print("\nQueen"); break;
        case 13: System.out.print("\nKing"); break;
        case 14: System.out.print("\nAce"); break;
      }
    }

    switch (suit) {
      case 1: System.out.print(" of Hearts."); break;
      case 2: System.out.print(" of Diamonds."); break;
      case 3: System.out.print(" of Spades."); break;
      case 4: System.out.print(" of Clubs."); break;
    }
  }
}
