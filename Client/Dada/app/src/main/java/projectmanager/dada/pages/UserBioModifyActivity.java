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
public class UserBioModifyActivity extends Activity {
    private User currentUser;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_bio_modify);
        editText = (EditText) findViewById(R.id.user_bio_edit);
        currentUser = DataManager.getInstance().getCurrentUser();
        if(currentUser != null){
            editText.setText(currentUser.getBio());
        }

        Button btnSave = (Button) findViewById(R.id.btn_save_bio);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bio = editText.getText().toString();
                UserBioModifyTask usernameModifyTask = new UserBioModifyTask(bio);
                usernameModifyTask.execute((Void) null);
            }
        });
    }

    public class UserBioModifyTask extends AsyncTask<Void, Void, Boolean> {
        private final String bio;

        private User currentUser;

        UserBioModifyTask(String bio) {
            this.bio = bio;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            currentUser = ApiManager.getInstance().handleUpdateProfile(DataManager.getInstance().getCurrentUser().getUserId(), DataManager.getInstance().getCurrentUser().getUsername(), DataManager.getInstance().getCurrentUser().getSex(), DataManager.getInstance().getCurrentUser().getAvatar(), bio);
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
                Toast.makeText(UserBioModifyActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
        }
    }
}