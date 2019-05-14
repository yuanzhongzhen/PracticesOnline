package net.lzzy.practicesonline.activities.constants;
import net.lzzy.practicesonline.activities.utils.AppUtils;

public class ApiConnstants {

    /**
     * 读取本地服务器的地址
     *
     * **/
    private static final String IP= AppUtils.loadServerSetting(AppUtils.getContext()).first;
    private static final String PORT=AppUtils.loadServerSetting(AppUtils.getContext()).second;
    private static final String PROTOCOL="http://";

    /**
     * API地址
     */

    public static final String URL_API=PROTOCOL.concat(IP).concat(":").concat(PORT);
    /**
     *
     * api项目地址**/
    private static  final String ACTIO_PRACTICES="/api/practices";
    private static final String ACTUIN_QUESTIONS="/api/pquestions?practiceid=";
    public  static  final  String URL_QUESTIONS=URL_API.concat(ACTUIN_QUESTIONS);
    public static final String URL_PRACTICES=URL_API.concat(ACTIO_PRACTICES);
/**
 * Proctice字段
 * **/
    public static  final String JSON_PRACTICE_API_ID="Id";
    public static  final String JSON_PRACTICE_NAME="Name";
    public static  final String JSON_PRACTICE_QUTLINES="OutLines";
    public static  final String JSON_PRACTICE_QUESTION_COUNT="QuestionCount";

/**
 * Question字段
 * **/
public static final String JSON_QUESTION_ANALYSIS = "Analysis";
    public static final String JSON_QUESTION_CONTENT = "Content";
    public static final String JSON_QUESTION_TYPE = "QuestionType";
    public static final String JSON_QUESTION_OPTIONS = "Options";
    public static  final String jSON_QUESTION_ANSWER="Answers";
    public static final String JSON_OPTION_OPTION_ID="OptionId";



/**
 * Opion字段
 * **/
    public static final String JSON_OPTION_CONTEN="Content";
    public static final String JSON_OPTION_LABEL="Label";
    public static final String JSON_OPTION__ID="Id";

/**
 * 提交结果
 * **/
    private static final String ACTION_QUESTIONS="/api/result/PracticeResul";

    public static final String URL_QUESTIONSS=URL_API.concat(ACTION_QUESTIONS);
    /**
     * PraticeResult字段
     */
    public static final String JSON_RESULT_API_ID = "PracticeID";
    public static final String JSON_RESULT_PRACTICE_ID = "PracticeID";
    public static final String JSON_RESULT_SCRORE_RATIO = "ScroreRatio";
    public static final String JSON_RESULT_WRONG_QUESTION_IDS = "WrongQuestionIds";
    public static final String JSON_RESULT_PHONE_NO = "PhoneNo";

}
