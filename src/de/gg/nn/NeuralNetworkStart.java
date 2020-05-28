/**
 *
 */
package de.gg.nn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

/**
 * @author PeanutPiek
 *
 */
public class NeuralNetworkStart {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

    TrainingData train = new TrainingData("train.dat");

    int[] topology = train.getTopology();
    NeuralNetwork net = new NeuralNetwork(topology);

    double[] inputValues, targetValues, resultValues;

    File file = new File("out.dat");

    StringBuilder out = new StringBuilder();
    out.append("Input;Prediction;Oútput;Error\n");

    int trainPassed = 0;
    while (!train.isEof()) {
      trainPassed++;
      System.out.print("Pass " + trainPassed);

      inputValues = train.getNextInputs();
      if (inputValues.length != topology[0]) {
        System.err.println("Test InputValues Amount does not apply with Count of Neurons in InputLayer.");
        break;
      }

      System.out.println(": Inputs: " + Arrays.toString(inputValues));
      net.feedForward(inputValues);

      resultValues = net.getResults();
      System.out.println("Outputs: " + Arrays.toString(resultValues));

      targetValues = train.getTargetOutputs();
      System.out.println("Targets: " + Arrays.toString(targetValues));

      net.backProp(targetValues);
      System.out.println("Net recent average error: " + net.getRecentAverageError());

      // [Input];[PredictedOutput];[Output];Error
      out.append(String.format("%s;%s;%s;%s", Arrays.toString(inputValues).replace("[", "").replace("]", ""),
          Arrays.toString(targetValues).replace("[", "").replace("]", ""),
          Arrays.toString(resultValues).replace("[", "").replace("]", ""), net.getRecentAverageError()));
      out.append("\n");

    }

    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(file));

      bw.write(out.toString());

      bw.close();
    } catch (Exception e) {
      if (bw != null) {
        try {
          bw.close();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
      e.printStackTrace();
    }

  }

}
