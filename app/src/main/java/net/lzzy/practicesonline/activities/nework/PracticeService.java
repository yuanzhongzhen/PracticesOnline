package net.lzzy.practicesonline.activities.nework;

import net.lzzy.practicesonline.activities.constants.ApiConnstants;
import net.lzzy.practicesonline.activities.models.Practice;
import net.lzzy.sqllib.JsonConverter;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.jar.JarException;

/**
 * Created by lzzy_gxy on 2019/4/22.
 * Description:
 */
public class PracticeService {
    public static String getPracticesFrmServer() throws IOException {
    return  APiService.okGet(ApiConnstants.URL_PRACTICES);
    }
    public static List<Practice>getPractices(String json) throws IllegalAccessException, JarException, JSONException, InstantiationException {
        JsonConverter<Practice>converter=new JsonConverter<>(Practice.class);
        return converter.getArray(json);
    }

}
