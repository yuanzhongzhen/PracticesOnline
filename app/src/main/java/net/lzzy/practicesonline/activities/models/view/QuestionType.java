package net.lzzy.practicesonline.activities.models.view;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public enum  QuestionType {
    SINGLE_CHOICE("单项选择"),MULTI_CHOICE("多项选择"),JUDE("判断");

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
                return type;
            }
        }
        return null;
   }

}
