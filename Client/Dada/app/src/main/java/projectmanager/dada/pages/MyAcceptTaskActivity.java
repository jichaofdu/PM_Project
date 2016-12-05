package projectmanager.dada.pages;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import projectmanager.dada.R;
import projectmanager.dada.adapter.ViewMyAcceptTaskAdapter;
import projectmanager.dada.model.Task;
import projectmanager.dada.model.User;
import projectmanager.dada.util.ApiManager;
import projectmanager.dada.util.DataManager;

public class MyAcceptTaskActivity extends AppCompatActivity {

    private GetMyAcceptTaskSetTask  getMyAcceptSetTask;
    private View                    progressView;
    private ListView                myAcceptListView;
    private ArrayList<Task>              myAcceptTaskList;
    private ViewMyAcceptTaskAdapter myAcceptTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_accept_task);

        myAcceptTaskList = new ArrayList<>();
        tryGetMyAcceptTasks();

        myAcceptListView = (ListView) findViewById(R.id.my_accept_task_list_view);
        progressView = findViewById(R.id.get_my_accept_task_progress);
        myAcceptTaskAdapter = new ViewMyAcceptTaskAdapter(MyAcceptTaskActivity.this,
                R.layout.my_accept_task_view,myAcceptTaskList);
        myAcceptListView.setAdapter(myAcceptTaskAdapter);
    }

    /**
     * 执行从服务器获取数据的动作
     */
    private void tryGetMyAcceptTasks(){
        showProgress(true);
        getMyAcceptSetTask = new GetMyAcceptTaskSetTask();
        getMyAcceptSetTask.execute((Void) null);
    }


    /**
     * 在用户从服务器获取自己发布的任务的时候
     * @param show 设定进度框显示与否
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            myAcceptListView.setVisibility(show ? View.GONE : View.VISIBLE);
            myAcceptListView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    myAcceptListView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            myAcceptListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * 从服务器获取数据的线程
     */
    public class GetMyAcceptTaskSetTask extends AsyncTask<Void, Void, Boolean> {

        private User nowLoginUser;

        GetMyAcceptTaskSetTask(){
            nowLoginUser = DataManager.getInstance().getCurrentUser();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ApiManager apiManager = new ApiManager();
            ArrayList<Task> acceptList = apiManager.handleGetAcceptTasks(nowLoginUser.getUserId(),
                    0,10);
            if(acceptList == null || acceptList.isEmpty()){
                System.out.println("[Tip] Get My accept task set fail. Empty Set.");
                return false;
            }else{
                myAcceptTaskList = acceptList;
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getMyAcceptSetTask = null;
            showProgress(false);
            if (success == true) {
                //todo 更改显示在图中的数据
            } else {
                //todo  图中不现实任何东西
            }
        }

        @Override
        protected void onCancelled() {
            getMyAcceptSetTask = null;
            showProgress(false);
        }
    }




}
