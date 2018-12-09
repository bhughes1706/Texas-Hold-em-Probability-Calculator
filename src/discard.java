public class discard {
  discard next;
  card card;
  discard(){
    next = null;
    card = null;
  }
  discard(card discarded){
    card = discarded;
  }

}
