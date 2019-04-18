package net.lzzy.practicesonline.activities.models;

import net.lzzy.practicesonline.activities.constants.DbConstants;
import net.lzzy.practicesonline.activities.utils.AppUtils;
import net.lzzy.sqllib.SqlRepository;

import java.util.List;
import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/4/17.
 * Description:
 */
public class FavoriteFactory {
    private static final FavoriteFactory OUR_INSYANCE = new FavoriteFactory();
    private  SqlRepository<Favorite>repository;
    public static FavoriteFactory getInstance() {
        return OUR_INSYANCE;
    }

    private FavoriteFactory() {
        repository=new SqlRepository<>(AppUtils.getContext(),Favorite.class, DbConstants.packager);
    }
    private Favorite geByQuestion(String qisetionId){
        try {
            List<Favorite>favorites=repository
                    .getByKeyword(qisetionId,new String[]{Favorite.COL_QUESTION_ID},true);
            if (favorites.size()>0){
                return favorites.get(0);
            }
        }catch (IllegalAccessException|InstantiationException e){
            e.printStackTrace();
        }
        return null;
    }
    public String getDeleteString(String questionId){
        Favorite favorite=geByQuestion(questionId);
        return favorite ==null?null:repository.getDeleteString(favorite);

    }
    public boolean isQuestionStearred(String questionId){
        try {
            List<Favorite>favorites=repository.getByKeyword(questionId,
                    new String[]{Favorite.COL_QUESTION_ID},true);
            return  favorites.size()>0;
        }catch (IllegalAccessException | InstantiationException e){
            e.printStackTrace();
            return false;
        }

    }
    public void  starQuestion(UUID questionId){
        Favorite favorite=geByQuestion(questionId.toString());
        if (favorite==null){
            favorite=new Favorite();
            favorite.setQuestionid(questionId);
            repository.insert(favorite);
        }
    }

   public void cancelStarQuestion(UUID quesetionId){
        Favorite favorite=geByQuestion(quesetionId.toString());
        if (favorite!=null){
            repository.delete(favorite);
        }
   }

}
