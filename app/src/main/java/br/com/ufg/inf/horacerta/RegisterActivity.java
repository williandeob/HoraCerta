package br.com.ufg.inf.horacerta;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.UtilConnection;


public class RegisterActivity extends AppCompatActivity {

    private UserRegisterTask mRegisterTask = null;

    // UI references.
    private EditText mUserNameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mEmailView;
    private EditText mNomeView;
    private View mProgressView;
    private View mRegisterFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUserNameView = (EditText)findViewById(R.id.user_name);
        mPasswordView = (EditText)findViewById(R.id.password);
        mConfirmPasswordView = (EditText)findViewById(R.id.confirm_password);
        mEmailView = (EditText)findViewById(R.id.email);
        mNomeView = (EditText)findViewById(R.id.nome);
        mProgressView = findViewById(R.id.register_progress);
        mRegisterFormView = findViewById(R.id.register_form);

        Button mRegister_button = (Button) findViewById(R.id.register_button);
        mRegister_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarUsuario();
            }
        });
    }

    private boolean validarFormularioDeCadastroDeUsuario(String userName, String email, String nome, String password, String confirmPassword) {
        mUserNameView.setError(null);
        mEmailView.setError(null);
        mNomeView.setError(null);
        mPasswordView.setError(null);
        mConfirmPasswordView.setError(null);

        if ("".equals(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            mUserNameView.requestFocus();
            return false;
        } else if ("".equals(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            return false;
        } else if ("".equals(nome)) {
            mNomeView.setError(getString(R.string.error_field_required));
            mNomeView.requestFocus();
            return false;
        } else if ("".equals(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
             mPasswordView.requestFocus();
            return false;
        } else if ("".equals(confirmPassword)) {
            mConfirmPasswordView.setError(getString(R.string.error_field_required));
            mConfirmPasswordView.requestFocus();
            return false;
        } else if(!email.contains("@")){
            mEmailView.setError("Email inválido");
            mEmailView.requestFocus();
            return false;
        } else if(password.length()<4){
            mPasswordView.setError("Password mínimo 04 dígitos");
            mPasswordView.requestFocus();
            return false;
        } else if(!password.equals(confirmPassword)) {
            mConfirmPasswordView.setError("Password não confirmado");
            mConfirmPasswordView.requestFocus();
            return false;
        } else{
            return true;
        }
    }

    private void cadastrarUsuario() {
        if (mRegisterTask != null) {
            return;
        }

        String userName = mUserNameView.getText().toString().trim();
        String email = mEmailView.getText().toString().trim();
        String nome = mNomeView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        String confirmPassword = mConfirmPasswordView.getText().toString().trim();

        if (validarFormularioDeCadastroDeUsuario(userName, email, nome, password, confirmPassword)) {
            showProgress(true);
            mRegisterTask = new UserRegisterTask(userName, email, nome, password);
            mRegisterTask.execute((Void) null);
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, String> {

        private final String mUserName;
        private final String mEmail;
        private final String mNome;
        private final String mPassword;


        UserRegisterTask(String userName, String email ,String nome, String password) {
            mUserName = userName;
            mEmail = email;
            mNome = nome;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            String responseRequest;

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username" ,mUserName);
                jsonObject.put("nome" ,mNome);
                jsonObject.put("email" ,mEmail);
                jsonObject.put("password" ,mPassword);

                responseRequest = UtilConnection.buildRequest("usuario", "POST", jsonObject, getApplicationContext());
            }catch (IOException ex){
                responseRequest = "errorException";
            } catch (JSONException e) {
                responseRequest = "errorException";
            }

            return responseRequest;
        }

        @Override
        protected void onPostExecute(final String responseRequest) {
            mRegisterTask = null;
            showProgress(false);
            Toast toast;

            switch (responseRequest){
                case "errorConnection":
                    toast = Toast.makeText(getApplicationContext(), "Não foi possivel acessar a rede!", Toast.LENGTH_LONG);
                    toast.show();
                    break;
                case "errorAutenticacao" :
                    //TODO implementar saída para usuario inválido
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                    break;
                case "errorResponse":
                    //TODO implementar saída para codigo de resposta diferente de 200 e 401
                    break;
                case "errorException":
                    toast = Toast.makeText(getApplicationContext(), "Ocorreu uma execção durante a requisição!", Toast.LENGTH_LONG);
                    toast.show();
                    break;
                default: //sucesso
                    toast = Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }

}
