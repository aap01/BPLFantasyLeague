package com.example.aap.bplfantasyleague.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aap.bplfantasyleague.control.ProfileActivity;
import com.example.aap.bplfantasyleague.R;
import com.example.aap.bplfantasyleague.model.PlayerList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Tab1 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    DatabaseReference udref;
    String userId;
    private OnFragmentInteractionListener mListener;

    public void setUserId(String userId){
        this.userId = userId;
    }

    public Tab1() {

    }

    public static Tab1 newInstance(String param1, String param2) {
        Tab1 fragment = new Tab1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View tab1_view = inflater.inflate(R.layout.fragment_tab1, container, false);
        recyclerView = tab1_view.findViewById(R.id.myteam_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return tab1_view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        udref= FirebaseDatabase.getInstance().getReference().child("USERS").child(userId)
                .child("Team").child("Players");
        FirebaseRecyclerAdapter<PlayerList,Tab1.PlayerViewHolder>
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PlayerList, Tab1.PlayerViewHolder>(
                PlayerList.class,
                R.layout.myteam_layout,
                Tab1.PlayerViewHolder.class,
                udref) {


            @Override
            protected void populateViewHolder(final Tab1.PlayerViewHolder viewHolder, final PlayerList model, final int position) {
                viewHolder.setName(model.getName().toUpperCase());
                viewHolder.setRole(model.getRole());
                final String uKey = "USERS/"+userId+"/Team/Players/"+getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileActivity_intent = new Intent(getContext(),ProfileActivity.class);
                        profileActivity_intent.putExtra("uKey",uKey);
                        startActivity(profileActivity_intent);
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public static class PlayerViewHolder extends RecyclerView.ViewHolder{
        final View mView;
        public PlayerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView userNameView= mView.findViewById(R.id.myteam_snap_name);
            userNameView.setText(name);
        }

        public void setRole(String Role){
            TextView userRoleView= mView.findViewById(R.id.myteam_snap_role);
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

    }
}
