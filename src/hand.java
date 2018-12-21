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

}

  //used to store info about hand
class hand_info{
  protected int deck_high; //high card on board/kitty
  protected int hand_high; //high card in player's hand
  protected int kind_high; //how many of kind there is
  protected int value_kind_high; //high card in of_kind match
  protected int value_second_pair; //high card in second of two pair
  protected int flush_high;
  protected int flush_total;
  protected int [] straight_opportunities;
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
    straight_opportunities = new int[13];
  }
}
