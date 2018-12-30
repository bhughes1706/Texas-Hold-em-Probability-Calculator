import java.util.Random;
import static java.lang.Math.pow;

class deck {
  private node head;
  private int deck_size;
  private hand hand[];
  private hand dealer;
  private final int players = 2;
  private final int user = 0;
  private final int opponent = 1;

  deck() {
    head = null;
    deck_size = 0;
    hand = new hand[players];
  }

  //creates 52 card deck. takes first suit as argument (0)
  protected void init_deck() {
    deck_size = build_deck(0);
  }

  //builds each suit up to 4 by calling build_suit
  // caller: init_deck  callee: build_deck
  private int build_deck(int suit) {
    int count = 0;
    if (suit == 4)
      return count;
    count = build_suit(suit, 0);
    return build_deck(suit + 1) + count;
  }

  //builds each card in suit, up to 13
  // caller: build_deck  callee: add
  private int build_suit(int suit, int value) {
    if (value == 13)
      return 0;
    add(suit, value);
    return build_suit(suit, value + 1) + 1;
  }

  //adds each card built to LLL, caller: build_suit
  private void add(int suit, int value) {
    if (head == null) {
      head = new node(suit, value);
      return;
    }
    node temp = new node(suit, value);
    temp.next = head;
    head = temp;
  }

  //builds dealer hand by creating 5 card slots, and adds 3
  protected void add_dealer() {
    dealer = new hand(5);
    for (int i = 0; i < 3; ++i)
      deal_card(3);
  }

  //adds empty opponent hand
  protected void add_opponent(){
    hand[opponent] = new hand(2);
  }

  //builds user hand by creating 2 cards to 2 empty slots
  protected void add_user() {
    hand[user] = new hand(2);
    for (int i = 0; i < 2; ++i)
      deal_card(user);
  }

  //deals one card to hand, 0 hand_number is user
  //3 or higher will deal to dealer's hand
  protected int deal_card(int hand_number) throws NullPointerException {
      //if deck is empty -- shouldn't ever happen
    if (head == null) return 0;
    Random rand = new Random(); //gets random card placement
    int card_placement = rand.nextInt(deck_size);
    card temp;
      //if head is needed
    if (card_placement == 0)
      temp = remove_first(head);
      //if any other card is needed
    else
      temp = deal_from_deck(head, card_placement);
      //if hand is opponent or user
    if (hand_number < 3)
      hand[hand_number].add(temp);
      //if dealing to dealer
    else {
      dealer.add(temp);
      --deck_size;
        //if dealer has full hand
      if (dealer.total_cards == dealer.max_cards)
        return 1;
    }
    --deck_size;
    return 0;
  }

  //if head is removed, this function will be called
  private card remove_first(node head) {
    card temp;
    if (head.next == null) {
      temp = head.card;
      this.head = null;
      return temp;
    }
    temp = head.card;
    this.head = head.next;
    return temp;
  }

  //function traverses through LLL to find card_placement
  private card deal_from_deck(node head, int card_placement) {
    if (head == null)
      return null;
    if (card_placement == 1)
      return remove_card(head);
    return deal_from_deck(head.next, --card_placement);
  }

  //removes card (only if it's not first card
  private card remove_card(node head) {
    card temp_card = head.next.card;
    if (head.next.next == null) {
      head.next = null;
      return temp_card;
    }
    node temp_node = head.next.next;
    head.next = null;
    head.next = temp_node;
    return temp_card;
  }

  //display's user's and dealer's cards
  protected void display() {
    System.out.print("\nYour hand:");
    hand[user].display();
    if (dealer != null){
      System.out.print("\n\nDealer's hand: ");
      dealer.display();
    }
  }

    //used to determine how many cards to deal,
    //or if any cards have been dealt to dealer
  public int dealer_number() {
    if(dealer != null)
      return dealer.total_cards();
    else
      return 0;
  }

  /*
    evaluates hand and passes card_info class back to caller,
    passes null if there are no cards in hand -- Uses the below
    functions to find specific conditions in hand. Most loop
    through the hand and check conditions. straight finder is
    the most robust algorithm, the rest are fairly simple.
  */
  protected hand_info get_info(int hnd) {
    //creates combined hand with user's and dealer's hand
    hand eval = new hand(7);
    if(hand[hnd] != null)
      eval.to_copy(hand[hnd]); //copies user's hand
    if(dealer != null)
      eval.to_copy(dealer); //copies dealer's hand

    if (eval.total_cards == 0)
      return null;

    //the below evaluates hand for each of the following conditions.
    high_card_finder(hnd);
    of_kind_finder(eval, hnd);
    full_house_finder(eval, hnd);
    two_pair_finder(eval, hnd);
    flush_finder(eval, hnd);
    straight_finder(eval, hnd);

    //uses found conditions to calculate odds for each of below
    find_two_kind_odds(eval, hnd);
    find_three_kind_odds(eval, hnd);
    find_two_pair_odds(eval, hnd);
    find_four_kind_odds(eval, hnd);
    find_full_house_odds(eval, hnd);
    find_flush_odds(eval, hnd);
    find_straight_odds(eval, hnd);
    return hand[hnd].info; //returns to main
  }

