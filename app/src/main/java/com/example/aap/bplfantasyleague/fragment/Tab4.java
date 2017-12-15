package com.example.aap.bplfantasyleague.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aap.bplfantasyleague.R;
import com.example.aap.bplfantasyleague.model.MatchList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Tab4 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;
    private FloatingActionButton fab;
    public Tab4() {
    }

    public static Tab4 newInstance(String param1, String param2) {
        Tab4 fragment = new Tab4();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_tab4, container, false);
        recyclerView= view.findViewById(R.id.recycler_view_fixtures);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("MATCHES");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
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
        FirebaseRecyclerAdapter<MatchList,Tab4.MatchViewHolder>
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MatchList, Tab4.MatchViewHolder>(
                MatchList.class,
                R.layout.match_layout,
                Tab4.MatchViewHolder.class,
                databaseReference) {


            @Override
            protected void populateViewHolder(Tab4.MatchViewHolder viewHolder, MatchList model, int position) {
                viewHolder.setId(model.getId());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());
                viewHolder.setVenue(model.getVenue());
                viewHolder.setTeam1(model.getTeam1());
                viewHolder.setTeam2(model.getTeam2());

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public MatchViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setId(int Id){
            TextView userNameView= mView.findViewById(R.id.match_no);
            userNameView.setText(Integer.toString(Id)+'.');
        }
        public void setDate(String Date){
            TextView userNameView= mView.findViewById(R.id.match_date);
            userNameView.setText(Date);
        }
        public void setTime(String Time){
            TextView userNameView= mView.findViewById(R.id.match_time);
            userNameView.setText("GMT+6 : "+Time);
        }
        public void setVenue(String Venue){
            TextView userNameView= mView.findViewById(R.id.match_Venue);
            userNameView.setText("At "+Venue);
        }
        public void setTeam1(String Team1){
            TextView userNameView= mView.findViewById(R.id.match_team1);
            userNameView.setText(Team1);
        }
        public void setTeam2(String Team2){
            TextView userNameView= mView.findViewById(R.id.match_team2);
            userNameView.setText(Team2);
        }

    }
}
