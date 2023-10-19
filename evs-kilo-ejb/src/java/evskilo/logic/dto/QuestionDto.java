/**
 * evskilo.logic.dto is the package of all ejb-module dtos.
 */
package evskilo.logic.dto;

import evskilo.persistence.entities.Question;
import evskilo.persistence.entities.enums.QuestionType;
import java.util.List;
import java.util.stream.Collectors;

/**
* 
* QuestionDto class 
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-20
* 
*/
public class QuestionDto {

    private Long pollId;
    private Long id;
    private String title;
    private Boolean canAddAnswers;
    private Integer maxAnswers;
    private Integer minAnswers;
    private ItemDetailsDto createItemInfo = new ItemDetailsDto();
    private List<ItemDetailsDto> itemsList;
    private List<Long> selectedAnswers;
    private QuestionType type;

    public QuestionDto() {
        this.minAnswers = 1;
    }

    /**
     *
     * @return
     */
    public ItemDetailsDto getCreateItemInfo() {
        return createItemInfo;
    }

    /**
     *
     * @return
     */
    public Long getPollId() {
        return pollId;
    }

    /**
     *
     * @param pollId
     */
    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public Boolean getCanAddAnswers() {
        return canAddAnswers;
    }

    /**
     *
     * @param canAddAnswers
     */
    public void setCanAddAnswers(Boolean canAddAnswers) {
        this.canAddAnswers = canAddAnswers;
    }

    /**
     *
     * @return
     */
    public Integer getMaxAnswers() {
        return maxAnswers;
    }

    /**
     *
     * @param maxAnswers
     */
    public void setMaxAnswers(Integer maxAnswers) {
        this.maxAnswers = maxAnswers;
    }

    /**
     *
     * @return
     */
    public Integer getMinAnswers() {
        return minAnswers;
    }

    /**
     *
     * @param maxAnswers
     */
    public void setMinAnswers(Integer maxAnswers) {
        this.minAnswers = maxAnswers;
    }

    /**
     *
     * @return
     */
    public List<ItemDetailsDto> getItemsList() {
        return itemsList;
    }

    /**
     *
     * @param itemsList
     */
    public void setItemsList(List<ItemDetailsDto> itemsList) {
        this.itemsList = itemsList;
    }

    /**
     *
     * @return
     */
    public List<Long> getSelectedAnswer() {
        return selectedAnswers;
    }

    /**
     *
     * @param selectedAnswer
     */
    public void setSelectedAnswer(List<Long> selectedAnswer) {
        this.selectedAnswers = selectedAnswer;
    }

    /**
     *
     * @param type
     */
    public void setType(QuestionType type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public QuestionType getType() {
        return type;
    }

    /**
     *
     * @param question
     * @return
     */
    public static QuestionDto fromEo(Question question) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setPollId(question.getPoll().getId());
        questionDto.setTitle(question.getTitle());
        questionDto.setCanAddAnswers(question.getCanAddAnswers());
        questionDto.setMaxAnswers(question.getMaxAnswers());
        questionDto.setMinAnswers(question.getMinAnswers());
        questionDto.setId(question.getId());
        questionDto.setType(QuestionType.valueOf(question.getType()));
        questionDto.setItemsList(question.getItems()
                .stream()
                .map(item -> ItemDetailsDto.fromEo(item))
                .collect(Collectors.toList()));
        questionDto.createItemInfo.setQuestionId(question.getId());
        return questionDto;
    }

}
