package net.lzzy.practicesonline.activities.models;

import net.lzzy.sqllib.Ignored;
import net.lzzy.sqllib.Sqlitable;

import java.util.Date;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public class Practice extends BaseEntity implements Sqlitable {
  @Ignored
   public static final String COL_NAME="name";
 @Ignored
  public static final String  COL_OUTLINES="outkines";
   public static final String  COL_API_ID="outlines";
    @Override
    public boolean needUpdate(){
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIntquestionCount() {
        return intquestionCount;
    }

    public void setIntquestionCount(int intquestionCount) {
        this.intquestionCount = intquestionCount;
    }

    public Date getDowntoladDote() {
        return downtoladDote;
    }

    public void setDowntoladDote(Date downtoladDote) {
        this.downtoladDote = downtoladDote;
    }

    public String getOutlines() {
        return outlines;
    }

    public void setOutlines(String outlines) {
        this.outlines = outlines;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public int getApild() {
        return apild;
    }

    public void setApild(int apild) {
        this.apild = apild;
    }

    private String name;
    private  int intquestionCount;
    private Date downtoladDote;
    private String outlines;
    private boolean isDownloaded;
    private int apild;

}
