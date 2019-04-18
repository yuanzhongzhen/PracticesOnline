package net.lzzy.practicesonline.activities.models;

import net.lzzy.sqllib.Sqlitable;

import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public class Favorite extends BaseEntity implements Sqlitable {
   public static final String COL_QUESTION_ID="questionid";
    private UUID questionid;

    public UUID getQuestionid() {
        return questionid;
    }

    public void setQuestionid(UUID questionid) {
        this.questionid = questionid;
    }

    public int getTimens() {
        return timens;
    }

    public void setTimens(int timens) {
        this.timens = timens;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    private  int timens;
    private boolean isDone;

    @Override
    public boolean needUpdate() {
        return false;
    }
}
