/**
 * evskilo.persistence.dao is the package of 
 * all ejb-module dao methods.
 */
package evskilo.persistence.dao;

import evskilo.persistence.entities.Organizer;
import java.util.Optional;
import java.util.logging.Level;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.logging.Logger;

/**
* 
* OrganizerAccess class 
* Implementation of OrganizerAccess dao methods
* extends BaseAccess<>
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-14 
*/
@Stateless
@LocalBean
public class OrganizerAccess extends BaseAccess<Organizer> {

    private static final Logger LOG = Logger.getLogger(OrganizerAccess.class.getName());
    
    public OrganizerAccess() {
        super(Organizer.class);
    }

    /**
     *
     * @param username
     * @return
     */
    public Optional<Organizer> getByUsername(String username) {
        try {
            TypedQuery<Organizer> query = entityManager.createQuery(
                    "SELECT o FROM Organizer o WHERE o.user.userName = :username", Organizer.class)
                    .setParameter("username", username);

            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException exception) {
            LOG.log(Level.WARNING, "NoResultException", exception);
            return Optional.empty();
        }
    }

    public Organizer getOrganizerByUserId(Long userId) {
        try {
            return (Organizer) entityManager.createQuery(
                    "SELECT o FROM Organizer o WHERE o.user.id = :userId")
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException exception) {
            LOG.log(Level.WARNING, "NoResultException", exception);
        }
        return null;
    }

}
