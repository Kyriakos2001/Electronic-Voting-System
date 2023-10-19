/**
 * evskilo.logic.impl is the package of 
 * all ejb-module business logic implementation classes.
 */
package evskilo.logic.impl;

import evskilo.logic.exception.*;
import evskilo.logic.util.ExceptionUtil;
import evskilo.logic.util.StringUtil;
import evskilo.persistence.dao.*;
import evskilo.persistence.entities.Item;
import evskilo.persistence.entities.Organizer;
import evskilo.persistence.entities.Poll;
import evskilo.persistence.entities.Question;
import evskilo.persistence.entities.enums.PollStates;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
* 
* ValidateLogicImpl class 
* Implementation of validation methods
* from logic impl classes
* 
* @author  Aishwarya
* @version 1.0
* @since   2023-07-30 
*/
@Stateless
public class ValidateLogicImpl {

    @Inject
    private PollAccess pollDao;

    @Inject
    private QuestionAccess questionAccess;

    @Inject
    private ItemAccess itemAccess;

    /**
     *
     * @param pollId
     * @param title
     * @throws PollException
     */
    public void validateTitle(Long pollId, String title) throws PollException {
        if (StringUtil.isEmpty(title)) {
            ExceptionUtil.throwException(ExceptionType.VALUE_MISSING, StringUtil.getLabel("labelTitle"));
        }
        if (title.length() > 50) {
            ExceptionUtil.throwException(ExceptionType.INVALID_VALUE, StringUtil.getLabel("labelTitle"), StringUtil.getLabel("invalidTitleLength"));
        }
        Optional<Poll> existingPoll = pollDao.getByTitle(title);
        if (existingPoll.isPresent() && !existingPoll.get().getId().equals(pollId)) {
            ExceptionUtil.throwException(ExceptionType.INVALID_VALUE, StringUtil.getLabel("labelTitle"), StringUtil.getLabel("duplicatePoll"));
        }
    }

    /**
     *
     * @param startPoll
     * @param endPoll
     * @throws PollException
     */
    public void validateDate(LocalDateTime startPoll, LocalDateTime endPoll) throws PollException {
        if (startPoll == null) {
            ExceptionUtil.throwException(ExceptionType.VALUE_MISSING, StringUtil.getLabel("labelStartDate"));
        }
        if (endPoll == null) {
            ExceptionUtil.throwException(ExceptionType.VALUE_MISSING, StringUtil.getLabel("labelEndDate"));
        }
        LocalDateTime now = LocalDateTime.now();

        if (startPoll.compareTo(now) < 0) {
            ExceptionUtil.throwException(ExceptionType.INVALID_VALUE, StringUtil.getLabel("labelStartDate"), StringUtil.getLabel("invalidStartDate"));
        }

        if (startPoll.compareTo(endPoll) > 0) {
            ExceptionUtil.throwException(ExceptionType.INVALID_VALUE, StringUtil.getLabel("labelEndDate"), StringUtil.getLabel("invalidEndDate"));
        }

    }

    /**
     *
     * @param pollId
     * @param org
     * @return
     * @throws PollException
     */
    public Poll validateOwningPoll(Long pollId, Organizer org) throws PollException {
        Poll poll = pollDao.getByID(pollId);
        if (poll == null) {
            ExceptionUtil.throwException(ExceptionType.NOT_FOUND, StringUtil.getLabel("poll"));
        }
        if (poll.getOrganizer() != org) {
            ExceptionUtil.throwException(ExceptionType.INVALID_OPERATION, StringUtil.getLabel("notOrganizersPoll"), StringUtil.getLabel("viewingPoll"));
        }
        return poll;
    }

    /**
     *
     * @param pollId
     * @param org
     * @return
     * @throws PollException
     */
    public Poll validateDeletablePoll(Long pollId, Organizer org) throws PollException {
        Poll poll = validateOwningPoll(pollId, org);
        if (poll.getState() == PollStates.STARTED || poll.getState() == PollStates.VOTING) {
            ExceptionUtil.throwException(ExceptionType.INVALID_PARAMETER, StringUtil.getLabel("poll"), StringUtil.getLabel("startedAndNotFinishedPoll"));
        }
        return poll;
    }

    /**
     *
     * @param pollId
     * @param org
     * @return
     * @throws PollException
     */
    public Poll validateEditablePoll(Long pollId, Organizer org) throws PollException {
        Poll poll = validateDeletablePoll(pollId, org);
        if (poll.getState() != PollStates.PREPARED) {
            ExceptionUtil.throwException(ExceptionType.INVALID_OPERATION, StringUtil.getLabel("finishedPoll"), StringUtil.getLabel("editingPoll"));
        }
        return poll;
    }

