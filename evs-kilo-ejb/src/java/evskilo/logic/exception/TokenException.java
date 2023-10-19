/**
 * evskilo.logic.exception is the package of all ejb-module exception classes.
 */
package evskilo.logic.exception;

/**
* 
* TokenException class 
* 
* @author  Marvin Nöthen
* @version 1.0
* @since   2023-09-11 
*/
public class TokenException extends Exception{
    public TokenException(String message){
        super(message);
    }
}
