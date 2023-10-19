/**
 * evs.kilo.web is the package of all web-module bean classes.
 */
package evs.kilo.web;

import evskilo.logic.OrganizerLogic;
import evskilo.logic.dto.ItemDetailsDto;
import evskilo.logic.dto.PollDetailsDto;
import evskilo.logic.dto.QuestionDto;
import evskilo.logic.exception.PollException;
import evskilo.logic.util.Pair;
import evskilo.persistence.entities.enums.QuestionType;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
* 
* The QuestionManagementBean class is used as model 
* for defining application Questions and poll question
* methods and operations
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-20 
*/
@SessionScoped
@Named
public class QuestionManagementBean extends BaseBean {

    @EJB
    private OrganizerLogic organizerLogic;
    private List<QuestionType> optionType;
    private final QuestionDto questionDto = new QuestionDto();
    private Long selectedPollId;

    /**
     *
     */
    protected void redirectToThisWithParams() {
        redirectTo(getRequestCompletePath(), new Pair<>("pollId", this.selectedPollId.toString()));
    }

    /**
     *
     * @return
     */
    public List<QuestionType> getOptionType() {
        if (optionType == null) {
            optionType = new ArrayList<>();
            optionType.add(QuestionType.BOOLEAN);
            optionType.add(QuestionType.MULTIPLE_CHOICE);
            optionType.add(QuestionType.SINGLE_CHOICE);
        }
        return optionType;
    }

    /**
     *
     * @param optionType
     */
    public void setOptionType(List<QuestionType> optionType) {
        this.optionType = optionType;
    }

    /**
     *
     * @param selectedPollId
     */
    public void setSelectedPollId(Long selectedPollId) {
        this.selectedPollId = selectedPollId;
    }

    /**
     *
     * @return
     */
    public Long getSelectedPollId() {
        return selectedPollId;
    }

    /**
     *
     * @return
     */
    public QuestionDto getCreateQuestionDto() {
        return questionDto;
    }

    /**
     *
     * @return
     */
    public PollDetailsDto getSelectedPoll() {
        clearException();
        try {
            return organizerLogic.getPollById(selectedPollId);
        } catch (PollException exception) {
            parseVotingException(exception);
            return new PollDetailsDto();
        }
    }

    /**
     *
     * @return
     */
    public List<QuestionDto> getPollQuestions() {
        clearException();
        try {
            return organizerLogic.getPollQuestions(selectedPollId);
        } catch (PollException exception) {
            parseVotingException(exception);
            return new ArrayList<>();
        }
    }

    /**
     *
     * @param model
     */
    public void editQuestion(QuestionDto model) {
        clearException();
        try {
            organizerLogic.addUpdateQuestion(model);
            redirectToThisWithParams();
        } catch (PollException exception) {
            parseVotingException(exception);
        }
    }

    /**
     *
     */
    public void createQuestion() {
        clearException();
        try {
            this.questionDto.setPollId(this.selectedPollId);
            organizerLogic.addUpdateQuestion(questionDto);
            redirectToThisWithParams();
        } catch (PollException exception) {
            parseVotingException(exception);
        }
    }

    /**
     *
     * @param questionId
     */
    public void deleteQuestion(Long questionId) {
        clearException();
        try {
            organizerLogic.deleteQuestion(questionId);
            redirectToThisWithParams();
        } catch (PollException exception) {
            parseVotingException(exception);
        }
    }

    /**
     *
     * @param model
     */
    public void addItem(ItemDetailsDto model) {
        clearException();
        try {
            organizerLogic.addUpdateItem(model);
            redirectToThisWithParams();
        } catch (PollException exception) {
            parseVotingException(exception);
        }
    }

    /**
     *
     * @param model
     */
    public void editItem(ItemDetailsDto model) {
        clearException();
        try {
            organizerLogic.addUpdateItem(model);
            redirectToThisWithParams();
        } catch (PollException exception) {
            parseVotingException(exception);
        }
    }

    /**
     *
     * @param itemId
     */
    public void deleteItem(Long itemId) {
        clearException();
        try {
            organizerLogic.deleteItem(itemId);
            redirectToThisWithParams();
        } catch (PollException exception) {
            parseVotingException(exception);
        }
    }

}
