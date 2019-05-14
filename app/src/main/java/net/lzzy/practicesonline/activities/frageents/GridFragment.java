package net.lzzy.practicesonline.activities.frageents;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/5/13.
 * Description:
 */
public class GridFragment extends BeseFargment {
    private static  final String EXTRA_PRACTICE_ID="extraPracticeId";
    private List<QuestionResult>questionResult;


        public static GridFragment Instance(List<QuestionResult>questionResults){
            Bundle bundle= new Bundle();
            GridFragment gridFragment=new GridFragment();
             bundle.putParcelableArrayList(EXTRA_PRACTICE_ID, (ArrayList<?extends Parcelable>)questionResults);
            gridFragment.setArguments(bundle);
             return gridFragment;

        }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            questionResult=getArguments().getParcelableArrayList(EXTRA_PRACTICE_ID);
        }
    }

    @Override
    protected void populate() {
        //定义适配器
        GridView gridView=find(R.id.fragement_grid_count_grid);
        int width=getActivity().getWindowManager().getDefaultDisplay().getWidth()/5;
        BaseAdapter baseAdapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return questionResult.size();
            }

            @Override
            public Object getItem(int position) {
                return questionResult.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
               if (convertView ==null){
                   convertView= LayoutInflater.from(getContext()).inflate(R.layout.fragment_grid_item,null);


               }
               TextView textView=convertView.findViewById(R.id.fragement_grid_item_tv);
               QuestionResult rsult=questionResult.get(position);
               if (rsult.isRight()){
                   textView.setBackgroundResource(R.drawable.bg_circular_green);
               }else {
                   textView.setBackgroundResource(R.drawable.bg_circular_red);
               }
               textView.setText(position+1+"");
               textView.setTag(position);
               textView.setHeight(width);

                return convertView;
            }
        };
        gridView.setAdapter(baseAdapter);
    }

    @Override
    public int getLayoutRes() {
            return R.layout.fragement_grid;
    }

    @Override
    public void search(String kw) {

    }
}
