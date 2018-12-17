class hand {
  private card [] hand;
  private int total_cards;
  private int max_cards = 5; //for 5 card poker games
  private discard head;
  private card_info info;

    //sets hand array size with max_cards (**can be improved**)
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
  protected int discard(int card_select){
    if(total_cards == 0)
      return 0;
    if(head == null) {
      head = new discard(hand[card_select]);
      hand[card_select] = null;
      return 1;
    }
    discard temp = new discard(hand[card_select]);
    temp.next = head;
    head = temp;
    hand[card_select] = null;
    return 1;
  }

    //adds new card in slot where card was discarded from
  protected void add_after_discard(card temp, int card_select) {
    hand[card_select] = new card(temp);
  }

    //evaluates hand and passes card_info class back to caller
  protected card_info get_info(){
    high_card_finder();
    of_kind_finder();
    flush_finder();
    straight_finder();
    return info;
  }

    //adds high card info to card_info class variable
  protected void high_card_finder(){
    int high = 0;
    for(int i = 0; i < total_cards; ++i){
      if(hand[i].value > high)
        high = hand[i].value;
    }
    info.high_card = high;
  }

    //checks for pairs and updates card_info variable
    //updates how many of a kind and value of kind
  protected void of_kind_finder(){
    int counter;
    for(int i = 0; i < total_cards; ++i){
      counter = 1;
      for(int j = 0; j < total_cards; ++j){
        if(i != j){
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
    }
      //if three of a kind, then checks for full house
    if(info.kind_high == 3)
      full_house_finder();
    if(info.kind_high == 2)
      two_pair_finder();
  }

    //if there is 3 of kind, then checks for full house
  protected void full_house_finder(){
    int secondary = 0;
    int tripwire = 0;
    for(int i = 0; i < total_cards-1; ++i){
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
  protected void two_pair_finder(){
    int i, j, count = 0;
    for(i = 0; i < total_cards; ++i){
      count = 0;
      for(j = 0; j < total_cards; ++j){
        if(i != j) {
          if (hand[i].value != info.value_kind_high && hand[i].value == hand[j].value)
            ++count;
        }
      }
      if(count > 0)
        info.two_pair = true;
    }
  }

    //evaluates if there is only one suit in hand
  protected void flush_finder(){
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
  protected void straight_finder(){
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
  protected void bubble_sort(card [] copy_hand){
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

  protected card_info(){ kind_high = 0; value_kind_high = 0; high_card = 0;
    full_house = false; flush = false; straight = false;
  }
}
