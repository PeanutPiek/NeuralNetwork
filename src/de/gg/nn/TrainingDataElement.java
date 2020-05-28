/**
 *
 */
package de.gg.nn;

/**
 * @author PeanutPiek
 *
 */
public class TrainingDataElement {

  private final double[] input;

  private final double[] output;

  public TrainingDataElement(double[] in, double[] out) {
    this.input = in;
    this.output = out;
  }

  /**
   * @return the input
   */
  public double[] getInput() {
    return input;
  }

  /**
   * @return the output
   */
  public double[] getOutput() {
    return output;
  }

}
