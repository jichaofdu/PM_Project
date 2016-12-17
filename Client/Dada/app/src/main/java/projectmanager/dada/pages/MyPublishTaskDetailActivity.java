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

public class MyPublishTaskDetailActivity extends Activity {

    private View myPublishTaskDetailView;
    private View progressView;
    private Task selectedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedTask = DataManager.getInstance().getSelectedMyPublishTask();
        setContentView(R.layout.activity_my_publish_task_detail);

        myPublishTaskDetailView = findViewById(R.id.activity_my_publish_task_detail);
        progressView = findViewById(R.id.get_my_publish_task_progress);

        TextView titleView = (TextView)myPublishTaskDetailView.findViewById(R.id.my_publish_detail_title);
        titleView.setText(selectedTask.getTitle());
        TextView publishTimeView = (TextView)myPublishTaskDetailView.findViewById(R.id.my_publish_detail_publish_time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        publishTimeView.setText(sdf.format(selectedTask.getPublishedTime()));
        TextView deadlineView = (TextView)myPublishTaskDetailView.findViewById(R.id.my_publish_detail_deadline);
        deadlineView.setText(sdf.format(selectedTask.getDeadline()));
        TextView descriptionView = (TextView)myPublishTaskDetailView.findViewById(R.id.my_publish_detail_description);
        descriptionView.setText(selectedTask.getDescription());
        TextView statusView = (TextView)myPublishTaskDetailView.findViewById(R.id.my_publish_detail_status);
        statusView.setText(StatusType.getTypeByStatusId(selectedTask.getStatus()));
        TextView accepterView = (TextView)myPublishTaskDetailView.findViewById(R.id.my_publish_detail_acceptor);
        if(selectedTask.getAccepter() == null){
            accepterView.setText("尚无人接受任务");
        }else{
            accepterView.setText(selectedTask.getAccepter().getUsername());
        }
        TextView creditView = (TextView)myPublishTaskDetailView.findViewById(R.id.my_publish_detail_spend_credit);
        creditView.setText("" + selectedTask.getCredit());
        TagListView mTagListView = (TagListView) findViewById(R.id.my_publish_detail_tag_view);
        List<Tag> mTags = new ArrayList<Tag>();
        for (int i = 0; i < selectedTask.getTags().length; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(false);
            tag.setTitle(selectedTask.getTags()[i]);
            mTags.add(tag);
        }
        mTagListView.setTags(mTags);

        Button cancelButton = (Button)findViewById(R.id.my_publish_detail_cancel_task);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCancelButton();
            }
        });
        if(selectedTask.getStatus() != StatusType.CLOSED.getCode()){
            cancelButton.setVisibility(View.VISIBLE);
        }

        Button confirmButton = (Button)findViewById(R.id.my_publish_detail_confirm_task);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println();
            }
        });
        if(selectedTask.getStatus() == StatusType.WAITCONFIRM.getCode()){
            confirmButton.setVisibility(View.VISIBLE);
        }

    }

    private void clickCancelButton(){

    }

    private void clickComfirmButton(){

    }




    /**
     * 显示进度条
     * @param show 设定进度框显示与否
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            myPublishTaskDetailView.setVisibility(show ? View.GONE : View.VISIBLE);
            myPublishTaskDetailView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    myPublishTaskDetailView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            myPublishTaskDetailView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * 连接服务器取消任务的线程
     */
    public class CancelMyPublishTask extends AsyncTask<Void, Void, Boolean> {
        private String cancelResult;
        @Override
        protected Boolean doInBackground(Void... voids) {
            cancelResult = "";
            showProgress(true);
            cancelResult = ApiManager.getInstance().handleCancelTask(
                    DataManager.getInstance().getSelectedMyPublishTask().getTaskId(),
                    DataManager.getInstance().getCurrentUser().getUserId());
            if(cancelResult != null && cancelResult.equals("succeed")){
                return true;
            }else{
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            if(success == true){
                Toast.makeText(MyPublishTaskDetailActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(MyPublishTaskDetailActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected void onCancelled() {
            showProgress(false);
            Toast.makeText(MyPublishTaskDetailActivity.this, "你取消了本次操作", Toast.LENGTH_SHORT).show();
        }
    }

}
