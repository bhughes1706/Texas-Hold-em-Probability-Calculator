class hand {
  private card [] hand;
  private int total_cards;
  private discard head;
  private card_info info;
  private int discard_total;
  private final int max_cards = 5;

    //sets hand array size
  hand(){
    hand = new card[max_cards];
    info = new card_info();
    total_cards = 0;
    head = null;
  }

    //adds one new card to hand
  protected void add(card to_add){
    hand[total_cards] = new card(to_add);
    ++total_cards;
  }

    //displays for every card in hand
  protected void display() {
    for(int i = 0; i < total_cards; ++i){
      System.out.print("\nCard " + (i+1) + ":");
      hand[i].display();
    }
  }

    //removes card from hand and adds it to the discard class
  protected int discard(int card_select) throws NullPointerException{
    if(total_cards == 0)
      return 0;
    if(head == null) {
      head = new discard(hand[card_select]);
      hand[card_select] = null;
      ++discard_total;
      return 1;
    }
    discard temp = new discard(hand[card_select]);
    temp.next = head;
    head = temp;
    hand[card_select] = null;
    ++discard_total;
    return 1;
  }

    //adds new card in slot where card was discarded from
  protected void add_after_discard(card temp, int card_select) {
    hand[card_select] = new card(temp);
  }

    //evaluates hand and passes card_info class back to caller
    //passes null if there is no cards in hand
  protected card_info get_info(int deck_size) {
    if(total_cards == 0)
      return null;
    high_card_finder();
    of_kind_finder();
    full_house_finder();
    two_pair_finder();
    flush_finder();
    straight_finder();

    find_two_kind_odds(deck_size);
    find_three_kind_odds(deck_size);
    find_four_kind_odds(deck_size);
    find_full_house_odds(deck_size);
    find_flush_odds(deck_size);
    find_straight_odds(deck_size);
    return info;
  }

  private void find_straight_odds(int deck_size) {
  }

  private void find_flush_odds(int deck_size) {
  }

  private void find_three_kind_odds(int deck_size) {
    if(info.kind_high > 2)
      info.three_kind_odds = 100;
    else if(info.kind_high + (max_cards - total_cards) < 3)
      info.three_kind_odds = 0;
    else if(info.kind_high == 2){
      float total = total_cards * 3 - discard_match_value(head);
      info.two_kind_odds = total/deck_size;
    }
  }

  private void find_two_kind_odds(int deck_size) {
    if(info.kind_high > 1)
      info.two_kind_odds = 100;
    else if(total_cards == max_cards)
      info.two_kind_odds = 0;
    else{
      float total = total_cards * 3 - discard_match_value(head);
      info.two_kind_odds = total/deck_size*(max_cards-total_cards);
    }
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

  //adds high card info to card_info class variable
  private void high_card_finder(){
    int high = 0;
    for(int i = 0; i < total_cards; ++i){
      if(hand[i].value > high)
        high = hand[i].value;
    }
    info.high_card = high;
  }

    //checks for pairs and updates card_info variable
    //updates how many of a kind and value of kind
  private void of_kind_finder(){
    int counter;
    for(int i = 0; i < total_cards-1; ++i){
      counter = 1;
      for(int j = i+1; j < total_cards; ++j){
        if(hand[i].value == hand[j].value){
          ++counter;
          if(info.kind_high <= counter){
            if(info.kind_high < counter)
              info.value_kind_high = hand[i].value;
            else if(info.value_kind_high < hand[i].value)
              info.value_kind_high = hand[i].value;
            info.kind_high = counter;
          }
        }
      }
    }
    if(info.kind_high == 0)
      info.kind_high = 1;
  }

  //if there is 3 of kind, then checks for full house
  private void full_house_finder(){
    int secondary = 0;
    int tripwire = 0;
    for(int i = 0; i < total_cards; ++i){
      if(hand[i].value != info.kind_high)
        secondary = hand[i].value;
    }
    for(int j = 0; j < total_cards; ++j){
      if(hand[j].value != info.kind_high || hand[j].value != secondary)
        ++tripwire;
    }
    if(tripwire == 0)
      info.full_house = true;
    else
      info.full_house = false;
  }

    //if there is 2 kind, checks for another pair
  private void two_pair_finder(){
    int i, j, count;
    for(i = 0; i < total_cards-1; ++i){
      count = 0;
      for(j = i+1; j < total_cards; ++j){
          if (hand[i].value != info.value_kind_high && hand[i].value == hand[j].value)
            ++count;
      }
      if(count > 0)
        info.two_pair = true;
    }
  }

    //evaluates if there is only one suit in hand
  private void flush_finder(){
    int first = hand[0].suit;
    int count = 0;
    for(int i = 1; i < total_cards; ++i){
      if(hand[i].suit != first)
        ++count;
    }
    if(count == 0)
      info.flush = true;
    else
      info.flush = false;
  }

    //evaluates if hand is a current straight
    //does this by copying hand and bubble sorting(since only max of 5 items)
  private void straight_finder(){
    card [] copy_hand = new card[total_cards];
    for(int i = 0; i < total_cards; ++i)
      copy_hand[i] = new card(hand[i]);
    bubble_sort(copy_hand);
    int straight = 0;
    for(int j = 0; j < total_cards-1; ++j){
      if(copy_hand[j].value+1 != copy_hand[j+1].value)
        ++straight;
    }
    if(straight == 0)
      info.straight = true;
    else
      info.straight = false;
  }

    //sorts hand for straight evaluation
  private void bubble_sort(card [] copy_hand){
    int i, j;
    card temp;
    for(i = 0; i < total_cards-1; ++i){
      for(j = 0; j < total_cards-i-1; ++ j){
        if(copy_hand[j].value > copy_hand[j+1].value){
          temp = copy_hand[j];
          copy_hand[j] = copy_hand[j+1];
          copy_hand[j+1] = temp;
        }
      }
    }
  }

  private int discard_match_value(discard head){
    if(head == null)
      return 0;
    int counter = 0, i;
    for(i = 0; i < total_cards; ++i){
      if(hand[i].value == head.card.value){
        ++counter;
      }
    }
    return discard_match_value(head.next) + counter;
  }
}

  //used to store info about hand
class card_info{
  protected int high_card;
  protected int kind_high;
  protected int value_kind_high;
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

  protected card_info(){ kind_high = 0; value_kind_high = 0; high_card = 0;
    full_house = false; flush = false; straight = false;
  }
}
