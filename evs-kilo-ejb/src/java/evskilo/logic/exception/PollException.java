/**
 * evskilo.logic.exception is the package of all ejb-module exception classes.
 */
package evskilo.logic.exception;

/**
* 
* PollException class 
* 
* @author  Uthara
* @version 1.0
* @since   2023-07-25 
*/
public class PollException extends Exception{
    public PollException(String message){
        super(message);
    }
}