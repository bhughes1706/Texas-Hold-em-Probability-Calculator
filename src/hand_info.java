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

  //****GENERAL_INFO HAND CLASS FUNCTION MUST BE DONE BEFORE ANY OF THE FOLLOWING****

  //only finds best possible hand strength -- used for iteration due to its quickness
  //cannot be used for odds calculation of hand types
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

  //evaluates further depending on the highest hand type
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

  //this is an evaluation done to allow the odds functions to work correctly
  //finds any hand that needs more than one of - two kind, full house, two pair, etc.
  protected void multiples_finder() {
    if(rank_no[2] > 0) {
      kind_high = 2;
      if(rank_no[3] > 0)
        full_house = true;
    }
    if(rank_no[3] > 0)
      kind_high = 3;
    if(rank_no[4] > 0)
      kind_high = 4;
    if(rank_no[2] == 2)
      two_pair = true;
    if(rank_no[2] == 3)
      three_pair = true;

  }

  //finds the highest number of suits that match and records the number
  //this is less intensive than the quick evaluation, due to not caring
  //about the rank of the flush, since odds don't care about it.
  protected void flush_finder() {
    for(int i = 0; i < 4; ++i){
      if(suit_no[i] != 0)
        flush_total = i;
    }
    if(flush_total == 5)
      flush = true;
  }

  //holds possible straights and which cards are needed for straight
  //find_straight_odds fill out the array for each card
  protected void straight_finder(int total_cards){
    int count;
    int i,j,k,l;

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
        if(ranks[j+k] != 0) {
          ++count;
        }
        //if there is a straight
        if(count == 5) {
          straight = true;
          return; //no reason to continue
        }
        //if there is cards needed to make straight and it's possible with
        //remaining number of cards to be dealt
        else if(count >= (total_cards-2)){
          for(l = 0; l < 5; ++l){
            if(ranks[j+l] == 0 && count > straight_opportunities[j+l]){
              straight_opportunities[j+l] = count;
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
        if(straight_opportunities[i] == 4){
          ++fours;
          //counts 3's to the left of the 4
          for(j = 0; j < i; ++j){
            if(straight_opportunities[j] == 3)
              ++left;
          }
          //counts 3's to the right of the 4
          for(k = i+1; k < 13; ++k){
            if(straight_opportunities[k] == 3)
              ++right;
          }
          //if left and right are non-zero and equal OR
          // if there are multiple 4's
          if((left > 0 && right == left) || fours > 1 || (left + right == 1)){
            for(j = 0; j < 13; ++j){
              //eliminates all 3's
              if(straight_opportunities[j] == 3)
                straight_opportunities[j] = 0;
            }
          }
          //right is larger, so eliminate all left 3's
          else if(left < right){
            for(l = 0; l < i; ++l){
              if(straight_opportunities[l] == 3)
                straight_opportunities[l] = 0;
            }
          }
          //left is larger so eliminate all right 3's
          else if(right < left){
            for(l = i+1; l < 13; ++l){
              if(straight_opportunities[l] == 3)
                straight_opportunities[l] = 0;
            }
          }
        }
      }
    }
  }
}
