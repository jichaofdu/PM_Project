package projectmanager.dada.pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import projectmanager.dada.R;

public class PublishTaskStepOneActivity extends AppCompatActivity {

    private Button stepOneFinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task_step_one);

        stepOneFinishButton = (Button)findViewById(R.id.first_step_finish_button);

        stepOneFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //// TODO: 2016/11/27
                //  将从地图上选到的地址信息，存放在DataManager中。留作后用。
                //然后跳转到第二步的页面中


                finish();
                Intent nextPage = new Intent(PublishTaskStepOneActivity.this,PublishTaskStepTwoActivity.class);
                startActivity(nextPage);
            }
        });

    }
}
