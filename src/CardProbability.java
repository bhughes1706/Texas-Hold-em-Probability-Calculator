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
    } catch (NullPointerException err){}
  }
}

