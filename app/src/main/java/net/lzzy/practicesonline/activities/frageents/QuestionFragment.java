package net.lzzy.practicesonline.activities.frageents;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.FavoriteFactory;
import net.lzzy.practicesonline.activities.models.Option;
import net.lzzy.practicesonline.activities.models.Question;
import net.lzzy.practicesonline.activities.models.QuestionFactory;
import net.lzzy.practicesonline.activities.models.view.QuestionType;
import net.lzzy.practicesonline.activities.models.UserCookies;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/4/30.
 * Description:
 */
public class QuestionFragment extends BeseFargment {
    public static final String ARGS_QUESTION_ID = "argsQuestionId";
    public static final String ARGS_POS = "argsPos";
    public static final String ARGS_ISCOMMITTED = "argsIscommitted";
    private Question question;
    private int pos;
    private boolean isCommitted;
    private TextView tvType;
    private ImageButton imgFavoorite;
    private TextView tvcontent;
    private RadioGroup rgOptions;
    private boolean isMulti=false;
    private QuestionFactory factory=QuestionFactory.getInstance();



    public static QuestionFragment newInstance(String questionId,int pos,boolean iscommitted){
        QuestionFragment fragment=new QuestionFragment();
        Bundle args=new Bundle();
        args.putString(ARGS_QUESTION_ID,questionId);
        args.putInt(ARGS_POS,pos);
        args.putBoolean(ARGS_ISCOMMITTED,iscommitted);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            pos=getArguments().getInt(ARGS_POS);
            isCommitted=getArguments().getBoolean(ARGS_ISCOMMITTED);
            question= QuestionFactory.getInstance().getById(getArguments().getString(ARGS_QUESTION_ID));
        }
    }
      private void initView(){
      tvType=find(R.id.fragement_question_tv_question_lv);
      imgFavoorite=find(R.id.fragement_question_img_favorite);
      tvcontent=find(R.id.fragement_question_tv_content);
      rgOptions=find(R.id.fragement_question__content);
      if (isCommitted){
          rgOptions.setOnClickListener(v ->
                  new AlertDialog.Builder(getContext())
                    .setMessage(question.getAnalysis())
                   .show());
      }


      }

       private void adapterVuew(){
        tvType.setText((pos+1)+"、"+question.getTupe().toString());
        tvcontent.setText(question.getContent());
        factory=QuestionFactory.getInstance();

           //region 设置收藏
           imgFavoorite.setOnClickListener(v -> {
               if (FavoriteFactory.getInstance().isQuestionStearred(question.getId().toString())) {
                   FavoriteFactory.getInstance().cancelStarQuestion(question.getId());
                   imgFavoorite.setBackgroundResource(R.drawable.on_favorite);
               } else {
                   FavoriteFactory.getInstance().starQuestion(question.getId());
                   imgFavoorite.setBackgroundResource(R.drawable.off_favorite);
               }

           });
           FavoriteFactory favoriteFactory = FavoriteFactory.getInstance();
           if (favoriteFactory.isQuestionStearred(question.getId().toString())) {
              imgFavoorite.setBackgroundResource(R.drawable.on_favorite);
           } else {
              imgFavoorite.setBackgroundResource(R.drawable.off_favorite);
           }
           //endregion
//           List<Option> options = question.getOptions();
//           if (question.getTupe().equals(QuestionType.SINGLE_CHOICE)) {
//               for (Option option : options) {
//                   RadioButton radioButton=new RadioButton(getContext());
//                   radioButton.setText(option.getLabel()+"、"+option.getContent());
//                   rgOptions.addView(radioButton);
//               }
//           } else if (question.getTupe().equals(QuestionType.MULTI_CHOICE)){
//               for (Option option : options) {
//                   CheckBox checkBox=new CheckBox(getContext());
//                   checkBox.setText(option.getLabel()+"、"+option.getContent());
//                   rgOptions.addView(checkBox);
//               }
//
//           }
           //region 设置选项




       }

    @Override
    protected void populate() {
      initView();
      adapterVuew();
      gennerateOptions();

        /*displayQuestion();*/
    }
    @SuppressLint("ResourceType")
    private void gennerateOptions(){
        isMulti=question.getTupe()==QuestionType.MULTI_CHOICE;
        List<Option>options=question.getOptions();
        for (Option option:options){
            CompoundButton btm=isMulti?new CheckBox(getContext()):new RadioButton(getContext());
             String content=option.getLabel()+"."+option.getContent();
             if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
                 btm.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
             }
             btm.setText(content);
             btm.setEnabled(!isCommitted);
             btm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     UserCookies.getInstance().changeOptionState(option,isChecked,isMulti);
                 }
             });
            if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                btm.setTextAppearance(R.drawable.on_favorite);
            }
                rgOptions.addView(btm);
            //勾选，到文件中找是否存在该选项id,存在则勾选
            boolean shouldCheck=UserCookies.getInstance().isOptionSeleceted(option);
            if (isMulti){
                btm.setChecked(shouldCheck);
            }else if (shouldCheck){
                rgOptions.check(btm.getId());
            }
            if (isCommitted&&option.isAnswer()){
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    btm.setTextColor(getResources().getColor(R.color.colorAccent,null));
                }else {
                    btm.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        }
    }
    private void displayQuestion(){
        isMulti=question.getTupe()==QuestionType.MULTI_CHOICE;
        int label=pos+1;
        String  gType=label+"."+question.getTupe().toString();
        tvType.setText(question.getContent());
        int starId=FavoriteFactory.getInstance().isQuestionStearred(question.getId().toString())?
               android. R.drawable.star_on: android.R.drawable.star_off;
        imgFavoorite.setImageResource(starId);
        imgFavoorite.setOnClickListener(v -> switchStar());
    }
    private void switchStar(){
        FavoriteFactory factory=FavoriteFactory.getInstance();
        if (factory.isQuestionStearred(question.getId().toString())) {
            factory.cancelStarQuestion(question.getId());
            imgFavoorite.setImageResource(android.R.drawable.star_off);
        }else {
          factory.starQuestion(question.getId());
          imgFavoorite.setImageResource(android.R.drawable.star_on);

    }

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_question;
    }

    @Override
    public void search(String kw) {

    }
}
