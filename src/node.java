class node {
  card card;
  node next;

  node(int suit_init, int value_init){
    card = new card(suit_init, value_init);
  }
}
