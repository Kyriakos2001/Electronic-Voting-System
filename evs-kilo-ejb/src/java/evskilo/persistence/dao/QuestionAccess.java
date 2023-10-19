/**
 * evskilo.persistence.dao is the package of 
 * all ejb-module dao methods.
 */
package evskilo.persistence.dao;

import evskilo.persistence.entities.Question;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

/**
* 
* QuestionAccess class 
* Implementation of QuestionAccess dao methods
* extends BaseAccess<>
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-20 
*/
@Stateless
@LocalBean
public class QuestionAccess extends BaseAccess<Question> {

    public QuestionAccess() {
        super(Question.class);
    }

    /**
     *
     * @param pollId
     * @param title
     * @return
     */
    public Optional<Question> getByTitleAndPoll(Long pollId, String title) {
        try {
            System.out.println("entityManager := " + entityManager);
            Question question = (Question) entityManager.createQuery("SELECT q FROM Question q WHERE q.title=:title AND q.poll.id=:pollId")
                    .setParameter("title", title)
                    .setParameter("pollId", pollId)
                    .getSingleResult();
            return Optional.ofNullable(question);
        } catch (NoResultException exception) {
            Logger.getLogger("QuestionAccess").log(Level.WARNING, "NoResultException", exception);
            return Optional.empty();
        }
    }

    /**
     *
     * @param pollId
     * @return
     */
    public List<Question> getQuestionByPollId(Long pollId) {
        List<Question> questionList = (List<Question>) entityManager.createQuery("SELECT q FROM Question q WHERE q.poll.id=:pollId")
                .setParameter("pollId", pollId)
                .getResultList();
        return questionList;
    }
}
