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
  protected void add(card to_add){
    hand[total_cards] = new card(to_add);
    ++total_cards;
  }
  protected void display() {
    for(int i = 0; i < total_cards; ++i){
      System.out.println("\nCard " + (i+1) + ":");
      hand[i].display();
    }
  }
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
  protected void add_after_discard(card temp, int card_select) {
    hand[card_select] = new card(temp);
  }
}
