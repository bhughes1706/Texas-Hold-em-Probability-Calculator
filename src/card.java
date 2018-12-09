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
    System.out.print("\nThe " );
    switch(value){
      case 9: System.out.print("The Jack "); break;
      case 10: System.out.print("The Queen"); break;
      case 11: System.out.print("The King"); break;
      case 12: System.out.print("The Ace"); break;
      default: System.out.print("The " + (value + 2)); break;
    }
    switch(suit){
      case 0: System.out.print(" of Hearts"); break;
      case 1: System.out.print(" of Diamonds"); break;
      case 2: System.out.print(" of Clubs"); break;
      case 3: System.out.print(" of Spades"); break;
    }
  }
}