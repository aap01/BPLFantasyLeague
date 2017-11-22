package com.example.aap.bplfantasyleague.control;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aap.bplfantasyleague.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    TextView profile_name,profile_age,profile_role,profile_price,profile_team,profile_country;
    DatabaseReference dref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String uKey = getIntent().getStringExtra("uKey");
        dref= FirebaseDatabase.getInstance().getReference(uKey);
       dref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               profile_name = findViewById(R.id.profile_name);
               profile_name.setText(dataSnapshot.child("Name").getValue().toString());
               profile_age = findViewById(R.id.profile_age);
               profile_age.setText(dataSnapshot.child("Age").getValue().toString());
               profile_role = findViewById(R.id.profile_role);
               profile_role.setText(dataSnapshot.child("Role").getValue().toString());
               profile_price = findViewById(R.id.profile_price);
               profile_price.setText(dataSnapshot.child("Price").getValue().toString()+'M');
               profile_team = findViewById(R.id.profile_team);
               profile_team.setText(dataSnapshot.child("Team").getValue().toString());
               profile_country = findViewById(R.id.profile_country);
               profile_country.setText(dataSnapshot.child("Country").getValue().toString());
               if(dataSnapshot.child("Points").exists()){
                   Toast.makeText(ProfileActivity.this,"EXISTS POINTS ON PROFILE ACTIVITY CLASS!",Toast.LENGTH_LONG).show();
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
