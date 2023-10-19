/**
 * evskilo.logic.impl is the package of 
 * all ejb-module business logic implementation classes.
 */
package evskilo.logic.impl;


import javax.ejb.Stateless;
import javax.inject.Inject;
import evskilo.logic.OrganizerLogic;
import evskilo.logic.CommonLogic;
import evskilo.logic.exception.*;
import evskilo.persistence.entities.*;
import evskilo.persistence.entities.enums.*;
import evskilo.logic.util.*;
import evskilo.persistence.dao.*;
import evskilo.logic.dto.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;

/**
* 
* OrganizerLogicImpl class 
* Implementation of OrganizerLogic methods
* 
* @author  Uthara
* @version 1.0
* @since   2023-07-25 
*/
@Stateless
public class OrganizerLogicImpl implements OrganizerLogic {

    @Inject
    private OrganizerAccess organizerAccess;

    @Inject
    private PollAccess pollAccess;

    @Inject
    private ValidateLogicImpl dataValidation;

    @Inject
    private Principal principal;

    @Inject
    private QuestionAccess questionAccess;

    @Inject
    private ItemAccess itemAccess;

    @Inject
    private VoterListAccess voterListAccess;

    @Inject
    private UserAccess userAccess;

    @Inject
    private TokenAccess tokenAccess;

    @Inject
    private CommonLogic commonLogic;

    @Inject
    private VoterAccess voterAccess;

    private Organizer getOrganizer() throws PollException {
        Optional<Organizer> organizer = organizerAccess.getByUsername(principal.getName());
        if (!organizer.isPresent()) {
            ExceptionUtil.throwException(ExceptionType.NOT_FOUND, StringUtil.getLabel("organizer"));
        }
        return organizer.get();
    }

    /**
     *
     * @param model
     * @return
     * @throws PollException
     */
    @Override
    public Long addUpdatePoll(PollDto model) throws PollException {

        dataValidation.validateTitle(model.getPollId(), model.getTitle());
        dataValidation.validateDate(model.getStartDate(), model.getEndDate());

        Poll poll = new Poll();
        if (model.getPollId() != null) {
            dataValidation.validateEditablePoll(model.getPollId(), getOrganizer());
            poll = pollAccess.getByID(model.getPollId());
        }
        poll.setOrganizer(getOrganizer());
        poll.setTitle(model.getTitle());
        poll.setDescription(model.getDescription());
        poll.setStartDate(model.getStartDate());
        poll.setEndDate(model.getEndDate());
        poll.setState(PollStates.PREPARED);

        pollAccess.persist(poll);
        return poll.getId();
    }

    /**
     * 
     * @param pollId
     * @throws PollException 
     */
    @Override
    @RolesAllowed(ADMIN_ROLE)
    public void deletePollById(Long pollId) throws PollException {
        Poll poll = pollAccess.getPollById(pollId);
        deletePollInstance(poll);
        sendDeletedByAdminEmail(poll);
    }

    /**
     *
     * @param pollId
     * @throws PollException
     */
    @Override
    public void deletePoll(Long pollId) throws PollException {
        Poll poll = pollAccess.getByOrganizerAndId(
                getOrganizer().getId(), pollId).orElse(null);
        deletePollInstance(poll);
        sendDeletedByOrganizerEmail(poll);
    }

    private void deletePollInstance(Poll poll) throws PollException {
        if (poll == null) {
            ExceptionUtil.throwException(ExceptionType.VALUE_MISSING,
                            StringUtil.getLabel("pollIdMissingDeletePoll"));
        }
        
        //1.Fetch Question list associated with poll id
        List<Question> questionList = questionAccess.getQuestionByPollId(poll.getId());
        if (!questionList.isEmpty()) {
            for (Question question : questionList) {
                //2.Fetch Item list associated with question id
                List<Item> itemList = itemAccess.getByQuestionId(question.getId());
                itemList.forEach(item -> itemAccess.deleteByID(item.getId()));
                //3.Delete Question using question id
                questionAccess.deleteByID(question.getId());
            }
        }
            
        //4.Fetch Token using poll_id
        tokenAccess.getTokenByPollId(poll.getId()).forEach(
                token -> tokenAccess.deleteByID(token.getId()));
            
        //5.Delete Poll using poll id
        pollAccess.deleteByID(poll.getId());
    }

