package projectmanager.dada.pages;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import projectmanager.dada.R;
import projectmanager.dada.adapter.ViewMyPublishTaskAdapter;
import projectmanager.dada.model.Task;
import projectmanager.dada.model.User;
import projectmanager.dada.util.ApiManager;
import projectmanager.dada.util.DataManager;

;

public class MyPublishTaskActivity extends AppCompatActivity {

    private GetMyPublishTaskSetTask  getMyPublishSetTask;
    private ListView                 myPublishListView;
    private View                     progressView;
    private ArrayList<Task>          myPublishTaskList;
    private ViewMyPublishTaskAdapter myPublishTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish_task);

        myPublishListView = (ListView) findViewById(R.id.my_publish_task_list_view);
        progressView = findViewById(R.id.get_my_publish_task_progress);

        myPublishTaskList = new ArrayList<>();

        tryGetMyPublishTasks();
    }


    /**
     * ---------------------------------------------------------------------------------------------
     * -------------------------------------向服务器提交伪造的数据-------------------------------------
     */

    /**
     * 执行从服务器获取数据的动作
     */
    private void tryGetMyPublishTasks(){
        showProgress(true);
        getMyPublishSetTask = new GetMyPublishTaskSetTask();
        getMyPublishSetTask.execute((Void) null);
    }

    /**
     * 在用户从服务器获取自己发布的任务的时候
     * @param show 设定进度框显示与否
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            myPublishListView.setVisibility(show ? View.GONE : View.VISIBLE);
            myPublishListView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    myPublishListView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            myPublishListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * 从服务器获取数据的线程
     */
    public class GetMyPublishTaskSetTask extends AsyncTask<Void, Void, Boolean>{
        private User nowLoginUser;
        GetMyPublishTaskSetTask(){
            nowLoginUser = DataManager.getInstance().getCurrentUser();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            showProgress(true);
            ArrayList<Task> acceptList = ApiManager.getInstance().handleGetPublishTasks(nowLoginUser.getUserId(),
                    0,10);
            if(acceptList == null || acceptList.isEmpty()){
                System.out.println("[Tip] Get My publish task set fail. Empty Set.");
                return false;
            }else{
                myPublishTaskList = acceptList;
                System.out.println("[Tip]My Publish Tasks Size: " + myPublishTaskList.size());
                return true;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            getMyPublishSetTask = null;
            showProgress(false);
            if (success == true) {
                myPublishTaskAdapter = new ViewMyPublishTaskAdapter(MyPublishTaskActivity.this,
                        R.layout.my_publish_task_view,myPublishTaskList);
                myPublishListView.setAdapter(myPublishTaskAdapter);
                myPublishListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Task clickTask = myPublishTaskList.get(position);
                        DataManager.getInstance().setSelectedMyPublishTask(clickTask);
                        Intent nextPage = new Intent(MyPublishTaskActivity.this,MyPublishTaskDetailActivity.class);
                        startActivity(nextPage);

                    }
                });

                myPublishTaskAdapter.notifyDataSetChanged();
            }
        }
        @Override
        protected void onCancelled() {
            getMyPublishSetTask = null;
            showProgress(false);
        }
    }




}
