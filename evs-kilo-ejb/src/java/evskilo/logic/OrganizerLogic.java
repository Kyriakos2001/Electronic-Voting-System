/**
 * evskilo.logic is the package of all ejb-module logic interfaces.
 */
package evskilo.logic;

import evskilo.logic.dto.ItemDetailsDto;
import evskilo.logic.dto.PollDetailsDto;
import evskilo.logic.dto.PollDto;
import evskilo.logic.dto.PollResultDto;
import evskilo.logic.dto.QuestionDto;
import evskilo.logic.dto.StartPollDto;
import evskilo.logic.exception.PollException;
import java.util.List;
import javax.ejb.Remote;

/**
* 
* Remote facade class
* The OrganizerLogic class is used for calling
* application Organizer Logic methods definitions
* 
* @author  Uthara
* @version 1.0
* @since   2023-07-25 
*/
@Remote
public interface OrganizerLogic {

    public static final String ADMIN_ROLE = "ADMIN";

    /**
     *
     * @param model
     * @return
     * @throws PollException
     */
    Long addUpdatePoll(PollDto model) throws PollException;

    /**
     * 
     * @param pollId
     * @throws PollException 
     */
    void deletePollById(Long pollId) throws PollException;

    /**
     *
     * @param pollId
     * @throws PollException
     */
    void deletePoll(Long pollId) throws PollException;

    /**
     *
     * @return @throws PollException
     */
    List<PollDetailsDto> getPolls() throws PollException;

    /**
     *
     * @param model
     * @throws PollException
     */
    void startPoll(StartPollDto model) throws PollException;

    /**
     *
     * @param model
     * @return
     * @throws PollException
     */
    Long addUpdateQuestion(QuestionDto model) throws PollException;

    /**
     *
     * @param model
     * @return
     * @throws PollException
     */
    Long addUpdateItem(ItemDetailsDto model) throws PollException;

    /**
     *
     * @param id
     * @throws PollException
     */
    void deleteQuestion(Long id) throws PollException;

    /**
     *
     * @param id
     * @throws PollException
     */
    void deleteItem(Long id) throws PollException;

    /**
     *
     * @param pollId
     * @return
     * @throws PollException
     */
    PollDetailsDto getPollById(Long pollId) throws PollException;

    /**
     *
     * @param pollId
     * @return
     * @throws PollException
     */
    List<QuestionDto> getPollQuestions(Long pollId) throws PollException;
    
    /**
     *
     * @param pollId
     * @param isPublished
     * @return
     * @throws PollException
     */
    public String publishResult(Long pollId, boolean isPublished) throws PollException;
    
    /**
     *
     * @param pollId
     * @return
     * @throws PollException
     */
    public PollResultDto viewResult(Long pollId) throws PollException;
}

