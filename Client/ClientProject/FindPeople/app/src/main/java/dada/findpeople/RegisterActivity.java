package dada.findpeople;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    private UserRegisterTask authTask;
    private EditText emailView;
    private EditText nicknameView;
    private EditText passwordView;
    private EditText passwordAgainView;
    private View progressView;
    private View loginFormView;
    private Button emailRegisterButton;
    private Button turnToLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailView = (EditText) findViewById(R.id.email_register);
        nicknameView = (EditText) findViewById(R.id.nickname_register);
        passwordView = (EditText) findViewById(R.id.password_register);
        passwordAgainView = (EditText)findViewById(R.id.passwordAgain_register);
        emailRegisterButton = (Button) findViewById(R.id.email_register_button);
        emailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        turnToLoginButton = (Button) findViewById(R.id.email_turn_to_login_button);
        turnToLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToLoginPage();
            }
        });
        loginFormView = findViewById(R.id.register_form);
        progressView = findViewById(R.id.register_progress);
    }

    private void attemptRegister() {
        if (authTask != null) {
            return;
        }
        emailView.setError(null);
        nicknameView.setError(null);
        passwordView.setError(null);
        passwordAgainView.setError(null);
        String email = emailView.getText().toString();
        String nickname = nicknameView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordAgain = passwordAgainView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (!isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }
        if(!isPasswordAgainValid(password,passwordAgain)){
            passwordAgainView.setError("您的两次密码输入不相同");
            focusView = passwordAgainView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }
        if(!isNicknameValid(nickname)){
            nicknameView.setError("您的昵称不符合规范");
            focusView = nicknameView;
            cancel = true;
        }
        if (cancel == true) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            authTask = new UserRegisterTask(email,nickname, password);
            authTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isNicknameValid(String nickname){
        if(nickname != null && nickname.length() > 0) {
            return true;
        }else{
            return false;
        }
    }

    private boolean isPasswordValid(String password) {
        if(password != null && password.length() >= 6){
            return true;
        }else{
            return false;
        }
    }

    private boolean isPasswordAgainValid(String password,String passwordAgain){
        return (passwordAgain.length() >=6 && password.equals(passwordAgain));
    }

    private void turnToLoginPage(){
        System.out.println("[Tip]点击了按钮，准备跳转到登录页面");
        Intent intent = new Intent();
        intent.setClass(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void turnToMainActivity(){
        System.out.println("[Tip]从LoginActivity进入到MainActivity");
        Intent intent = new Intent();
        intent.setClass(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String nickname;
        private final String password;

        UserRegisterTask(String em,String nc, String pw) {
            email = em;
            password = pw;
            nickname = nc;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            authTask = null;
            showProgress(false);
            if (success == true) {
                turnToMainActivity();
            } else {
                passwordView.setError(getString(R.string.error_incorrect_register));
                passwordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            authTask = null;
            showProgress(false);
        }
    }
}

