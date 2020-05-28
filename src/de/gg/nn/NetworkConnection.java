/**
 *
 */
package de.gg.nn;

/**
 * @author PeanutPiek
 *
 */
public class NetworkConnection {

  private double weight = 0.0;

  private double deltaWeight = 0.0;

  /**
   * @return the weight
   */
  public double getWeight() {
    return weight;
  }

  /**
   * @param weight
   *          the weight to set
   */
  public void setWeight(double weight) {
    this.weight = weight;
  }

  /**
   * @return the deltaWeight
   */
  public double getDeltaWeight() {
    return deltaWeight;
  }

  /**
   * @param deltaWeight
   *          the deltaWeight to set
   */
  public void setDeltaWeight(double deltaWeight) {
    this.deltaWeight = deltaWeight;
  }

}
