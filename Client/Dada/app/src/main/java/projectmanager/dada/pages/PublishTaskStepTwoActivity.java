package projectmanager.dada.pages;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.method.DateTimeKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import projectmanager.dada.R;
import projectmanager.dada.model.Task;
import projectmanager.dada.util.DataManager;

public class PublishTaskStepTwoActivity extends AppCompatActivity {

    private EditText inputTaskTitle;
    private EditText inputTaskContent;
    private Button deadline;
    private EditText inputTaskLocation;
    private EditText inputTaskCredit;
    private Button   stepTwoFinishButton;
    private String dateString;
    private Calendar calendar = Calendar.getInstance();
    private Calendar now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task_step_two);

        inputTaskTitle = (EditText) findViewById(R.id.step_two_input_title);
        inputTaskContent = (EditText) findViewById(R.id.step_two_input_content);
        inputTaskLocation = (EditText) findViewById(R.id.step_two_input_location);
        deadline = (Button) findViewById(R.id.step_two_deadline);
        inputTaskCredit = (EditText) findViewById(R.id.step_two_input_credit);
        stepTwoFinishButton = (Button) findViewById(R.id.step_two_ok_button);
        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now = Calendar.getInstance();
                new DatePickerDialog(PublishTaskStepTwoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, final int y, final int m, final int d) {

                        new TimePickerDialog(PublishTaskStepTwoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int h, int mm) {
                                calendar.set(y, m, d, h ,mm);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                deadline.setText(sdf.format(calendar.getTime()));
                            }
                        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show();
                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        stepTwoFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //// TODO: 2016/11/27
                //   将获取到的用户的输入信息存放在DataManager中，留作后用
                String title = inputTaskTitle.getText().toString();
                String content = inputTaskContent.getText().toString();
                String locationDscp = inputTaskLocation.getText().toString();
                Date date = calendar.getTime();
                View focus = null;
                boolean b = false;
                int credit = 0;
                if (inputTaskCredit.getText() != null) {
                     credit = Integer.parseInt(inputTaskCredit.getText().toString());
                }
                Task task = DataManager.getInstance().getNewTask();
                task.setTitle(title);
                task.setDescription(content);
                task.setDeadline(date);
                task.setCredit(credit);
                task.getLocation().setDescription(locationDscp);
                if (credit > DataManager.getInstance().getCurrentUser().getCredit()) {
                    inputTaskCredit.setError(getString(R.string.error_credit));
                    focus = inputTaskCredit;
                    b = true;
                }
                if (TextUtils.isEmpty(content)){
                    inputTaskContent.setError(getString(R.string.error_field_required));
                    focus = inputTaskContent;
                    b = true;
                }
                if (TextUtils.isEmpty(title)){
                    inputTaskTitle.setError(getString(R.string.error_field_required));
                    focus = inputTaskTitle;
                    b = true;
                }
                if (b) {
                    focus.requestFocus();
                }else {
//                finish();
                    Intent nextPage = new Intent(PublishTaskStepTwoActivity.this, PublishTaskStepThreeActivity.class);
                    startActivity(nextPage);
                }
            }
        });

    }
}