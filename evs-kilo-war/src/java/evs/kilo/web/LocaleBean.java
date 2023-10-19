/**
 * evs.kilo.web is the package of all web-module bean classes.
 */
package evs.kilo.web;

import evskilo.logic.UserLogic;
import evskilo.logic.dto.UserDto;
import evskilo.logic.util.StringUtil;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Locale;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
* 
* The LocaleBean class is used model for defining
* application localization operations
* 
* @author  Uthara
* @version 1.0
* @since   2023-07-09 
*/
@SessionScoped
@Named
public class LocaleBean extends BaseBean {

    private static final long serialVersionUID = 516756595421760915L;

    @EJB
    private UserLogic cl;
    private Locale userLocale;

    /**
     *
     * @return
     */
    public Locale getUserLocale() {
        Principal p = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getUserPrincipal();
        if (p != null && !StringUtil.isEmpty(p.getName())
                && !p.getName().equals("anonymous")) {
            userLocale = getLanguageForUser(p.getName());
        }

        if (userLocale == null) {
            userLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        }
        if (userLocale == null) {
            userLocale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
        }

        return userLocale;
    }

    /**
     *
     * @param userLocale
     */
    public void setUserLocale(Locale userLocale) {
        setLanguageForUser(userLocale);
        this.userLocale = userLocale;
        checkParamRequest();
    }
    
    public void checkParamRequest(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (!externalContext.getRequestParameterMap().isEmpty())
           redirectTo(externalContext.getRequestHeaderMap().get("referer")); 
    }

    /**
     *
     */
    public void selectEnglish() {
        setUserLocale(Locale.ENGLISH);
    }

    /**
     *
     */
    public void selectGerman() {
        setUserLocale(Locale.GERMAN);
    }

    public LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }

    /**
     *
     * @return
     */
    public String getFlagImageUrl() {
        String flagName;
        if (userLocale != null && userLocale.equals(Locale.GERMAN)) {
            flagName = "DE.png";
        } else {
            flagName = "US.png";
        }
        return "icons/flags/" + flagName;
    }

    /**
     *
     * @param loc
     */
    public void setLanguageForUser(Locale loc) {
        Principal p = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getUserPrincipal();
        if (p != null) {
            cl.addOrUpdateUserLocale(p.getName(), loc);
        }
    }

    /**
     *
     * @param username
     * @return
     */
    public Locale getLanguageForUser(String username) {
        if (!"".equals(username)) {
            UserDto udto = cl.getUserLocale(username);
            return udto.getuserLocale();
        }
        return null;

    }

}
