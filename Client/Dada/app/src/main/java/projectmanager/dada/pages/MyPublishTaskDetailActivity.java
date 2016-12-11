package projectmanager.dada.pages;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import projectmanager.dada.R;
import projectmanager.dada.model.Task;
import projectmanager.dada.util.ApiManager;
import projectmanager.dada.util.DataManager;

public class MyPublishTaskDetailActivity extends AppCompatActivity {

    private Task thisSelectTask;
    private View myPublishTaskDetailView;
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish_task_detail);
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
                    thisSelectTask.getTaskId(),
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
