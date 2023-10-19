/**
 * evskilo.logic.util is the package of 
 * all ejb-module util methods.
 */
package evskilo.logic.util;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

/**
* 
* StringUtil class 
* Implementation of custom String methods
* 
* @author  Uthara
* @version 1.0
* @since   2023-08-25 
*/
public class StringUtil {

    private StringUtil() {
    }

    public static DateTimeFormatter fmt = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern(getLabel("dateFormat"))
            .toFormatter(Locale.getDefault());

    ;

    /**
     *
     * @param text
     * @return
     */
    public static Boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }

    /**
     *
     * @param date
     * @return
     */
    public static LocalDateTime parseDate(String date) {
        LocalDateTime formattedDT = LocalDateTime.parse(date, fmt);
        return formattedDT;

    }

    /**
     *
     * @param date
     * @return
     */
    public static String dateToString(LocalDateTime date) {
        return date == null ? null : date.format(fmt);
    }

    /**
     *
     * @param key
     * @param parameters
     * @return
     */
    public static String getLabel(String key, Object... parameters) {
        String label = getResource("i18n.messages", key);
        return parameters == null ? label : MessageFormat.format(label, parameters);
    }

    /**
     *
     * @param resourceName
     * @param key
     * @return
     */
    public static String getResource(String resourceName, String key) {
        return ResourceBundle
                .getBundle(resourceName)
                .getString(key);
    }
}
