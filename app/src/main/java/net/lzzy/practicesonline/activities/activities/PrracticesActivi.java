package net.lzzy.practicesonline.activities.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import net.lzzy.practicesonline.R;

import net.lzzy.practicesonline.activities.frageents.PracticesFrangmnt;
import net.lzzy.practicesonline.activities.utils.ViewUtils;

/**
 *
 * @author lzzy_gxy
 * @date 2019/4/16
 * Description:
 */
public class PrracticesActivi extends BaseActivity implements PracticesFrangmnt.OnPracticeListener{

    public static final String EXTRA_PRACTICE_ID = "extrapracticeid";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SearchView search=findViewById(R.id.bar_title_search);
        search.setQueryHint("请输入关键词搜索");
        //todo:在fragment中实现搜索
        search.setOnQueryTextListener(new ViewUtils.AbstractQueyListener() {
            @Override
            public void handleQuery(String kw) {
                ((PracticesFrangmnt)getFragment()).search(kw);
            }
        });
        SearchView.SearchAutoComplete auto=search.findViewById(R.id.search_src_text);
        auto.setHintTextColor(Color.WHITE);
        auto.setTextColor(Color.WHITE);
        ImageView icon=search.findViewById(R.id.search_button);
        ImageView icX=search.findViewById(R.id.search_close_btn);
        ImageView icG=search.findViewById(R.id.search_go_btn);
        icon.setColorFilter(Color.WHITE);
        icX.setColorFilter(Color.WHITE);
        icG.setColorFilter(Color.WHITE);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_practices;
    }

    @Override
    protected int getContainerId() {
        return R.id.activity_practices_container;
    }

    @Override
    protected Fragment createFragment() {
        return new PracticesFrangmnt();
    }

    @Override
    public void OnPractice(String practiceId) {
        Intent intent=new Intent(PrracticesActivi.this,QuestionActivity.class);
        intent.putExtra(EXTRA_PRACTICE_ID,practiceId);
        startActivity(intent);
    }
}
