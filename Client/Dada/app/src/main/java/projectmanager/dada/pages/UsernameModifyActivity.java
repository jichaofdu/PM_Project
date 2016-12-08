package projectmanager.dada.pages;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import projectmanager.dada.R;
import projectmanager.dada.model.User;
import projectmanager.dada.util.ApiManager;
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

        Button btnSave = (Button) findViewById(R.id.btn_save_username);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editText.getText().toString();
                Toast.makeText(UsernameModifyActivity.this, username, Toast.LENGTH_SHORT).show();
                UsernameModifyTask usernameModifyTask = new UsernameModifyTask(username);
                usernameModifyTask.execute((Void) null);
            }
        });
    }

    public class UsernameModifyTask extends AsyncTask<Void, Void, Boolean> {
        private final String username;

        private User currentUser;

        UsernameModifyTask(String username) {
            this.username = username;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            currentUser = ApiManager.getInstance().handleUpdateProfile(DataManager.getInstance().getCurrentUser().getUserId(), username, DataManager.getInstance().getCurrentUser().getSex(), DataManager.getInstance().getCurrentUser().getAvatar(), DataManager.getInstance().getCurrentUser().getBio());
            if(currentUser == null){
                System.out.println("[Tip] Login Fail.");
                return false;
            }else{
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success){
                DataManager.getInstance().setCurrentUser(currentUser);
                finish();

            }else {
                Toast.makeText(UsernameModifyActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
        }
    }
}