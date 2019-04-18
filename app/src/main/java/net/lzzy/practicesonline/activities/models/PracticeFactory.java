package net.lzzy.practicesonline.activities.models;

import androidx.fragment.app.Fragment;

import net.lzzy.practicesonline.activities.constants.DbConstants;
import net.lzzy.practicesonline.activities.utils.AppUtils;
import net.lzzy.sqllib.DbPackager;
import net.lzzy.sqllib.SqlRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/4/17.
 * Description:
 */
public class PracticeFactory {
    private  static final PracticeFactory OUR_INSTANCE=new PracticeFactory();
    private SqlRepository<Practice> repository;

    public static PracticeFactory getInstance(){
        return OUR_INSTANCE;

    }
    private PracticeFactory(){
        repository=new SqlRepository<>(AppUtils.getContext(),Practice.class, DbConstants.packager);

    }
    public List<Practice>get(){
        return repository.get();
    }
    public Practice getById(String id){
        return  repository.getById(id);
    }
    public List<Practice>search(String kw){
        try {
            return repository.getByKeyword(kw,
                    new String[]{Practice.COL_NAME,Practice.COL_OUTLINES},false);
        }catch (IllegalAccessException|InstantiationException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    private boolean isPracticesInDb(Practice practice){
        try {
        return repository.getByKeyword(String.valueOf(practice.getApild()),
               new String[]{Practice.COL_API_ID},true ).size()>0;

        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return true;
        }

    }
    public  boolean add(Practice practice){
        if (isPracticesInDb(practice)){
            return false;

        }
        repository.insert(practice);
        return  true;
    }
   public UUID getPracticeId(int apaId){
        try {
            List<Practice>practices=repository.getByKeyword(String.valueOf(apaId),
                    new String[]{Practice.COL_API_ID},true);
            if (practices.size()>0){
                return practices.get(0).getId();
            }
        }catch (IllegalAccessException | InstantiationException e){
            e.printStackTrace();
        }
        return null;
   }
   private void setPracticeDown(String id){
        Practice practice =getById(id);
        if (practice!=null){
            practice.setDownloaded(true);
            repository.update(practice);

        }
   }
   public void saceQuestiongs(List<Question>questions,UUID practiceId){
        for (Question q:questions){
            QuesetionFaceory.getInstance().insert(q);
        }
        setPracticeDown(practiceId.toString());
   }
   public boolean deletePracticeAndRelated(Practice practice){
       try {
           List<String>sqlActions=new ArrayList<>();
           sqlActions.add(repository.getDeleteString(practice));
           QuesetionFaceory faceory=QuesetionFaceory.getInstance();
           List<Question>questions=faceory.getBupraceice(practice.getId().toString());
           if (questions.size()>0){
               for (Question q:questions){
                   sqlActions.addAll(faceory.getDeleteString(q));
               }
           }
           repository.exeSqls(sqlActions);
           if (!isPracticesInDb(practice)){

           }
           return true;
       }catch (Exception e){
           return  false;
       }
   }
}


