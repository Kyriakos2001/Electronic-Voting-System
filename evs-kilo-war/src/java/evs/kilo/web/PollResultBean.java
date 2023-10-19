/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evs.kilo.web;

import evskilo.logic.OrganizerLogic;
import evskilo.logic.VoteLogic;
import evskilo.logic.dto.PollResultDto;
import evskilo.logic.dto.QuestionResultDto;
import evskilo.logic.exception.PollException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author uthar
 */
@RequestScoped
@ManagedBean
@Named
public class PollResultBean extends BaseBean {
    @EJB
    private VoteLogic voterLogic;
    
    @EJB
    private OrganizerLogic organizerLogic; 
    
    @Inject
    private LoginBean loginBean;

    private PollResultDto selectedPoll;

    private String publishKey;  
    
    private List<List<String>> colorSet;  
    
    private Long pollId;
    
    private ExternalContext externalContext;
    
     
     
    private void getResultsByRole(){    
         externalContext = FacesContext.getCurrentInstance().getExternalContext();
         if(externalContext.getRequestParameterMap().get("publishKey") != null)
            this.publishKey = externalContext.getRequestParameterMap().get("publishKey"); 
         
         if(externalContext.getRequestParameterMap().get("pollId") != null)
            this.pollId = Long.valueOf(externalContext.getRequestParameterMap().get("pollId"));
         
         if(publishKey == null && pollId == null){
             loginBean.invalidateSession();
         }
    }
    
    /**
     *
     * @return
     */
    public PollResultDto getSelectedPoll() {   
        getResultsByRole();
        try {
            if (loginBean.isOrganizer() && pollId != null){                    
                    this.selectedPoll = organizerLogic.viewResult(pollId);                    
            }
            else{
                this.selectedPoll = voterLogic.viewResult(publishKey);
            }                
            if (selectedPoll != null){                    
                getChartColors();
                return selectedPoll;
            }
            return null;

        } catch (PollException exception) {
            parseVotingException(exception);  

        }
        return null;
    }    
    
    /**
     *
     * @param index
     * @return
     */
    public List<String> getColorSet(int index) {
          return colorSet.get(index);
    }   
    
    /**
     *
     * @param votes
     * @return
     */
    public String getProgressColor(int votes) {
        if (votes > 75)
            return "bg-success";
        else if (votes > 50)
            return "bg-info";
        else if( votes > 25)
            return "bg-warning";
        else
            return "bg-danger";
       
    }  
    
    /**
     *
     */
    public void getChartColors(){
       colorSet = new ArrayList<>();       
       for (QuestionResultDto data1 : selectedPoll.getQuestionResults()) {
           List<String> colors = new ArrayList<>();
           if (data1.getItemResults().size() == 2){
               colors.add("\'yellowgreen\'");
               colors.add("\'palevioletred\'");
           }
           else{
               for(int i=0; i < data1.getItemResults().size(); i++){               
               String colorString = "\'#" + Math.round(Math.random()*585255)+ "\'";  
               colors.add(colorString);
           }
           }
           
           colorSet.add(colors);
        }        
    }
    
}
