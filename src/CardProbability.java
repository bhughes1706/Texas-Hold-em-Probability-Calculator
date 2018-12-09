import java.util.InputMismatchException;
import java.util.Scanner;

public class CardProbability {
  public static void main(String[] args){
    System.out.println("\nThis is a program that trains the user to determine the " +
        "\nprobability that their cards will beat a generic hand.");
    deck deck = new deck();
    try {
      deck.init_deck();
      deck.add_hand();
      for(int i = 0; i < 3; ++i)
        deck.deal_card(0);
      deck.display(0);
      init_menu(deck);
    } catch (NullPointerException err){}
  }
  private static void init_menu(deck deck) {
    int select = 0;
    try {
      System.out.println("\n\nWould you like to: \n1) Guess probability of winning hand against" +
          " generic hand\n2) See probabilities\n3) Discard some/all cards\n4) Quit");
      Scanner in = new Scanner(System.in);
      select = in.nextInt();
    } catch (InputMismatchException error) {
      System.out.println("\nPlease enter correct data type.");
      init_menu(deck);
    }
    switch(select){
      case 1: probability_guess(deck); break;
      case 2: prob_print(deck); break;
      case 3: discard(deck); break;
      case 4: break;
    }
  }
  private static void discard(deck deck) {
  }
  private static void prob_print(deck deck) {
  }
  private static void probability_guess(deck deck) {
  }
}