  private void high_card_finder(int hnd) {
    int high = 0;
    for (int i = 0; i < hand[hnd].total_cards; ++i) {
      if (hand[hnd].card[i].value > high)
        high = hand[hnd].card[i].value;
    }
    hand[hnd].info.hand_high = high;
    if(dealer != null) {
      for (int j = 0; j < dealer.total_cards; ++j) {
        if (dealer.card[j].value > high)
          high = dealer.card[j].value;
      }
      hand[hnd].info.deck_high = high;
    }
  }

  private void of_kind_finder(hand eval, int hnd) {
    int counter;
    for (int i = 0; i < eval.total_cards - 1; ++i) {
      counter = 1;
      for (int j = i + 1; j < eval.total_cards; ++j) {
        if (eval.card[i].value == eval.card[j].value) {
          ++counter;
          if (hand[hnd].info.kind_high <= counter) {
            if (hand[hnd].info.kind_high < counter)
              hand[hnd].info.value_kind_high = eval.card[i].value;
            else if (hand[hnd].info.value_kind_high < eval.card[i].value)
              hand[hnd].info.value_kind_high = eval.card[i].value;
            hand[hnd].info.kind_high = counter;
          }
        }
      }
    }
    if (hand[hnd].info.kind_high == 0)
      hand[hnd].info.kind_high = 1;
  }

  private void full_house_finder(hand eval, int hnd) {
    if (hand[hnd].info.kind_high < 3)
      return;
    int tripwire = 0;
    int high = hand[hnd].info.value_kind_high;
    for (int i = 0; i < eval.total_cards - 1; ++i) {
      if (eval.card[i].value != high)
        for (int j = i + 1; j < eval.total_cards; ++j) {
          if (eval.card[j].value != high && eval.card[j] == eval.card[i])
            ++tripwire;
        }
      if (tripwire == 1)
        hand[hnd].info.full_house = true;
    }
  }

  private void two_pair_finder(hand eval, int hnd) {
    if (hand[hnd].info.kind_high < 2)
      return;
    int i, j, count;
    for (i = 0; i < eval.total_cards - 1; ++i) {
      count = 0;
      int high = hand[hnd].info.value_kind_high;
      if (eval.card[i].value != high) {
        for (j = i + 1; j < eval.total_cards; ++j) {
          if (eval.card[j].value != high && eval.card[i].value == eval.card[j].value) {
            ++count;
            hand[hnd].info.value_second_pair = eval.card[i].value;
            hand[hnd].info.two_pair = true;
          }
        }
      }
    }
  }

  private void flush_finder(hand eval, int hnd) {
    int count;
    int high;
    hand[hnd].info.flush_high = 0;
    hand[hnd].info.flush_total = 0;

    for (int i = 0; i < eval.total_cards - 1; ++i) {
      count = 1;
      high = 0;
      for (int j = i + 1; j < eval.total_cards; ++j) {
        if (eval.card[i].suit == eval.card[j].suit) {
          ++count;
          if(eval.card[i].value > eval.card[j].value) {
            if(high < eval.card[i].value)
              high = eval.card[i].value;
          }
          else {
            if(high < eval.card[j].value)
              high = eval.card[j].value;
          }
        }
      }
      if(count > hand[hnd].info.flush_total){
        hand[hnd].info.flush_total = count;
        hand[hnd].info.flush_high = high + 2;
      }
      if(count > 4)
        hand[hnd].info.flush = true;
    }
  }

  private void straight_finder(hand eval, int hnd){
    //holds possible straights and which cards are needed for straight
    //find_straight_odds fill out the array for each card

      //creates array to hold current card values
    boolean [] straight_array = new boolean[13];
      //where cards are needed for a possible straight
    int [] needed = new int[13];
    int total_cards = eval.total_cards;
    int count;
    int i,j,k, l;

      //find where all the cards are
    for(i = 0; i < total_cards; ++i)
      straight_array[eval.card[i].value] = true;

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
          hand[hnd].info.straight = true;
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
    hand[hnd].info.straight_opportunities = needed;
  }

