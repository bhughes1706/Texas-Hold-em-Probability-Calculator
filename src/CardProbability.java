import java.util.InputMismatchException;
import java.util.Scanner;

public class CardProbability {
  public static void main(String[] args) {
    System.out.println("\nThis is a program that trains the user to determine the " +
        "\nprobability that their cards will beat a generic hand.");
    deck deck = new deck();
    try {
      deck.init_deck();
      deck.add_hand();
      for (int i = 0; i < 3; ++i) {
        if (deck.deal_card(0) == 0)
          System.out.println("\nEmpty deck."); //won't ever happen
      }
      deck.display(0);
      int selector = 0;
      while(selector >= 0 && selector <= 2) {
        switch (selector) {
          case 0:
            selector = init_menu(deck); break;
          case 1:
            selector = in_midst_menu(deck, 0); break;
          case 2:
            selector = final_menu(deck, 0); break;
        }
      }
    } catch (NullPointerException err) {
      err.printStackTrace();
    }
  }

  private static int discard(deck deck, boolean[] entered) {
    int select = 0;
    deck.display(0);
    System.out.println("\nWhich card would you like to exchange?" +
        "\nOr enter 4 to return with current hand.");
    Scanner in = new Scanner(System.in);
    try {
      do {
        select = in.nextInt();
        if (select < 0 || select > 4)
          System.out.println("\nEnter valid number.");
      } while (select < 0 || select > 4);
    } catch (InputMismatchException error) {
      System.out.println("\nPlease enter correct data type.");
      return 0;
    }
    switch (select) {
      case 1:
        if (!entered[0]) {
          deck.discard(0, 0);
          entered[0] = true;
        } else
          System.out.println("You've already discarded this position.");
        break;
      case 2:
        if (!entered[1]) {
          deck.discard(0, 1);
          entered[1] = true;
        } else
          System.out.println("You've already discarded this position.");
        break;
      case 3:
        if (!entered[2]) {
          deck.discard(0, 2);
          entered[2] = true;
        } else
          System.out.println("You've already discarded this position.");
        break;
      case 4:
        return 1;
    }
    if (entered[0] && entered[1] && entered[2])
      return 1;
    else {
      System.out.println("\nYou can select another card to discard.");
      return 0;
    }
  }

  private static void prob_print(deck deck) {
    card_info temp = new card_info();
    temp = deck.get_info(0);
    System.out.println("\nHigh card: " + (temp.high_card+2));
    System.out.println("Of a kind: " + temp.kind_high);
    System.out.println("Full House: " + temp.full_house);
    System.out.println("Flush: " + temp.flush);
    System.out.println("Straight: " + temp.straight);
  }

  private static void probability_guess(deck deck) {
  }

  private static int init_menu(deck deck) {
    int select = 0;
    int discard_helper = 0;
    try {
      while (select <= 0 || select > 5) {
        System.out.println("\n\nWould you like to:" +
            "\n1) Guess probability of winning hand against" +
            " generic hand\n2) See probabilities\n3) Discard some/all cards" +
            "\n4) Take another card\n5) Quit");
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
      case 3: {
        boolean[] entered = new boolean[3];
        while (discard_helper == 0)
          discard_helper = discard(deck, entered);
        return 1;
      }
      case 4:
        return 1;
      case 5:
        return 2;
    }
    return 2;
  }

  private static int in_midst_menu(deck deck, int hand_num) {
    deck.deal_card(hand_num);
    deck.display(hand_num);
    int select = 0;
    try {
      while (select <= 0 || select > 4) {
        System.out.println("\n\nWould you like to:" +
            "\n1) Guess probability of winning hand against" +
            " generic hand\n2) See probabilities\n3) Take another card" +
            "\n4) Quit");
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
        return 1;
      case 2:
        prob_print(deck);
        return 1;
      case 3:
        return 2;
      case 4:
        return 3;
    }
    return 1;
  }

  private static int final_menu (deck deck, int hand_num){
    deck.deal_card(hand_num);
    deck.display(hand_num);
    int select = 0;
    try {
      while (select <= 0 || select > 2) {
        System.out.println("\n\nWould you like to:" +
            "\n1) Guess probability of winning hand against" +
            " generic hand\n2) Quit");
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