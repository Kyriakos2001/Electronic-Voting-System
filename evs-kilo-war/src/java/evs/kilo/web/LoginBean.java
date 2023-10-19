/**
 * evs.kilo.web is the package of all web-module bean classes.
 */
package evs.kilo.web;

import evskilo.logic.UserLogic;
import evskilo.logic.dto.UserDto;
import java.io.IOException;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
* 
* The LoginBean class is used as model for defining
* application login and operations
* 
* @author  Anu Antony
* @version 1.0
* @since   2023-07-13 
*/
@SessionScoped
@Named
public class LoginBean extends BaseBean {

    private static final Logger LOG = Logger.getLogger(LoginBean.class.getName());

    @EJB
    private UserLogic cl;
    private Principal oldPrincipal = null; // used to detect changed login
    private UserDto currentUser;

    @PostConstruct
    public void newSession() {
        LOG.info("NEW SESSION");
    }

    /**
     *
     * @return
     */
    public boolean isLoggedIn() {
        UserDto us = getUser();
        if (us != null) {
            cl.addOrUpdateRole(us, FacesContext.getCurrentInstance()
                    .getExternalContext().isUserInRole("ADMIN"));
        }
        return us != null;

    }

    /**
     *
     * @return
     */
    public boolean isOrganizer() {
        return isUserInRole("ORGANIZER");
    }

    /**
     *
     * @return
     */
    public boolean isAdmin() {
        return isUserInRole("ADMIN");
    }

    /**
     *
     * @return
     */
    public UserDto getUser() {
        Principal p = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getUserPrincipal();
        if (p == null) {
            currentUser = null;
        } else {
            if (!p.equals(oldPrincipal)) {
                LOG.log(Level.INFO, "LOGIN user {0}", p.getName());
                currentUser = cl.addOrUpdateUser(p);
            }
        }

        oldPrincipal = p;
        return currentUser;
    }

    /**
     *
     */
    public void invalidateSession() {
        LOG.log(Level.INFO, "invalidateSession()");
        Principal p = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getUserPrincipal();
        if (p != null) {
            LOG.log(Level.INFO, "LOGOUT user {0}", p.getName());
        }
        currentUser = null;
        oldPrincipal = null;
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();
    }

    /**
     *
     */
    public void logout() {
        invalidateSession();
        
        try {
            FacesContext.getCurrentInstance().
                    getExternalContext().
                    redirect("/evs-kilo/pages/organizer/polls.xhtml");
        } catch (IOException ex) {
            FacesContext.getCurrentInstance()
                    .responseComplete();
        }
    }

    private boolean isUserInRole(String role) {
        return !isLoggedIn() ? false : FacesContext.getCurrentInstance()
                .getExternalContext().isUserInRole(role);

    }
}
