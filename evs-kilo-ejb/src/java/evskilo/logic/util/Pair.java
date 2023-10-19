/**
 * evskilo.logic.util is the package of 
 * all ejb-module util methods.
 */
package evskilo.logic.util;

/**
* 
* Pair class 
* Generic Class for implementing 
* Collections framework
* 
* @author  Uthara
* @version 1.0
 * @param <T1>
 * @param <T2>
* @since   2023-07-25 
*/
public class Pair<T1, T2> {

    private T1 key;
    private T2 value;

    /**
     *
     * @param key
     * @param value
     */
    public Pair(T1 key, T2 value) {
        this.key = key;
        this.value = value;
    }

    /**
     *
     * @return
     */
    public T1 getKey() {
        return key;
    }

    /**
     *
     * @param key
     */
    public void setKey(T1 key) {
        this.key = key;
    }

    /**
     *
     * @return
     */
    public T2 getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(T2 value) {
        this.value = value;
    }

    /**
     *
     * @param key
     * @param value
     */
    public void add(T1 key, T2 value) {
    }
}
