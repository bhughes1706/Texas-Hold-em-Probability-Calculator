/*
  This class only holds the numerical value and suit of card
  value is 0 - 12. Add 2 for actual value and account for face cards
 */

public class card {
  protected int value;
  protected int suit;

  card(int suit_init, int value_init){
    value = value_init;
    suit = suit_init;
  }
  card(card to_copy){
    value = to_copy.value;
    suit = to_copy.suit;
  }
  protected void display() {
    System.out.print(" The " );

    // 9 + 2 = 11, which is the Jack. Default case is numbered card.
    switch(value){
      case 9: System.out.print("Jack "); break;
      case 10: System.out.print("Queen"); break;
      case 11: System.out.print("King"); break;
      case 12: System.out.print("Ace"); break;
      default: System.out.print((value + 2)); break;
    }

    switch(suit){
      case 0: System.out.print(" of Hearts"); break;
      case 1: System.out.print(" of Diamonds"); break;
      case 2: System.out.print(" of Clubs"); break;
      case 3: System.out.print(" of Spades"); break;
    }
  }
}