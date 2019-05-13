package net.lzzy.practicesonline.activities.nework;

import net.lzzy.practicesonline.activities.constants.ApiConnstants;
import net.lzzy.practicesonline.activities.models.Practice;
import net.lzzy.practicesonline.activities.models.view.PracticeResult;
import net.lzzy.sqllib.JsonConverter;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/4/22.
 * Description:
 */
public class PracticeService {
    public static String getPracticesFromServer() throws IOException {
        return ApiService.okGet(ApiConnstants.URL_PRACTICES);
    }

    public static List<Practice> getPractices(String json) throws IllegalAccessException, JSONException, InstantiationException {
        JsonConverter<Practice> converter=new JsonConverter<>(Practice.class);
        return converter.getArray(json);
    }
     public static int poseResult(PracticeResult practiceResult)throws JSONException ,IOException{
            return ApiService.okPost(ApiConnstants.URL_QUESTIONSS,practiceResult.toJson());
      }
}