    private void sendDeletedByAdminEmail(Poll poll) throws PollException{
        commonLogic.sendPollDeletedByAdminEmail(poll.getOrganizer().getUser().getUserName(), 
                poll.getTitle());
        if (poll.getState() == PollStates.STARTED 
                || poll.getState() == PollStates.VOTING) {
            for (Voter voter : voterAccess.getVotersByListId(poll.getVoterList().getId())) {
                commonLogic.sendPollDeletedByAdminEmail(voter.getVotingUser().getUserName(), 
                        poll.getTitle());
            }
        }
    }

    private void sendDeletedByOrganizerEmail(Poll poll) throws PollException{
        if (poll.getState() == PollStates.STARTED 
                || poll.getState() == PollStates.VOTING) {
            for (Voter voter : voterAccess.getVotersByListId(poll.getVoterList().getId())) {
                commonLogic.sendPollDeletedByOrganizerEmail(voter.getVotingUser().getUserName(), 
                        poll.getTitle(), poll.getOrganizer().getUser().getUserName());
            }
        }
    }

    /**
     *
     * @param model
     * @return
     * @throws PollException
     */
    @Override
    public Long addUpdateQuestion(QuestionDto model) throws PollException {
        dataValidation.validateQuestionTitle(model.getPollId(), model.getId(), model.getTitle());
        dataValidation.validateEditablePoll(model.getPollId(), getOrganizer());
        //validationLogic.validateDecisionMode(model.getDecisionMode());
        dataValidation.validateMaxAnswers(model.getMaxAnswers());

        System.out.println("getPollId :=" + model.getPollId());

        Poll poll = pollAccess.getByID(model.getPollId());

        System.out.println("After the poll fetch");

        Question question = new Question();
        if (model.getId() != null) {
            question = poll.getQuestions().stream()
                    .filter(qs -> qs.getId().equals(model.getId()))
                    .findFirst()
                    .orElse(null);
            if (question == null) {
                ExceptionUtil.throwException(ExceptionType.NOT_FOUND, StringUtil.getLabel("question"));
            }
        }
        question.setMaxAnswers(model.getMaxAnswers());
        question.setMinAnswers(model.getMinAnswers());
        question.setTitle(model.getTitle());
        question.setPoll(poll);
        question.setType(model.getType().toString());
        question.setCanAddAnswers(!model.getType().equals(QuestionType.BOOLEAN));
        questionAccess.persist(question);

        Question q = questionAccess.getByID(question.getId());
        if (q != null && q.getItems() != null) {
            q.getItems().forEach((t) -> {
                try {
                    deleteItem(t.getId());
                } catch (PollException ex) {
                }
            });
        }

        if (model.getType().equals(QuestionType.BOOLEAN)) {
            createBooleanOptions(question.getId());
        }

        return question.getId();
    }

    /**
     *
     * @param id
     * @throws PollException
     */
    @Override
    public void deleteQuestion(Long id) throws PollException {
        Question question = questionAccess.getByID(id);
        if (question == null) {
            ExceptionUtil.throwException(ExceptionType.NOT_FOUND, StringUtil.getLabel("question"));
        }
        dataValidation.validateEditablePoll(question.getPoll().getId(), getOrganizer());
        questionAccess.deleteByID(id);
    }

    /**
     *
     * @param model
     * @return
     * @throws PollException
     */
    @Override
    public Long addUpdateItem(ItemDetailsDto model) throws PollException {
        dataValidation.validateItemName(model.getQuestionId(), model.getId(), model.getName());

        Question question = questionAccess.getByID(model.getQuestionId());
        if (question == null) {
            ExceptionUtil.throwException(ExceptionType.NOT_FOUND, StringUtil.getLabel("question"));
        }
        dataValidation.validateEditablePoll(question.getPoll().getId(), getOrganizer());
        Item item = new Item();
        if (model.getId() != null) {
            item = question.getItems().stream()
                    .filter(it -> it.getId().equals(model.getId()))
                    .findFirst()
                    .orElse(null);
            if (item == null) {
                ExceptionUtil.throwException(ExceptionType.NOT_FOUND, StringUtil.getLabel("item"));
            }
        }
        item.setName(model.getName());
        item.setQuestion(question);
        item.setDescription(model.getDescription());
        itemAccess.persist(item);
        return item.getId();
    }

    /**
     *
     * @param id
     * @throws PollException
     */
    @Override
    public void deleteItem(Long id) throws PollException {
        Item item = itemAccess.getByID(id);
        if (item == null) {
            ExceptionUtil.throwException(ExceptionType.NOT_FOUND, StringUtil.getLabel("item"));
        }
        Question question = item.getQuestion();
        dataValidation.validateEditablePoll(question.getPoll().getId(), getOrganizer());
        itemAccess.deleteByID(id);
    }

