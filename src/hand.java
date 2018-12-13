class hand {
  private card [] hand;
  private int total_cards;
  private int max_cards = 5; //for 5 card poker games
  private discard head;

    //sets hand array size with max_cards (**can be improved**)
  hand(){
    hand = new card[max_cards];
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
      System.out.println("\nCard " + (i+1) + ":");
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

    //checks for pairs and return card_info class
    // containing how many of a kind and value
  protected card_info of_kind(){
    card_info temp = new card_info();
    int counter;
    for(int i = 0; i < total_cards; ++i){
      counter = 1;
      for(int j = 0; j < total_cards; ++j){
        if(i != j){
          if(hand[i].value == hand[j].value){
            ++counter;
            if(temp.kind_high <= counter){
              if(temp.kind_high < counter)
                temp.value_kind_high = hand[i].value;
              else if(temp.value_kind_high < hand[i].value)
                temp.value_kind_high = hand[i].value;
              temp.kind_high = counter;
            }
          }
        }
      }
    }
      //if three of a kind, then checks for full house
    if(temp.kind_high == 3)
      temp = full_house(temp);
    return temp;
  }

    //if there is 3 of kind, then checks for full house
  protected card_info full_house(card_info temp){

    return temp;
  }
}

  //used to return info about hand to main
class card_info{
  protected int high_card;
  protected int kind_high;
  protected int value_kind_high;
  protected boolean full_house;
  protected boolean flush;
  protected boolean straight;

  protected card_info(){ kind_high = 0; value_kind_high = 0; high_card = 0; }
}
