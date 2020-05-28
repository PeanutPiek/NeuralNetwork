package de.gg.nn;

public interface ITrainingGenerator {

  double[] nextInput(int nodesInFirstLayer);

  double[] nextOutput(int nodesInLayer, double[] input);

}
