package com.example.aap.bplfantasyleague.control;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.example.aap.bplfantasyleague.R;
import com.example.aap.bplfantasyleague.adapter.PagerAdapter;
import com.example.aap.bplfantasyleague.fragment.Tab1;
import com.example.aap.bplfantasyleague.fragment.Tab2;
import com.example.aap.bplfantasyleague.fragment.Tab3;
import com.example.aap.bplfantasyleague.fragment.Tab4;
import com.example.aap.bplfantasyleague.fragment.Tab5;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        Tab1.OnFragmentInteractionListener,
        Tab2.OnFragmentInteractionListener,
        Tab3.OnFragmentInteractionListener,Tab4.OnFragmentInteractionListener,
        Tab5.OnFragmentInteractionListener{


    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tableyout;
    String userId;
    FirebaseUser user;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        if(user!=null){

            userId = getIntent().getStringExtra("userId");
            Toast.makeText(this,"USER ID : "+userId,Toast.LENGTH_SHORT).show();
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_main);
            toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.navigation_actionbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            getSupportActionBar().setTitle(R.string.myteam);
            tableyout = (TabLayout)findViewById(R.id.tab_layout);
            final int[]tabIcons={
                    R.drawable.ic_myteam_black_24dp,
                    R.drawable.ic_points_black_24dp,
                    R.drawable.ic_transfer_arrows_black_24dp,
                    R.drawable.ic_fixtures_black_24dp,
                    R.drawable.ic_finance_black_24dp
            };
            for(int i=0;i<5;i++){
                TabLayout.Tab tab = tableyout.newTab();
                tab.setIcon(tabIcons[i]);
                tableyout.addTab(tab);
            }

            tableyout.setTabGravity(TabLayout.GRAVITY_FILL);
            viewPager =(ViewPager)findViewById(R.id.pager);
            final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tableyout.getTabCount(),userId);
            viewPager.setAdapter(pagerAdapter);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableyout));
            tableyout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position =tab.getPosition();
                    switch (position){
                        case 0:
                            getSupportActionBar().setTitle(R.string.myteam);
                            break;
                        case 1:
                            getSupportActionBar().setTitle(R.string.points);
                            break;
                        case 2:
                            getSupportActionBar().setTitle(R.string.transfers);
                            break;
                        case 3:
                            getSupportActionBar().setTitle(R.string.fixtures);
                            break;
                        case 4:
                            getSupportActionBar().setTitle(R.string.finance);
                            break;
                    }

                    viewPager.setCurrentItem(position);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            navigationView =  findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_extended_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //Intent intent = new Intent(MainActivity.this,SelectSquadActivity.class);
            //startActivity(intent);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.myteam:
                viewPager.setCurrentItem(0);
                getSupportActionBar().setTitle(R.string.myteam);
                break;
            case R.id.profile_points:
                viewPager.setCurrentItem(1);
                getSupportActionBar().setTitle(R.string.points);
                break;
            case R.id.transfers:
                viewPager.setCurrentItem(2);
                getSupportActionBar().setTitle(R.string.transfers);
                break;
            case R.id.fixtures:
                viewPager.setCurrentItem(3);
                getSupportActionBar().setTitle(R.string.fixtures);
                break;
            case R.id.finance:
                viewPager.setCurrentItem(4);
                getSupportActionBar().setTitle(R.string.finance);
                break;
            case R.id.settings:
                Toast.makeText(MainActivity.this,"Selected TItem : "+item.getTitle(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                Toast.makeText(MainActivity.this,"Selected TItem : "+item.getTitle(),Toast.LENGTH_SHORT).show();
                mAuth.getInstance().signOut();
                Intent intent2= new Intent(MainActivity.this,EmailPasswordActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onStart() {
        super.onStart();

        user = mAuth.getCurrentUser();
        if(user==null){
            Intent startIntent = new Intent(MainActivity.this,EmailPasswordActivity.class);
            startActivity(startIntent);
            finish();
        }

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putAll(savedInstanceState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }
}
