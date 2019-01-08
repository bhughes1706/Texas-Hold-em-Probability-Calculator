//used to store info about hand
class hand_info {
  protected int kind_high; //how many of kind there is
  protected int value_kind_high; //high card in of_kind match
  protected int value_second_pair; //high card in second of two pair
  protected int flush_high; //highest card in flush
  protected int flush_total; //highest number of cards that match a suit
  protected int straight_high;
  protected int hand_strength;

  //holds possible straights and which cards are needed for straight
  //find_straight_odds fill out the array for each cards as follows...
  //3: three cards of five match / 4: four of five match / 0: no possible straight
  //ie - 0 3 3 0 0 0 3 3 0 .. 0 -- 5,6,7 in hand, needing 3,4 or 8,9 (2 to draw)
  //ie - 0 4 0 0 0 0 4 0 .. 0 -- 5,6,7,8 in hand, needing only 4 or 9
  //:::if at least four cards to be dealt:::
  //ie - 1 1 1 0 2 2 2 0 1 1 ... 5 and 9 in hand, multiple opportunities with low odds
  protected int[] straight_opportunities;

  //holds general information about the hand
  protected int[] ranks; //which ranks are included
  protected int[] rank_no; //how many of each rank
  protected int[] suits; //which suits are in hand
  protected int[] suit_no; //how many of each suit

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

  //initializes array to proper values
  protected hand_info() {
    straight_opportunities = new int[13];
    ranks = new int[13];
    rank_no = new int[5];
    suits = new int[4];
    suit_no = new int[8];
    hand_strength = 0;
  }

  protected void quick_evaluation() {
    int i, j, count;

    if (rank_no[2] == 1) {
      hand_strength = 1;
      if (rank_no[3] == 1) {
        hand_strength = 6;
        additional_evaluation();
        return;
      }
    }

    if (rank_no[2] == 2 || rank_no[2] == 3 && hand_strength < 2) {
      hand_strength = 2;
      additional_evaluation();
      return;
    }

    if (rank_no[3] > 0 && hand_strength < 3)
      hand_strength = 3;

    if (rank_no[4] > 0) {
      hand_strength = 7;
      additional_evaluation();
      return;
    }

    //only runs if possible hand strength is higher than current
    //finds flush
    if (hand_strength < 5) {
      for (i = 0; i < 4; ++i) {
        if (suit_no[i] > 4)
          hand_strength = 5;
      }
    }

    //only runs if possible hand strength is higher than current
    //checks for straights, and finds highest straight card
    if (hand_strength < 4 || hand_strength == 5) {

      for (i = 0; i < 8; ++i) {
        count = 0;
        if (ranks[i] != 0) {
          for (j = i + 1; j < 5; ++j) {
            if (ranks[j] != 0)
              ++count;
          }
          if (count == 4) {
            if (hand_strength == 5)
              straight = true;
            else
              hand_strength = 4;
            if (straight_high < j)
              //acquires straight high value for possible ties
              straight_high = j;
          }
        }
      }
    }
  }

  protected void additional_evaluation(){
    //this adds evaluations as needed
    //most are simply finding the highest of_kind or second_pair
    int i;
    switch (hand_strength) {
      case 1:
        for (i = 0; i < 13; ++i) {
          if (ranks[i] > 1)
            value_kind_high = i;
        }break;
      case 2:
        for(i = 0; i < 13; ++i){
          if(ranks[i] > 1) {
            value_second_pair = value_kind_high;
            value_kind_high = i;
          }
        }break;
      case 3:
        for (i = 0; i < 13; ++i) {
          if (ranks[i] > 2)
            value_kind_high = i;
        }break;
      case 6:
        for(i = 0; i < 13; ++i){
          if(ranks[i] == 3)
            value_kind_high = i;
          else if(ranks[i] == 2)
            value_second_pair = i;
        }break;
      case 7:
        for (i = 0; i < 13; ++i) {
          if (ranks[i] > 3)
            value_kind_high = i;
        }
    }
  }
}
