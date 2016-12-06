package projectmanager.dada.pages;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import projectmanager.dada.R;
import projectmanager.dada.model.User;
import projectmanager.dada.util.DataManager;

/**
 * Created by JScarlet on 2016/12/6.
 */
public class UsernameModifyActivity extends Activity {
    private User currentUser;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.username_modify);
        currentUser = DataManager.getInstance().getCurrentUser();
        editText = (EditText) findViewById(R.id.username_edit);
        if(currentUser != null){
            editText.setText(currentUser.getUsername());
        }

        Button btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editText.getText().toString();
                Toast.makeText(UsernameModifyActivity.this, username, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
