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

  protected card_info get_info(){
    high_card();
    of_kind();
    flush_finder();
    straight_finder();
    return info;
  }

  protected void high_card(){
    int high = 0;
    for(int i = 0; i < total_cards; ++i){
      if(hand[i].value > high)
        high = hand[i].value;
    }
    info.high_card = high;
  }

    //checks for pairs and return card_info class
    // containing how many of a kind and value
  protected void of_kind(){
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
      full_house();
  }

    //if there is 3 of kind, then checks for full house
  protected void full_house(){
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
  }

  protected void straight_finder(){

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

  protected card_info(){ kind_high = 0; value_kind_high = 0; high_card = 0;
    full_house = false; flush = false; straight = false;
  }
}
