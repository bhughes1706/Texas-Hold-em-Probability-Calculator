class hand {
  private card [] hand;
  private int total_cards;
  private final int max_cards = 5; //for 5 card poker games
  private discard head;

  hand(){
    hand = new card[max_cards];
    total_cards = 0;
    head = null;
  }
  void add(card to_add){
    hand[total_cards] = new card(to_add);
    ++total_cards;
  }
  protected void display() {
    for(int i = 0; i < total_cards; ++i){
      hand[i].display();
    }
  }
  protected int discard(int card_select){
    if(total_cards == 0)
      return 0;
    if(head == null) {
      head = new discard(hand[card_select]);
      hand[card_select] = null;
      --total_cards;
      return 1;
    }
    discard temp = new discard(hand[card_select]);
    temp.next = head;
    head = temp;
    --total_cards;
    return 1;
  }
}
