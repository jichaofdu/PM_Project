package projectmanager.dada.pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import projectmanager.dada.R;

public class PublishTaskStepTwoActivity extends AppCompatActivity {

    private EditText inputTaskTitle;
    private EditText inputTaskContent;
    private Button   stepTwoFinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task_step_two);

        inputTaskTitle = (EditText) findViewById(R.id.step_two_input_title);
        inputTaskContent = (EditText) findViewById(R.id.step_two_input_content);
        stepTwoFinishButton = (Button) findViewById(R.id.step_two_ok_button);
        stepTwoFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //// TODO: 2016/11/27
                //   将获取到的用户的输入信息存放在DataManager中，留作后用

//                finish();
                Intent nextPage = new Intent(PublishTaskStepTwoActivity.this,PublishTaskStepThreeActivity.class);
                startActivity(nextPage);
            }
        });

    }
}
