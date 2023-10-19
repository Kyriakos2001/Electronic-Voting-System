/**
 * evs.kilo.web is the package of all web-module bean classes.
 */
package evs.kilo.web;

import evskilo.logic.TokenLogic;
import evskilo.logic.util.StringUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
* 
* The TokenBean class is used as model 
* for defining application token and 
* token methods and operations
* 
* @author  Marvin Nöthen
* @version 1.0
* @since   2023-09-11 
*/
@Named
@RequestScoped
public class TokenBean extends BaseBean {

    @EJB
    private TokenLogic tokenLogic;

    private static final Logger LOG = Logger.getLogger(TokenBean.class.getName());

    private static final String USER_VOTING_PATH = "/evs-kilo/pages/participant/voting.xhtml";

    private String token;

    /**
     * 
     * @return 
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     *
     */
    public void redirectToVotingPage(){
        if (tokenLogic.validateToken(token)) {
            redirectTo(USER_VOTING_PATH + "?token=" + token);
        }
        else {
            FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, 
                        StringUtil.getLabel("tokenInvalidMessage"), null));
        }
    }

    private void redirectTo(String url) {
        try {
            getExternalContext().redirect(url);
        } catch (IOException exception) {
            LOG.log(Level.WARNING, "IOException", exception);
        }
    }

    private ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }
}