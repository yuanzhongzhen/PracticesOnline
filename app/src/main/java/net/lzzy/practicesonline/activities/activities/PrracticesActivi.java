package net.lzzy.practicesonline.activities.activities;

import androidx.fragment.app.Fragment;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.frageents.PracticesFrangmnt;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public class PrracticesActivi extends BaseActivity {
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
        return new PracticesFrangmnt();
    }
}
