package com.example.aap.bplfantasyleague.control;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.aap.bplfantasyleague.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    TextView profile_name,profile_age,profile_role,profile_price,profile_team,profile_country;
    DatabaseReference dref;
    BarChart barChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        barChart = findViewById(R.id.activity_profile_graph);
        String uKey = getIntent().getStringExtra("uKey");
        dref= FirebaseDatabase.getInstance().getReference(uKey);
        final ArrayList<BarEntry> barEntries = new ArrayList<>();
        final ArrayList<String> mathes =new ArrayList<>();
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
               if(dataSnapshot.child("Scores").exists()){
                   int x = 0;
                   for(DataSnapshot d: dataSnapshot.child("Scores").getChildren()){
                       String matchName = d.getKey().toString();
                       mathes.add(matchName);
                       Float s = Float.parseFloat(d.getValue().toString());
                       barEntries.add(new BarEntry(x,s));
                       x++;
                   }
                   final BarDataSet barDataSet = new BarDataSet(barEntries,"SCORES");
                   final BarData barData = new BarData(barDataSet);
                   barDataSet.setDrawValues(true);
                   //---------Formatting xaxis----------//
                   barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(mathes));
                   Description description = new Description();
                   description.setText("Score/Match");
                   barChart.animateY(1000);
                   barChart.setDrawValueAboveBar(false);
                   barChart.setDescription(description);
                   barChart.setData(barData);
                   barChart.setFitBars(true);
                   barChart.invalidate();
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
