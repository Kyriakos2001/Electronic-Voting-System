package evskilo.logic.dto;

import evskilo.persistence.entities.Item;

/**
 *
 * @author uthar
 */
public class ItemResultDto {

    private Long id;
    private Integer voteCounts;
    private String description;
    private String name;

    public static ItemResultDto fromEo(Item model){
        ItemResultDto itemResultDto = new ItemResultDto();
        itemResultDto.setId(model.getId());
        itemResultDto.setName(model.getName());
        itemResultDto.setDescription(model.getDescription());
        itemResultDto.setVoteCounts(model.getVotes());
        return itemResultDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVoteCounts() {
        return voteCounts;
    }

    public void setVoteCounts(Integer voteCounts) {
        this.voteCounts = voteCounts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
