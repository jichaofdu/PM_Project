package projectmanager.dada.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import projectmanager.dada.R;

public class PublishTaskStepThreeActivity extends AppCompatActivity {

    private Button stepThreeFinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task_step_three);

        stepThreeFinishButton = (Button) findViewById(R.id.step_three_ok_button);
        stepThreeFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //// TODO: 2016/11/27
                //   将用户的输入进行联网操作。
                //   然后成功后再返回到主页面去


                finish();
            }
        });

    }
}
