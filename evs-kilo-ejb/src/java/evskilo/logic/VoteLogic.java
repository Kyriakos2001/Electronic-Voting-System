package evskilo.logic;

import evskilo.logic.dto.PollDetailsDto;
import evskilo.logic.dto.PollResultDto;
import evskilo.logic.exception.PollException;
import javax.ejb.Remote;

/**
 *
 * @author uthar
 */
@Remote
public interface VoteLogic {  

    /**
     *
     * @param token
     * @param poll
     * @return
     * @throws PollException
     */
    Long addVote(String token, PollDetailsDto poll) throws PollException;
    
    /**
     *
     * @param publishToken
     * @return
     * @throws PollException
     */
    PollResultDto viewResult(String publishToken) throws PollException;
}
