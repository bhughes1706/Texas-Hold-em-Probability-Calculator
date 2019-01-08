import static java.lang.Math.pow;

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

    for(j = 0; j < 4; ++j)
        ++info.suit_no[info.suits[j]];

    for(i = 0; i < total_cards; ++i)
      ++info.ranks[card[i].value];

    for(j = 0; j < 13; ++j)
      ++info.rank_no[info.ranks[j]];
  }

  protected void card_sorter(){
    if(card[0].value < card[1].value){
      card temp = card[0];
      this.card[0] = card[1];
      this.card[1] = temp;
    }
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
      if (tripwire > 0) {
        info.full_house = true;
        if (info.hand_strength < 6)
          info.hand_strength = 6;
      }
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
    if(info.two_pair && info.hand_strength < 2)
      info.hand_strength = 3;
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
        if(info.flush && info.hand_strength < 5)
          info.hand_strength = 5;
    }
  }

  protected void straight_finder(){
    //holds possible straights and which cards are needed for straight
    //find_straight_odds fill out the array for each card

    //creates array to hold current card values
    boolean [] straight_array = new boolean[13];
    //where cards are needed for a possible straight
    int [] needed = new int[13];
    int high_straight = 0;
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
          high_straight = j + k;
          ++count;
        }
        //if there is a straight
        if(count == 5) {
          info.straight = true;
          info.straight_high = high_straight;
          if(info.hand_strength < 4)
            info.hand_strength = 4;
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

  protected void quick_evaluation() {
    info.quick_evaluation();
  }

    /*
    The following use the above finder functions' data to evaluate
    the to probability of specific events. All use simple
    statistical modeling.
   */

  //this is correct
  protected void find_two_kind_odds() {
    //CASE 1: already two of kind
    //CASE 2: possible -- (1 - never have a match to anything in hand)

    //CASE 1: currently have two_kind
    if(info.kind_high > 1){
      info.two_kind_odds = 100;
      return;
    }

    int to_deal = 7 - total_cards;
    double total = 0;
    double other_total = 1;
    int deck = 52 - total_cards;

    //Case 2
    for(int i = 0; i < to_deal; ++i){
      total = (total_cards + i) * 3; //each card subsequent will be single rank
      other_total *= (deck - total - i) / (deck - i); //the chance of each deal not matching card
    }
    total = 1 - other_total; //chance of getting match
    info.two_kind_odds = 100*total;
  }

  //pretty sure this is correct
  protected void find_two_pair_odds() {
    //CASE 1: already two_pair
    //CASE 2: not possible
    //CASE 3: possible
    //Case 3.1: two pair are not current ranks in hand
    //Case 3.2: one card of one rank of two_pair is currently in hand
    //Case 3.3: one card of each rank of two_pair is in hand
    //Case 3.3.1: one pair of two_pair is in hand
    //Case 3.4: one pair and one card of two_pair in hand

    if(info.two_pair) {
      info.two_pair_odds = 100;
      return;
    }

    double total = 0;
    double other_total = 0;
    int high = info.kind_high;
    int deck = 52 - total_cards;
    int to_deal = 7 - total_cards;
    int current_ranks = total_cards;

    if(high > 1) {
      current_ranks -= high;
      ++current_ranks;
    }

    //Case 3.1: two pair are not current ranks in hand
    if(to_deal >= 4){
      other_total = pow(combo(4,2),2)
          *combo(13-current_ranks, 2);
      if(to_deal > 4)
        other_total *= combo(13-current_ranks-1, to_deal-4)
            * pow(4, to_deal-4);
      total += other_total;
      other_total = 0;
    }

    //Case 3.2: one card of one rank of two_pair is currently in hand
    if(to_deal >= 3) {
      //eliminate anything not a single card
      int temp_ranks = current_ranks;
      if (high > 1)
        --temp_ranks;
      if (temp_ranks != 0) {
        other_total = 3 * combo(13 - current_ranks, 1) * combo(4, 2);
        if (to_deal > 3)
          other_total *= combo(13 - current_ranks - 1, to_deal - 3)
              * pow(4, to_deal - 3);
        total += other_total;
      }
      other_total = 0;
    }

    //Case 3.3: one card of each rank of two_pair is in hand
    if(to_deal >= 2) {
      int temp_ranks = current_ranks;
      if (high > 1)
        --temp_ranks;
      if (temp_ranks != 0) {
        other_total = 3 * temp_ranks * combo(temp_ranks, 2);
        if (to_deal > 2)
          other_total *= combo(13 - current_ranks, to_deal - 2) * pow(4, to_deal - 2);
        total += other_total;
      }
      other_total = 0;

      //Case 3.3.1: one pair of two_pair is in hand
      if (high > 1) {
        other_total += combo(4, 2) * (13 - current_ranks);
        if(to_deal > 2)
          other_total *= combo(13 - current_ranks - 1, to_deal - 2) * pow(4, to_deal - 2);
        total += other_total;
      }
      other_total = 0;
    }

    //Case 3.4: one pair and one card of two_pair in hand
    if(to_deal >= 1 && high > 1){
      --current_ranks;
      if(current_ranks != 0) {
        other_total = 3 * current_ranks;
        if (to_deal > 1) {
          other_total *= combo(13 - current_ranks + 1, to_deal - 1)
              * pow(4, to_deal - 1);
        }
        total += other_total;
      }
    }

    total /= combo(deck,to_deal);
    info.two_pair_odds = 100 * total;
  }

  //pretty sure this is correct
  protected void find_three_kind_odds() {
    //CASE 1: already three of kind
    //CASE 2: three of a kind not possible
    //CASE 3: possible
    //Case 3.1: three will match, but no rank currently in hand
    //Case 3.2: two will match to a single rank currently in hand
    //Case 3.3: one will match to two of a kind currently in hand
    //accounts for two pair, three pair

    if(info.kind_high > 2) {
      info.three_kind_odds = 100;
      return;
    }
    else if(info.kind_high + 7 - total_cards < 3) {
      info.three_kind_odds = 0;
      return;
    }

    int total_ranks = total_cards;
    int two_pair = 0;
    int deck = 52 - total_cards;
    int to_deal = 7 - total_cards;
    double total = 0;

    if(info.two_pair) {
      total_ranks -= 2;
      ++two_pair;
    }
    else if(info.kind_high > 1)
      total_ranks -= info.kind_high - 1;

    //Case 3.1: three will match, but no rank currently in hand
    if(to_deal > 2) {
      //13-total_ranks = not any current value of card in hand
      //*4 = any suit // combo(10, to_deal-3) = other cards not in hand and not future 3 kind
      //pow = any suit of other cards that don't matter
      total += (13 - total_ranks) * 4 *
          ((combo(12 - total_ranks, 2)) * pow(4, to_deal - 3) + (combo(total_ranks, 2) * pow(3, to_deal - 3)));
    }

    //Case 3.2: two will match to a single rank currently in hand
    if(to_deal > 1) {
      //other cards besides two of a kind & two pair
      int other_high = info.kind_high; //besides matching cards
      if (two_pair != 0) //and besides if two pair (shouldn't be possible)
        other_high *= 2;
      if (total_cards != other_high) {
        //ranks_not_important -> possible ranks to choose from, not two_kind or two_pair
        //combo(3,2) -> cards you need to match one in hand
        //combo(12-high+1+two_pair, to_deal - 2) -> random cards that don't matter
        //pow -> same as above
        int ranks_not_important = total_ranks - info.kind_high + 1 - two_pair;
        total += ranks_not_important * combo(3, 2) *
            (combo(13 - total_ranks, to_deal - 2) * pow(4, to_deal - 2)
                + (combo(ranks_not_important - 1, 1) * 3));
      }
    }

    //odds of getting match to two_of kind, two_pair, three_pair
    //high -> really combo(2,1) as only need one of two cards (4 if two_pair)
    //combo(12,to_deal-1) -> all other cards of ranks that don't matter
    //pow -> same as above, for suits
    if(two_pair != 0)
      info.kind_high *= 2;
    if(info.kind_high > 1) {
      total += info.kind_high * combo(12, to_deal - 1) * pow(4, to_deal - 1);
    }
    total /= combo(deck, to_deal);
    info.three_kind_odds = 100*total;
  }

  //99% sure this is correct
  protected void find_four_kind_odds() {
    /*CASE 1: already is four_kind
      CASE 2: no possibility of four_kind
      CASE 3: possibility
        Case 3.1: four of kind is not currently a rank
        Case 3.2: currently single rank in hand
        Case 3.3: currently two-kind, or pair, or three-pair
        Case 3.4: currently three-kind, or pair of triples
    */
    int high = info.kind_high; //high kind (3 for three of a kind)
    int to_deal = 7 - total_cards; //the remaining cards to be dealt

    //CASE 1: four_kind already
    if(high > 3) {
      info.four_kind_odds = 100;
      return;
    }

    //CASE 2: four_kind not a possibility
    else if(high + to_deal < 4) {
      info.four_kind_odds = 0;
      return;
    }

    int total_ranks = total_cards - high + 1; //how many ranks in hand
    double total = 0; //to hold running total of viable hands
    int deck = 52 - total_cards; //how many cards in deck
    double other_total = 0; //used for each section, adds to total

    //checks for less ranks
    if(info.full_house || info.two_pair)
      total_ranks -= 1;
    //if(hand[hnd].info.three_pair || hand[hnd].info.two_threes)
    //  total_ranks -= 1;

    //Case 3.1: four_kind comes from no current rank
    if(to_deal >= 4){
      //13-total_ranks = not any current value of card in hand
      //*4 = any suit // combo(12, to_deal-4) = other cards
      //pow = any suit of other cards that don't matter
      other_total = (13 - total_ranks) * 4;
      if(to_deal > 4)
        other_total *= (combo(12 - total_ranks, to_deal - 4)) * pow(4, to_deal - 4) +
            combo(total_ranks, to_deal - 4) * pow(3, to_deal - 4);
      total += other_total;
    }

    //Case 3.2: four_kind comes from a single current rank
    if(to_deal >= 3) {
      int single_ranks = total_ranks;
      if (info.full_house)
        single_ranks -= 3;
      else if (info.two_pair)
        single_ranks -= 2;
      else if (high > 1)
        single_ranks -= high;
      if (single_ranks != 0) {
        other_total = single_ranks;
        if (to_deal > 3)
          other_total *= combo(12 - total_ranks, to_deal - 3) * pow(4, to_deal - 3) +
              combo(total_ranks, to_deal - 3) * pow(3, to_deal - 3);
      }
      total += other_total;
    }

    //Case 3.3: four_kind comes from current 2 of a kind rank
    if(to_deal >= 2) {
      int double_ranks = 0;
      if(info.full_house)
        ++double_ranks;
      else if(info.two_pair)
        double_ranks += 2;
      else if(high == 2)
        ++double_ranks;
      if(double_ranks != 0){
        other_total = double_ranks;
        if(to_deal > 2)
          other_total *= combo(12,to_deal-2)*pow(4,to_deal-2);
        total += other_total;
      }
    }

    //Case 3.4: four_kind comes from current three of a kind rank
    if(to_deal >= 1){
      int triple_ranks = 0;
      //if(hand[hnd].info.two_threes)
      //++triple_ranks;
      if(info.full_house || high == 3){
        ++triple_ranks;
      }
      if(triple_ranks != 0) {
        other_total = triple_ranks;
        if(to_deal > 1){
          other_total *= combo(13-triple_ranks, to_deal-triple_ranks)
              *pow(4,to_deal-triple_ranks);
        }
        total += other_total;
      }
    }
    //total will have all viable four_kind hands
    //divides by all possible hands
    total /= combo(deck, to_deal);
    info.four_kind_odds = 100*total;
  }

  //pretty sure this is correct
  protected void find_full_house_odds(){
    //CASE 1: already two_pair
    //CASE 2: not possible
    //CASE 3: possible
    //Case 3.1: full house are not current ranks in hand
    //Case 3.2: one card of one rank of full_house is currently in hand
    //Case 3.3: one card of each rank of full_house is in hand
    //Case 3.3.1: one pair of full_house is in hand
    //Case 3.4: one pair and one card of two_pair in hand
    //Case 3.4.1: three_kind in hand, need pair
    //Case 3.5: two_pair in hand, need one more

    int to_deal = 7 - total_cards; //the remaining cards to be dealt

    //CASE 1: already full house
    if(info.full_house){
      info.full_house_odds = 100;
      return;
    }

    //CASE 2: not possible
    if((info.kind_high == 1 && to_deal < 3) || (info.kind_high == 2 && to_deal < 2) && !info.two_pair){
      info.full_house_odds = 0;
      return;
    }

    double other_total = 0;
    double second_total = 0;
    int total_ranks = total_cards - info.kind_high + 1; //how many ranks in hand
    if(info.two_pair)
      --total_ranks;
    double total = 0; //to hold running total of viable hands
    int deck = 52 - total_cards; //how many cards in deck
    int single_ranks = total_ranks;
    if(info.two_pair)
      --single_ranks;
    if(info.kind_high > 1)
      --single_ranks;

    //Case 3.1: full house are not current ranks in hand
    if(to_deal >= 5){
      other_total = (13 - total_ranks) * (12 - total_ranks) * combo(4,3)
          * combo(4,2);
      if(to_deal > 5){
        other_total *= (13 - total_ranks - 2)
            * pow(combo(4,to_deal - 1),to_deal-5);
      }
      total += other_total;
      other_total = 0;
    }

    //Case 3.2: one card of one rank of full_house is currently in hand
    if(to_deal >= 4){
      if(single_ranks != 0) {
        other_total = (13 - total_ranks) * combo(4, 3) * 3 * single_ranks;
        other_total += (13 - total_ranks) * combo(4, 2)
            * pow(combo(3, 2), single_ranks);
        if (to_deal > 4)
          other_total *= (13 - total_ranks - 1) * pow(4, to_deal - 4);
        total += other_total;
        other_total = 0;
      }
    }

    //Case 3.3: one card of each rank of full_house is in hand
    if(to_deal >= 3){
      if(single_ranks != 0){
        other_total = pow(pow(3,2),2);
        if(to_deal > 3)
          other_total *= 11*(pow(4,to_deal-3));
      }
      //Case 3.3.1: one pair of full_house is in hand
      if(info.kind_high == 2){
        second_total = combo(12,1) * combo(4,3);
        second_total += combo(2,1) * combo(12,1)
            * combo(4,2);
        if(to_deal > 3)
          second_total *= 11 * pow(4,to_deal-3);
      }
      total += other_total + second_total;
      other_total = 0;
      second_total = 0;
    }

    //Case 3.4: one pair and one card of full_house in hand
    if(to_deal >= 2 && info.kind_high >= 2){
      if(single_ranks != 0 && info.kind_high < 3){
        //getting two matches to single cards
        other_total = combo(3,2) * single_ranks;
        other_total += 2; //get match to two_kind to complete full_house
        if(to_deal > 2)
          other_total *= (13 - total_ranks) * pow(4,to_deal-2);
      }
      //Case 3.4.1: three_kind in hand, need pair
      if(info.kind_high >= 3){
        second_total = (13 - total_ranks) * combo(4,2);
        if(to_deal > 2)
          second_total *= (13-total_ranks-1) * pow(4,to_deal-2);
      }
      total += other_total + second_total;
      other_total = 0;
    }

    //Case 3.5: need one card to complete full_house
    if(to_deal >= 1 && ((info.kind_high == 3 && single_ranks > 0) || info.two_pair)){
      //Case 3.5.1: hand three_kind and single rank in hand
      if(info.kind_high == 3)
        other_total = 3 * single_ranks;
        //Case 3.5.2: have two pair in hand
      else if(info.two_pair)
        other_total += 2 * 2;
      if(to_deal > 1){
        other_total *= (13-total_ranks) * pow(4,to_deal-1);
      }
      total += other_total;
    }

    total /= combo(deck,to_deal);
    info.full_house_odds = 100*total;
  }

  //this works
  protected void find_straight_odds() {
    if(total_cards == 2){
      find_straight_odds_two();
      return;
    }

    int fours = 0;
    int threes = 0;

    if(info.straight) {
      info.straight_odds = 100;
      return;
    }


    double total;
    float deck = 52 - total_cards;
    for(int i = 0; i < 13; ++i) {
      if (info.straight_opportunities[i] == 3)
        ++threes;
      if (info.straight_opportunities[i] == 4)
        ++fours;
    }

    if(threes == 0 && fours == 0){
      info.straight_odds = 0;
      return;
    }

    if(total_cards == 6){
      total = (fours*4)/deck;
      info.straight_odds = total*100;
    }

    else if(total_cards == 5){
      total = 1 - ((fours*4)/deck);
      total *= 1 - ((fours*4)/(deck-1));
      info.straight_odds = 100*(1 - total);


      if(threes > 0) {
        double other_total;
        double another_total;

        if (threes % 2 == 0) {
          other_total = (threes * 4) / deck;
          other_total *= (4 + (threes - 2)) / (deck - 1);
        } else {
          other_total = ((threes - 2) * 4) / deck;
          other_total *= (threes - 1) * 4 / (deck - 1);
          another_total = (threes - 1) * 4 / deck;
          another_total *= (threes - 2) * 4 / (deck - 1);
          other_total += another_total;
        }

        info.straight_odds += other_total * 100;
      }
    }
  }

  //in progress
  protected void find_straight_odds_two(){
    int first = card[0].value;
    int second = card[1].value;
    if(first > second){
      int temp = first;
      first = second;
      second = temp;
    }

    //finds each run of five that will not run into current ranks
    // (x x x x x) x 8 -> will be one empty five position
    // (x [x <x x x) x] x> x 10 -> will be three empty five positions
    int empty_fives = 0;
    int hit = 0, i, j;

    for(i = 0; i < 6; ++i){
      if(i == first || i == second)
        ++hit;
    }
    if(hit == 0)
      ++empty_fives;

    for(i = 0; i < 7; ++i){
      hit = 0;
      for(j = i; j < i+7; ++j){
        if(j == first || j == second)
          ++hit;
      }
      if(hit == 0)
        ++empty_fives;
    }

    hit = 0;
    for(i = 7; i < 13; ++i){
      if(i == first || i == second)
        ++hit;
    }
    if(hit == 0)
      ++empty_fives;

    System.out.println("\nFives: " + empty_fives);

    double total = 0;
    double other_total;

    if(empty_fives != 0){
      other_total = empty_fives * pow(4,5);
      total += other_total;
      other_total = 0;
    }

    /*
      Interrogates evaluation from straight_finder fx
      if 2's, then only need 3 cards of 5 to complete straight
      if 1's, then need 4 of 5 to complete straight
     */

    int user_cards = total_cards;
    int to_deal = 7 - user_cards;
    int deck = 52 - user_cards;
    int ones = 0;
    int twos = 0;

    for(i = 0; i < 13; ++i){
      if(info.straight_opportunities[i] == 1)
        ++ones;
      else if (info.straight_opportunities[i] == 2)
        ++twos;
    }

    if(ones != 0){

    }

    if(twos != 0){

    }

    total /= combo(deck,to_deal);
    info.straight_odds = 100 * total;
  }

  //needs refinement
  protected void find_flush_odds(){
    /*CASE 1: if already flush in hand
      CASE 2: no possibility of flush
      CASE 3: possible
        Case 3.1: no suits of flush in hand
        Case 3.2: any number of flush_suits that are possible
    */

    int flush_number = info.flush_total;
    int to_deal = 7 - total_cards;

    //CASE 1: if already flush in hand
    if(flush_number == 5){
      info.flush_odds = 100;
      return;
    }

    //CASE 2: no possibility of flush
    if(flush_number + to_deal < 5){
      info.flush_odds = 0;
      return;
    }

    double total = 0;
    double all_others = 0;
    double not_in_hand = 0;
    double from_hand = 0;
    int deck = 52 - total_cards;

    // CASE 3: possible flush
    // Case 3.1: no suits of flush in hand
    if(to_deal >= 5)
      not_in_hand = combo(13,5) * info.suit_no[0];

    // Case 3.2: possibility of flush_number becoming flush
    from_hand = combo(13 - flush_number,5 - flush_number);
    if(flush_number == 1)
      from_hand *= total_cards;//since either could become flush

    //possibility of all other cards in cards not in flush
    if(flush_number + to_deal > 5) {
      if(info.suit_no[0] != 0)
        all_others += combo(13, to_deal - (5 - flush_number)) * pow(info.suit_no[0], to_deal - (5 - flush_number));
      if(info.suit_no[1] != 0 && flush_number != 1)
        all_others += combo(12, to_deal - (5 - flush_number)) * pow(info.suit_no[1], to_deal - (5 - flush_number));
    }
    else
      all_others = 1;
    from_hand *= all_others;
    total += from_hand + not_in_hand;
    total /= combo(deck,to_deal);
    info.flush_odds = 100 * total;
  }

  /*
    Following is used for computations in find_odds functions.
   */
  private double combo(int all, int choose){
    if(all < 1 || choose < 1)
      return 1;
    return factorial(all)/(factorial(all-choose)*factorial(choose));
  }

  private double factorial(int number){
    if (number <= 1) return 1;
    else return number * factorial(number - 1);
  }

  protected void get_flush_high() {
    int i, j, suit = 0;
    for(i = 0; i < 4; ++i) {
      if (info.suits[i] == 5)
        suit = info.suits[i];
    }
    if(suit != 0) {
      for (i = 0; i < total_cards; ++i) {
        if (card[i].suit == suit)
          info.flush_high = card[i].value;
      }
    }

    //if there's a straight present, checks for straight flush
    if(info.straight){
      int count = 0;
      for(i = 0; i < total_cards; ++i){
        if(card[i].value >= (info.flush_high - 4) && card[i].suit == suit)
          ++count;
      }
      if(count == 5)
        info.hand_strength = 8;
      if(count == 5 && info.flush_high == 12)
        info.hand_strength = 9;
    }
  }
}