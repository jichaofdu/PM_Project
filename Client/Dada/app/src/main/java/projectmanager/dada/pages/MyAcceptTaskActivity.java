package projectmanager.dada.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import projectmanager.dada.R;
import projectmanager.dada.adapter.ViewMyAcceptTaskAdapter;
import projectmanager.dada.model.Task;

public class MyAcceptTaskActivity extends AppCompatActivity {

    ListView myAcceptListView;
    List<Task> myAcceptTaskList;
    ViewMyAcceptTaskAdapter myAcceptTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_accept_task);

        buildData();

        myAcceptListView = (ListView) findViewById(R.id.my_accept_task_list_view);
        myAcceptTaskAdapter = new ViewMyAcceptTaskAdapter(MyAcceptTaskActivity.this,
                R.layout.my_accept_task_view,myAcceptTaskList);
        myAcceptListView.setAdapter(myAcceptTaskAdapter);
    }


    private void buildData(){
        myAcceptTaskList = new ArrayList<>();
        Task task1 = new Task(0,"第一条","第一条的内容",null,null,null,null,null,0,0,null);
        myAcceptTaskList.add(task1);
        Task task2 = new Task(0,"第二条","第二条的内容",null,null,null,null,null,0,0,null);
        myAcceptTaskList.add(task2);
    }


}
