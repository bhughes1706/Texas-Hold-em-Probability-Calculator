import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.DecimalFormat;

//All functions and menu are for testing purposes at the moment
//could be improved
public class CardProbability {
  public static void main(String[] args) {
    int selector = 1;
    while(selector == 1)
      selector = welcome();
    deck deck = new deck();

    try {
      deck.init_deck();
      deck.add_user();
      deck.add_opponent();
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

  public static void init_odds(){
    InitProbabilities init = new InitProbabilities();
    DecimalFormat df = new DecimalFormat("##.###");
    System.out.println(
        "\nRoyal flush odds: " + df.format(init.royal_flush) + "%"
      + "\nStraight flush odds: " + df.format(init.straight_flush) + "%"
      + "\nFour of a kind odds: " + df.format(init.four_kind) + "%"
      + "\nFull house odds: " + df.format(init.full_house) + "%"
      + "\nFlush odds: " + df.format(init.flush) + "%"
      + "\nStraight odds: " + df.format(init.straight) + "%"
      + "\nThree of a kind odds: " + df.format(init.three_kind) + "%"
      + "\nTwo pair odds: " + df.format(init.two_pair) + "%"
      + "\nOne pair odds: " + df.format(init.one_pair) + "%"
    );
  }

    //just used for testing right now
  private static void prob_print(deck deck) {
    hand_info temp;
    DecimalFormat df = new DecimalFormat("##.##");
    temp = deck.get_info(0);
    System.out.println("Of a kind: " + temp.kind_high);
    System.out.println("Full House: " + temp.full_house);
    System.out.println("Flush: " + temp.flush);
    System.out.println("Straight: " + temp.straight);
    System.out.println("Two Pairs: " + temp.two_pair);
    System.out.println("Flush total: " + temp.flush_total);
    System.out.println("Flush high: " + temp.flush_high);
    for(int i = 0; i < 13; ++i)
      System.out.println("Card " + i + ": " + temp.straight_opportunities[i]);

    System.out.println("\nThe probability your hand will include:" +
        "\nTwo of a kind: " + df.format(temp.two_kind_odds) + "%" +
        "\nThree of a kind: " + df.format(temp.three_kind_odds) + "%" +
        "\nFour of a kind: " + df.format(temp.four_kind_odds) + "%" +
        "\nTwo pairs: " + df.format(temp.two_pair_odds) + "%" +
        "\nFull house: " + df.format(temp.full_house_odds) + "%" +
        "\nAny Flush: " + df.format(temp.flush_odds) + "%" +
        "\nStraight: " + df.format(temp.straight_odds) + "%");

    //System.out.println("\nThe probability your hand will beat a random hand: " +
    //    df.format(temp.random_hand) + "%");
  }

    //needs implemented
  private static void probability_guess(deck deck) {
  }

  private static int welcome() {
    Scanner in = new Scanner(System.in);
    int selector = 0;
    System.out.println("\nWelcome to Texas Hold'em training. " +
        "\nThis program will train you to recognize probabilities in all situations" +
        "\nLet's start with an easy one: ");
    try {
      System.out.println("\nWould you like to:" +
          "\n1) See initial probabilities of all hand types" +
          "\n2) Deal cards" +
          "\n3) Quit");
      selector = in.nextInt();
      switch (selector) {
        case 1:
          init_odds();
          return 1;
        case 2:
          return 0;
      }
    } catch (InputMismatchException error) {
      System.out.println("\nPlease enter correct data type.");
      return 1;
    }
    return 2;
  }

    //menu available mid-hand
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
        deck.deal_card(3);
      case 4:
        return 3;
    }
    return 3;
  }

    //removes options that were available mid-hand
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