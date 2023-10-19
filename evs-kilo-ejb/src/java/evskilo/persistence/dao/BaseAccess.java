/**
 * evskilo.persistence.dao is the package of 
 * all ejb-module dao methods.
 */
package evskilo.persistence.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
* 
* BaseAccess class 
* Implementation of BaseAccess dao methods
* 
* @author  Aishwarya
* @version 1.0
 * @param <T>
* @since   2023-07-23 
*/
public abstract class BaseAccess<T> {

    @PersistenceContext(unitName = "evs-kilo-ejbPU")
    protected EntityManager entityManager;

    protected Class<T> entityClass;

    /**
     *
     * @param entityClass
     */
    public BaseAccess(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     *
     * @param id
     * @return
     */
    public T getByID(Long id) {
        return entityManager.find(entityClass, id);
    }

    /**
     *
     * @param id
     */
    public void deleteByID(Long id) {
        entityManager.remove(getByID(id));
    }

    /**
     *
     * @param model
     */
    public void persist(T model) {
        entityManager.persist(model);
    }

}
