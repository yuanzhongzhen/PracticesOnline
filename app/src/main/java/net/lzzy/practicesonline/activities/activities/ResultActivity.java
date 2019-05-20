package net.lzzy.practicesonline.activities.activities;
import android.content.Intent;

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



    @Override
    protected int getLayoutRes() {
        return R.layout.activity_result;
    }

    @Override
    protected int getContainerId() {
        return R.id.activity_result_containe;
    }

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
