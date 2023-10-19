/**
 * evskilo.logic is the package of all ejb-module logic interfaces.
 */
package evskilo.logic;

import evskilo.logic.exception.PollException;
import java.time.LocalDateTime;

/**
* 
* The CommonLogic class is used for calling
* application Common Logic methods definitions
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-12 
*/
public interface CommonLogic {

    /**
     *
     */
    public void operateStates();

    /**
     *
     * @param to
     * @param startDate
     * @param endDate
     * @param pollToken
     * @param pollTitle
     * @throws Exception
     */
    public void sendVotingInvitationEmail(String to, LocalDateTime startDate, LocalDateTime endDate, String pollToken, String pollTitle) throws Exception;

    /**
     * 
     * @param to
     * @param pollTitle
     * @param organizerName
     * @throws PollException 
     */
    public void sendPollDeletedByOrganizerEmail(String to, String pollTitle, String organizerName) throws PollException;

    /**
     * 
     * @param to
     * @param pollTitle
     * @throws PollException 
     */
    public void sendPollDeletedByAdminEmail(String to, String pollTitle) throws PollException;
    
    /**
     *
     * @param toEmail
     * @param publishKey
     * @param pollTitle
     * @param isOrganizer
     * @throws PollException
     */
    public void sendResultEmail(String toEmail, String publishKey, String pollTitle, Boolean isOrganizer) throws PollException;
}
