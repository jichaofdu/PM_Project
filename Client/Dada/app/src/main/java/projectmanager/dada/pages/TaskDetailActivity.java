package projectmanager.dada.pages;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import projectmanager.dada.R;
import projectmanager.dada.model.Tag;
import projectmanager.dada.model.TagListView;
import projectmanager.dada.model.TagView;

/**
 * Created by JScarlet on 2016/12/11.
 */
public class TaskDetailActivity extends Activity {
    private TagListView mTagListView;
    private final List<Tag> mTags = new ArrayList<Tag>();
    private final String[] titles = { "快递", "外卖", "紧急" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        mTagListView = (TagListView) findViewById(R.id.task_tag_view);
        mTagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {

                if (tag.isChecked()) {
                    tagView.setBackgroundResource(R.drawable.tag_normal);
                    tagView.setTextColor(getResources().getColor(R.color.blue));
                    tag.setChecked(false);
                }else {
                    tagView.setBackgroundResource(R.drawable.tag_checked_normal);
                    tagView.setTextColor(getResources().getColor(R.color.white));
                    tag.setChecked(true);
                }

            }
        });
        initTags();
        mTagListView.setTags(mTags);
    }

    private void initTags() {
        for (int i = 0; i < titles.length; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(false);
            tag.setTitle(titles[i]);
            mTags.add(tag);
        }
    }
}
