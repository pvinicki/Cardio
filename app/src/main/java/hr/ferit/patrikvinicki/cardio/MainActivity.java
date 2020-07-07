package hr.ferit.patrikvinicki.cardio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    private Intent intent;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getApplicationContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        //change if so user gets taken to login if not logged in
        int loggedin = prefs.getInt("loggedin", 0);
        if(loggedin == 0){
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
        }

        initializeUI();
    }

    public void initializeUI() {
        Context context = getApplicationContext();
        this.toolbar = findViewById(R.id.mainToolbar);
        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.navView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profileItem:
                        break;

                    case R.id.routinesItem:
                        intent = new Intent(MainActivity.this, RoutinesActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.statisticsItem:
                        intent = new Intent(MainActivity.this, StatsActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.logoutItem:
                        editor = prefs.edit();
                        editor.clear();
                        editor.apply();

                        intent = new Intent(MainActivity.this, login.class);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });
    }
}
