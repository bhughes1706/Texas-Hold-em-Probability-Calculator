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

  protected void general_info(){
    if(total_cards == 0)
      return;

    int i, j;
    for(i = 0; i < total_cards; ++i)
      ++info.suits[card[i].suit];

    for(j = 0; j < 4; ++j){
      switch(info.suits[j]){
        case 0: ++info.suit_no[0]; break;
        case 1: ++info.suit_no[1]; break;
        case 2: ++info.suit_no[2]; break;
        case 3: ++info.suit_no[3]; break;
        default: break; //don't care about three or four, could never have more than one and still get a flush
      }
    }

    for(i = 0; i < total_cards; ++i)
      ++info.ranks[card[i].suit];
    for(j = 0; j < 13; ++j)
      ++info.rank_no[info.ranks[j]];
  }

  protected void add_dealer_high(int high){
    info.deck_high = high;
  }

  protected void add_hand_high(int high){
    info.hand_high = high;
  }

  protected void high_card_finder(){
    int high = 0;
    for (int i = 0; i < total_cards; ++i) {
      if (card[i].value > high)
        high = card[i].value;
    }
    info.high_card = high;
  }

  protected void of_kind_finder(){
    int counter;
    for (int i = 0; i < total_cards - 1; ++i) {
      counter = 1;
      for (int j = i + 1; j < total_cards; ++j) {
        if (card[i].value == card[j].value) {
          ++counter;
          if (info.kind_high <= counter) {
            if (info.kind_high < counter)
              info.value_kind_high = card[i].value;
            else if (info.value_kind_high < card[i].value)
              info.value_kind_high = card[i].value;
            info.kind_high = counter;
          }
        }
      }
    }
    if (info.kind_high == 0)
      info.kind_high = 1;
  }

  protected void full_house_finder() {
    int tripwire = 0;
    int high = info.value_kind_high;
    for (int i = 0; i < total_cards - 1; ++i) {
      if (card[i].value != high)
        for (int j = i + 1; j < total_cards; ++j) {
          if (card[j].value != high && card[j] == card[i])
            ++tripwire;
        }
      if (tripwire == 1)
        info.full_house = true;
    }
  }

  protected void pair_finder(){
    if (info.kind_high < 2)
      return;
    if(info.rank_no[2] == 3)
      info.three_pair = true;
    int i, j;
    for (i = 0; i < total_cards - 1; ++i) {
      int high = info.value_kind_high;
      if (card[i].value != high) {
        for (j = i + 1; j < total_cards; ++j) {
          if (card[j].value != high && card[i].value == card[j].value) {
            info.value_second_pair = card[i].value;
            info.two_pair = true;
          }
        }
      }
    }
  }

  protected void flush_finder(){
    int count;
    int high;
    info.flush_high = 0;
    info.flush_total = 0;

    for (int i = 0; i < total_cards - 1; ++i) {
      count = 1;
      high = 0;
      for (int j = i + 1; j < total_cards; ++j) {
        if (card[i].suit == card[j].suit) {
          ++count;
          if(card[i].value > card[j].value) {
            if(high < card[i].value)
              high = card[i].value;
          }
          else {
            if(high < card[j].value)
              high = card[j].value;
          }
        }
      }
      if(count > info.flush_total){
        info.flush_total = count;
        info.flush_high = high + 2;
      }
      if(count > 4)
        info.flush = true;
    }
  }

  protected void straight_finder(){
    //holds possible straights and which cards are needed for straight
    //find_straight_odds fill out the array for each card

    //creates array to hold current card values
    boolean [] straight_array = new boolean[13];
    //where cards are needed for a possible straight
    int [] needed = new int[13];
    int count;
    int i,j,k, l;

    //find where all the cards are
    for(i = 0; i < total_cards; ++i)
      straight_array[card[i].value] = true;

    //evaluates each five card chunk for possible straights
    //if five cards to be dealt, then evaluates each position
    //for needing 3, 4, or 5 cards in five spaces
    //if two cards are to be dealt, then needs four in five
    //if one card left to deal then needs four in five
    //achieved by: count >= (total_cards - 2)
    for(j = 0; j < 9; ++j){
      count = 0;
      //runs forward through five positions and counts cards
      for(k = 0; k < 5; ++k){
        if(straight_array[j+k]) {
          ++count;
        }
        //if there is a straight
        if(count == 5) {
          info.straight = true;
          break; //no reason to continue
        }
        //if there is cards needed to make straight and it's possible with
        //remaining number of cards to be dealt
        else if(count >= (total_cards-2)){
          for(l = 0; l < 5; ++l){
            if(!straight_array[j+l] && count > needed[j+l]){
              needed[j+l] = count;
            }
          }
        }
      }
    }

      /* a very specific problem arises with the above algorithm when total_cards is 5
         0 4 0 0 0 0 4 0 .. 0 -- 5,6,7,8 in hand, needing only 4 or 9
         the above algorithm will evaluate to:
         3 4 0 0 0 0 4 3 0 .. 0
         Therefore, even numbers of 3's on each side of one 4 need to be removed
         OR if there are two 4's, then eliminate all 3's no matter what
         0 0 0 4 0 0 0 3 3 .. 0 -- 3,4,6,7,9 in hand, needing 5, OR: 10, Jack
         the above algorithm will evaluate to
         3 0 0 4 0 0 3 0 3 .. 0
         Therefore, the smaller quantity of 3's on one side of the 4 need to
         be removed (when non-equal) - while the larger quantity stays
      */
    if(total_cards == 5) {
      int right = 0;
      int left = 0;
      int fours = 0;

      //checks for 4's in needed array. No need to check end values (0, 12)
      for (i = 1; i < 12; ++i){
        //counts the number of fours
        if(needed[i] == 4){
          ++fours;
          //counts 3's to the left of the 4
          for(j = 0; j < i; ++j){
            if(needed[j] == 3)
              ++left;
          }
          //counts 3's to the right of the 4
          for(k = i+1; k < 13; ++k){
            if(needed[k] == 3)
              ++right;
          }
          //if left and right are non-zero and equal OR
          // if there are multiple 4's
          if((left > 0 && right == left) || fours > 1 || (left + right == 1)){
            for(j = 0; j < 13; ++j){
              //eliminates all 3's
              if(needed[j] == 3)
                needed[j] = 0;
            }
          }
          //right is larger, so eliminate all left 3's
          else if(left < right){
            for(l = 0; l < i; ++l){
              if(needed[l] == 3)
                needed[l] = 0;
            }
          }
          //left is larger so eliminate all right 3's
          else if(right < left){
            for(l = i+1; l < 13; ++l){
              if(needed[l] == 3)
                needed[l] = 0;
            }
          }
        }
      }
    }
    //copies total evaluation into hand_info object
    info.straight_opportunities = needed;
  }

}


  //used to store info about hand
