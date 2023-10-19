/**
 * evs.kilo.web is the package of all web-module bean classes.
 */
package evs.kilo.web;

import evskilo.logic.exception.PollException;
import evskilo.logic.util.Pair;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
* 
* The BaseBean class is an abstract parent class
* which contains base properties and operations
* for other class to extend
* 
* @author  Anu Antony
* @version 1.0
* @since   2023-07-13 
*/
public abstract class BaseBean implements Serializable {

    private String pageException = "";

    /**
     *
     * @return
     */
    public String getPageException() {
        return pageException;
    }

    /**
     *
     * @param pageException
     */
    public void setPageException(String pageException) {
        this.pageException = pageException;
    }

    /**
     *
     */
    public void clearException() {
        this.pageException = "";
    }

    /**
     *
     * @param exception
     */
    protected void parseVotingException(PollException exception) {
        Logger.getLogger("BaseBean").log(Level.WARNING, "VotingException", exception);
        setPageException(exception.getMessage());
    }

    /**
     *
     * @param url
     * @param queryString
     */
    protected void redirectTo(String url, Pair<String, String>... queryString) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            if (queryString.length > 0) {
                StringBuilder urlBuilder = new StringBuilder(url);
                urlBuilder.append("?");
                for (Pair<String, String> param : queryString) {
                    urlBuilder.append(param.getKey());
                    urlBuilder.append("=");
                    urlBuilder.append(param.getValue());
                    urlBuilder.append("&");
                }
                url = urlBuilder.toString();
            }
            externalContext.redirect(url);
        } catch (IOException exception) {
            Logger.getLogger("BaseBean").log(Level.WARNING, "IOException", exception);
            parseVotingException(new PollException("NotFoundException: Was not able to redirect"));
        }
    }

    /**
     *
     * @return
     */
    protected String getRequestCompletePath() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        return externalContext.getRequestContextPath()
                + externalContext.getRequestServletPath();
    }

    /**
     *
     */
    protected void redirectToThis() {
        redirectTo(getRequestCompletePath());
    }

}
