/**
 * evskilo.logic is the package of all ejb-module logic interfaces.
 */
package evskilo.logic;

import evskilo.logic.dto.PollDetailsDto;
import evskilo.logic.exception.TokenException;
import evskilo.persistence.entities.enums.PollStates;
import java.util.List;
import javax.ejb.Remote;

/**
* 
* Remote facade class
* The PollLogic class is used for calling
* application Poll Logic methods definitions
* 
* @author  Marvin Nöthen
* @version 1.0
* @since   2023-09-04 
*/
@Remote
public interface PollLogic {

    public static final String ADMIN_ROLE = "ADMIN";

    /**
     * 
     * @return 
     */
    List<PollDetailsDto> getAllPolls();

    /**
     * 
     * @param tokenValue
     * @return
     * @throws TokenException 
     */
    public PollDetailsDto getPollByToken(String tokenValue) throws TokenException;

    /**
     * 
     * @param tokenValue
     * @return
     * @throws TokenException 
     */
    public PollStates getPollStateByToken(String tokenValue) throws TokenException;

}
