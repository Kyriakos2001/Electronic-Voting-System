/**
 * evskilo.logic.dto is the package of all ejb-module dtos.
 */
package evskilo.logic.dto;

import java.util.Locale;
import javax.xml.bind.annotation.XmlRootElement;

/**
* 
* UserDto class 
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-14
* 
*/
@XmlRootElement
public class UserDto {

    private final String username;
    private Locale userLocale;

    public UserDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setuserLocale(Locale locale) {
        this.userLocale = locale;
    }

    public Locale getuserLocale() {
        return userLocale;
    }

}
