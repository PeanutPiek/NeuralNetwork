/**
 *
 */
package de.gg.nn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PeanutPiek
 *
 */
public class NeuralNetwork {

  private double m_error;

  private final List<NetworkLayer> m_layers;

  private double m_recentAverageError;

  private double m_recentAverageSmoothingFactor;

  public NeuralNetwork(int[] topology) {
    m_layers = new ArrayList<NetworkLayer>();
    int numLayers = topology.length;
    for (int layerNum = 0; layerNum < numLayers; ++layerNum) {
      m_layers.add(new NetworkLayer());
      int numOutputs = layerNum == topology.length - 1 ? 0 : topology[layerNum + 1];
      int neuronNum;
      for (neuronNum = 0; neuronNum <= topology[layerNum]; ++neuronNum) {
        m_layers.get(m_layers.size() - 1).add(new Neuron(numOutputs, neuronNum));
        System.out.println(
            String.format("Create a new Neuron[%d] with %d Outputs in Layer %d", neuronNum, numOutputs, layerNum + 1));
      }

      m_layers.get(m_layers.size() - 1).get(neuronNum - 1).setOutputValue(1.0);
    }
  }

  public void feedForward(double[] inputValues) {
    if (inputValues.length == m_layers.get(0).size() - 1) {
      for (int i = 0; i < inputValues.length; ++i) {
        m_layers.get(0).get(i).setOutputValue(inputValues[i]);
      }

      for (int layerNum = 1; layerNum < m_layers.size(); ++layerNum) {
        NetworkLayer prevLayer = m_layers.get(layerNum - 1);
        for (int n = 0; n < m_layers.get(layerNum).size() - 1; ++n) {
          m_layers.get(layerNum).get(n).feedForward(prevLayer);
        }
      }
    }
  }

  public double[] getResults() {
    NetworkLayer outputLayer = m_layers.get(m_layers.size() - 1);
    double[] resultValues = new double[outputLayer.size() - 1];
    for (int n = 0; n < outputLayer.size() - 1; ++n) {
      resultValues[n] = outputLayer.get(n).getOutputValue();
    }
    return resultValues;
  }

  public double getRecentAverageError() {
    return m_error;
  }

  public void backProp(double[] targetValues) {

    NetworkLayer outputLayer = m_layers.get(m_layers.size() - 1);
    m_error = 0.0;

    for (int n = 0; n < outputLayer.size() - 1; ++n) {
      double delta = targetValues[n] - outputLayer.get(n).getOutputValue();
      m_error += delta * delta;
    }
    m_error /= outputLayer.size() - 1;
    m_error = Math.sqrt(m_error);

    m_recentAverageError = (m_recentAverageError * m_recentAverageSmoothingFactor + m_error)
        / (m_recentAverageSmoothingFactor + 1.0);

    for (int n = 0; n < outputLayer.size() - 1; ++n) {
      outputLayer.get(n).calcOutputGradients(targetValues[n]);
    }

    for (int layerNum = m_layers.size() - 2; layerNum > 0; --layerNum) {
      NetworkLayer hiddenLayer = m_layers.get(layerNum);
      NetworkLayer nextLayer = m_layers.get(layerNum + 1);

      for (int n = 0; n < hiddenLayer.size(); ++n) {
        hiddenLayer.get(n).calcHiddenGradients(nextLayer);
      }
    }

    for (int layerNum = m_layers.size() - 1; layerNum > 0; --layerNum) {
      NetworkLayer layer = m_layers.get(layerNum);
      NetworkLayer prevLayer = m_layers.get(layerNum - 1);

      for (int n = 0; n < layer.size() - 1; ++n) {
        layer.get(n).updateInputWeights(prevLayer);
      }
    }
  }

}
