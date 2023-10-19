/**
 * evs.kilo.web is the package of all web-module bean classes.
 */
package evs.kilo.web;

import evskilo.logic.VoterListLogic;
import evskilo.logic.dto.VoterListDto;
import evskilo.logic.exception.PollException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
* 
* The VoterBean class is used as model 
* for defining application voter management
* and voter methods
* 
* @author  Anu Antony
* @version 1.0
* @since   2023-09-02 
*/
@Named
@RequestScoped
public class VoterBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private VoterListLogic voterListLogic;

    private String voterListName;
    
    private String voterEmails;
    
    public VoterBean() {
        
    }

    /**
     *
     * @return
     */
    public String getVoterListName() {
        return voterListName;
    }

    /**
     *
     * @param voterListName
     */
    public void setVoterListName(String voterListName) {
        this.voterListName = voterListName;
    }

    /**
     *
     * @return
     */
    public String getVoterEmails() {
        return voterEmails;
    }

    /**
     *
     * @param voterEmails
     */
    public void setVoterEmails(String voterEmails) {
        this.voterEmails = voterEmails;
    }

    /**
     *
     */
    public void createVoterList(){
        clearException();
        try {
            voterListLogic.createVotersList(voterListName, voterEmails);
            redirectToThis();
        } catch (PollException exception) {
            parseVotingException(exception);
        }
    }

    /**
     *
     * @return @throws PollException
     */
    public List<VoterListDto> getAllVoterLists() throws PollException {
        return voterListLogic.getAllVoterLists();
    }

    /**
     *
     * @param voterListName
     * @throws PollException
     */
    public void deleteVoterList(String voterListName) throws PollException {
        voterListLogic.deleteVoterList(voterListName);
    }

    /**
     * 
     * @param voterListName
     * @param voterEmail
     * @throws PollException 
     */
    public void deleteVoterFromList(String voterListName, String voterEmail) throws PollException{
        try {
            voterListLogic.deleteVoterFromList(voterListName, voterEmail);
        } catch (PollException exception) {
            parseVotingException(exception);
        }
    }
}
