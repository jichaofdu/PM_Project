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

public class MyAcceptTaskDetail extends AppCompatActivity {

    private Task thisSelectTask;
    private View myAcceptTaskDetailView;
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_accept_task_detail);
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
    public class DoneMyAcceptTask extends AsyncTask<Void, Void, Boolean> {
        private String doneResult;
        @Override
        protected Boolean doInBackground(Void... voids) {
            doneResult = "";
            showProgress(true);
            doneResult = ApiManager.getInstance().handleDoneTask(
                    thisSelectTask.getTaskId(),
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
                Toast.makeText(MyAcceptTaskDetail.this, "操作成功", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(MyAcceptTaskDetail.this, "操作失败", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected void onCancelled() {
            showProgress(false);
            Toast.makeText(MyAcceptTaskDetail.this, "你取消了本次操作", Toast.LENGTH_SHORT).show();
        }
    }




}
