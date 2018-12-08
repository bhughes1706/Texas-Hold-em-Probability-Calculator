class deck {
  node head;
  int deck_size;
  hand hand[];
  int hand_count;
  final int max_players = 6;

  deck(){
    head = null;
    deck_size = 0;
    hand_count = 0;
    hand = new hand[max_players];
    }
  protected void init_deck(){ deck_size = build_deck(0); }
  protected int build_deck(){
    return build_deck(0);
  }
  private int build_deck(int suit){
    int count = 0;
    if(suit == 4)
      return count;
    count = build_suit(suit, 0);
    return build_deck(suit + 1) + count;
  }
  private int build_suit(int suit, int value){
    if(value == 13)
      return 0;
    add(suit, value);
    return build_suit(suit, value + 1) + 1;
  }
  private void add(int suit, int value){
    if(head == null){
      head = new node(suit, value);
      return;
    }
    node temp = new node(suit, value);
    temp.next = head;
    head = temp;
  }
  protected void add_hand(){
    hand[hand_count] = new hand();
  }
  protected void deal_card(hand add_to){

  }
}
