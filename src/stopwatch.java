/* This class provides a stopwatch to benchmark performance for the iterations
   and odds making main functions
 */

class stopwatch {
  private long start = 0;

  protected void begin() {
    start = System.currentTimeMillis();
  }

  protected double elapsedTime() {
    long now = System.currentTimeMillis();
    return (now - start) / 1000.0;
  }
}