public class card {
  private int value;
  private int suit;

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