/**
 * evs.kilo.web is the package of all web-module bean classes.
 */
package evs.kilo.web;

import evskilo.logic.OrganizerLogic;
import evskilo.logic.dto.PollDetailsDto;
import evskilo.logic.dto.PollDto;
import evskilo.logic.dto.StartPollDto;
import evskilo.logic.exception.PollException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import evskilo.logic.PollLogic;
import javax.annotation.ManagedBean;

/**
* 
* The PollManagementBean class is used as the model for
* creating and managing poll and related poll operations.
* 
* @author  Aishwarya Saravanan
* @version 1.0
* @since   2023-07-25 
*/
@SessionScoped
@ManagedBean
@Named
public class PollManagementBean extends BaseBean {

    @EJB
    private OrganizerLogic organizerLogic;

    @EJB
    private PollLogic pollLogic;

    @Inject
    private LoginBean loginBean;
    private final PollDto createPollDto;
    private String selectedVoterListName;
    private List<String> voterEmailIds;
    
    /**
     *
     * @return
     */
    public List<String> getVoterEmailIds(){
        return voterEmailIds;
    }
    
    /**
     *
     * @param voterEmailIds
     */
    public void setVoterEmailIds(List<String> voterEmailIds){
        this.voterEmailIds = voterEmailIds;
    }
    
    /**
     *
     * @return
     */
    public String getSelectedVoterListName() {
        return selectedVoterListName;
    }

    /**
     *
     * @param selectedVoterListName
     */
    public void setSelectedVoterListName(String selectedVoterListName) {
        this.selectedVoterListName = selectedVoterListName;
    }

    /**
     *
     */
    public PollManagementBean() {
        this.createPollDto = new PollDto();
    }

    /**
     *
     * @return
     */
    public PollDto getcreatePollDto() {
        return createPollDto;
    }

    /**
     *
     * @param poll
     * @return
     */
    public String getBadgeType(PollDetailsDto poll) {
        return switch (poll.getState()) {
            case PREPARED ->
                "bg-primary";
            case STARTED ->
                "bg-warning";
            case VOTING ->
                "bg-success";
            case FINISHED ->
                "bg-secondary";
            default ->
                "bg-danger";
        };
    }

    /**
     *
     */
    public void createPoll() {
        clearException();
        try {
            organizerLogic.addUpdatePoll(createPollDto);
            redirectToThis();
        } catch (PollException exception) {
            parseVotingException(exception);
        }
    }

    /**
     *
     * @param model
     */
    public void editPoll(PollDto model) {
        clearException();
        try {
            organizerLogic.addUpdatePoll(model);
            redirectToThis();
        } catch (PollException exception) {
            parseVotingException(exception);
        }
    }

    /**
     * 
     * @param pollId 
     */
    public void removeAnyPoll(Long pollId) {
        clearException();
        try {
            organizerLogic.deletePollById(pollId);
            redirectToThis();
        } catch (PollException exception) {

        }
    }
    /**
     *
     * @param pollId
     */
    public void removePoll(Long pollId) {
        clearException();
        try {
            organizerLogic.deletePoll(pollId);
            redirectToThis();
        } catch (PollException exception) {

        }
    }

    /**
     *
     * @return
     */
    public List<PollDetailsDto> getPolls() {
        clearException();
        try {
            return organizerLogic.getPolls();
        } catch (PollException exception) {
            parseVotingException(exception);
            return new ArrayList<>();
        }
    }

    /**
     *
     * @return
     */
    public List<PollDetailsDto> getAllPolls() {
        
        return loginBean.isAdmin() ? pollLogic.getAllPolls() : new ArrayList<>();
    }

    /**
     *
     * @param model
     */
    public void startPoll(StartPollDto model) {
        clearException();
        try {
            organizerLogic.startPoll(model);
            redirectToThis();
        } catch (PollException exception) {
            parseVotingException(exception);
        }
    }
    
    public void publishResult(Long pollId) {
        try {
            organizerLogic.publishResult(pollId, true);   
            redirectToThis();
        } catch (PollException exception) {
            parseVotingException(exception);            
        }
    }

    public void unPublishResult(Long pollId) {
        try {
            organizerLogic.publishResult(pollId, false); 
            redirectToThis();
        } catch (PollException exception) {
            parseVotingException(exception);            
        }
    }
}