class hand_info{
  protected int high_card; //highest card regardless
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
    //ie - 0 3 3 0 0 0 3 3 0 .. 0 -- 5,6,7 in hand, needing 3,4 or 8,9 (2 to draw)
    //ie - 0 4 0 0 0 0 4 0 .. 0 -- 5,6,7,8 in hand, needing only 4 or 9
      //:::if at least four cards to be dealt:::
    //ie - 1 1 1 0 2 2 2 0 1 1 ... 5 and 9 in hand, multiple opportunities with low odds
  protected int [] straight_opportunities;

  //holds general information about the hand
  protected int [] ranks; //which ranks are included
  protected int [] rank_no; //how many of each rank
  protected int [] suits; //which suits are in hand
  protected int [] suit_no; //how many of each suit

  //all booleans are true if found in hand
  protected boolean full_house;
  protected boolean flush;
  protected boolean straight;
  protected boolean two_pair;
  protected boolean three_pair;

  //found by calling find_x_odds functions
  protected double two_kind_odds;
  protected double three_kind_odds;
  protected double four_kind_odds;
  protected double full_house_odds;
  protected double flush_odds;
  protected double straight_odds;
  protected double two_pair_odds;

    //initializes array to proper value
  protected hand_info(){
    straight_opportunities = new int[13];
    ranks = new int[13];
    rank_no = new int[4];
    suits = new int[4];
    suit_no = new int[4];
  }
}
