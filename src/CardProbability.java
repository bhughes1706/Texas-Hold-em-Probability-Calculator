import java.util.InputMismatchException;
import java.util.Scanner;

//All functions and menu are for testing purposes at the moment
//could be improved
public class CardProbability {
  public static void main(String[] args) {
    System.out.println("\nThis is a program that trains the user to determine the " +
        "\nprobability that their cards will beat a generic hand.");
    deck deck = new deck();
    try {
      deck.init_deck();
      deck.add_user();
      deck.add_dealer();
      int selector = 0;
      while (selector == 0 || selector == 1) {
        switch (selector) {
          case 0:
            selector = init_menu(deck); break;
          case 1:
              selector = final_menu(deck); break;
          }
        }
    } catch(NullPointerException err) {
      err.printStackTrace();
    }
  }

    //just used for testing right now
  private static void prob_print(deck deck) {
    hand_info temp;
    temp = deck.get_info(0);
    System.out.println("Of a kind: " + temp.kind_high);
    System.out.println("Full House: " + temp.full_house);
    System.out.println("Flush: " + temp.flush);
    System.out.println("Straight: " + temp.straight);
    System.out.println("Two Pairs: " + temp.two_pair);

    System.out.println("\nThe probability your hand will include:" +
        "\nTwo of a kind: " + temp.two_kind_odds + "%" +
        "\nThree of a kind: " + temp.three_kind_odds + "%");
  }
    //needs implemented
  private static void probability_guess(deck deck) {
  }

  private static int init_menu(deck deck) {
    int select = 0;
    deck.display();
    try {
      while (select <= 0 || select > 4) {
        System.out.println("\n\nWould you like to:" +
            "\n1) Guess probability of winning hand against" +
            " random hand\n2) See probabilities of different hands" +
            "\n3) Deal another card\n4) Quit");
        Scanner in = new Scanner(System.in);
        select = in.nextInt();
      }
    } catch (InputMismatchException error) {
      System.out.println("\nPlease enter correct data type.");
      return 0;
    }
    switch (select) {
      case 1:
        probability_guess(deck);
        return 0;
      case 2:
        prob_print(deck);
        return 0;
      case 3:
        return deck.deal_card(3);
      case 4:
        return 3;
    }
    return 3;
  }

  private static int final_menu (deck deck){
    deck.display();
    int select = 0;
    try {
      while (select <= 0 || select > 2) {
        System.out.println("\n\nWould you like to:" +
            "\n1) Guess probability of winning hand against" +
            " random hand\n2) Quit");
        Scanner in = new Scanner(System.in);
        select = in.nextInt();
      }
    } catch (InputMismatchException error) {
      System.out.println("\nPlease enter correct data type.");
      return 0;
    }
    switch(select){
      case 1:
        probability_guess(deck);
        return 2;
      case 2:
        return 3;
    }
    return 2;
  }
}