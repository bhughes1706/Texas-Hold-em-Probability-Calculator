/*
  Holds the cards dealt from the deck. Contained in deck class.
  Contains the hand_info class, which is a list of information about the hand
 */
class hand {
  protected card [] card;
  protected int total_cards;
  protected hand_info info;
  protected int max_cards;

    //sets hand array size
  hand(int max){
    max_cards = max; //2 for user and opponent, 5 for dealer
    card = new card[max_cards]; //builds positions for cards
    info = new hand_info();
    total_cards = 0; //no cards dealt during initialization
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

  public int total_cards() {
    return total_cards;
  }
}

  //used to store info about hand
class hand_info{
  protected int deck_high; //high card for dealer
  protected int hand_high; //high card in player's hand
  protected int kind_high; //how many of kind there is
  protected int value_kind_high; //high card in of_kind match
  protected int value_second_pair; //high card in second of two pair
  protected int flush_high; //highest card in flush
  protected int flush_total; //highest number of cards that match a suit

    //holds possible straights and which cards are needed for straight
    //find_straight_odds fill out the array for each cards as follows...
    //3: three cards of five match / 4: four of five match / 0: no possible straight
    //ie - 0 3 3 0 0 0 3 3 0 .. 0 -- 5,6,7 in hand, needing 3,4 or 8,9
    //ie - 0 4 0 0 0 0 4 0 .. 0 -- 5,6,7,8 in hand, needing only 4 or 9
  protected int [] straight_opportunities;

  //all booleans are true if found in hand
  protected boolean full_house;
  protected boolean flush;
  protected boolean straight;
  protected boolean two_pair;

  //found by calling find_x_odds functions
  protected float two_kind_odds;
  protected float three_kind_odds;
  protected float four_kind_odds;
  protected float full_house_odds;
  protected float flush_odds;
  protected float straight_odds;
  protected float two_pair_odds;

    //initializes array to proper value
  protected hand_info(){
    straight_opportunities = new int[13];
  }
}
