package net.lzzy.practicesonline.activities.models.view;
import net.lzzy.practicesonline.activities.constants.ApiConnstants;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/5/8.
 * Description:
 */
public class PracticeResult {
    public static final String SPLITTES = ",";
    private List<QuestionResult>results;
    private int id;
    private String info;

    public PracticeResult (List<QuestionResult>results, int apiId, String info){
     this.id=apiId;
     this.info=info;
     this.results=results;
    }
    public List<QuestionResult> getResults() {
        return results;
    }

    public int getId() {
        return id;
    }

   private double getInfo() {
        int infot=0;
        for (QuestionResult result:results){
            if (result.isRight()){
                infot=infot+1;
            }
        }
    /*计算分数*/
        return  infot/results.size()*100;
    }

    private double getRatio(){
        return 0;
    }
    /**
     * 错误的题目序号
     * **/
    private String getWrongOrders(){
       int i=0;
       String  ids="";
       for (QuestionResult result:results){
           i++;
           if (!result.isRight()){
               ids=ids.concat(i+ SPLITTES);
           }
       }
       if (ids.endsWith(SPLITTES)){
           ids=ids.substring(0,ids.length()-1);
       }
       return ids;
    }



    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
            json.put(ApiConnstants.JSON_RESULT_API_ID,id);
            json.put(ApiConnstants.JSON_RESULT_PHONE_NO,info);
            json.put(ApiConnstants.JSON_RESULT_SCRORE_RATIO,
                    new DecimalFormat("#00").format(getRatio()));
            json.put(ApiConnstants.JSON_RESULT_WRONG_QUESTION_IDS,getWrongOrders());
        return json;
    }

}

