package projectmanager.dada.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import projectmanager.dada.MainActivity;
import projectmanager.dada.R;
import projectmanager.dada.model.Tag;
import projectmanager.dada.model.TagListView;
import projectmanager.dada.model.TagView;
import projectmanager.dada.model.Task;
import projectmanager.dada.model.User;
import projectmanager.dada.util.ApiManager;
import projectmanager.dada.util.DataManager;

public class PublishTaskStepThreeActivity extends Activity {

    private Button stepThreeFinishButton;
    private Button addTagButton;
    private EditText inputTag;
    private TagListView mTagListView;
    private final List<Tag> mTags = new ArrayList<Tag>();
    private final String[] titles = { "快递", "外卖", "紧急" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task_step_three);
        mTagListView = (TagListView) findViewById(R.id.tagView);
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

        addTagButton = (Button) findViewById(R.id.add_tag_button);
        inputTag = (EditText) findViewById(R.id.input_tag);
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = inputTag.getText().toString();
                if (TextUtils.isEmpty(tag)){
                    inputTag.setError(getString(R.string.error_field_required));
                }else {
                    Tag t = new Tag();
                    t.setId(mTags.size());
                    t.setChecked(false);
                    t.setTitle(tag);
                    mTags.add(t);
                    mTagListView.setTags(mTags);
                }
            }
        });

        stepThreeFinishButton = (Button) findViewById(R.id.step_three_ok_button);
        stepThreeFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //// TODO: 2016/11/27
                //   将用户的输入进行联网操作。
                //   然后成功后再返回到主页面去
                Task task = DataManager.getInstance().getNewTask();
                task.setPublisher(DataManager.getInstance().getCurrentUser());
                String[] s = new String[mTagListView.getCheckedTags().size()];
                for (int i = 0; i < s.length; i++) {
                    s[i] = mTagListView.getCheckedTags().get(i).getTitle();
                }
                task.setTags(s);
                task.getLocation().setDescription("here");

                PublishTask publishTask = new PublishTask(task);
                publishTask.execute((Void) null);
            }
        });

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

    public class PublishTask extends AsyncTask<Void, Void, Boolean> {

        private Task task;

        PublishTask(Task task) {
            this.task = task;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ApiManager apiManager = new ApiManager();
            Task t = apiManager.handlePublishTask(task);
            if(t == null){
                System.out.println("[Tip] Publish New Task Fail.");
                return false;
            }else{
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(PublishTaskStepThreeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"Publish New Task Fail",
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
