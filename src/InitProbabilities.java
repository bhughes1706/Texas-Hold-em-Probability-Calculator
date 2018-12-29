import static java.lang.Math.pow;

class InitProbabilities {
  /*
  Used for initial probabilities of specific hands before
  cards are dealt. Could be hard coded, but used
  combinations and factorials for the mathematical example
  and for flexibility in different poker games for future coding,
  and, because it's good practice

  http://pi.math.cornell.edu/~mec/2006-2007/Probability/Texasholdem.pdf
  http://mathforum.org/library/drmath/view/65306.html
  were both used as reference and for initial guidance
  */

  double full_house;
  double four_kind;
  double three_kind;
  double flush;
  double straight_flush;
  double royal_flush;
  double straight;
  double two_pair;
  double one_pair;

  InitProbabilities() {
    straight_flush = straight_flush_odds();
    four_kind = four_kind_odds();
    full_house = full_house_odds();
    three_kind = three_kind_odds();
    flush = flush_odds();
    royal_flush = royal_flush_odds();
    straight = straight_odds();
    one_pair = two_kind_odds();
    two_pair = two_pair_odds();
  }

  private double two_pair_odds(){
    double total;
    total = (combo(13,5) -10) * 10;
    total *= (6*62 + 24*63 + 6*64); //get one pair
    total += combo(13,3)*(pow(combo(4,2), 3))*40;
    return 100*(total/combo(52,7));
  }

  private double two_kind_odds() {
    double total;
    total = combo(13,6) - 71;
    total *= combo(4,2) * combo(4,2);
    total *= pow(4,5) - 30 - 4;
    return 100*(total / combo(52,7));
  }

  private double straight_odds() {
    double total = pow(4,7) - (4*combo(7,5)*3*3);
    int other = 0;
    total *= 217;
    total += 71*pow(6,2)*(pow(4,5) - 34);
    total += 10*combo(5,4)*4*(pow(4,4) -3);
    for(int i = 62; i < 65; ++i){
      other += 6*i;
    }
    total += 10*combo(5,2) * other;
    return 100*(total/combo(52,7));
  }

  private double royal_flush_odds() {
      return 100*(4*combo(47,2)/combo(52,7));
    }

  private double flush_odds() {
    double total = 4*combo(13,7);
    total += 4*combo(13,6)*(39);
    total += 4*combo(13,5)*combo(39,2);
    return (100*(total / combo(52,7))) - straight_flush_odds();
  }

  private double three_kind_odds() {
    return 100*(((combo(13,5)-10)*5*4*253) / combo(52,7));
  }

  private double straight_flush_odds() {
    double total = combo(47,2)*4;
    total += combo(46,2)*4*9;
    return 100*(total/combo(52,7)) - royal_flush_odds();
  }

  private double four_kind_odds() {
    return 100*((combo(48,3)*13) / combo(52,7));
  }

  private double full_house_odds() {
      //all full houses with extra cards being pair
    double first_total = combo(13,1);
    first_total *= combo(4,3);
    first_total *= combo(12,2);
    first_total *= combo(4,2)*6;
      //hand has two triples
    double next_total = combo(13,2);
    next_total *= combo(4,3)*4;
    next_total *= 44;
      //cards are different than one another
    double third_total = combo(4,3)*13;
    third_total *= combo(4,2)*12;
    third_total *= combo(11,2)*16;
      //add, divide by all possible hands
    double total = first_total + next_total + third_total;
    total = total/(combo(52,7));
    return total*100;
  }

  private double combo(int top, int bottom){
    if(top == 0 || bottom == 0)
      return 0;
    return factorial(top)/(factorial(top-bottom)*factorial(bottom));
  }

  private double factorial(int number){
    if (number <= 1) return 1;
    else return number * factorial(number - 1);
  }

}
