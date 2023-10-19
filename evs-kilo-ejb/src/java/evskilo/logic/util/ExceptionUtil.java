/**
 * evskilo.logic.util is the package of 
 * all ejb-module util methods.
 */
package evskilo.logic.util;

import evskilo.logic.exception.ExceptionType;
import static evskilo.logic.exception.ExceptionType.INSUFFICIENT_EMAILS;
import static evskilo.logic.exception.ExceptionType.QUESTION_NOT_COMPLETE;
import evskilo.logic.exception.PollException;

/**
* 
* ExceptionUtil class 
* Implementation of Exception methods
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-15 
*/
public class ExceptionUtil {

    /**
     *
     * @param e
     * @param message
     * @throws PollException
     */
    public static void throwException(ExceptionType e, String message) throws PollException {

        switch (e) {
            case INTERNAL ->
                throw new PollException(StringUtil.getLabel("internalException", message));
            case NOT_FOUND ->
                throw new PollException(StringUtil.getLabel("notFoundException", message));
            case VALUE_MISSING ->
                throw new PollException(StringUtil.getLabel("valueMissingException", message));
            case INSUFFICIENT_EMAILS ->
                throw new PollException(StringUtil.getLabel("insufficientVoterEmailId", message));
            case VOTING_DATA_ERROR ->
                throw new PollException(StringUtil.getLabel("votingDataDoesNotMatchException", message));

        }
    }

    /**
     *
     * @param e
     * @param message
     * @param param
     * @throws PollException
     */
    public static void throwException(ExceptionType e, String message, String param) throws PollException {
        switch (e) {
            case INVALID_OPERATION ->
                throw new PollException(StringUtil.getLabel("invalidOperation", param, message));
            case INVALID_PARAMETER ->
                throw new PollException(StringUtil.getLabel("invalidParameterException", param, message));
            case INVALID_VALUE ->
                throw new PollException(StringUtil.getLabel("invalidValueException", param, message));
            case QUESTION_NOT_COMPLETE ->
                throw new PollException(StringUtil.getLabel("questionNotCompleteException", param, message));
            case INVALID_EMAIL ->
                throw new PollException(StringUtil.getLabel("invalidVoterEmailId", param, message));
        }
    }
}
