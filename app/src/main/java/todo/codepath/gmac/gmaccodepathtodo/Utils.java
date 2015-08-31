package todo.codepath.gmac.gmaccodepathtodo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils
{
    public enum DialogReason
    {
        ADD,
        EDIT,
    };

    public static String getCurrentDateTime()
    {
        SimpleDateFormat df = new SimpleDateFormat("MMM dd EEE HH:mm");
        return df.format(new Date(System.currentTimeMillis()));
    }
};
