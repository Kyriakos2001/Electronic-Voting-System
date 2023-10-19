/**
 * evskilo.logic.dto is the package of all ejb-module dtos.
 */
package evskilo.logic.dto;

import evskilo.logic.util.StringUtil;
import java.time.LocalDateTime;
import java.util.List;

/**
* 
* PollDto class 
* 
* @author  Uthara
* @author  Aishwarya
* @version 1.0
* @since   2023-07-23 
*/
public class PollDto {

    private Long pollId;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<QuestionDto> questions;

    /**
     *
     * @param pollId
     */
    public void setPollId(Long pollId) {
        this.pollId = pollId;
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
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     *
     * @param startDate
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @param endDate
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @param startDate
     */
    public void setStartDateString(String startDate) {
        this.startDate = StringUtil.parseDate(startDate);
    }

    /**
     *
     * @param endDate
     */
    public void setEndDateString(String endDate) {
        this.endDate = StringUtil.parseDate(endDate);
    }

    /**
     *
     * @return
     */
    public String getStartDateString() {
        if (startDate != null) {
            return StringUtil.dateToString(startDate);
        } else {
            return "";
        }
    }

    /**
     *
     * @return
     */
    public String getEndDateString() {
        if (startDate != null) {
            return StringUtil.dateToString(endDate);
        } else {
            return "";
        }

    }

    /**
     *
     * @return
     */
    public List<QuestionDto> getQuestions() {
        return questions;
    }

    /**
     *
     * @param questions
     */
    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }
}
