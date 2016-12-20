package projectmanager.dada.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import projectmanager.dada.R;
import projectmanager.dada.model.Tag;
import projectmanager.dada.model.TagListView;
import projectmanager.dada.model.Task;
import projectmanager.dada.util.ApiManager;
import projectmanager.dada.util.DataManager;

/**
 * Created by JScarlet on 2016/12/11.
 */
public class TaskDetailActivity extends AppCompatActivity {
    private TagListView mTagListView;

    private final String[] titles = { "快递", "外卖", "紧急" };
    private TextView taskTitle;
    private TextView taskPublisher;
    private TextView taskPublishTime;
    private TextView taskDeadline;
    private TextView taskDescription;
    private TextView taskStatus;
    private TextView taskAcceptor;
    private TextView taskCredit;
    private Button btnAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        final Task task = (Task) getIntent().getSerializableExtra("task");
        Toast.makeText(this, task.getTitle(), Toast.LENGTH_SHORT).show();

        taskTitle = (TextView) findViewById(R.id.task_title);
        taskPublisher = (TextView) findViewById(R.id.task_publisher);
        taskPublishTime = (TextView) findViewById(R.id.task_publish_time);
        taskDeadline = (TextView) findViewById(R.id.task_deadline);
        taskDescription = (TextView) findViewById(R.id.task_description);
        taskStatus = (TextView) findViewById(R.id.task_status);
        taskAcceptor = (TextView) findViewById(R.id.task_acceptor);
        taskCredit = (TextView) findViewById(R.id.task_credit);

        btnAccept = (Button) findViewById(R.id.btn_accept_task);

        taskTitle.setText(task.getTitle());
        taskPublisher.setText(task.getPublisher().getUsername());
        taskPublishTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(task.getPublishedTime()));
        taskDeadline.setText(new SimpleDateFormat("yyyy-MM-dd").format(task.getDeadline()));
        taskDescription.setText(task.getDescription());
        if(task.getStatus() == 1){
            taskStatus.setText("待接受");
        }else if(task.getStatus() == 2){
            taskStatus.setText("进行中");
            btnAccept.setText("进行中");
            btnAccept.setEnabled(false);
        }else if(task.getStatus() == 3){
            taskStatus.setText("已完成");
            btnAccept.setText("已完成");
            btnAccept.setEnabled(false);
        }else{
            taskStatus.setText("已取消");
            btnAccept.setEnabled(false);
        }
        if(task.getAccepter() == null){
            taskAcceptor.setText("无");
        }else {
            taskAcceptor.setText(task.getAccepter().getUsername());
        }

        taskCredit.setText(task.getCredit() + "");


        mTagListView = (TagListView) findViewById(R.id.task_tag_view);
        /*mTagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
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
        });*/
        mTagListView.setTags(initTags(task.getTags()));

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(TaskDetailActivity.this)
                        .setTitle("系统提示")
                        .setMessage("是否要接受任务？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(task.getPublisher().getUserId() == DataManager.getInstance().getCurrentUser().getUserId()){
                                    new AlertDialog.Builder(TaskDetailActivity.this)
                                            .setTitle("warning")
                                            .setMessage("亲，这是您自己发布的任务，需要别人来完成哦~")
                                            .show();
                                }else {
                                    AcceptTask acceptTask = new AcceptTask(task.getTaskId(), DataManager.getInstance().getCurrentUser().getUserId());
                                    acceptTask.execute((Void)null);
                                }

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });
    }


    private List<Tag> initTags(String[] tags) {
        List<Tag> mTags = new ArrayList<Tag>();
        for (int i = 0; i < tags.length; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(false);
            tag.setTitle(tags[i]);
            mTags.add(tag);
        }
        return mTags;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class AcceptTask extends AsyncTask<Void, Void, Boolean> {
        private int taskId;
        private int userId;
        private Task task;

        AcceptTask(int taskId, int userId) {
            this.taskId = taskId;
            this.userId = userId;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String status = ApiManager.getInstance().handleAcceptTask(taskId, userId);
            if(status.equals("succeed")){
                task = ApiManager.getInstance().handleGetTaskById(taskId);
                return true;
            }else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success && task != null){
                taskTitle.setText(task.getTitle());
                taskPublisher.setText(task.getPublisher().getUsername());
                taskPublishTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(task.getPublishedTime()));
                taskDeadline.setText(new SimpleDateFormat("yyyy-MM-dd").format(task.getDeadline()));
                taskDescription.setText(task.getDescription());
                taskStatus.setText(task.getStatus() + "");
                if(task.getAccepter() == null){
                    taskAcceptor.setText("无");
                }else {
                    taskAcceptor.setText(task.getAccepter().getUsername());
                }

                taskCredit.setText(task.getCredit() + "");
                btnAccept.setText("已接受");
                btnAccept.setEnabled(false);
                List<Task> taskList = DataManager.getInstance().getNearbyList();
                for(Task temp : taskList){
                    if(temp.getTaskId() == task.getTaskId()){
                        taskList.set(DataManager.getInstance().getNearbyList().indexOf(temp), task);
                        DataManager.getInstance().setNearbyList(taskList);
                    }
                }

            }else {
                Toast.makeText(TaskDetailActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
        }
    }
}
