class hand {
  protected card [] card;
  protected int total_cards;
  protected hand_info info;
  protected int max_cards;

    //sets hand array size
  hand(int max){
    max_cards = max;
    card = new card[max_cards];
    info = new hand_info();
    total_cards = 0;
  }

    //adds one new card to hand
  protected void add(card to_add){
    card[total_cards] = new card(to_add);
    ++total_cards;
  }

    //displays for every card in hand
  protected void display() {
    for(int i = 0; i < total_cards; ++i){
      System.out.print("\nCard " + (i+1) + ":");
      card[i].display();
    }
  }

    //for copying into evaluation hand, which is used for
    //combining dealer and user into one hand for evaluating
  protected void to_copy(hand copy){
    for(int i = total_cards; i < copy.total_cards+total_cards; ++i){
      card[i] = new card(copy.card[i-total_cards]);
    }
    total_cards+=copy.total_cards;
  }

  private void find_straight_odds(int deck_size) {
  }

  private void find_flush_odds(int deck_size) {
  }

  private void find_four_kind_odds(int deck_size) {
    if(info.kind_high > 3)
      info.four_kind_odds = 100;
    else if(info.kind_high + (max_cards - total_cards) < 4)
      info.four_kind_odds = 0;
    else{

    }
  }

  private void find_full_house_odds(int deck_size) {
  }

}

  //used to store info about hand
class hand_info{
  protected int deck_high;
  protected int hand_high;
  protected int kind_high;
  protected int value_kind_high;
  protected int value_second_pair;
  protected boolean full_house;
  protected boolean flush;
  protected boolean straight;
  protected boolean two_pair;
  protected float two_kind_odds;
  protected float three_kind_odds;
  protected float four_kind_odds;
  protected float full_house_odds;
  protected float flush_odds;
  protected float straight_odds;
  protected float two_pair_odds;

  protected hand_info(){ kind_high = 0; value_kind_high = 0; hand_high = 0;
    full_house = false; flush = false; straight = false;
  }
}
