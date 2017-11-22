package com.example.aap.bplfantasyleague.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.aap.bplfantasyleague.R;
import com.example.aap.bplfantasyleague.model.PlayerList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Tab3 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;


    String userId;
    RecyclerView sell_recycler;
    ExpandableListView expandableListView;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Tab3() {
    }
    public static Tab3 newInstance(String param1, String param2) {
        Tab3 fragment = new Tab3();
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
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);
        sell_recycler = view.findViewById(R.id.sell_recyclerview);
        sell_recycler.setHasFixedSize(true);
        sell_recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
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
        setSellView();
    }

    public void setSellView(){
        DatabaseReference udref= FirebaseDatabase.getInstance().getReference().child("USERS").child(userId)
                .child("Team").child("Players");
        FirebaseRecyclerAdapter<PlayerList,PlayerViewHolder>
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PlayerList, Tab3.PlayerViewHolder>(
                PlayerList.class,
                R.layout.sell_profile_layout,
                Tab3.PlayerViewHolder.class,
                udref) {


            @Override
            protected void populateViewHolder(final Tab3.PlayerViewHolder viewHolder, final PlayerList model, final int position) {
                viewHolder.setName(model.getName().toUpperCase());
            }
        };
        sell_recycler.setAdapter(firebaseRecyclerAdapter);
    }
    public static class PlayerViewHolder extends RecyclerView.ViewHolder{
        final View mView;
        public PlayerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView textView = mView.findViewById(R.id.sell_profile_name);
            textView.setText(name);
        }


    }
}
