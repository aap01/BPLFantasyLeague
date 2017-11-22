package com.example.aap.bplfantasyleague.control;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.aap.bplfantasyleague.R;
import com.example.aap.bplfantasyleague.model.PlayerList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class SelectSquadActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private DatabaseReference playerRef;
    RecyclerView recyclerView;
    String userId;
    DatabaseReference dref;
    int dollars;
    int no_of_playser_selected ;
    TextView counterView;
    Vector selected ;

    RecyclerView recyclerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_squad_layout);
        Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.select_squad_toolbar);
        toolbar.setTitle(R.string.select_team);
        counterView = findViewById(R.id.player_selected);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        selected = new Vector();
        userId=getIntent().getStringExtra("userId");
        dref = FirebaseDatabase.getInstance().getReference().child("USERS").child(userId);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView balance = findViewById(R.id.total_balance_value);
                dollars = Integer.parseInt(dataSnapshot.child("Balance").getValue().toString());
                balance.setText((dataSnapshot.child("Balance").getValue().toString()+"M $"));
                no_of_playser_selected=Integer.parseInt(dataSnapshot.child("TotalSelected").getValue().toString());
                counterView.setText(dataSnapshot.child("TotalSelected").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dref = dref.child("Team").child("Players");
        playerRef= FirebaseDatabase.getInstance().getReference().child("PLAYERS");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<PlayerList,PlayerViewHolder>
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PlayerList, PlayerViewHolder>(
                PlayerList.class,
                R.layout.profile_layout,
                PlayerViewHolder.class,
                playerRef) {


            @Override
            public void onBindViewHolder(PlayerViewHolder viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);
                String uKey = getRef(position).getKey();
                if(selected.contains(uKey)){
                    viewHolder.mView.findViewById(R.id.profile_snap).setBackgroundColor(Color.parseColor("#90ee90"));

                }
                else{
                    viewHolder.mView.findViewById(R.id.profile_snap).setBackgroundColor(Color.parseColor("#CCd6d7d7"));
                }
            }

            @Override
            protected void populateViewHolder(final PlayerViewHolder viewHolder, final PlayerList model, final int position) {
                viewHolder.setName(model.getName().toUpperCase());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setRole(model.getRole());
                final String uKey = getRef(position).getKey();
                final DatabaseReference udref = FirebaseDatabase.getInstance().getReference("USERS/"+userId);
                final DatabaseReference pdref =FirebaseDatabase.getInstance().getReference("PLAYERS/"+uKey);

                Query query = udref.child("Team").child("Players");
                if(query.equals(uKey)){
                    selected.addElement(uKey);
                    viewHolder.mView.findViewById(R.id.profile_snap).setBackgroundColor(Color
                    .parseColor("#90ee90"));
                }
                //viewHolder.setPoints(0);//---------AS NO POINTS BEEN UPDATED YET-----------//
                viewHolder.mView.findViewById(R.id.profile_snap).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.mView.setEnabled(false);//----------DISABLING FURTHER CLICK UNTIL PROCESS-------//

                        int price_to_pay = (int)model.getPrice();
                        if(selected.contains(uKey)){
                            dollars+=price_to_pay;
                            no_of_playser_selected--;
                            udref.child("Balance").setValue(dollars);
                            udref.child("TotalSelected").setValue(no_of_playser_selected);//---eto joghonno ekta line!--//
                            udref.child("Team").child("Players").child(uKey).removeValue();
                            selected.removeElement(uKey);
                            view.setBackgroundColor(Color.parseColor("#CCd6d7d7"));
                        }
                        else if(!selected.contains(uKey)) {
                            if(price_to_pay<=dollars){
                                DatabaseReference utmp = udref.child("Team").child("Players").child(uKey);
                                utmp.child("PlayerId").setValue(model.getPlayerId());
                                utmp.child("Name").setValue(model.getName());
                                utmp.child("Price").setValue(model.getPrice());
                                utmp.child("Team").setValue(model.getTeam());
                                utmp.child("Country").setValue(model.getCountry());
                                utmp.child("Role").setValue(model.getRole());
                                utmp.child("Age").setValue(model.getAge());

                                no_of_playser_selected++;
                                dollars-=price_to_pay;
                                udref.child("Balance").setValue(dollars);
                                udref.child("TotalSelected").setValue(no_of_playser_selected);//---eto joghonno ekta line!--//

                                if(no_of_playser_selected==11){
                                    Intent i = new Intent(SelectSquadActivity.this,MainActivity.class);
                                    i.putExtra("userId",userId);
                                    startActivity(i);
                                    finish();
                                }

                                selected.addElement(uKey);
                                view.setBackgroundColor(Color.parseColor("#90ee90"));
                            }
                        }
                        viewHolder.mView.setEnabled(true);//---------ENABLING CLICKS NOW-------------//

                    }
                });


            }



        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class PlayerViewHolder extends RecyclerView.ViewHolder{
        final View mView;
        FloatingActionButton fab;TextView role,points;
        boolean clicked;
        public PlayerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            clicked=false;
        }
        public void setName(String name){
            TextView userNameView= mView.findViewById(R.id.profile_snap_name);
            userNameView.setText(name);
        }
        public void setPrice(long price){
            TextView userPriceView= mView.findViewById(R.id.profile_snap_price);
            userPriceView.setText(Integer.toString((int)price)+"M $");
        }
        public void setRole(String Role){
            TextView userRoleView= mView.findViewById(R.id.profile_snap_role);
            if(Role.compareTo("Bat")==0){
                userRoleView.setText("Batsman");
            }
            if(Role.compareTo("Bowl")==0){
                userRoleView.setText("Bowler");
            }
            if(Role.compareTo("All")==0){
                userRoleView.setText("Allrounder");
            }
        }
        /*public void setPoints(int p){
            TextView userPriceView= mView.findViewById(R.id.profile_snap_points);
            userPriceView.setText("POINTS : "+Integer.toString(p));
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.squad_selection_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {

        Intent startIntent = new Intent(SelectSquadActivity.this,MainActivity.class);
        startActivity(startIntent);
        finish();

    }

}
