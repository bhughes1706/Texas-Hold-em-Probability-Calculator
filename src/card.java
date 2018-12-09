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
}