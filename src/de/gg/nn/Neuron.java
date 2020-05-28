/**
 *
 */
package de.gg.nn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author PeanutPiek
 *
 */
public class Neuron {

  private double m_outputValue;

  private double m_gradient;

  private int m_weightIndex;

  private final List<NetworkConnection> m_outputWeights;

  private final ITransferFunction m_transfer;

  private final double eta = 0.75;

  private final double alpha = 0.5;

  public Neuron(int numOutputs, int weightIndex) {
    m_transfer = new SimpleTransferFunction();
    m_outputWeights = new ArrayList<NetworkConnection>();
    for (int c = 0; c < numOutputs; ++c) {
      m_outputWeights.add(new NetworkConnection());
      m_outputWeights.get(m_outputWeights.size() - 1).setWeight(randomWeight());
    }
  }

  public void feedForward(NetworkLayer prevLayer) {

    double sum = 0.0;
    for (int n = 0; n < prevLayer.size(); ++n) {
      sum += prevLayer.get(n).getOutputValue() * prevLayer.get(n).m_outputWeights.get(m_weightIndex).getWeight();
    }

    m_outputValue = m_transfer.getValue(sum);
  }

  /**
   * @return the m_outputValue
   */
  public double getOutputValue() {
    return m_outputValue;
  }

  /**
   * @param m_outputValue
   *          the m_outputValue to set
   */
  public void setOutputValue(double m_outputValue) {
    this.m_outputValue = m_outputValue;
  }

  protected double randomWeight() {
    Random rand = new Random();
    return rand.nextDouble();
  }

  public void calcOutputGradients(double targetVal) {
    double delta = targetVal - m_outputValue;
    m_gradient = delta * m_transfer.getValueDerivative(m_outputValue);
  }

  public void calcHiddenGradients(NetworkLayer nextLayer) {
    double dow = sumDow(nextLayer);
    m_gradient = dow * m_transfer.getValueDerivative(m_outputValue);
  }

  private double sumDow(NetworkLayer nextLayer) {
    double sum = 0.0;
    for (int n = 0; n < nextLayer.size() - 1; ++n) {
      sum += m_outputWeights.get(n).getWeight() * nextLayer.get(n).getGradient();
    }
    return sum;
  }

  private double getGradient() {
    return m_gradient;
  }

  public void updateInputWeights(NetworkLayer prevLayer) {
    for (int n = 0; n < prevLayer.size(); ++n) {
      Neuron neuron = prevLayer.get(n);
      double oldDeltaWeight = neuron.m_outputWeights.get(m_weightIndex).getDeltaWeight();
      double newDeltaWeight = eta * neuron.getOutputValue() * m_gradient + alpha * oldDeltaWeight;

      neuron.m_outputWeights.get(m_weightIndex).setDeltaWeight(newDeltaWeight);
      neuron.m_outputWeights.get(m_weightIndex)
          .setWeight(neuron.m_outputWeights.get(m_weightIndex).getWeight() + newDeltaWeight);
    }
  }

}
