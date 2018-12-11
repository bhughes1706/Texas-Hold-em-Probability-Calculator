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
      for(int i = 0; i < 3; ++i){
        if(deck.deal_card(0) == 0)
          System.out.println("\nEmpty deck."); //won't ever happen
      }
      deck.display(0);
      init_menu(deck, 0);
    } catch (NullPointerException err){}
  }
  private static void discard(deck deck, boolean [] entered) {
    int select = 0;
    deck.display(0);
    System.out.println("\nWhich card would you like to discard?");
    Scanner in = new Scanner(System.in);
    try {
      select = in.nextInt();
    } catch (InputMismatchException error) {
      System.out.println("\nPlease enter correct data type.");
      discard(deck, entered);
    }
    switch (select) {
      case 1:
        if(!entered[0]){
        deck.discard(0, 0);
        entered[0] = true;}
        else
          System.out.println("You've already discarded this position.");
        break;
      case 2:
        if(!entered[1]){
        deck.discard(0, 1);
        entered[1] = true;}
        else
          System.out.println("You've already discarded this position.");
        break;
      case 3:
        if(!entered[2]){
        deck.discard(0, 2);
        entered[2] = true;}
        else
          System.out.println("You've already discarded this position.");
        break;
    }
    if (entered[0] && entered[1] && entered[2])
      in_midst_menu(deck);
    else {
      System.out.println("\nYou can select another card to discard.");
      discard(deck, entered);
    }
  }
  private static void prob_print(deck deck) {
  }
  private static void probability_guess(deck deck) {
  }
  private static void init_menu(deck deck, int tripwire) {
    int select = 0;
    try {
      while(select <= 0 || select > 5) {
        System.out.println("\n\nWould you like to: \n1) Guess probability of winning hand against" +
            " generic hand\n2) See probabilities\n3) Discard some/all cards" +
            "\n4) Take another card\n5) Quit");
        Scanner in = new Scanner(System.in);
        select = in.nextInt();
      }
    } catch (InputMismatchException error) {
      System.out.println("\nPlease enter correct data type.");
      init_menu(deck, tripwire);
    }
    switch(select){
      case 1: probability_guess(deck); break;
      case 2: prob_print(deck); break;
      case 3: if(tripwire == 0){
        boolean [] entered = new boolean[3];
        discard(deck, entered); ++tripwire; break;
      }
      else{
        System.out.println("\nYou've done this. Don't be cheating."); break;
      }
      case 4: deck.deal_card(0);
        in_midst_menu(deck); break;
      case 5: break;
    }
  }
  private static void in_midst_menu(deck deck){

  }
  private static void final_menu(deck deck){}
}

