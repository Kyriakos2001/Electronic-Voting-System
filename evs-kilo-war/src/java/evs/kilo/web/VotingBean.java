/**
 * evs.kilo.web is the package of all web-module bean classes.
 */
package evs.kilo.web;

import evskilo.logic.*;
import evskilo.logic.dto.PollDetailsDto;
import evskilo.logic.exception.*;
import evskilo.persistence.entities.enums.PollStates;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
* 
* The VotingBean class is used as model 
* for defining application participant management
* and for voting operations
* 
* @author  Marvin Nöthen
* @version 1.0
* @since   2023-09-11 
*/
@ViewScoped
@Named
public class VotingBean extends BaseBean {
    
    @EJB
    private TokenLogic tokenLogic;

    @EJB
    private PollLogic pollLogic;
    
    @EJB
    private VoteLogic voteLogic;
    
    @EJB
    private OrganizerLogic organizerLogic;

    private static final Logger LOG = Logger.getLogger(VotingBean.class.getName());
    
    private static final String PARTICIPANT_THANK_YOU_PATH = "endvoting.xhtml";

    private String token;

    private PollDetailsDto pollDetailsDto;

    @PostConstruct
    public void init() {
        
        Map<String, String> parameterMap = getExternalContext().getRequestParameterMap();
        token = parameterMap.get("token");
        loadPoll();
    }

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
     * @return
     */
    public PollDetailsDto getPollInfo() {
        return pollDetailsDto;
    }

    /**
     *
     * @return
     */
    public PollStates getState() {
        try {
            return pollLogic.getPollStateByToken(token);
        } catch (TokenException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        
        return PollStates.INVALID;
    }

    /**
     *
     */
    public void submitVote() {
         try {
            voteLogic.addVote(token, pollDetailsDto);
            redirectTo(PARTICIPANT_THANK_YOU_PATH);
        } catch (PollException exception) {
            parseVotingException(exception);
        }
        
    }

    /**
     *
     */
    public void loadPoll() {
        try {
            pollDetailsDto = pollLogic.getPollByToken(token);
            System.out.println("Received pollDetailsDto: " + pollDetailsDto.getDescription());
        } catch (TokenException exception) {
            System.out.println(exception.getMessage());
        }
    }
    
    /**
     * 
     * @throws evskilo.logic.exception.PollException 
     */
    public void getQuestions() throws PollException{
         try {
            pollDetailsDto = pollLogic.getPollByToken(token);
            if(pollDetailsDto!=null){
                Long pollId = pollDetailsDto.getPollId();
                pollDetailsDto.setQuestions(
                        organizerLogic.getPollQuestions(pollId));
            }
            System.out.println("Received pollDetailsDto: " + pollDetailsDto.getQuestions());
        } catch (TokenException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }
}
