package net.lzzy.practicesonline.activities.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;


import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.frageents.PracticesFrangmnt;
import net.lzzy.practicesonline.activities.models.Practice;
import net.lzzy.practicesonline.activities.utils.ViewUtils;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public class PrracticesActivi extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SearchView search = findViewById(R.id.bar_title_search);
        search.setQueryHint("输出关键搜索词");
         search.setOnQueryTextListener(new ViewUtils.AbstractQueyListener() {
             @Override
             public void handleQuery(String kw) {
                 ((PracticesFrangmnt)getFragment()).search(kw);
             }
         });
        SearchView.SearchAutoComplete auto = search.findViewById(R.id.search_src_text);
        auto.setHintTextColor(Color.WHITE);
        auto.setTextColor(Color.WHITE);
        ImageView icon = search.findViewById(R.id.search_button);
        ImageView icX = search.findViewById(R.id.search_close_btn);
        ImageView igG = search.findViewById(R.id.search_go_btn);
        icon.setColorFilter(Color.WHITE);
        igG.setColorFilter(Color.WHITE);
        icX.setColorFilter(Color.WHITE);
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
    protected Fragment createFragent() {
        return null;
    }

}