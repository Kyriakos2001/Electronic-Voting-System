/**
 * evskilo.logic.dto is the package of all ejb-module dtos.
 */
package evskilo.logic.dto;

import evskilo.persistence.entities.Item;

/**
* 
* ItemDetailsDto class 
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-20 
*/
public class ItemDetailsDto {

    private Long questionId;
    private Long id;
    private String description;
    private String name;

    /**
     *
     * @return
     */
    public Long getQuestionId() {
        return questionId;
    }

    /**
     *
     * @param questionId
     */
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @param item
     * @return
     */
    public static ItemDetailsDto fromEo(Item item) {
        ItemDetailsDto itemInfoDto = new ItemDetailsDto();
        itemInfoDto.setQuestionId(item.getQuestion().getId());
        itemInfoDto.setId(item.getId());
        itemInfoDto.setName(item.getName());
        itemInfoDto.setDescription(item.getDescription());
        return itemInfoDto;
    }
}
