/**
 *
 */
package de.gg.nn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author PeanutPiek
 *
 */
public class TrainingData {

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Usage: TrainingData <topology> <amount> <outfile>");
      return;
    }

    String topo = args[0];
    String[] splits = topo.split(",");

    if (splits.length < 2) {
      System.out.println("Topology must contain minimum two Layers, ex. '3,2,1'");
      return;
    }

    int dataCount = Integer.parseInt(args[1]);
    int nodesInFirstLayer = Integer.parseInt(splits[0]);
    int nodesInLastLayer = Integer.parseInt(splits[splits.length - 1]);

    String filename = args[2];
    File file = new File(filename);

    ITrainingGenerator gen = new SimpleTrainingGenerator();

    BufferedWriter bw = null;
    try {

      bw = new BufferedWriter(new FileWriter(file));

      bw.write(Arrays.toString(topo.split(",")).replace("[", "").replace("]", ""));
      bw.write("\n");

      for (int c = 0; c < dataCount; ++c) {

        double[] input = gen.nextInput(nodesInFirstLayer);
        double[] output = gen.nextOutput(nodesInLastLayer, input);

        bw.write(Arrays.toString(input).replace("[", "").replace("]", ""));
        bw.write("\n");

        bw.write(Arrays.toString(output).replace("[", "").replace("]", ""));
        bw.write("\n");

      }

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

  private int currentElement;

  private int[] m_topology;

  private List<TrainingDataElement> m_data;

  public TrainingData(String filename) {
    currentElement = -1;
    loadTrainingData(new File(filename));
  }

  private void loadTrainingData(File tFile) {

    if (m_data == null) {
      m_data = new ArrayList<TrainingDataElement>();
    } else {
      m_data.clear();
    }

    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(tFile));

      String line = br.readLine();
      String[] splits = line.split(", ");
      m_topology = new int[splits.length];
      for (int i = 0; i < splits.length; ++i) {
        m_topology[i] = Integer.parseInt(splits[i]);
      }

      String input = null, output = null;
      while ((line = br.readLine()) != null) {
        if (input == null) {
          input = line;
        } else if (output == null) {
          output = line;
        }

        if (input != null && output != null) {
          double[] in, out;

          splits = input.split(", ");
          in = new double[splits.length];
          for (int i = 0; i < splits.length; ++i) {
            in[i] = Double.parseDouble(splits[i]);
          }

          splits = output.split(", ");
          out = new double[splits.length];
          for (int i = 0; i < splits.length; ++i) {
            out[i] = Double.parseDouble(splits[i]);
          }

          m_data.add(new TrainingDataElement(in, out));

          input = null;
          output = null;
        }
      }

      br.close();
    } catch (Exception e) {
      if (br != null) {
        try {
          br.close();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
      e.printStackTrace();
    }
  }

  public int[] getTopology() {
    return m_topology;
  }

  public boolean isEof() {
    return currentElement >= m_data.size() - 1;
  }

  public double[] getNextInputs() {
    return m_data.get(++currentElement).getInput();
  }

  public double[] getTargetOutputs() {
    return m_data.get(currentElement).getOutput();
  }

}