    /**
     *
     * @param pollId
     * @param org
     * @return
     * @throws PollException
     */
    public Poll validateStartablePoll(Long pollId, Organizer org) throws PollException {
        Poll poll = validateEditablePoll(pollId, org);
        if (poll.getQuestions().isEmpty()) {
            ExceptionUtil.throwException(ExceptionType.INVALID_OPERATION, StringUtil.getLabel("leastQuestion"), StringUtil.getLabel("titleStartPoll"));
        }
        for (Question question : poll.getQuestions()) {
            validateStartableQuestion(question);
        }
        validateDate(poll.getStartDate(), poll.getEndDate());
        return poll;
    }
    
     public void validateViewablePoll(Long pollId, Organizer thisOrganizer) throws PollException {
        Poll poll = validateOwningPoll(pollId, thisOrganizer);
        if (poll.getState() != PollStates.FINISHED) {
            ExceptionUtil.throwException(ExceptionType.INVALID_PARAMETER, StringUtil.getLabel("poll"), StringUtil.getLabel("startedAndNotFinishedPoll"));
        }
        if (poll.getSubmittedVotes() < 3) {
            ExceptionUtil.throwException(ExceptionType.INVALID_OPERATION, StringUtil.getLabel("insufficientPoll"), StringUtil.getLabel("viewingPoll"));            
        }
    }
     

    /**
     *
     * @param pollId
     * @param questionId
     * @param title
     * @throws PollException
     */
    public void validateQuestionTitle(Long pollId, Long questionId, String title) throws PollException {
        if (StringUtil.isEmpty(title)) {
            ExceptionUtil.throwException(ExceptionType.VALUE_MISSING, StringUtil.getLabel("labelTitle"));
        }
        if (title.length() > 100 || title.length() < 4) {
            ExceptionUtil.throwException(ExceptionType.INVALID_PARAMETER, StringUtil.getLabel("labelTitle"), StringUtil.getLabel("invalidTitleLength"));
        }
        Optional<Question> question = questionAccess.getByTitleAndPoll(pollId, title);
        if (question.isPresent() && !question.get().getId().equals(questionId)) {
            ExceptionUtil.throwException(ExceptionType.INVALID_VALUE, StringUtil.getLabel("labelTitle"), StringUtil.getLabel("duplicatePoll"));
        }
    }

    /**
     *
     * @param question
     * @throws PollException
     */
    public void validateStartableQuestion(Question question) throws PollException {
        if (question.getItems().size() < 2) {
            ExceptionUtil.throwException(ExceptionType.QUESTION_NOT_COMPLETE, StringUtil.getLabel("leastOption"), question.getTitle());
        }
        if (question.getMaxAnswers() > question.getItems().size()) {
            ExceptionUtil.throwException(ExceptionType.INVALID_OPERATION, StringUtil.getLabel("maxOption"), question.getTitle());
        }
    }

    /**
     *
     * @param maxAnswers
     * @throws PollException
     */
    public void validateMaxAnswers(int maxAnswers) throws PollException {
        if (maxAnswers < 1) {
            ExceptionUtil.throwException(ExceptionType.INVALID_PARAMETER, StringUtil.getLabel("maxAnswers"), StringUtil.getLabel("maxQuestion"));
        }
    }

    /**
     *
     * @param questionId
     * @param itemId
     * @param name
     * @throws PollException
     */
    public void validateItemName(Long questionId, Long itemId, String name) throws PollException {
        if (StringUtil.isEmpty(name)) {
            ExceptionUtil.throwException(ExceptionType.VALUE_MISSING, StringUtil.getLabel("labelName"));
        }
        if (name.length() > 40 || name.length() < 1) {
            ExceptionUtil.throwException(ExceptionType.INVALID_PARAMETER, StringUtil.getLabel("labelName"), StringUtil.getLabel("shortQuestion"));
        }
        Optional<Item> item = itemAccess.getByNameAndQuestion(questionId, name);
        if (item.isPresent() && !item.get().getId().equals(itemId)) {
            ExceptionUtil.throwException(ExceptionType.INVALID_PARAMETER, StringUtil.getLabel("labelName"), StringUtil.getLabel("duplicateQuestion"));
        }
    }

    /**
     *
     * @param string
     * @throws PollException
     */
    public void validateVotersListName(String string) throws PollException {
        if (null == string || string.isEmpty()) {
            ExceptionUtil.throwException(ExceptionType.VALUE_MISSING, "Empty VotersListName");
        }
    }

    /**
     *
     * @param email
     * @throws PollException
     */
    public void validateEmail(String email) throws PollException {
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
        } catch (AddressException ex) {
            Logger.getLogger(ValidateLogicImpl.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionUtil
                    .throwException(ExceptionType.INVALID_EMAIL,
                            StringUtil.getLabel("invalidVoterEmailId", email));

        }
    }
}