    /**
     *
     * @return @throws PollException
     */
    @Override
    public List<PollDetailsDto> getPolls() throws PollException {
        return pollAccess.getAllByOrganizer(getOrganizer().getId())
                .stream()
                .map(poll -> PollDetailsDto.fromEo(poll))
                .collect(Collectors.toList());

    }

    /**
     *
     * @param pollId
     * @return
     * @throws PollException
     */
    @Override
    public List<QuestionDto> getPollQuestions(Long pollId) throws PollException {
        Poll poll = dataValidation.validateOwningPoll(pollId, getOrganizer());
        return poll.getQuestions().stream()
                .map(question -> QuestionDto.fromEo(question))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param pollId
     * @return
     * @throws PollException
     */
    @Override
    public PollDetailsDto getPollById(Long pollId) throws PollException {
        Optional<Poll> poll = pollAccess.getByOrganizerAndId(getOrganizer().getId(), pollId);
        if (!poll.isPresent()) {
            ExceptionUtil.throwException(ExceptionType.NOT_FOUND, StringUtil.getLabel("poll"));
        }
        PollDetailsDto pollInfoDto = PollDetailsDto.fromEo(poll.get());
        pollInfoDto.setQuestions(poll.get().getQuestions()
                .stream()
                .map(question -> {
                    return QuestionDto.fromEo(question);
                })
                .collect(Collectors.toList()));
        return pollInfoDto;
    }

    /**
     *
     * @param model
     * @throws PollException
     */
    @Override
    public void startPoll(StartPollDto model) throws PollException {
        dataValidation.validateStartablePoll(model.getPollId(), getOrganizer());

        VoterList voterList = voterListAccess.getVoterListByName(
                getOrganizer().getId(), model.getVoterListName());
        List<Voter> voters = voterAccess.getVotersByListId(voterList.getId());
        
        if (voters.size() < 3) {
            ExceptionUtil.throwException(ExceptionType.INVALID_OPERATION, 
                    StringUtil.getLabel("leastParticipant"),
                    StringUtil.getLabel("titleStartPoll"));
        }
        
        Poll poll = pollAccess.getByID(model.getPollId());
        poll.setVoterList(voterList);
        
        for (Voter voter : voters) {
            Token token = new Token();
            token.setPoll(poll);
            try {
                commonLogic.sendVotingInvitationEmail(
                        voter.getVotingUser().getUserName(), 
                        poll.getStartDate(), poll.getEndDate(),
                        token.getToken(), poll.getTitle());
            } catch (Exception ex) {
                Logger.getLogger(OrganizerLogicImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            tokenAccess.persist(token);
        }
        
        poll.setState(PollStates.STARTED);
        pollAccess.persist(poll);
    }

    /**
     *
     * @param questionId
     * @throws PollException
     */
    private void createBooleanOptions(Long questionId) throws PollException {
        var itemYes = new ItemDetailsDto();
        itemYes.setName("Yes");
        itemYes.setQuestionId(questionId);
        addUpdateItem(itemYes);

        var itemNo = new ItemDetailsDto();
        itemNo.setName("No");
        itemNo.setQuestionId(questionId);
        addUpdateItem(itemNo);
    }  
    
    /**
     *
     * @param pollId
     * @param isPublished
     * @return
     * @throws PollException
     */
    @Override
    public String publishResult(Long pollId, boolean isPublished) throws PollException {
        dataValidation.validateViewablePoll(pollId, getOrganizer());
        Poll poll = pollAccess.getByID(pollId);
        
        List<Voter> selectedParticipants = voterAccess.getVotersByListId(poll.getVoterList().getId());
              
        if (isPublished) {
            poll.setPublicResultKey(UUID.randomUUID().toString());
            for (Voter selectedParticipant : selectedParticipants) {
                commonLogic.sendResultEmail(selectedParticipant.getVotingUser().getUserName(), poll.getPublicResultKey(), poll.getTitle(), false);
            }
            commonLogic.sendResultEmail(getOrganizer().getUser().getUserName(), poll.getPublicResultKey(), poll.getTitle(), true);
        } else {
            poll.setPublicResultKey(null);
        }
        pollAccess.persist(poll);
        return poll.getPublicResultKey();
    }

    /**
     *
     * @param pollId
     * @return
     * @throws PollException
     */
    @Override
    public PollResultDto viewResult(Long pollId) throws PollException {
        
         Poll poll = pollAccess.getByID(pollId);
         if (poll != null){
             dataValidation.validateViewablePoll(poll.getId(), getOrganizer());             
             return PollResultDto.fromEo(poll);
         }
         return null;
    }
}
