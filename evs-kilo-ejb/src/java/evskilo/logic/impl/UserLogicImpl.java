/**
 * evskilo.logic.impl is the package of 
 * all ejb-module business logic implementation classes.
 */
package evskilo.logic.impl;

import evskilo.logic.UserLogic;
import evskilo.logic.dto.UserDto;
import evskilo.persistence.dao.*;
import evskilo.persistence.entities.Organizer;
import evskilo.persistence.entities.User;
import javax.ejb.Stateless;
import java.security.Principal;
import java.util.Locale;
import javax.inject.Inject;

/**
* 
* UserLogicImpl class 
* Implementation of UserLogic methods
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-14 
*/
@Stateless
public class UserLogicImpl implements UserLogic {

    @Inject
    private OrganizerAccess organizerAccess;

    @Inject
    private UserAccess userAccess;

    /**
     *
     * @param p
     * @return
     */
    @Override
    public UserDto addOrUpdateUser(Principal p) {

        if (p.getName() != null) {
            User us = userAccess.getUser(p.getName());
            return new UserDto(us.getUserName());
        }
        return null;

    }

    /**
     *
     * @param p
     * @param isAdmin
     * @return
     */
    @Override
    public UserDto addOrUpdateRole(UserDto p, boolean isAdmin) {

        if (p.getUsername() != null) {

            if (!isAdmin) {
                Organizer organizer = organizerAccess.getByUsername(p.getUsername()).orElse(null);
                if (organizer == null || "".equals(organizer.getUser().getUserName())) {
                    organizer = new Organizer();
                    User us = userAccess.getUser(p.getUsername());
                    organizer.setUser(us);
                    organizerAccess.persist(organizer);
                }
                return new UserDto(p.getUsername());
            } else {
                return null;
            }

        }
        return null;

    }

    /**
     *
     * @param username
     * @param loc
     * @return
     */
    @Override
    public UserDto addOrUpdateUserLocale(String username, Locale loc) {

        if (!"".equals(username)) {
            User us = userAccess.getUser(username);

            us.setuserLocale(loc);
            userAccess.persist(us);
        }
        return new UserDto(username);
    }

    /**
     *
     * @param username
     * @return
     */
    @Override
    public UserDto getUserLocale(String username) {

        UserDto udto = new UserDto(username);
        if (!"".equals(username)) {
            User us = userAccess.getUser(username);
            if ("en".equals(us.getuserLocale())) {
                udto.setuserLocale(Locale.ENGLISH);
            } else if ("de".equals(us.getuserLocale())) {

                udto.setuserLocale(Locale.GERMAN);
            }
        }
        return udto;
    }
}
