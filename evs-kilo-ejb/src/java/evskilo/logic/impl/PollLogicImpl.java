/**
 * evskilo.logic.impl is the package of 
 * all ejb-module business logic implementation classes.
 */
package evskilo.logic.impl;

import evskilo.logic.dto.PollDetailsDto;
import evskilo.persistence.dao.PollAccess;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import evskilo.logic.PollLogic;
import evskilo.logic.exception.TokenException;
import evskilo.persistence.dao.TokenAccess;
import evskilo.persistence.entities.Token;
import evskilo.persistence.entities.enums.PollStates;

/**
* 
* PollLogicImpl class 
* Implementation of PollLogic methods
* 
* @author  Marvin Nöthen
* @version 1.0
* @since   2023-09-04 
*/
@Stateless
public class PollLogicImpl implements PollLogic {

    @Inject
    private PollAccess pollAccess;

    @Inject
    private TokenAccess tokenAccess;

    /**
     * 
     * @return 
     */
    @Override
    @RolesAllowed(ADMIN_ROLE)
    public List<PollDetailsDto> getAllPolls() {
        return pollAccess.getAll()
                .stream()
                .map(poll -> PollDetailsDto.fromEo(poll))
                .collect(Collectors.toList());
    }

    /**
     * 
     * @param tokenValue
     * @return
     * @throws TokenException 
     */
    @Override
    public PollDetailsDto getPollByToken(String tokenValue) throws TokenException {
        PollDetailsDto poll = getPoll(tokenValue);
        return (poll != null && poll.getState() == PollStates.VOTING) ? poll : null;
    }

    /**
     * 
     * @param tokenValue
     * @return
     * @throws TokenException 
     */
    @Override
    public PollStates getPollStateByToken(String tokenValue) throws TokenException {
        
        return getPoll(tokenValue).getState();
    }

    private PollDetailsDto getPoll(String tokenValue) throws TokenException {
        
        Token token = tokenAccess.getTokenByValue(tokenValue);
        
        if (token == null) {
            throw new TokenException(String.format("Token %s is invalid!", tokenValue));
        }
        
        return PollDetailsDto.fromEo(token.getPoll());
    }
}
