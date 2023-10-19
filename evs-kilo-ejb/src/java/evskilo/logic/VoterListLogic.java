/**
 * evskilo.logic is the package of all ejb-module logic interfaces.
 */
package evskilo.logic;

import evskilo.logic.dto.VoterListDto;
import evskilo.logic.exception.PollException;
import java.util.List;
import javax.ejb.Remote;

/**
* 
* Remote facade class
* The VoterListLogic class is used for calling
* application VoterList Logic methods definitions
* 
* @author  Marvin Nöthen
* @version 1.0
* @since   2023-09-13 
*/
@Remote
public interface VoterListLogic {

    /**
     * 
     * @param listName 
     * @param voterEmails 
     * @throws evskilo.logic.exception.PollException 
     */
    public void createVotersList(String listName, String voterEmails) throws PollException ;

    /**
     * 
     * @return
     * @throws PollException 
     */
    public List<VoterListDto> getAllVoterLists() throws PollException ;

    /**
     *
     * @param voterListName
     * @throws PollException
     */
    void deleteVoterList(String voterListName) throws PollException;
    
    /**
     * 
     * @param voterListName
     * @param voterEmail
     * @throws PollException 
     */
    public void deleteVoterFromList(String voterListName, String voterEmail) throws PollException;
}
