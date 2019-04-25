package net.lzzy.practicesonline.activities.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by lzzy_gxy on 2019/4/24.
 * Description:
 */
public class DateTimeUtils {
    public static final SimpleDateFormat DATE_ITE_FORMAT=
            new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT=
            new SimpleDateFormat("yyyy-MM-dd");
}
