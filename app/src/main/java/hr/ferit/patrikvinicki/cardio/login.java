package hr.ferit.patrikvinicki.cardio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class login extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button btnLogin;
    private TextView linkRegister;
    private DBhandler db;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getApplicationContext().getSharedPreferences("session", MODE_PRIVATE);
        db = new DBhandler(this, "cardio.db", null, 3);
        Toast.makeText(getApplicationContext(), "loggedin: " + prefs.getInt("loggedin", 0) + "username is: " + prefs.getString("username", "null") , Toast.LENGTH_LONG ).show();
        initializeUI();
    }

    public void initializeUI(){
        username   = (EditText) findViewById(R.id.edEmail);
        password      = (EditText) findViewById(R.id.edPwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        linkRegister = (TextView) findViewById(R.id.tvLinkRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });
    }

    void validateData(){
        if(isEmpty(username)){
            username.setError("Invalid username");
            return;
        }

        if(isEmpty(password)){
            password.setError("Invalid password");
            return;
        }

        if(db.authenticate(createUser())){
            //log the user in
            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG ).show();
            editor = prefs.edit();
            editor.putInt("loggedin", 1);
            editor.putString("username", username.getText().toString());
            editor.apply();

            intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG ).show();
        }
    }

    boolean isEmpty(EditText text){
        CharSequence temp = text.getText().toString();
        return TextUtils.isEmpty(temp);
    }

    public user createUser(){
        String usr = username.getText().toString();
        String pwd = password.getText().toString();

        user user = new user(usr, pwd);
        return user;
    }
}
