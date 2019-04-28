package net.lzzy.practicesonline.activities.nework;


import net.lzzy.practicesonline.activities.constants.ApiConnstants;
import net.lzzy.practicesonline.activities.models.Option;
import net.lzzy.practicesonline.activities.models.Question;
import net.lzzy.sqllib.JsonConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/4/22.
 * Description:
 */
public class QuestionService {
    public static String getQuestionsOfPracticeFromServer(int apiId) throws IOException {
        String address= ApiConnstants.URL_QUESTIONS+apiId;
        return ApiService.okGet(address);
    }

    public static List<Question> getQuestions(String json, UUID practiceId) throws IllegalAccessException, JSONException, InstantiationException {
        JsonConverter<Question> converter=new JsonConverter<>(Question.class);
        List<Question> questions=converter.getArray(json);
        for (Question question:questions){
            question.setPracticeId(practiceId);
        }
        return questions;
    }

    public static List<Option> getOptionsFromJson(String jsonOptions,String jsonAnswers)throws IllegalAccessException, JSONException, InstantiationException{
        JsonConverter<Option> converter=new JsonConverter<>(Option.class);
        List<Option> options=converter.getArray(jsonOptions);
        List<Integer> answerIds=new ArrayList<>();
        JSONArray array = (JSONArray)(new JSONTokener(jsonAnswers)).nextValue();
        for (int i=0;i<array.length();i++){
            JSONObject object=array.getJSONObject(i);
            answerIds.add(object.getInt(ApiConnstants.JSON_OPTION_OPTION_ID));
        }
        for (Option o:options){
            if (answerIds.contains(o.getApild())) {
                o.setAnswer(true);
            }else {
                o.setAnswer(false);
            }
        }
        return options;
    }
}