  /*
    The following use the above finder functions' data to evaluate
    the to probability of specific events. All use simple
    statistical modeling.
   */

  private void find_two_kind_odds(hand eval, int hnd) {
    //CASE 1: already two of kind
    //CASE 2: possible
      //Case 2.1: two will match, but no rank currently in hand
      //Case 2.2: one will match to a single rank currently in hand

    //CASE 1: currently have two_kind
    if(hand[hnd].info.kind_high > 1){
      hand[hnd].info.two_kind_odds = 100;
      return;
    }

    int user_cards = eval.total_cards;
    int to_deal = 7 - eval.total_cards;
    double total = 0;
    double other_total = 0;

    //Case 2.1
    if(to_deal > 1) {
      other_total = (13 - user_cards) * combo(4, 2);
      if(to_deal > 2)
        other_total *= combo(13 - user_cards - 1, to_deal - 2) * pow(4, to_deal - 2);
      total += other_total;
    }
    //Case 2.2
      other_total = user_cards * 3;
    if(to_deal > 1) {
      other_total *= combo(13 - user_cards, to_deal - 1) * pow(4, to_deal - 1);
    }
    total += other_total;


    //Divide by all possible hands
    total /= combo(52 - user_cards,to_deal);

    hand[hnd].info.two_kind_odds = 100*total;
  }

  //this is correct
  private void find_two_pair_odds(hand eval, int hnd) {
    //CASE 1: already two_pair
    //CASE 2: not possible
    //CASE 3: possible
      //Case 3.1: two pair are not current ranks in hand
      //Case 3.2: one card of one rank of two_pair is currently in hand
      //Case 3.3: one card of each rank of two_pair is in hand
        //Case 3.3.1: one pair of two_pair is in hand
      //Case 3.4: one pair and one card of two_pair in hand

    if(hand[hnd].info.two_pair) {
      hand[hnd].info.two_pair_odds = 100;
      return;
    }

    double total = 0;
    double other_total = 0;
    int user_total = eval.total_cards;
    int high = hand[hnd].info.kind_high;
    int deck = 52 - user_total;
    int to_deal = 7 - user_total;
    int current_ranks = user_total;

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
    hand[hnd].info.two_pair_odds = 100 * total;
  }

  //pretty sure this is correct
  private void find_three_kind_odds(hand eval, int hnd) {
    //CASE 1: already three of kind
    //CASE 2: three of a kind not possible
    //CASE 3: possible
      //Case 3.1: three will match, but no rank currently in hand
      //Case 3.2: two will match to a single rank currently in hand
      //Case 3.3: one will match to two of a kind currently in hand
        //accounts for two pair, three pair

    int high = hand[hnd].info.kind_high;

    if(high > 2) {
      hand[hnd].info.three_kind_odds = 100;
      return;
    }
    else if(high + 7 - eval.total_cards < 3) {
      hand[hnd].info.three_kind_odds = 0;
      return;
    }

    int user_cards = eval.total_cards;
    int total_ranks = user_cards;
    int two_pair = 0;
    int deck = 52 - eval.total_cards;
    int to_deal = 7 - eval.total_cards;
    double total = 0;

    if(hand[hnd].info.two_pair) {
      total_ranks -= 2;
      ++two_pair;
    }
    else if(high > 1)
      total_ranks -= high - 1;

    //odds of getting three not matching in hand at moment
    if(to_deal > 2) {
        //13-total_ranks = not any current value of card in hand
        //*4 = any suit // combo(12, to_deal-3) = other cards
        //pow = any suit of other cards that don't matter
        total += (13 - total_ranks) * 4 *
            (combo(12, to_deal - 3)) * pow(4, to_deal - 3);
    }

    //other cards besides two of a kind & two pair
    int other_high = high; //besides matching cards
    if(two_pair != 0) //and besides if two pair (shouldn't be possible)
      other_high *= 2;
    if(user_cards != other_high && to_deal > 1) {
        //ranks_not_important -> possible ranks to choose from, not two_kind or two_pair
        //combo(3,2) -> cards you need to match one in hand
        //combo(12-high+1+two_pair, to_deal - 2) -> random cards that don't matter
        //pow -> same as above
      int ranks_not_important = total_ranks + high - 1 - two_pair;
      total += ranks_not_important * combo(3, 2) *
          combo(12-high+1+two_pair, to_deal - 2) * pow(4, to_deal - 2);
      }

    //odds of getting match to two_of kind, two_pair, three_pair
    //high -> really combo(2,1) as only need one of two cards (4 if two_pair)
    //combo(12,to_deal-1) -> all other cards of ranks that don't matter
    //pow -> same as above, for suits
    if(two_pair != 0)
      high *= 2;
    if(high > 1) {
      total += high * combo(12, to_deal - 1) * pow(4, to_deal - 1);
    }
    total /= combo(deck, to_deal);
    hand[hnd].info.three_kind_odds = 100*total;
  }

