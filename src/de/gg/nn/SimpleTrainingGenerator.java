/**
 *
 */
package de.gg.nn;

import java.util.Random;

/**
 * @author PeanutPiek
 *
 */
public class SimpleTrainingGenerator implements ITrainingGenerator {

  @Override
  public double[] nextInput(int nodesInLayer) {
    double[] inputValues = new double[nodesInLayer];
    Random rand = new Random();
    for (int i = 0; i < nodesInLayer; ++i) {
      inputValues[i] = rand.nextBoolean() ? 1.0 : 0.0;
    }
    return inputValues;
  }

  @Override
  public double[] nextOutput(int nodesInLayer, double[] input) {
    double[] outputValues = new double[nodesInLayer];
    for (int i = 0; i < nodesInLayer; ++i) {
      double sum = 0.0;
      for (int n = 0; n < input.length; ++n) {
        sum += input[n];
      }
      outputValues[i] = sum / input.length;
    }
    return outputValues;
  }

}
