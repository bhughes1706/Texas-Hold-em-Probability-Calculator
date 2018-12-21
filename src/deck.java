import java.util.Random;

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
    //caller: init_deck  callee: build_deck
  private int build_deck(int suit) {
    int count = 0;
    if (suit == 4)
      return count;
    count = build_suit(suit, 0);
    return build_deck(suit + 1) + count;
  }

    //builds each card in suit, up to 13
    //caller: build_deck  callee: add
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

    //builds user hand by creating 2 cards to 2 empty slots
  void add_user() {
    hand[user] = new hand(2);
    hand[opponent] = new hand(2);
    for (int i = 0; i < 2; ++i)
      deal_card(user);
  }

    //deals one card to hand, 0 hand_number is user
    //3 or higher will deal to dealer's hand
  protected int deal_card(int hand_number) throws NullPointerException {
    if (head == null) return 0;
    Random rand = new Random();
    int card_placement = rand.nextInt(deck_size);
    card temp;
    if (card_placement == 0)
      temp = remove_first(head);
    else
      temp = deal_from_deck(head, card_placement);
    if (hand_number < 3)
      hand[hand_number].add(temp);
    else {
      dealer.add(temp);
      --deck_size;
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
      head = null;
      return temp;
    }
    temp = head.card;
    node temp_node = head.next;
    head = temp_node;
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

    System.out.print("\n\nDealer's hand: ");
    dealer.display();
  }

  //evaluates hand and passes card_info class back to caller
  //passes null if there is no cards in hand
  protected hand_info get_info(int hnd) {
    //creates combined hand with user's and dealer's hand
    hand eval = new hand(7);
    eval.to_copy(hand[hnd]); //copies user's hand
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

  //adds high card info to card_info class variable
  private void high_card_finder(int hnd) {
    int high = 0;
    for (int i = 0; i < hand[hnd].total_cards; ++i) {
      if (hand[hnd].card[i].value > high)
        high = hand[hnd].card[i].value;
    }
    hand[hnd].info.hand_high = high;
    for (int j = 0; j < dealer.total_cards; ++j) {
      if (dealer.card[j].value > high)
        high = dealer.card[j].value;
    }
    hand[hnd].info.deck_high = high;
  }

  //checks for pairs and updates card_info variable
  //updates how many of a kind and value of kind
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

  //if there is 3 of kind, then checks for full house
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

  //if there is 2 kind, checks for another pair
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

  //evaluates if there is only one suit in hand
  //and finds largest suit amount, and largest card in that total
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

  //evaluates if hand is a current straight
  //does this by copying hand and bubble sorting(since only max of 5 items)
  private void straight_finder(hand eval, int hnd){
    boolean [] straight_array = new boolean[13];
    int [] needed = new int[13];
    int count;
    for(int i = 0; i < eval.total_cards; ++i)
      straight_array[eval.card[i].value] = true;
    for(int j = 0; j < 9; ++j){
      count = 0;
      for(int k = 0; k < 5; ++k){
        if(straight_array[j+k]) {
          ++count;
        }
        if(count == 5) {
          hand[hnd].info.straight_odds = 100;
          hand[hnd].info.straight = true;
        }
        else if(count >= (eval.total_cards-2)){
          for(int l = 0; l < 5; ++l){
            if(!straight_array[j+l] && count > needed[j+l])
              needed[j+l] = count;
          }
        }
      }
    }
    hand[hnd].info.straight_opportunities = needed;
  }

  private void find_two_kind_odds(hand eval, int hnd) {
    if(hand[hnd].info.kind_high > 1)
      hand[hnd].info.two_kind_odds = 100;
    else if(eval.total_cards == 7)
      hand[hnd].info.two_kind_odds = 0;
    else{
      float total;
      float user_cards = eval.total_cards;

      total =  ((user_cards * 3) / (52 - user_cards));
      if(user_cards == 5){
        total = 1 - total;
        total *= 1 - (((user_cards+1)*3)/(52-user_cards -1));
        total = 1 - total;
      }
      hand[hnd].info.two_kind_odds = total*100;
    }
  }

  private void find_two_pair_odds(hand eval, int hnd){
    float total;
    int count;
    float high = hand[hnd].info.kind_high;
    float deck = 52 - eval.total_cards;

    if(hand[hnd].info.two_pair)
      hand[hnd].info.two_pair_odds = 100;
    else if(high == 1 && 7 - eval.total_cards < 2)
      hand[hnd].info.two_pair_odds = 0;
    else if(high > 1){
      total = ((eval.total_cards - high)*3)/deck;
      if(eval.total_cards == 5){
        total = 1 - total;
        total *= (1 - (((eval.total_cards + 1 - high)*3)/(deck - 1)));
        total = 1 - total;
      }
      hand[hnd].info.two_pair_odds = 100*total;
    }
    else{
      hand[hnd].info.two_pair_odds = 0;
      total = (3*eval.total_cards)/deck;
      total *= (3*(eval.total_cards-1)/(deck-1));
      hand[hnd].info.two_pair_odds = (100*total);
    }
  }

  private void find_three_kind_odds(hand eval, int hnd) {
    float total;
    int count;
    int high = hand[hnd].info.kind_high;
    float deck = 52 - eval.total_cards;

    if(high > 2)
      hand[hnd].info.three_kind_odds = 100;
    else if(high + 7 - eval.total_cards < 3)
      hand[hnd].info.three_kind_odds = 0;
    else if(high == 2 && !hand[hnd].info.two_pair){
      count = 0;
      total = 1;
      total = 2 / deck;

      if(eval.total_cards == 5){
        total = 1 - total;
        total *= (1 - (2 / deck));
        total = 1 - total;
        float other_total = ((eval.total_cards-2)*3)/deck;
        other_total *= 1/(deck - 1);
        total += other_total;
      }
      hand[hnd].info.three_kind_odds = 100*total;
    }
    else if(hand[hnd].info.two_pair){
      total = 4/deck;
      if(eval.total_cards == 5){
        total = 1 - total;
        total *= 1 - (4/(deck - 1));
        total = 1 - total;
        float other_total = ((eval.total_cards-4)*3)/deck;
        other_total *= 2/(deck - 1);
        total += other_total;
      }
      hand[hnd].info.three_kind_odds = 100*total;
    }
    else{
      total = (3*eval.total_cards)/(deck);
      total *= 2/(deck - 1);
      hand[hnd].info.three_kind_odds = (100*total);
    }
  }

  private void find_four_kind_odds(hand eval, int hnd) {
    float total;
    float deck = 52 - eval.total_cards;
    if(hand[hnd].info.kind_high > 3)
      hand[hnd].info.four_kind_odds = 100;
    else if(hand[hnd].info.kind_high + (7 - eval.total_cards) < 4)
      hand[hnd].info.four_kind_odds = 0;
    else if (hand[hnd].info.kind_high == 2) {
      total = 2 / deck;
      total *= 1 / deck;
      hand[hnd].info.four_kind_odds = total*100;
    }
    else if(hand[hnd].info.two_pair && eval.total_cards < 6){
      total = 4/deck;
      total *= 1/deck;
      hand[hnd].info.four_kind_odds = total*100;
    }
    else if(hand[hnd].info.kind_high == 3){
      total = 1/deck;
      if(eval.total_cards == 5){
        total = 1 - total;
        total *= 1 - (1/(deck - 1));
        total = 1 - total;
      }
      hand[hnd].info.four_kind_odds = total;
    }
    else {
      total = 1;
      int count = 0;
      for(int i = eval.total_cards; i < 7; ++i){
        total *= 1-((2)/(float)(52-eval.total_cards-count));
        ++count;
      }
      hand[hnd].info.four_kind_odds = (1-total)*100;
    }
  }

  private void find_full_house_odds(hand eval, int hnd){
    int high = hand[hnd].info.kind_high;
    float total =1;
    int count = 0;
    float card_num = eval.total_cards;
    float deck = 52 - eval.total_cards;

    if(high == 1)
      hand[hnd].info.full_house_odds = 0;
    else if(hand[hnd].info.full_house)
      hand[hnd].info.full_house_odds = 100;
    else if(high == 3){
      total *= ((card_num - high) * 3) / (deck);
      if(card_num == 5) {
        total = 1 - total;
        total *= 1 - (((card_num - high + 1) * 3) / (deck - 1));
        total = 1 - total;
      }
      hand[hnd].info.full_house_odds = 100*total;
    }
    else if(hand[hnd].info.two_pair){
      total = 4/deck;
      if(card_num == 5){
        total = 1 - total;
        total *= 1 - (4/(deck - 1));
        total = 1 - total;
        float other = ((eval.total_cards-4)*3)/deck;
        other *= 2 / (deck - 1);
        total += other;
      }
      hand[hnd].info.full_house_odds = 100*total;
    }
    else if(high == 2 && card_num == 5){
      total = (card_num - high)*3 / deck;
      total *= 2/(deck - 1);
      float other_total = 2/deck;
      other_total *= (card_num - high)*3 / (deck-1);
      total += other_total;
      hand[hnd].info.full_house_odds = 100*total;
    }
  }

  private void find_straight_odds(hand eval, int hnd) {
    int fours = 0;
    int threes = 0;

    if(hand[hnd].info.straight)
      hand[hnd].info.straight_odds = 100;
    else{
      float total;
      float deck = 52 - eval.total_cards;
      for(int i = 0; i < 13; ++i) {
        if (hand[hnd].info.straight_opportunities[i] == 3)
          ++threes;
        if (hand[hnd].info.straight_opportunities[i] == 4)
          ++fours;
      }
      if(threes == 0 && fours == 0)
        hand[hnd].info.straight_odds = 0;
      else if(eval.total_cards == 6){
        total = (fours*4)/deck;
        hand[hnd].info.straight_odds = total*100;
      }

    }
  }

  private void find_flush_odds(hand eval, int hnd) {
    float total;
    int cards = eval.total_cards;
    float flush_number = hand[hnd].info.flush_total;

    if(hand[hnd].info.flush)
      hand[hnd].info.flush_odds = 100;
    else if(flush_number + (7 - cards) < 5)
      hand[hnd].info.flush_odds = 0;
    else if(flush_number == 3){
      total = (13 - flush_number)/(52 - cards);
      total *= (13 - flush_number - 1) / (52 - cards - 1);
      hand[hnd].info.flush_odds = total*100;
    }
    else if(flush_number == 4){
      total = (13 - flush_number) / (52 - cards);
      if(cards == 5){
        total = 1 - total;
        total *= 1 - ((13 - flush_number) / (52 - cards - 1));
        total = 1 - total;
      }
      hand[hnd].info.flush_odds = total*100;
    }

  }

  private double combo(int top, int bottom){
    if(top == 0 || bottom == 0)
      return 0;
    return factorial(top)/(factorial(top-bottom)*factorial(bottom));
  }

  private double factorial(int number){
    if (number <= 1) return 1;
    else return number * factorial(number - 1);
  }

}
