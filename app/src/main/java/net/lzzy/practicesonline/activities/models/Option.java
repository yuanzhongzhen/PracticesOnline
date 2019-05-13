package net.lzzy.practicesonline.activities.models;

import net.lzzy.practicesonline.activities.constants.ApiConnstants;
import net.lzzy.sqllib.Ignored;
import net.lzzy.sqllib.Jsonable;
import net.lzzy.sqllib.Sqlitable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public class Option extends BaseEntity implements Sqlitable, Jsonable {
   @Ignored
    public static final String COL_QUESTION_ID="questionid";
    private String content;
    private String label;
    private UUID questionid;
    private boolean isAnswer;
    private int apild;

    @Override
    public boolean needUpdate(){
        return false;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public UUID getQuestionid() {
        return questionid;
    }

    public void setQuestionid(UUID questionid) {
        this.questionid = questionid;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    public int getApild() {
        return apild;
    }

    public void setApild(int apild) {
        this.apild = apild;
    }


    @Override
    public JSONObject toJson() throws JSONException {
        return null;
    }

    @Override
    public void fromJson(JSONObject jsonObject) throws JSONException {
    content=jsonObject.getString(ApiConnstants.JSON_OPTION_CONTEN);
    label=jsonObject.getString(ApiConnstants.JSON_OPTION_LABEL);
    apild=jsonObject.getInt(ApiConnstants.JSON_OPTION__ID);
    }
}
