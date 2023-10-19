/**
 * evs.kilo.web is the package of all web-module bean classes.
 */
package evs.kilo.web;

import evskilo.logic.CommonLogic;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.ejb.Singleton;

/**
* 
* The CommonBean class is used for calling
* application startup operations
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-12 
*/
@Startup
@Singleton
public class CommonBean {

    @EJB
    private CommonLogic commonLogic;

    /**
     *
     */
    @PostConstruct
    public void onStartup() {
        Logger.getLogger("SystemEngine").log(Level.INFO, "Engine Started...");
    }

    /**
     * commonLogic.operateStates()
     */
    @Schedule(dayOfMonth = "*", hour = "*", minute = "*/1", persistent = false)
    public void handlePollStates() {
        Logger.getLogger("SystemEngine").log(Level.INFO, "Engine operateStates...");
        commonLogic.operateStates();
    }

}