  //this is correct
  private void find_four_kind_odds(hand eval, int hnd) {
    /*CASE 1: already is four_kind
      CASE 2: no possibility of four_kind
      CASE 3: possibility
        Case 3.1: four of kind is not currently a rank
        Case 3.2: currently single rank in hand
        Case 3.3: currently two-kind, or pair, or three-pair
        Case 3.4: currently three-kind, or pair of triples
    */
    int high = hand[hnd].info.kind_high; //high kind (3 for three of a kind)
    int user_cards = eval.total_cards; //how many cards the user has
    int to_deal = 7 - user_cards; //the remaining cards to be dealt

    //CASE 1: four_kind already
    if(high > 3) {
      hand[hnd].info.four_kind_odds = 100;
      return;
    }

    //CASE 2: four_kind not a possibility
    else if(high + to_deal < 4) {
      hand[hnd].info.four_kind_odds = 0;
      return;
    }

    int total_ranks = user_cards - high + 1; //how many ranks in hand
    double total = 0; //to hold running total of viable hands
    int deck = 52 - user_cards; //how many cards in deck
    double other_total = 0; //used for each section, adds to total

    //checks for less ranks
    if(hand[hnd].info.full_house || hand[hnd].info.two_pair)
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
        other_total *= (combo(12, to_deal - 4)) * pow(4, to_deal - 4);
      total += other_total;
    }

    //Case 3.2: four_kind comes from a single current rank
    if(to_deal >= 3) {
      int single_ranks = total_ranks;
      if (hand[hnd].info.full_house)
        single_ranks -= 5;
      else if (hand[hnd].info.two_pair)
        single_ranks -= 4;
      else if (high > 1)
        single_ranks -= high;
      if (single_ranks != 0) {
        other_total = single_ranks;
        if (to_deal > 3)
          other_total *= combo(12, to_deal - 3) * pow(4, to_deal - 3);
      }
      total += other_total;
    }

