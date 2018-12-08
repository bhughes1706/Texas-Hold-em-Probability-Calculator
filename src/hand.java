
public class hand {
  private card [] hand;
  private int total_cards;
  private int max_cards;

  //assumes five card poker game if no parameters
  hand(){ hand = new card[5]; max_cards = 5; }
  hand(int initial){
    hand = new card[initial];
    max_cards = initial;
  }
  protected void deal_init(int initial){
    for(int i = 0; i < initial; ++i) {
      hand[i] = new card();
      hand[i].deal();
      if(i > 1){
        while(check(i))
          hand[i].deal();
      }
    }
    total_cards = initial;
  }
  protected void deal_one(){
    hand[total_cards] = new card();
    hand[total_cards].deal();
    ++total_cards;
  }
  protected void display_all(){
    for(int i = 0; i < total_cards; ++i){
      hand[i].display();
    }
  }
  protected boolean check(int current){
    if(hand[current].value == hand[current-1].value &&
        hand[current].suit == hand[current-1].suit)
      return true;
    return false;
  }
  protected boolean not_full(){
    if(total_cards == max_cards)
      return false;
    return true;
  }
  protected void guess(){}
  public void replace(int entry) {
    hand[entry-1].deal();
    for(int i = 0; i < total_cards; ++i){
      if(hand[i] != null && hand[i] != hand[entry]){
        while(check){

        }
      }
    }
  }
}
