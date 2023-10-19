/**
 * evskilo.logic.dto is the package of all ejb-module dtos.
 */
package evskilo.logic.dto;

/**
* 
* StartPollDto class 
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-12
* 
*/
public class StartPollDto {

    private Long pollId;
    private String voterListName;

    public StartPollDto() {
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
    public Long getPollId() {
        return pollId;
    }

    /**
     *
     * @param pollId
     */
    public StartPollDto(Long pollId) {
        this.pollId = pollId;
    }

    /**
     *
     * @return
     */
    public String getVoterListName() {
        return voterListName;
    }

    /**
     *
     * @param voterListName
     */
    public void setVoterListName(String voterListName) {
        this.voterListName = voterListName;
    }
}
