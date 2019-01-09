/*
  This class only holds the numerical value and suit of card
  value is 0 - 12. Add 2 for actual value and account for face cards

  Suit 1: Hearts // 2: Diamonds // 3: Clubs // 4: Spades
 */

public class card {
  protected int rank;
  protected int suit;

  //constructor - used when adding a new card during deck initialization
  card(int suit_init, int rank_init){
    rank = rank_init;
    suit = suit_init;
  }

  //copy constructor, used when copying a card from the deck
  card(card to_copy){
    rank = to_copy.rank;
    suit = to_copy.suit;
  }

  //for displaying one single card, accounts for face cards and suits
  protected void display() {
    System.out.print(" The " );

    // 9 + 2 = 11, which is the Jack. Default case is numbered card.
    switch(rank){
      case 9: System.out.print("Jack"); break;
      case 10: System.out.print("Queen"); break;
      case 11: System.out.print("King"); break;
      case 12: System.out.print("Ace"); break;
      default: System.out.print((rank + 2)); break;
    }

    switch(suit){
      case 0: System.out.print(" of Hearts"); break;
      case 1: System.out.print(" of Diamonds"); break;
      case 2: System.out.print(" of Clubs"); break;
      case 3: System.out.print(" of Spades"); break;
    }
  }
}