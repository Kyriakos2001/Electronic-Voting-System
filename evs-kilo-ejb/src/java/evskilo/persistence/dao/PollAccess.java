/**
 * evskilo.persistence.dao is the package of 
 * all ejb-module dao methods.
 */
package evskilo.persistence.dao;

import evskilo.persistence.entities.Poll;
import evskilo.persistence.entities.enums.PollStates;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* 
* PollAccess class 
* Implementation of PollAccess dao methods
* extends BaseAccess<>
* 
* @author  Aishwarya
* @version 1.0
* @since   2023-08-23 
*/
@Stateless
@LocalBean

public class PollAccess extends BaseAccess<Poll> {

    public PollAccess() {
        super(Poll.class);
    }

    /**
     *
     * @param title
     * @return
     */
    public Optional<Poll> getByTitle(String title) {
        try {
            Poll poll = (Poll) entityManager.createQuery("SELECT p FROM Poll p WHERE p.title=:title")
                    .setParameter("title", title)
                    .getSingleResult();
            return Optional.ofNullable(poll);
        } catch (NoResultException exception) {
            Logger.getLogger("PollAccess").log(Level.WARNING, "NoResultException", exception);
            return Optional.empty();
        }
    }

    /**
     *
     * @return
     */
    public List<Poll> getAll() {
        return (List<Poll>) entityManager.createQuery("SELECT p FROM Poll p")
                .getResultList();
    }

    /**
     *
     * @param organizerId
     * @return
     */
    public List<Poll> getAllByOrganizer(Long organizerId) {
        return (List<Poll>) entityManager.createQuery("SELECT p FROM Poll p WHERE p.organizer.id=:organizerId")
                .setParameter("organizerId", organizerId)
                .getResultList();
    }

    /**
     *
     * @param state
     * @return
     */
    public List<Poll> getAllByState(PollStates state) {
        return (List<Poll>) entityManager.createQuery("SELECT p FROM Poll p WHERE p.state=:state")
                .setParameter("state", state)
                .getResultList();
    }

    /**
     *
     * @param organizerId
     * @param pollId
     * @return
     */
    public Optional<Poll> getByOrganizerAndId(Long organizerId, Long pollId) {
        try {
            Poll poll = (Poll) entityManager.createQuery("SELECT p FROM Poll p WHERE p.organizer.id=:organizerId AND p.id=:pollId")
                    .setParameter("organizerId", organizerId)
                    .setParameter("pollId", pollId)
                    .getSingleResult();
            return Optional.ofNullable(poll);
        } catch (NoResultException exception) {
            Logger.getLogger("PollAccess").log(Level.WARNING, "NoResultException", exception);
            return Optional.empty();
        }
    }
    
    /**
     *
     * @param publishKey
     * @return
     */
    public Optional<Poll> getByPublishKey(String publishKey){
        try {
            Poll poll = (Poll) entityManager.createQuery("SELECT p FROM Poll p WHERE p.publicResultKey=:publishKey")
                    .setParameter("publishKey", publishKey)
                    .getSingleResult();
            return Optional.ofNullable(poll);
        } catch (NoResultException exception) {
            Logger.getLogger("PollAccess").log(Level.WARNING, "NoResultException", exception);
            return Optional.empty();
        }
    }

    /**
     *
     * @param pollId
     * @return
     */
    public Poll getPollById(Long pollId) {
        try {
            return (Poll) entityManager.createQuery("SELECT p FROM Poll p WHERE p.id=:pollId")
                    .setParameter("pollId", pollId)
                    .getSingleResult();
        } catch (NoResultException exception) {
            Logger.getLogger("PollAccess").log(Level.WARNING, "NoResultException", exception);
        }
        return null;
    }

    /**
     *
     * @param voterListId
     * @return
     */
    public List<Poll> getPollsByVoterList(Long voterListId) {
        return (List<Poll>) entityManager.createQuery(
                "SELECT p FROM Poll p WHERE p.voterList.id=:voterListId")
                .setParameter("voterListId", voterListId)
                .getResultList();
    }
}
