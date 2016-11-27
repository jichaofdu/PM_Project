package projectmanager.dada;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import projectmanager.dada.model.User;
import projectmanager.dada.util.ApiManager;

public class RegisterActivity extends AppCompatActivity{

    private UserRegisterTask registerTask = null;
    private EditText         phoneView;
    private EditText         usernameView;
    private EditText         passwordView;
    private EditText         passwordAgainView;
    private View             progressView;
    private View             registerFormView;
    private Button           registerButton;
    private Button           turnLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_register);
        registerFormView = findViewById(R.id.register_form);
        progressView = findViewById(R.id.register_progress);
        phoneView = (EditText) findViewById(R.id.register_phone);
        usernameView = (EditText) findViewById(R.id.register_username);
        passwordView = (EditText) findViewById(R.id.register_password);
        passwordAgainView = (EditText) findViewById(R.id.register_passwordAgain);
        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        turnLoginButton = (Button) findViewById(R.id.register_turn_login_button);
        turnLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                turnToLoginPage();
            }
        });
    }

    /**
     * 点击“跳转到登录”按钮之后，关闭注册页面并打开登录页面
     */
    private void turnToLoginPage(){
        //1.首先关闭本页面
        //2.打开登录页面
        finish();
        Intent nextPage = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(nextPage);
    }

    /**
     * 在点击“注册”按钮之后，尝试注册的方法。
     */
    private void attemptRegister() {
        if (registerTask != null) {
            return;
        }
        phoneView.setError(null);
        passwordView.setError(null);
        String phone = phoneView.getText().toString();
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordAgain = passwordAgainView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }
        if(password.equals(passwordAgain) == false){
            passwordView.setError(getString(R.string.error_password_again_not_match));
            focusView = passwordAgainView;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            phoneView.setError(getString(R.string.error_field_required));
            focusView = phoneView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            registerTask = new UserRegisterTask(phone, username ,password);
            registerTask.execute((Void) null);
        }
    }

    /**
     * 检测输入框中的密码是否是一个有效格式的密码
     * @param password 用户传入的密码
     * @return         返回密码是否符合用户的要求
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }


    /**
     * 在用户注册线程进行的时候，显示旋转的载入框，表示联网操作进行中
     * @param show 设定进度框显示与否
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            registerFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 本类的主要作用是另起一个线程，实现用户的注册操作
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String phone;
        private final String username;
        private final String password;
        private User registerUser;

        UserRegisterTask(String ph, String un, String pw) {
            phone = ph;
            username = un;
            password = pw;
            registerUser = null;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ApiManager apiManager = new ApiManager();
            registerUser = apiManager.handleRegister(phone,username,password);
            if(registerUser == null){
                System.out.println("[Tip] Register Fail.");
                return false;
            }else{
                System.out.println("[Tip] Register Success. User Id is:" + registerUser.getUserId());
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            registerTask = null;
            showProgress(false);
            if (success == true) {
                //1.结束本页面
                //2.将当前登录的User对象的保存下来
                //3.并跳转到主页面去
                finish();
                Intent nextPage = new Intent(RegisterActivity.this,MainActivity.class);
                nextPage.putExtra("user",registerUser);
                startActivity(nextPage);
            } else {
                //1.登陆失败的提示信息
                phoneView.setError(getString(R.string.error_incorrect_password));
                //2.登陆失败，将焦点放在密码框焦点上
                passwordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            registerTask = null;
            showProgress(false);
        }
    }
}

