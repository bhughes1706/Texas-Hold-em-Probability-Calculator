
class hand {
  private card [] hand;
  private int total_cards;
  private final int max_cards = 5; //for 5 card poker games

  hand(){
    hand = new card[max_cards];
    total_cards = 0;
  }
  void add(card to_add){
    hand[total_cards] = new card(to_add);
    ++total_cards;
  }
}
