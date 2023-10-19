/**
 * evskilo.persistence.dao is the package of 
 * all ejb-module dao methods.
 */
package evskilo.persistence.dao;

import evskilo.persistence.entities.Item;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

/**
* 
* ItemAccess class 
* Implementation of ItemAccess dao methods
* extends BaseAccess<>
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-20 
*/
@Stateless
@LocalBean
public class ItemAccess extends BaseAccess<Item> {

    /**
     *
     */
    public ItemAccess() {
        super(Item.class);
    }

    /**
     *
     * @param questionId
     * @param name
     * @return
     */
    public Optional<Item> getByNameAndQuestion(Long questionId, String name) {
        try {
            Item item = (Item) entityManager.createQuery("SELECT i FROM Item i WHERE i.name=:name AND i.question.id=:questionId")
                    .setParameter("name", name)
                    .setParameter("questionId", questionId)
                    .getSingleResult();
            return Optional.ofNullable(item);
        } catch (NoResultException exception) {
            Logger.getLogger("ItemAccess").log(Level.WARNING, "NoResultException", exception);
            return Optional.empty();
        }
    }

    /**
     *
     * @param questionId
     * @return
     */
    public List<Item> getByQuestionId(Long questionId) {
        List<Item> itemList = (List<Item>) entityManager.createQuery("SELECT i FROM Item i WHERE i.question.id=:questionId")
                .setParameter("questionId", questionId)
                .getResultList();
        return itemList;
    }
}
