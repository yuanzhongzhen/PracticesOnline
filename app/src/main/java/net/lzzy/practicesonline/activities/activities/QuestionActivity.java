package net.lzzy.practicesonline.activities.activities;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.frageents.QuestionFragment;
import net.lzzy.practicesonline.activities.models.Question;
import net.lzzy.practicesonline.activities.models.QuestionFactory;
import net.lzzy.practicesonline.activities.models.UserCookies;
import net.lzzy.practicesonline.activities.models.view.PracticeResult;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;
import net.lzzy.practicesonline.activities.nework.PracticeService;
import net.lzzy.practicesonline.activities.utils.AbstractStatiHhandler;
import net.lzzy.practicesonline.activities.utils.AppUtils;
import net.lzzy.practicesonline.activities.utils.ViewUtils;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class QuestionActivity extends AppCompatActivity {
    public static final int RESULT_CODE = 3;
    public static  final String EXTRA_PRACTICE_ID="extraPracticeId";
    public static final String EXTRA_RESU="extraResult";
    public static final int REQUEST_CODE_RESULT=0;
  private String practiceId;
  private int apiId;
  private List<Question>questions;
  private TextView tvView;
  private TextView tvCommit;
  private ViewPager pager;
  private TextView tvHint;
  private  boolean isCommitted=false;
  private FragmentStatePagerAdapter adapter;
  private LinearLayout container;
  private int pos;
  private View[] dost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_question);
        AppUtils.addActivity(this);
        retrieveDate();
        initViews();
        initDots();
        setListteners();
        pos = UserCookies.getInstance().getCurrentQuestion(practiceId);
        pager.setCurrentItem(pos);
        refreshDots(pos);
        UserCookies.getInstance().updateReadCount(questions.get(pos).getId().toString());

    }

    private void setListteners() {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                refreshDots(position);
                UserCookies.getInstance().upadteCurrentQuestion(practiceId,position);
                UserCookies.getInstance().updateReadCount(questions.get(position).getId().toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tvCommit.setOnClickListener(v -> commitPractice());
        tvView.setOnClickListener(v -> redirect());
    }
    /**
     * 数据回传
     * **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_RESULT&&resultCode==RESULT_CODE){
            int pos=data.getIntExtra(ResultActivity.POSITION,-1);
            if (pos>=0){
                pager.setCurrentItem(pos);
            }
        }
    }

    private void redirect() {
        List<QuestionResult> results=UserCookies.getInstance().getResultFromCookies(questions);
        Intent intent=new Intent(this,ResultActivity.class);
        intent.putExtra(EXTRA_PRACTICE_ID,practiceId);
        intent.putParcelableArrayListExtra(EXTRA_RESU, (ArrayList<? extends Parcelable>) results);
        startActivityForResult(intent,REQUEST_CODE_RESULT);
    }
    String info;
    private void commitPractice() {
        List<QuestionResult>results=UserCookies.getInstance().getResultFromCookies(questions);
        List<String>macs=AppUtils.getmACaddress();
        String[] items=new String[macs.size()];
        macs.toArray(items);
       info=items[0];
        new AlertDialog.Builder(this)
                .setTitle("选择地址")
                .setSingleChoiceItems(items,0,(dialog, which) -> info=items[which])
                .setNegativeButton("取消",null)
                 .setPositiveButton("提交",(dialog, which) -> {
                     PracticeResult result=new PracticeResult(results,apiId,"袁中震"+info);
                      postResult(result);
                 }).show();

    }

    private void postResult(PracticeResult result) {
        //todo:启动线程提交数据
        AppUtils.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int r= PracticeService.poseResult(result);
                    if (r>=200&&r<=220){
                        handler.sendEmptyMessage(1);
                    }else {
                        handler.sendEmptyMessage(2);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }

    private AbstractStatiHhandler<QuestionActivity> handler=new AbstractStatiHhandler<QuestionActivity>(this) {
        @Override
        public void handleMessage(Message msg, QuestionActivity questionActivity) {
            switch (msg.what){
                case 1:
                    questionActivity.isCommitted=true;
                    Toast.makeText(questionActivity,"提交成功",Toast.LENGTH_LONG).show();
                   UserCookies.getInstance().commitPrantice(practiceId);

                    break;
                case 2:
                    Toast.makeText(questionActivity,"提交失败",Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void initDots() {
        int count=questions.size();
        dost = new View[count];
        container = findViewById(R.id.activitu_question_dots);
        container.removeAllViews();
        int px= ViewUtils.dp2px(16,this);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(px,px);
        px=ViewUtils.dp2px(5,this);
        params.setMargins(px,px,px,px);
        for (int i=0;i<count;i++){
            TextView tvDot=new TextView(this);
            tvDot.setLayoutParams(params);
            tvDot.setBackgroundResource(R.drawable.dot_style);
            tvDot.setTag(i);
            //todo:tvDot添加点击监听
            tvDot.setOnClickListener(v -> pager.setCurrentItem((Integer) v.getTag()));
            container.addView(tvDot);
            dost[i]=tvDot;
        }
    }

    private void refreshDots(int pos){
        for (int i= 0;i<dost.length;i++){
            int drawable=i==pos?R.drawable.dot_fill_style:R.drawable.dot_style;
            dost[i].setBackgroundResource(drawable);
        }
    }
    private void initViews() {
   tvView=findViewById(R.id.activity_question_tv_view);
   tvCommit=findViewById(R.id.activity_question_tv_commit);
   tvHint=findViewById(R.id.activitu_question_hint);
   pager=findViewById(R.id.activitu_question_pager);
   isCommitted=UserCookies.getInstance().isPracticeCommitted(practiceId);
   if (isCommitted){
       tvCommit.setVisibility(View.GONE);
       tvView.setVisibility(View.VISIBLE);
       tvHint.setVisibility(View.VISIBLE);
   }else {
       tvView.setVisibility(View.GONE);
       tvCommit.setVisibility(View.VISIBLE);
       tvHint.setVisibility(View.GONE);
   }
         adapter =new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Question question=questions.get(position);
                return QuestionFragment.newInstance(question.getId().toString(),position,isCommitted);
            }

            @Override
            public int getCount() {
                return questions.size();
            }
        };
   pager.setAdapter(adapter);
    }

    private void retrieveDate() {
        practiceId = getIntent().getStringExtra(PracticesActivity.EXTRA_PRACTICE_ID);
        apiId = getIntent().getIntExtra(PracticesActivity.EXTRA_API_ID,-1);
        questions= QuestionFactory.getInstance().getByPractices(practiceId);
        isCommitted=UserCookies.getInstance().isPracticeCommitted(practiceId);
        if (apiId<0||questions==null||questions.size()==0){
            Toast.makeText(this, "no questions", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.setRunning(getLocalClassName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppUtils.setStopped(getLocalClassName());
    }
}
