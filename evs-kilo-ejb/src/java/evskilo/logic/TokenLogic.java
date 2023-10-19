/**
 * evskilo.logic is the package of all ejb-module logic interfaces.
 */
package evskilo.logic;

import javax.ejb.Remote;

/**
* 
* Remote facade class
* The TokenLogic class is used for calling
* application Token Logic methods definitions
* 
* @author  Marvin Nöthen
* @version 1.0
* @since   2023-09-11 
*/
@Remote
public interface TokenLogic {

    /**
     * 
     * @param tokenValue
     * @return 
     */
    boolean validateToken(String tokenValue);

    /**
     * 
     * @param tokenValue
     */
    void invalidateToken(String tokenValue);
}
