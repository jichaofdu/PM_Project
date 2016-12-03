package projectmanager.dada.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import projectmanager.dada.R;
import projectmanager.dada.adapter.ViewMyPublishTaskAdapter;
import projectmanager.dada.model.Task;

public class MyPublishTaskActivity extends AppCompatActivity {

    ListView myPublishListView;
    List<Task> myPublishTaskList;
    ViewMyPublishTaskAdapter myPublishTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish_task);

        buildData();

        myPublishListView = (ListView) findViewById(R.id.my_publish_task_list_view);
        myPublishTaskAdapter = new ViewMyPublishTaskAdapter(MyPublishTaskActivity.this,
                R.layout.my_publish_task_view,myPublishTaskList);
        myPublishListView.setAdapter(myPublishTaskAdapter);
    }

    private void buildData(){
        myPublishTaskList = new ArrayList<>();
        Task task1 = new Task(0,"第一条","第一条的内容",null,null,null,null,null,0,0,null);
        myPublishTaskList.add(task1);
        Task task2 = new Task(0,"第二条","第二条的内容",null,null,null,null,null,0,0,null);
        myPublishTaskList.add(task2);
    }
}
