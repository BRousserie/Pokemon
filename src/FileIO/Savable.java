/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package FileIO;

/**
 * Interface ensuring the implementing lasses to have a save method
 * @author Baptiste
 * @param <T>
 */
public interface Savable<T>
{

    /**
     *
     * @return the saved object 
     */
    public T save();
}
