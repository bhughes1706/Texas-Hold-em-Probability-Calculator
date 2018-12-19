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

  protected void init_deck() {
    deck_size = build_deck(0);
  }

  private int build_deck(int suit) {
    int count = 0;
    if (suit == 4)
      return count;
    count = build_suit(suit, 0);
    return build_deck(suit + 1) + count;
  }

  private int build_suit(int suit, int value) {
    if (value == 13)
      return 0;
    add(suit, value);
    return build_suit(suit, value + 1) + 1;
  }

  private void add(int suit, int value) {
    if (head == null) {
      head = new node(suit, value);
      return;
    }
    node temp = new node(suit, value);
    temp.next = head;
    head = temp;
  }

  protected void add_dealer() {
    dealer = new hand(5);
    for (int i = 0; i < 3; ++i)
      deal_card(3);
  }

  void add_user() {
    hand[user] = new hand(2);
    hand[opponent] = new hand(2);
    for (int i = 0; i < 2; ++i)
      deal_card(user);
  }

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

  private card deal_from_deck(node head, int card_placement) {
    if (head == null)
      return null;
    if (card_placement == 1)
      return remove_card(head);
    return deal_from_deck(head.next, --card_placement);
  }

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

  protected void display() {
    System.out.print("\nYour hand:");
    hand[user].display();

    System.out.print("\n\nDealer's hand: ");
    dealer.display();
  }

  //evaluates hand and passes card_info class back to caller
  //passes null if there is no cards in hand
  protected hand_info get_info(int hnd) {
    hand temp = new hand(7);
    temp.to_copy(hand[hnd]);
    temp.to_copy(dealer);

    if (temp.total_cards == 0)
      return null;

    high_card_finder(hnd);
    of_kind_finder(temp, hnd);
    full_house_finder(temp, hnd);
    two_pair_finder(temp, hnd);
    flush_finder(temp, hnd);
    straight_finder(temp, hnd);

    /*
    find_two_kind_odds(temp, hnd);
    find_three_kind_odds(temp, hnd);
    find_four_kind_odds(temp, hnd);
    find_full_house_odds(temp, hnd);
    find_flush_odds(temp, hnd);
    find_straight_odds(temp, hnd);*/
    return hand[hnd].info;
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
  private void of_kind_finder(hand temp, int hnd) {
    int counter;
    for (int i = 0; i < temp.total_cards - 1; ++i) {
      counter = 1;
      for (int j = i + 1; j < temp.total_cards; ++j) {
        if (temp.card[i].value == temp.card[j].value) {
          ++counter;
          if (hand[hnd].info.kind_high <= counter) {
            if (hand[hnd].info.kind_high < counter)
              hand[hnd].info.value_kind_high = temp.card[i].value;
            else if (hand[hnd].info.value_kind_high < temp.card[i].value)
              hand[hnd].info.value_kind_high = temp.card[i].value;
            hand[hnd].info.kind_high = counter;
          }
        }
      }
    }
    if (hand[hnd].info.kind_high == 0)
      hand[hnd].info.kind_high = 1;
  }

  //if there is 3 of kind, then checks for full house
  private void full_house_finder(hand temp, int hnd) {
    if (hand[hnd].info.kind_high < 3)
      return;
    int tripwire = 0;
    int high = hand[hnd].info.value_kind_high;
    for (int i = 0; i < temp.total_cards - 1; ++i) {
      if (temp.card[i].value != high)
        for (int j = i + 1; j < temp.total_cards; ++j) {
          if (temp.card[j].value != high && temp.card[j] == temp.card[i])
            ++tripwire;
        }
      if (tripwire == 1)
        hand[hnd].info.full_house = true;
    }
  }

  //if there is 2 kind, checks for another pair
  private void two_pair_finder(hand temp, int hnd) {
    if (hand[hnd].info.kind_high < 2)
      return;
    int i, j, count;
    for (i = 0; i < temp.total_cards - 1; ++i) {
      count = 0;
      int high = hand[hnd].info.value_kind_high;
      if (temp.card[i].value != high) {
        for (j = i + 1; j < temp.total_cards; ++j) {
          if (temp.card[j].value != high && temp.card[i].value == temp.card[j].value) {
            ++count;
            hand[hnd].info.value_second_pair = temp.card[i].value;
            hand[hnd].info.two_pair = true;
          }
        }
      }
    }
  }

  //evaluates if there is only one suit in hand
  private void flush_finder(hand temp, int hnd) {
    int count;
    for (int i = 0; i < temp.total_cards - 1; ++i) {
      count = 1;
      for (int j = i + 1; j < temp.total_cards; ++j) {
        if (temp.card[i].suit == temp.card[j].suit)
          ++count;
      }
    if(count > 4)
      hand[hnd].info.flush = true;
    }
  }

  //evaluates if hand is a current straight
  //does this by copying hand and bubble sorting(since only max of 5 items)
  private void straight_finder(hand temp, int hnd){
    bubble_sort(temp);
    int straight = 1;
    for(int j = 0; j < temp.total_cards-1; ++j) {
      if (temp.card[j].value + 1 != temp.card[j + 1].value)
        ++straight;
      else
        straight = 1;
      if (straight > 4)
        hand[hnd].info.straight = true;
    }
  }
  //sorts hand for straight evaluation

  private void bubble_sort(hand temp){
    int i, j;
    card card;
    for(i = 0; i < temp.total_cards-1; ++i){
      for(j = 0; j < temp.total_cards-i-1; ++ j){
        if(temp.card[j].value > temp.card[j+1].value){
          card = temp.card[j];
          temp.card[j] = temp.card[j+1];
          temp.card[j+1] = card;
        }
      }
    }
  }


}
