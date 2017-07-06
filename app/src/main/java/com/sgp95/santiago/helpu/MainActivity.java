package com.sgp95.santiago.helpu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private  CommentsFragment commentsFragment;
    private CreateCompleinFragment createCompleinFragment;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ImageView imageuser;
    FirebaseUser user;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if(user == null){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
            }
        };

       /*  */

       //set drawer and navigation
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);

        //get views from header
        View view = navigationView.getHeaderView(0);
        imageuser = (ImageView) view.findViewById(R.id.user_profile_img);
        TextView userCode = (TextView) view.findViewById(R.id.user_code_header);
        TextView userFullName = (TextView) view.findViewById(R.id.user_name_header);
        imageuser.setImageResource(R.drawable.burger);
        userCode.setText(user.getEmail().substring(0,7));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                 switch (item.getItemId()){
                    case R.id.opt_comments:
                        Bundle data = new Bundle();
                        data.putString("userCode", user.getEmail().substring(0,7));
                        commentsFragment = new CommentsFragment();
                        commentsFragment.setArguments(data);
                        showFragment(commentsFragment);
                        //Log.d("MainActivity","Option Selected: "+item.getTitle());
                        break;
                    case R.id.opt_make_complain:
                        createCompleinFragment = new CreateCompleinFragment();
                        showFragment(createCompleinFragment);
                        //Log.d("MainActivity","Option Selected: "+item.getTitle());
                        break;
                    case R.id.opt_history:
                        Log.d("MainActivity","Option Selected: "+item.getTitle());
                        break;
                    case R.id.opt_exit:

                        auth.signOut();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        Bundle data = new Bundle();
        data.putString("userCode", user.getEmail().substring(0,7));
        commentsFragment = new CommentsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        commentsFragment.setArguments(data);
        ft.add(R.id.content, commentsFragment);
        ft.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    public void showFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content,fragment);
        ft.commit();
    }
}