    //Case 3.3: four_kind comes from current 2 of a kind rank
    if(to_deal >= 2) {
      int double_ranks = 0;
      if(hand[hnd].info.full_house)
        ++double_ranks;
      else if(hand[hnd].info.two_pair)
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
      if(hand[hnd].info.full_house || high == 3){
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
      hand[hnd].info.four_kind_odds = 100*total;
  }

  //pretty sure this is correct
  private void find_full_house_odds(hand eval, int hnd){
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

    int high = hand[hnd].info.kind_high; //high kind (3 for three of a kind)
    int user_cards = eval.total_cards; //how many cards the user has
    int to_deal = 7 - user_cards; //the remaining cards to be dealt

    //CASE 1: already full house
    if(hand[hnd].info.full_house){
      hand[hnd].info.full_house_odds = 100;
      return;
    }

    //CASE 2: not possible
    if((high == 1 && to_deal < 3) || (high == 2 && to_deal < 2) && !hand[hnd].info.two_pair){
      hand[hnd].info.full_house_odds = 0;
      return;
    }

    double other_total = 0;
    double second_total = 0;
    int total_ranks = user_cards - high + 1; //how many ranks in hand
    if(hand[hnd].info.two_pair)
      --total_ranks;
    double total = 0; //to hold running total of viable hands
    int deck = 52 - user_cards; //how many cards in deck
    int single_ranks = total_ranks;
    if(hand[hnd].info.two_pair)
      --single_ranks;
    if(high > 1)
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
      if(high == 2){
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
    if(to_deal >= 2 && high >= 2){
      if(single_ranks != 0 && high < 3){
        //getting two matches to single cards
        other_total = combo(3,2) * single_ranks;
        other_total += 2; //get match to two_kind to complete full_house
        if(to_deal > 2)
          other_total *= (13 - total_ranks) * pow(4,to_deal-2);
      }
      //Case 3.4.1: three_kind in hand, need pair
      if(high >= 3){
        second_total = (13 - total_ranks) * combo(4,2);
        if(to_deal > 2)
          second_total *= (13-total_ranks-1) * pow(4,to_deal-2);
      }
      total += other_total + second_total;
      other_total = 0;
    }

    //Case 3.5: need one card to complete full_house
    if(to_deal >= 1 && ((high == 3 && single_ranks > 0) || hand[hnd].info.two_pair)){
      //Case 3.5.1: hand three_kind and single rank in hand
      if(high == 3)
        other_total = 3 * single_ranks;
      //Case 3.5.2: have two pair in hand
      else if(hand[hnd].info.two_pair)
        other_total += 2 * 2;
      if(to_deal > 1){
        other_total *= (13-total_ranks) * pow(4,to_deal-1);
      }
      total += other_total;
    }

    total /= combo(deck,to_deal);
    hand[hnd].info.full_house_odds = 100*total;
  }

  //this works
  private void find_straight_odds(hand eval, int hnd) {
    if(eval.total_cards == 2){
      find_straight_odds_two(eval, hnd);
      return;
    }

    int fours = 0;
    int threes = 0;

    if(hand[hnd].info.straight) {
      hand[hnd].info.straight_odds = 100;
      return;
    }


    double total;
    float deck = 52 - eval.total_cards;
    for(int i = 0; i < 13; ++i) {
      if (hand[hnd].info.straight_opportunities[i] == 3)
        ++threes;
      if (hand[hnd].info.straight_opportunities[i] == 4)
        ++fours;
    }

    if(threes == 0 && fours == 0){
      hand[hnd].info.straight_odds = 0;
      return;
    }

    if(eval.total_cards == 6){
      total = (fours*4)/deck;
      hand[hnd].info.straight_odds = total*100;
    }

    else if(eval.total_cards == 5){
      total = 1 - ((fours*4)/deck);
      total *= 1 - ((fours*4)/(deck-1));
      hand[hnd].info.straight_odds = 100*(1 - total);


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

        hand[hnd].info.straight_odds += other_total * 100;
      }
    }
  }

  //in progress
  private void find_straight_odds_two(hand eval, int hnd){
    int first = hand[hnd].card[0].value;
    int second = hand[hnd].card[1].value;
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

    int user_cards = eval.total_cards;
    int to_deal = 7 - user_cards;
    int deck = 52 - user_cards;
    int ones = 0;
    int twos = 0;

    for(i = 0; i < 13; ++i){
      if(hand[hnd].info.straight_opportunities[i] == 1)
        ++ones;
      else if (hand[hnd].info.straight_opportunities[i] == 2)
        ++twos;
    }

    if(ones != 0){

    }

    if(twos != 0){

    }

    total /= combo(deck,to_deal);
    hand[hnd].info.straight_odds = 100 * total;
  }

  //in progress
  private void find_flush_odds(hand eval, int hnd) {
    /*CASE 1: if already flush in hand
      CASE 2: no possibility of flush
      CASE 3: possible
        Case 3.1: only one of suit in hand
        Case 3.2: two of suit in hand
        Case 3.3: three of suit in hand
        Case 3.4: four of suit in hand
    */

    int flush_number = hand[hnd].info.flush_total;
    int user_cards = eval.total_cards;
    int to_deal = 7 - user_cards;

    //CASE 1: if already flush in hand
    if(flush_number == 5){
      hand[hnd].info.flush_odds = 100;
      return;
    }

    //CASE 2: no possibility of flush
    if(flush_number + to_deal < 5){
      hand[hnd].info.flush_odds = 0;
      return;
    }

    double total = 0;
    double other_total = 0;
    int deck = 52 - user_cards;

    //CASE 3: possible flush
    switch(flush_number){
      //Case 3.1: only one of suit in hand
      case 1:
        other_total = combo(12,4) * user_cards;
        if(to_deal > 4) {
          other_total *= combo(11, to_deal - 4) * pow(4, to_deal - 4);
        }
        break;
      //Case 3.2: two of suit in hand
      case 2:
        other_total = combo(11,3);
        if(to_deal > 3){
          other_total *= combo(12,to_deal - 3) * pow(4, to_deal - 3);
        }
        break;
      //Case 3.3: three of suit in hand
      case 3:
        other_total = combo(10,2);
        if(to_deal > 2){
          other_total *= combo(12, to_deal - 2) * pow(4, to_deal - 2);
        }
        break;
      //Case 3.4: four of suit in hand
      case 4:
        other_total= combo(9,1);
        if(to_deal > 1){
          other_total *= combo(12, to_deal - 1) * pow(4,to_deal - 1);
        }
        break;
    }
    total += other_total;
    total /= combo(deck,to_deal);
    hand[hnd].info.flush_odds = 100 * total;
  }

  private double combo(int all, int choose){
    if(all < 1 || choose < 1)
      return 1;
    return factorial(all)/(factorial(all-choose)*factorial(choose));
  }

  private double factorial(int number){
    if (number <= 1) return 1;
    else return number * factorial(number - 1);
  }
}