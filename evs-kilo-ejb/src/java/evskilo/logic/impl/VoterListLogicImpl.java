/**
 * evskilo.logic.impl is the package of 
 * all ejb-module business logic implementation classes.
 */
package evskilo.logic.impl;

import evskilo.logic.VoterListLogic;
import evskilo.logic.dto.VoterListDto;
import evskilo.logic.exception.ExceptionType;
import evskilo.logic.exception.PollException;
import evskilo.logic.util.ExceptionUtil;
import evskilo.logic.util.StringUtil;
import evskilo.persistence.dao.VoterAccess;
import evskilo.persistence.dao.OrganizerAccess;
import evskilo.persistence.dao.PollAccess;
import evskilo.persistence.dao.UserAccess;
import evskilo.persistence.dao.VoterListAccess;
import evskilo.persistence.entities.Voter;
import evskilo.persistence.entities.Organizer;
import evskilo.persistence.entities.Poll;
import evskilo.persistence.entities.User;
import evskilo.persistence.entities.VoterList;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
* 
* VoterListLogicImpl class 
* Implementation of VoterListLogic methods
* 
* @author  Marvin Nöthen
* @version 1.0
* @since   2023-13-09 
*/
@Stateless
public class VoterListLogicImpl implements VoterListLogic {

    @Inject
    private Principal principal;

    @Inject
    private ValidateLogicImpl dataValidation;

    @Inject
    private VoterListAccess voterListAccess;

    @Inject
    private UserAccess userAccess;

    @Inject
    private VoterAccess voterAccess;

    @Inject
    private OrganizerAccess organizerAccess;

    @Inject
    private PollAccess pollAccess;

    /**
     * 
     * @param listName
     * @param voterEmails 
     * @throws evskilo.logic.exception.PollException 
     */
    @Override
    public void createVotersList(String listName, String voterEmails) throws PollException {
        dataValidation.validateVotersListName(listName);
        Organizer organizer = getOrganizer();
        VoterList voterList = voterListAccess.getVoterListByName(organizer.getId(), listName);
        List<String> emailList = separateEmails(voterEmails);
        
        if (voterList == null) {
            if (emailList.size() < 3) {
                ExceptionUtil.throwException(ExceptionType.INSUFFICIENT_EMAILS,
                                StringUtil.getLabel("insufficientVoterEmailId"));
            }
            
            voterList = new VoterList();
            voterList.setVoterListName(listName);
            voterList.setOrganizer(organizer);
            voterListAccess.persist(voterList);
        }
        
        for (String email : emailList) {
            dataValidation.validateEmail(email);
            User user = userAccess.getUser(email);
            Voter voter = new Voter();
            voter.setVotingUser(user);
            voter.setVoterList(voterList);
            voterAccess.persist(voter);
        }
    }

    /**
     * 
     * @return
     * @throws PollException 
     */
    @Override
    public List<VoterListDto> getAllVoterLists() throws PollException {
        return voterListAccess.getVoterListsByOrganizer(getOrganizer().getId())
                .stream()
                .map(voterList -> {
                    List<String> emails = voterAccess.getVotersByListId(voterList.getId())
                            .stream()
                            .map(voter -> voter.getVotingUser().getUserName())
                            .collect(Collectors.toList());
                    return new VoterListDto(voterList.getId(), voterList.getVoterListName(), emails);
                }).collect(Collectors.toList());
    }

    @Override
    public void deleteVoterList(String voterListName) throws PollException {
        if (!StringUtil.isEmpty(voterListName)) {
            VoterList voterList = voterListAccess.getVoterListByName(getOrganizer().getId(), voterListName);
            List<Voter> voters = voterAccess.getVotersByListId(voterList.getId());
            
            for (Voter voter : voters) {
                deleteVoter(voter);
            }
            
            for (Poll poll : pollAccess.getPollsByVoterList(voterList.getId())) {
                poll.setVoterList(null);
            }
            
            voterListAccess.deleteByID(voterList.getId());
        }
    }

    @Override
    public void deleteVoterFromList(String voterListName, String voterEmail) throws PollException {
        if (StringUtil.isEmpty(voterListName) || StringUtil.isEmpty(voterEmail)) {
            return;
        }
        
        Long voterListId = voterListAccess.getVoterListByName(getOrganizer().getId(), voterListName).getId();
        List<Voter> voters = voterAccess.getVotersByListId(voterListId);
        
        if (voters.size() <= 3) {
            ExceptionUtil.throwException(ExceptionType.INSUFFICIENT_EMAILS,
                            StringUtil.getLabel("insufficientVoterEmailId"));
        }
        
        Long userId = userAccess.getUser(voterEmail).getId();
        deleteVoter(voterAccess.getVoter(voterListId, userId));
    }

    private Organizer getOrganizer() throws PollException {
        Optional<Organizer> organizer = organizerAccess.getByUsername(principal.getName());
        if (!organizer.isPresent()) {
            ExceptionUtil.throwException(ExceptionType.NOT_FOUND, StringUtil.getLabel("organizer"));
        }
        return organizer.get();
    }

    /**
     *
     * @return
     */
    private List<String> separateEmails(String emails) {
        return Arrays.stream(emails.split(";"))
                .filter(email -> !email.isEmpty())
                .collect(Collectors.toList());
    }

    private void deleteVoter(Voter voter) {
        Long userId = voter.getVotingUser().getId();
        voterAccess.deleteByID(voter.getId());
        
        if (voterAccess.getVotingListsByUserId(userId).isEmpty() 
                && organizerAccess.getOrganizerByUserId(userId) == null) {
            userAccess.deleteByID(userId);
        }
    }
}
