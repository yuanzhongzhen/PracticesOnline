package net.lzzy.practicesonline.activities.models;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public enum  QuestionType {
     SINGLE_CHOLCE("单项选择"),MULTI_CHOICE("不锁定"),JUDE("判断");
    private String name;
    QuestionType(String name){
    this.name=name;
}
   public String toString(){
        return name;
   }
   public static  QuestionType getInstatance(int ordina){
        for (QuestionType type:QuestionType.values()){
            if (type.ordinal()==ordina){

            }
        }
        return null;
   }

}
