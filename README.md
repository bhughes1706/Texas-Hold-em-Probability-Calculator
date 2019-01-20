# Texas Hold'em Continuing Probabilities Calculator

**Still in progress, in testing phase currently**
Straight finder for no dealer cards is still in progress, everything else functional

We all know college can get old after awhile, so I began this small program as a way to tinker with something that was easy to add to, incorporated some interesting mathematics, and didn't have a due date. There are many of poker probability calculators out there, but this one was designed to train the user to spot probabilities and make good decisions based on them. The main difference in this program vs. others was to use combinations to acquire the odds of an event happening instead of iterating over millions of hands to find the result. Probabilities at all parts of the program, besides the chance of beating a random hand, are exact to a thousandths of a percent and acquire results in ~20 ms on a 1.6 GHz laptop. The odds of beating a random hand does employ iteration, but acquires results in ~650 ms if no community cards are present and ~240 ms if all are present. All algorithms are my own, but http://mathforum.org/library/drmath/view/65306.html was of help when it came to some of the more difficult mathematics.

**Hand Probabilities Display**

Below is sample run of the probabilities of different hands, run in 11 ms on a 1.6 GHz laptop. 

![Imgur](https://i.imgur.com/3zmc3m8.png)

**Hand Win Probability Display**

Below is a sample run of the probability of winning against a random hand with no dealer hand present, run in 643 ms on a 1.6 GHz laptop. The program deals complete hands for the opponent (always assumes one) and the dealer, then evaluates all results, including ties.

![Imgur](https://i.imgur.com/cGBRsq4.png)

Another sample run, this time with all cards present. Since the program does not have to iterate through dealer hands, it only deals cards for one opponent, and evaluates the results. Ran in 245 ms on a 1.6 GHz laptop.

![Imgur](https://i.imgur.com/nUbkIj7.png)

****Possible further design elements****
--Track user's ability to guess the probabilities of their hand in random situations to show what they're weak in
--Implement probability guessing with differing opponent group sizing
