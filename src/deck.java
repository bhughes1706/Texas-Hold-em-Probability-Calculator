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

  //creates 52 card deck. takes first suit as argument (0)
  protected void init_deck() {
    build_deck(0);
  }

  //builds each suit up to 4 by calling build_suit
  // caller: init_deck  callee: build_deck
  private int build_deck(int suit) {
    int count = 0;
    if (suit == 4)
      return count;
    count = build_suit(suit, 0);
    return build_deck(suit + 1) + count;
  }

  //builds each card in suit, up to 13
  // caller: build_deck  callee: add
  private int build_suit(int suit, int value) {
    if (value == 13)
      return 0;
    add(suit, value);
    return build_suit(suit, value + 1) + 1;
  }

  //adds each card built to LLL, caller: build_suit
  private void add(int suit, int value) {
    if (head == null) {
      head = new node(suit, value);
      return;
    }
    node temp = new node(suit, value);
    temp.next = head;
    head = temp;
    ++deck_size;
  }

  //builds dealer hand by creating 5 card slots, and adds 3
  protected void add_dealer() {
    dealer = new hand(5);
  }

  //adds empty opponent hand
  protected void add_opponent(){
    hand[opponent] = new hand(2);
  }

  //builds user hand by creating 2 cards to 2 empty slots
  protected void add_user() {
    hand[user] = new hand(2);
    for (int i = 0; i < 2; ++i)
      deal_card(user);
  }

  //deals one card to hand, 0 hand_number is user
  //3 or higher will deal to dealer's hand
  protected void deal_card(int hand_number) throws NullPointerException {
      //if deck is empty -- shouldn't happen
    if (head == null) return;
    Random rand = new Random(); //gets random card placement
    int card_placement = rand.nextInt(deck_size);
    card temp;
      //if head is needed
    if (card_placement == 0)
      temp = remove_first(head);
      //if any other card is needed
    else
      temp = deal_from_deck(head, card_placement);
      //if hand is opponent or user
    if (hand_number < 3)
      hand[hand_number].add(temp);
      //if dealing to dealer
    else
      dealer.add(temp);
    --deck_size;
  }

  //if head is removed, this function will be called
  private card remove_first(node head) {
    card temp;
    if (head.next == null) {
      temp = head.card;
      this.head = null;
      return temp;
    }
    temp = head.card;
    this.head = head.next;
    return temp;
  }

  //function traverses through LLL to find card_placement
  private card deal_from_deck(node head, int card_placement) {
    if (head == null)
      return null;
    if (card_placement == 1)
      return remove_card(head);
    return deal_from_deck(head.next, --card_placement);
  }

  //removes card (only if it's not first card)
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

  //display's user's and dealer's cards
  protected void display() {
    System.out.print("\nYour hand:");
    hand[user].display();
    if (dealer != null){
      System.out.print("\n\nDealer's hand: ");
      dealer.display();
    }
  }

    //used to determine how many cards to deal,
    //or if any cards have been dealt to dealer
  public int dealer_number() {
    if(dealer != null)
      return dealer.total_cards();
    else
      return 0;
  }

  protected int [] iterator(int iterations){
    int dealer_start = dealer.total_cards, j, i;
    int [] record = new int[3];
    for(i = 0; i < iterations; ++i){
      for(j = 0; j < 2; ++j)
        deal_card(opponent);
      if(dealer_start < 5){
        for(j = dealer_start; j < 5; ++j)
          deal_card(3);
      }

      //return 0 for win, 1 for loss, 2 for tie
      ++record[compare(user, opponent)];

      for(j = 0; j < 2; ++j) {
        add(hand[opponent].card[j].suit, hand[opponent].card[j].value);
        hand[opponent].card[j] = null;
        --hand[opponent].total_cards;
      }
      for(j = dealer_start; j < 5; ++j) {
        add(dealer.card[j].suit, dealer.card[j].value);
        dealer.card[j] = null;
        --dealer.total_cards;
      }
      hand[user].info.hand_strength = 0;
      hand[opponent].info.hand_strength = 0;
    }
    return record;
  }

  private int compare(int one, int two) {
    hand_info user = get_compare_info(one);
    hand_info opponent = get_compare_info(two);

    if (user.hand_strength > opponent.hand_strength)
      return 0;

    else if (user.hand_strength < opponent.hand_strength)
      return 1;

    else if (user.hand_strength == 0) {
      if (hand[0].card[0].value > hand[1].card[0].value)
        return 0;
      if (hand[0].card[0].value < hand[1].card[0].value)
        return 1;
      if (hand[0].card[1].value > hand[1].card[1].value)
        return 0;
      if (hand[0].card[1].value < hand[1].card[1].value)
        return 1;
      return 2;
    }

    else if ((user.hand_strength > 0 && user.hand_strength < 4) || user.hand_strength == 7 || user.hand_strength == 6) {
      if (user.value_kind_high > opponent.value_kind_high)
        return 0;
      else if (user.value_kind_high < opponent.value_kind_high)
        return 1;
      else if (user.value_second_pair > opponent.value_second_pair)
        return 0;
      else if (user.value_second_pair < opponent.value_second_pair)
        return 1;
      //evaluates extra cards, can't apply to full house, as it already is 5 cards
      if(user.hand_strength != 6) {
      if (hand[0].card[0].value > hand[1].card[0].value)
          return 0;
        else if (hand[0].card[0].value < hand[1].card[0].value)
          return 1;
        else if (hand[0].card[1].value > hand[1].card[1].value)
          return 0;
        else if (hand[0].card[1].value < hand[1].card[1].value)
          return 1;
      }
      else
        return 2;
    }

    else if (user.hand_strength == 5) {
      if (user.flush_high > opponent.flush_high)
        return 0;
      else if (user.flush_high < opponent.flush_high)
        return 1;
    }

    else if (user.hand_strength == 4) {
      if (user.straight_high > opponent.straight_high)
        return 0;
      else if (user.straight_high < opponent.straight_high)
        return 1;
    }
    return 2;
  }

  /*
    evaluates hand and passes card_info class back to caller,
    passes null if there are no cards in hand -- Uses the below
    functions to find specific conditions in hand. Most loop
    through the hand and check conditions. straight finder is
    the most robust algorithm, the rest are fairly simple.
  */
  protected hand_info get_info(int hnd) {
    //creates combined hand with user's and dealer's hand
    hand eval = new hand(7);
    if(hand[hnd] != null)
      eval.to_copy(hand[hnd]); //copies user's hand
    if(dealer != null)
      eval.to_copy(dealer); //copies dealer's hand

    if (eval.total_cards == 0)
      return null;

    //the below evaluates hand for each of the following conditions.
    eval.general_info(); //must always do this first
    hand[hnd].card_sorter();
    eval.of_kind_finder(); //must be done before full_house
    eval.full_house_finder();
    eval.pair_finder();
    eval.flush_finder();
    eval.straight_finder();

    //uses found conditions to calculate odds for each of below
    eval.find_two_kind_odds();
    eval.find_three_kind_odds();
    eval.find_two_pair_odds();
    eval.find_four_kind_odds();
    eval.find_full_house_odds();
    eval.find_flush_odds();
    eval.find_straight_odds();
    return eval.info; //returns to main
  }

  //this is a truncated version that quickly evaluates a hand using the bare minimum
  //since only is interested in final results, it's much smaller than a full evaluation
  protected hand_info get_compare_info(int select){
    hand evaluation = new hand(7);
    evaluation.to_copy(hand[select]);
    evaluation.to_copy(dealer);

    evaluation.general_info();
    hand[select].card_sorter();

    evaluation.quick_evaluation();

    //the only thing quick_evaluation can't handle
    if(evaluation.info.hand_strength == 5)
      evaluation.get_flush_high();

    return evaluation.info;
  }
}