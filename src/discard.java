public class discard {
  protected discard next;
  protected card card;

  discard(){
    next = null;
    card = null;
  }
  discard(card discarded){
    card = discarded;
  }
}
