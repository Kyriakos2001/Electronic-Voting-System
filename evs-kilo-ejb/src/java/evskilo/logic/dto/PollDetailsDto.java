/**
 * evskilo.logic.dto is the package of all ejb-module dtos.
 */
package evskilo.logic.dto;

import evskilo.persistence.entities.Poll;
import evskilo.persistence.entities.enums.PollStates;

/**
* 
* PollDetailsDto class 
* extends PollDto class
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-12 
*/
public class PollDetailsDto extends PollDto {

    private final StartPollDto startPollDto = new StartPollDto();
    private final PollResultDto pollResultDto = new PollResultDto();
    private PollStates state;
    private String organizerName;
    

    /**
     *
     * @return
     */
    public PollStates getState() {
        return state;
    }

    /**
     *
     * @param organizerName
     */
    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    /**
     *
     * @return
     */
    public StartPollDto getStartPollDto() {
        return startPollDto;
    }
    
    /**
     *
     * @return
     */
    public PollResultDto getPollResultDto() {
        return pollResultDto;
    }

    /**
     *
     * @return
     */
    public String getOrganizerName() {
        return organizerName;
    }

    /**
     *
     * @param poll
     * @return
     */
    public static PollDetailsDto fromEo(Poll poll) {
        PollDetailsDto dto = new PollDetailsDto();
        dto.setPollId(poll.getId());
        dto.setTitle(poll.getTitle());
        dto.setDescription(poll.getDescription());
        dto.setOrganizerName(poll.getOrganizer().getUser().getUserName());
        dto.setStartDate(poll.getStartDate());
        dto.setEndDate(poll.getEndDate());
        dto.state = poll.getState();
        dto.startPollDto.setPollId(poll.getId());
        dto.pollResultDto.setPollId(poll.getId());
        dto.pollResultDto.setPublishKey(poll.getPublicResultKey());
        dto.pollResultDto.setSubmittedVotes(poll.getSubmittedVotes());
        return dto;
    }
}
