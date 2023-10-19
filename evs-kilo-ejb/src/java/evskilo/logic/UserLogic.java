/**
 * evskilo.logic is the package of all ejb-module logic interfaces.
 */
package evskilo.logic;

import evskilo.logic.dto.UserDto;
import java.security.Principal;
import java.util.Locale;
import javax.ejb.Remote;

/**
* 
* Remote facade class
* The UserLogic class is used for calling
* application User Logic methods definitions
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-14 
*/
@Remote
public interface UserLogic {

    /**
     * Returns the current user determined from caller principal and creates a
     * database entry if required.
     *
     * @param p
     * @return the user, or null if nobody is logged in
     */
    public UserDto addOrUpdateUser(Principal p);

    /**
     *
     * @param username
     * @param loc
     * @return
     */
    public UserDto addOrUpdateUserLocale(String username, Locale loc);

    /**
     *
     * @param username
     * @return
     */
    public UserDto getUserLocale(String username);

    /**
     *
     * @param user
     * @param isAdmin
     * @return
     */
    public UserDto addOrUpdateRole(UserDto user, boolean isAdmin);
}
