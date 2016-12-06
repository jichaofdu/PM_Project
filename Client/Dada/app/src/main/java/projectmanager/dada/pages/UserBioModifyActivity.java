package projectmanager.dada.pages;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import projectmanager.dada.R;
import projectmanager.dada.model.User;
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
    }
}
