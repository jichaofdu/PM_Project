package projectmanager.dada.pages;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import projectmanager.dada.R;
import projectmanager.dada.model.StatusType;
import projectmanager.dada.model.Tag;
import projectmanager.dada.model.TagListView;
import projectmanager.dada.model.Task;
import projectmanager.dada.util.ApiManager;
import projectmanager.dada.util.DataManager;

public class MyAcceptTaskDetailActivity extends Activity {

    private View myAcceptTaskDetailView;
    private View progressView;
    private Task selectedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedTask = DataManager.getInstance().getSelectedMyAcceptTask();
        setContentView(R.layout.activity_my_accept_task_detail);

        myAcceptTaskDetailView = findViewById(R.id.activity_my_accept_task_detail);
        progressView = findViewById(R.id.get_my_accept_task_progress);

        TextView titleView = (TextView)myAcceptTaskDetailView.findViewById(R.id.my_accept_detail_title);
        titleView.setText(selectedTask.getTitle());
        TextView publisher = (TextView)myAcceptTaskDetailView.findViewById(R.id.my_accept_detail_publisher);
        titleView.setText(selectedTask.getPublisher().getUsername());
        TextView publishTimeView = (TextView)myAcceptTaskDetailView.findViewById(R.id.my_accept_detail_publish_time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        publishTimeView.setText(sdf.format(selectedTask.getPublishedTime()));
        TextView deadlineView = (TextView)myAcceptTaskDetailView.findViewById(R.id.my_accept_detail_deadline);
        deadlineView.setText(sdf.format(selectedTask.getDeadline()));
        TextView descriptionView = (TextView)myAcceptTaskDetailView.findViewById(R.id.my_accept_detail_description);
        descriptionView.setText(selectedTask.getDescription());
        TextView statusView = (TextView)myAcceptTaskDetailView.findViewById(R.id.my_accept_detail_status);
        statusView.setText(StatusType.getTypeByStatusId(selectedTask.getStatus()));
        TextView creditView = (TextView)myAcceptTaskDetailView.findViewById(R.id.my_accept_detail_spend_credit);
        creditView.setText("" + selectedTask.getCredit());
        TagListView mTagListView = (TagListView) findViewById(R.id.my_accept_detail_tag_view);
        List<Tag> mTags = new ArrayList<Tag>();
        for (int i = 0; i < selectedTask.getTags().length; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(false);
            tag.setTitle(selectedTask.getTags()[i]);
            mTags.add(tag);
        }
        mTagListView.setTags(mTags);

        Button finishButton = (Button)findViewById(R.id.my_accept_detail_finish_task);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               clickFinishButton();
            }
        });
        if(selectedTask.getStatus() == StatusType.GOINGON.getCode()){
            finishButton.setVisibility(View.VISIBLE);
        }

        Button quitButton = (Button)findViewById(R.id.my_accept_detail_quit_task);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickQuitButton();
            }
        });
        if(selectedTask.getStatus() == StatusType.GOINGON.getCode()
                || selectedTask.getStatus() == StatusType.WAITCONFIRM.getCode()){
            quitButton.setVisibility(View.VISIBLE);
        }
    }

    private void clickFinishButton(){

    }

    private void clickQuitButton(){

    }

    /**
     * 显示进度条
     * @param show 设定进度框显示与否
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            myAcceptTaskDetailView.setVisibility(show ? View.GONE : View.VISIBLE);
            myAcceptTaskDetailView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    myAcceptTaskDetailView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            myAcceptTaskDetailView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 连接服务器完成任务
     */
    public class DoneMyFinishTask extends AsyncTask<Void, Void, Boolean> {
        private String doneResult;
        @Override
        protected Boolean doInBackground(Void... voids) {
            doneResult = "";
            showProgress(true);
            doneResult = ApiManager.getInstance().handleDoneTask(
                    selectedTask.getTaskId(),
                    DataManager.getInstance().getCurrentUser().getUserId());
            if(doneResult != null && doneResult.equals("succeed")){
                return true;
            }else{
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            if(success == true){
                Toast.makeText(MyAcceptTaskDetailActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(MyAcceptTaskDetailActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected void onCancelled() {
            showProgress(false);
            Toast.makeText(MyAcceptTaskDetailActivity.this, "你取消了本次操作", Toast.LENGTH_SHORT).show();
        }
    }




}
