package net.lzzy.practicesonline.activities.activities;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.frageents.ChartFrament;
import net.lzzy.practicesonline.activities.frageents.GridFragment;

import net.lzzy.practicesonline.activities.models.view.QuestionResult;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/5/13.
 * Description:
 */
public class ResultActivity extends BaseActivity implements GridFragment.GridKusteber ,ChartFrament.ChartKusteber{

    public static final String POSITION = "position";
    private List<QuestionResult>results;
    public static final String PRACTICE_ID="practiceId";
    private String practiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        practiceId=getIntent().getStringExtra(QuestionActivity.EXTRA_PRACTICE_ID);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        new AlertDialog.Builder(this)
                .setMessage("返回到哪里？")
                .setPositiveButton("查看收藏", (dialog, which) ->{
                    intent.putExtra(PRACTICE_ID,practiceId);
                    setResult(QuestionActivity.COLLECT_RESULT_CODE,intent);
                    finish();
                })
                .setNegativeButton("章节列表", (dialog, which) -> {
                    intent.setClass(this, PracticesActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNeutralButton("返回题目", (dialog, which) -> {
                    intent.setClass(this, QuestionActivity.class);
                    startActivity(intent);
                    finish();
                }).show();

    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_result;
    }

    @Override
    protected int getContainerId() {
        return R.id.activity_result_containe;
    }
/***/
    @Override
    protected Fragment createFragment() {
    results=getIntent().getParcelableArrayListExtra(QuestionActivity.EXTRA_RESU);
        return GridFragment.Instance(results);
    }


    @Override
    public void gotoChar() {
        getManager().beginTransaction().replace(R.id.activity_result_containe,
                ChartFrament.Instance(results)).commit();

    }

    @Override
    public void gotoActivity(Integer position) {
        Intent intent=new Intent();
        intent.putExtra(POSITION,position);
       setResult(QuestionActivity.RESULT_CODE,intent);
       finish();
    }


    @Override
    public void gotoGird() {
        getManager().beginTransaction().replace(R.id.activity_result_containe,
                GridFragment.Instance(results)).commit();

    }
}
