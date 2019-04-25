package net.lzzy.practicesonline.activities.models;

import net.lzzy.practicesonline.activities.constants.ApiConnstants;
import net.lzzy.practicesonline.activities.nework.QuestionService;
import net.lzzy.sqllib.Ignored;
import net.lzzy.sqllib.Jsonable;
import net.lzzy.sqllib.Sqlitable;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.cert.PKIXRevocationChecker;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public class Question extends BaseEntity implements Sqlitable, Jsonable {
   public static final String COL_PRACTICE_ID="practiceId";
    private String content;
    @Ignored
    private QuestionType tupe;
    private  int dbType;
    private String analysis;
    private UUID practiceID;
    private UUID practiceId;
    @Ignored
    private  List<Option>options;

    public UUID getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(UUID practiceId) {
        this.practiceId = practiceId;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public QuestionType getTupe() {
        return tupe;
    }

    public void setTupe(QuestionType tupe) {
        this.tupe = tupe;
    }

    public int getDbType() {
        return dbType;
    }

    public void setDbType(int dbType) {
        this.dbType = dbType;
        tupe=QuestionType.getInstatance(dbType);
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public UUID getPracticeID() {
        return practiceID;
    }

    public void setPracticeID(UUID practiceID) {
        this.practiceID = practiceID;
    }




    @Override
    public boolean needUpdate() {
        return false;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        return null;
    }

    @Override
    public void fromJson(JSONObject json) throws JSONException {
     analysis=json.getString(ApiConnstants.JSON_QUESTION_ANALYSIS);
     content=json.getString(ApiConnstants.JSON_QUESTION_CONTENT);
     setDbType(json.getInt(ApiConnstants.JSON_QUESTION_TYPE));
     String strOpions=json.getString(ApiConnstants.JSON_QUESTION_OPTIONS);
     String StraAnswers=json.getString(ApiConnstants.jSON_QUESTION_ANSWER);
     try {
         List<Option>options= QuestionService.getOptionFormJson(strOpions,StraAnswers);
      for (Option option:options){
          setOptions(options);
      }
      setOptions(options);

     }catch (IllegalAccessException|InstantiationException e){
         e.printStackTrace();

     }


    }


}
