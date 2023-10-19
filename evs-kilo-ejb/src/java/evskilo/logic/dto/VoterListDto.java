/**
 * evskilo.logic.dto is the package of all ejb-module dtos.
 */
package evskilo.logic.dto;

import java.util.List;

/**
* 
* VoterListDto class 
* Implementation of VoterListDto methods
* 
* @author  Marvin Nöthen
* @version 1.0
* @since   2023-13-09 
*/
public class VoterListDto {

    private Long id;
    private String voterListName;
    private List<String> voterEmails;

    public VoterListDto(Long id, String voterListName, List<String> voterEmails) {
        this.id = id;
        this.voterListName = voterListName;
        this.voterEmails = voterEmails;
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

    /**
     *
     * @return
     */
    public List<String> getVoterEmails() {
        return voterEmails;
    }

    /**
     *
     * @param voterEmails
     */
    public void setVoterEmails(List<String> voterEmails) {
        this.voterEmails = voterEmails;
    }
}
