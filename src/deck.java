import java.util.Random;

class deck {
  private node head;
  private int deck_size;
  private hand hand[];
  private final int players = 2;
  private final int user = 0;
  private final int opponent = 1;

  deck(){
    head = null;
    deck_size = 0;
    hand = new hand[players];
    }
  protected void init_deck(){ deck_size = build_deck(0); }
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
  void add_hand(){
    hand[user] = new hand();
    hand[opponent] = new hand();
  }
  protected int deal_card(int hand_number) throws NullPointerException {
    if(head == null) return 0;
    Random rand = new Random();
    int card_placement = rand.nextInt(deck_size);
    card temp;
    if(card_placement == 0)
      temp = remove_first(head);
    else
      temp = deal_from_deck(head, card_placement);
    hand[hand_number].add(temp);
    --deck_size;
    return 1;
  }
  private card remove_first(node head){
    card temp;
    if(head.next == null){
      temp = head.card;
      head = null;
      return temp;
    }
    temp = head.card;
    node temp_node = head.next;
    head = temp_node;
    return temp;
  }
  private card deal_from_deck(node head, int card_placement){
    if(head == null)
      return null;
    if(card_placement == 1)
      return remove_card(head);
    return deal_from_deck(head.next, --card_placement);
  }

  private card remove_card(node head){
    card temp_card = head.next.card;
    if(head.next.next == null){
      head.next = null;
      return temp_card;
    }
    node temp_node = head.next.next;
    head.next = null;
    head.next = temp_node;
    return temp_card;
  }

  protected void display(){
    System.out.println("\nYour hand:");
    hand[user].display();
  }

  protected void discard(int card_select) throws NullPointerException {
    hand[user].discard(card_select);
    if(head == null) return;
    Random rand = new Random();
    int card_placement = rand.nextInt(deck_size);
    card temp = deal_from_deck(head, card_placement);
    hand[user].add_after_discard(temp, card_select);
    --deck_size;
  }

  protected card_info get_info(){
    return hand[user].get_info(deck_size);
  }
}
