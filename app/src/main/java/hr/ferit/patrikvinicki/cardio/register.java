package hr.ferit.patrikvinicki.cardio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class register extends AppCompatActivity {
    private EditText username;
    private EditText email;
    private EditText password;
    private Button   register;
    private TextView linkLogin;
    private DBhandler db;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    private Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        prefs = getApplicationContext().getSharedPreferences("session", MODE_PRIVATE);
        db = new DBhandler(this, "cardio.db", null, 3);
        initializeUI();
    }

    public void initializeUI(){
        username = (EditText) findViewById(R.id.edUsernameRegister);
        email    = (EditText) findViewById(R.id.edEmailRegister);
        password = (EditText) findViewById(R.id.edPwdRegister);
        register = (Button) findViewById(R.id.btnRegister);
        linkLogin = (TextView) findViewById(R.id.tvLinkLogin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
            }
        });
    }

    void validateData(){
        if(isEmpty(username) || usernameLength(username)){
            username.setError("Invalid username");
            return;
        }

        if(!isEmail(email)){
            email.setError("Invalid email");
            return;
        }

        if(isEmpty(password)){
            password.setError("Invalid password");
            return;
        }

        if(db.addHandler(createUser())){
            editor = prefs.edit();
            editor.putInt("loggedin", 1);
            editor.putString("username", username.getText().toString());
            editor.apply();

            intent = new Intent(register.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Registered " + "loggedin: " + prefs.getInt("loggedin", 0) + "username is: " + prefs.getString("username", "null"), Toast.LENGTH_LONG ).show();
        } else {
            Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_LONG ).show();
        }
    }

    boolean isEmpty(EditText text){
        CharSequence temp = text.getText().toString();
        return TextUtils.isEmpty(temp);
    }

    boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean usernameLength(EditText text){
        CharSequence username = text.getText().toString();
        if(username.length() < 4){
            Toast.makeText(getApplicationContext(), "username must contain atleast 4 characters!", Toast.LENGTH_LONG ).show();
        }
        return(username.length() < 4);
    }

    public user createUser(){
        String usr = username.getText().toString();
        String mail = email.getText().toString();
        String pwd = password.getText().toString();

        user user = new user(usr, mail, pwd);
        return user;
    }
}
