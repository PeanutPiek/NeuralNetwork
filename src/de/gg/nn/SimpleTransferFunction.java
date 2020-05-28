/**
 *
 */
package de.gg.nn;

/**
 * @author PeanutPiek
 *
 */
public class SimpleTransferFunction implements ITransferFunction {

  /*
   * (non-Javadoc)
   *
   * @see de.gg.nn.ITransferFunction#getValue(double)
   */
  @Override
  public double getValue(double x) {
    return Math.tanh(x);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.gg.nn.ITransferFunction#getValueDerivative(double)
   */
  @Override
  public double getValueDerivative(double x) {
    return x * x;
  }

}
