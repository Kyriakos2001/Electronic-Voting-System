/**
 * evskilo.logic.impl is the package of 
 * all ejb-module business logic implementation classes.
 */
package evskilo.logic.impl;

import evskilo.logic.CommonLogic;
import evskilo.logic.exception.PollException;
import evskilo.logic.util.StringUtil;
import evskilo.persistence.dao.PollAccess;
import evskilo.persistence.entities.Poll;
import evskilo.persistence.entities.enums.PollStates;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.mail.internet.MimeMultipart;

/**
* 
* CommonLogicImpl class 
* Implementation of CommonLogic methods
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-12 
*/
@Stateless
public class CommonLogicImpl implements CommonLogic {

    public static final String ADMIN_ROLE = "ADMIN";

    private static final Logger LOG = Logger.getLogger(CommonLogicImpl.class.getName());

    @Inject
    private PollAccess pollAccess;

    @Resource(lookup = "mail/evskilo-mail")
    private Session mailSession;
    
    private final String voterResultLink = "https://localhost:8181/evs-kilo/pages/participant/results.xhtml?publishKey=";
     private final String organizerResultLink = "https://localhost:8181/evs-kilo/pages/organizer/results.xhtml?publishKey=";

    /**
     *
     */
    @Override
    public void operateStates() {
        List<Poll> startedPolls = pollAccess.getAllByState(PollStates.STARTED);
        List<Poll> votingPolls = pollAccess.getAllByState(PollStates.VOTING);
        startedPolls.forEach(startedPoll -> {
            if (startedPoll.getStartDate().compareTo(LocalDateTime.now()) < 0) {
                startedPoll.setState(PollStates.VOTING);
                pollAccess.persist(startedPoll);
            }
        });
        votingPolls.forEach(votingPoll -> {
            if (votingPoll.getEndDate().compareTo(LocalDateTime.now()) < 0) {
                votingPoll.setState(PollStates.FINISHED);
                pollAccess.persist(votingPoll);
            }
        });
    }

    /**
     *
     * @param to
     * @param startDate
     * @param endDate
     * @param pollToken
     * @param pollTitle
     * @throws Exception
     */
    @Override
    public void sendVotingInvitationEmail(String to, LocalDateTime startDate, LocalDateTime endDate, String pollToken, String pollTitle) throws Exception {
        sendEmail(to, StringUtil.getLabel("pollInvitationEmailSubject", pollTitle),
                StringUtil.getLabel("pollInvitationEmailContent", 
                        pollTitle, StringUtil.dateToString(startDate), 
                        StringUtil.dateToString(endDate), pollToken));
    }

    /**
     * 
     * @param to
     * @param pollTitle
     * @param organizerName
     * @throws PollException 
     */
    @Override
    public void sendPollDeletedByOrganizerEmail(String to, String pollTitle, String organizerName) throws PollException {
        sendEmail(to, StringUtil.getLabel("pollDeleteEmailSubject", pollTitle),
                StringUtil.getLabel("pollDeleteByOrganizerEmailContent", pollTitle, organizerName));
    }

    /**
     * 
     * @param to
     * @param pollTitle
     * @throws PollException 
     */
    @Override
    @RolesAllowed(ADMIN_ROLE)
    public void sendPollDeletedByAdminEmail(String to, String pollTitle) throws PollException {
        sendEmail(to, StringUtil.getLabel("pollDeleteEmailSubject", pollTitle),
                StringUtil.getLabel("pollDeleteByAdminEmailContent", pollTitle));
    }

    /**
     * 
     * @param to
     * @param subject
     * @param content
     * @throws PollException 
     */
    private void sendEmail(String to, String subject, String content) throws PollException {
        Message simpleMail = new MimeMessage(mailSession);

        try {
            simpleMail.setFrom(new InternetAddress("noreply@uni-koblenz.de"));
            simpleMail.setSubject(subject);
            simpleMail.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            MimeMultipart mailContent = new MimeMultipart();
            MimeBodyPart mailMessage = new MimeBodyPart();
            mailMessage.setContent(content, "text/html; charset=utf-8");
            mailContent.addBodyPart(mailMessage);

            simpleMail.setContent(mailContent);

            Transport.send(simpleMail);
        } catch (MessagingException exception) {
            LOG.log(Level.SEVERE, "MessagingException", exception);
            throw new PollException("Exception in sendPollDeleteEmail");
        }
    }
    
    /**
     *
     * @param toEmail
     * @param publishKey
     * @param pollTitle
     * @param isOrganizer
     * @throws PollException
     */
    @Override
    public void sendResultEmail(String toEmail, String publishKey, String pollTitle, Boolean isOrganizer) throws PollException {
        
        Message simpleMail = new MimeMessage(mailSession);
        
        try {
            simpleMail.setFrom(new InternetAddress("noreply@uni-koblenz.de"));
            simpleMail.setSubject(StringUtil.getLabel("resultEmailSubject"));
            simpleMail.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            
            MimeMultipart mailContent = new MimeMultipart();
            MimeBodyPart mailMessage = new MimeBodyPart();
            
            if(!isOrganizer)
                mailMessage.setContent(StringUtil.getLabel("resultEmailBody", pollTitle, publishKey, voterResultLink+publishKey), 
                        "text/html; charset=utf-8");
            else
                mailMessage.setContent(StringUtil.getLabel("resultEmailBody", pollTitle, publishKey, organizerResultLink+publishKey), 
                        "text/html; charset=utf-8");
            
            mailContent.addBodyPart(mailMessage);
            
            simpleMail.setContent(mailContent);
            
            Transport.send(simpleMail);
        } catch (MessagingException exception) {
            Logger.getLogger("CommonLogicImpl").log(Level.SEVERE, "MessagingException", exception);
            throw new PollException("Unknown exception in sending email");
        }
    }
}