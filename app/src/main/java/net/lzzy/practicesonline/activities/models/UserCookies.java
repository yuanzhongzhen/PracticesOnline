package net.lzzy.practicesonline.activities.models;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;
import net.lzzy.practicesonline.activities.models.view.WrongType;
import net.lzzy.practicesonline.activities.utils.AppUtils;
import net.lzzy.practicesonline.activities.utils.DateTimeUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/4/24.
 * Description:
 */
public class UserCookies {
    public static final String KEY_TIME = "key_time";
    private static final int FLAG_COMMIT_N = 0;
    private static final int FLAG_COMMIT_Y = 1;
    protected static final String ID_SPGLITTER = ".";
    private SharedPreferences spTime;
    private SharedPreferences spCommit;
    private SharedPreferences spPostion;
    private SharedPreferences soReadCount;
    private SharedPreferences spOption;
    private static final UserCookies INSTANCE=new UserCookies();

    private UserCookies(){
        spTime= AppUtils.getContext()
                .getSharedPreferences("refresh_time", Context.MODE_PRIVATE);
        spCommit=AppUtils.getContext()
                .getSharedPreferences("practice_comm",Context.MODE_PRIVATE);
        spPostion=AppUtils.getContext()
                .getSharedPreferences("queston_postion",Context.MODE_PRIVATE);
        soReadCount=AppUtils.getContext()
                .getSharedPreferences("read_count",Context.MODE_PRIVATE);
        spOption=AppUtils.getContext()
                .getSharedPreferences("read_options",Context.MODE_PRIVATE);
    }

    public static UserCookies getInstance(){
        return INSTANCE;
    }

    public void updateLastRefreshTime(){
        String time= DateTimeUtils.DATE_TIME_FORMAT.format(new Date());
        spTime.edit().putString(KEY_TIME,time).apply();
    }

    public String getLastRefreshTime(){
        return spTime.getString(KEY_TIME,"");
    }
    public boolean isPracticeCommitted(String practiceId){
     int result=spCommit.getInt(practiceId, FLAG_COMMIT_N);
     return  result==FLAG_COMMIT_Y;
  }
    public void commitPrantice(String practiceId){
       spCommit.edit().putInt(practiceId,FLAG_COMMIT_Y).apply();
 }
    public void upadteCurrentQuestion(String practiceId,int pos){
      spPostion.edit().putInt(practiceId,pos).apply();
 }
    public int getCurrentQuestion(String practiceId){
      return spPostion.getInt(practiceId,0);
  }
    public int getReadCount(String questionId){
        return  soReadCount.getInt(questionId,0);
  }
    public void updateReadCount(String questionId){
        int count=getReadCount(questionId)+1;
        soReadCount.edit().putInt(questionId,count).apply();
  }
   public void changeOptionState(Option option,boolean isChecked,boolean isMulti){
        String ids=spOption.getString(option.getQuestionid().toString(),"");
        String id=option.getId().toString();
        if (isMulti){
            if (isChecked && !ids.contains(id)){
                ids=ids.concat(ID_SPGLITTER).concat(id);
            }else if (!isChecked&&ids.contains(id)){
                ids=ids.replace(id,"");
            }
        }else {
            if (isChecked){
                ids=id;
            }
        }
        spOption.edit().putString(option.getQuestionid().toString(),trunckSplitter(ids)).apply();
   }
   private String trunckSplitter(String ids){
        boolean isSpltterRepeat=true;
        String repeatSplitter=ID_SPGLITTER.concat(ID_SPGLITTER);
        while (isSpltterRepeat){
            isSpltterRepeat=false;
            if (ids.contains(repeatSplitter)){
                isSpltterRepeat=true;
                ids=ids.replace(repeatSplitter,ID_SPGLITTER);
            }
        }
        if (ids.endsWith(ID_SPGLITTER)){
            ids=ids.substring(0,ids.length()-1);
        }
        if (ids.endsWith(ID_SPGLITTER)){
            ids=ids.substring(1);
        }
        return ids;
   }
   public boolean isOptionSeleceted(Option option){
        String ids=spOption.getString(option.getQuestionid().toString(),"");
        return ids.contains(option.getId().toString());
   }
  public List<QuestionResult>getResultFromCookies(List<Question>questions){
        List<QuestionResult>results=new ArrayList<>();
        for (Question question:questions){
            QuestionResult result=new QuestionResult();
            result.setQuestionId(question.getId());
            String checkedIds=spOption.getString(question.getId().toString(),"");
            result.setRight(isUserRight(checkedIds,question).first);
            result.setType(isUserRight(checkedIds,question).second);
            /*result.setType(getWrongType(checkedIds,question));*/
            results.add(result);
        }
        return results;

  }
  private Pair<Boolean,WrongType > isUserRight(String checkedIds,Question question){

    /*   //todo:是否计算正确
      for (Option option:question.getOptions()){
          if (option.isAnswer()){
              if (!checkedIds.contains(option.getId().toString())){
                  return false;
              }
          }else {
              if (checkedIds.contains(option.getId().toString())){
                  return false;
              }
          }
      }
        return true;*/
      boolean miss=false,extra=false;
      for (Option option:question.getOptions()){
          if (option.isAnswer()){
              if (!checkedIds.contains(option.getId().toString())){
                  miss=true;
              }
          }else {
              if (checkedIds.contains(option.getId().toString())){
                  extra=true;
              }
          }
      }
      if (miss&&extra){
        return new Pair<>(false,WrongType.WRONG_OPTIONS);
      }else if (miss){
         return new Pair<>(false ,WrongType.MISS_OPTIONS );
      }else if (extra){
          return new  Pair<>(false,WrongType.EXTRA_POTIONS);
      }else {
          return  new Pair<>(true,WrongType.RIGHT_OPTIONS);
      }
  }
  private WrongType getWrongType(String checkedIds,Question question){
       //todo:计算错误类型
      boolean miss=false,extra=false;
      for (Option option:question.getOptions()){
          if (option.isAnswer()){
              if (!checkedIds.contains(option.getId().toString())){
                  miss=true;
              }
          }else {
              if (checkedIds.contains(option.getId().toString())){
                  extra=true;
              }
          }
      }
      if (miss&&extra){
           return WrongType.WRONG_OPTIONS;
      }else if (miss){
          return WrongType.MISS_OPTIONS;
      }else if (extra){
          return WrongType.EXTRA_POTIONS;
      }else {
          return WrongType.RIGHT_OPTIONS;
      }

  }
}
