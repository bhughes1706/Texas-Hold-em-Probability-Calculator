import java.util.InputMismatchException;
import java.util.Scanner;

public class CardProbability {
  public static void main(String[] args){
    System.out.println("\nThis is a program that trains the user to determine the " +
        "\nprobability that their card hand will beat a generic hand.");
    deck deck = new deck();
    deck.build_deck();
    deck.add_hand();


  }
}

  /*
  private static void init_menu(hand hand){
    int entry = 0;
    Scanner in = new Scanner(System.in);
    while(entry != 3 && hand.not_full()){
      System.out.println("\nHand:");
      hand.display_all();
      System.out.println("\n\nWould you like to:\n1) Replace cards" +
          "\n2) Take another card\n3) Quit");
      try{
        entry = in.nextInt();
      }catch(InputMismatchException error){ entry = 3;
        System.out.println("\nPlease enter valid number."); }
      switch(entry){
        case 1: replace_cards(in); break;
        case 2: hand.deal_one(); break;
        case 3:
      }
    }
  }

  private static void replace_cards(Scanner in, hand hand) {
    System.out.println("\nWhich cards? ");
    int entry = in.nextInt();
    hand.replace(entry);
  }
}*/
