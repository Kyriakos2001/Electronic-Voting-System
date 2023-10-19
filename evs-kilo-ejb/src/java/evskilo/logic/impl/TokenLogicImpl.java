/**
 * evskilo.logic.impl is the package of 
 * all ejb-module business logic implementation classes.
 */
package evskilo.logic.impl;

import evskilo.logic.TokenLogic;
import evskilo.persistence.dao.TokenAccess;
import evskilo.persistence.entities.Token;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
* 
* TokenLogicImpl class 
* Implementation of TokenLogic methods
* 
* @author  Marvin Nöthen
* @version 1.0
* @since   2023-09-11 
*/
@Stateless
public class TokenLogicImpl implements TokenLogic {

    @Inject
    private TokenAccess tokenAccess;

    /**
     *
     * @param tokenValue
     * @return
     */
    @Override
    public boolean validateToken(String tokenValue) {
        
        return tokenAccess.getTokenByValue(tokenValue) != null;
    }

    /**
     * 
     * @param tokenValue 
     */
    @Override
    public void invalidateToken(String tokenValue) {
        Token token = tokenAccess.getTokenByValue(tokenValue);
        if (token != null)
        {
            tokenAccess.deleteByID(token.getId());
        }
    }
}