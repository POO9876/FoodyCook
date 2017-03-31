package com.example.hilary.foodycook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hilary.foodycook.adapters.MyFirebaseAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.parceler.Parcels;


import java.util.ArrayList;

/**
 * Created by hilary on 3/29/17.
 */

public class MyFoodsFragment extends Fragment {
    private static final String SAVED_ADAPTER_ITEMS = "SAVED_ADAPTER_ITEMS";
    private final static String SAVED_ADAPTER_KEYS = "SAVED_ADAPTER_KEYS";

    View rootView2;
    RecyclerView mRecyclerView;
    FirebaseRecyclerAdapter mAdapter;
    DatabaseReference mRootRef;
    DatabaseReference mFoodReference;

    MyFirebaseAdapter mMyAdapter;
    private ArrayList<Food> mAdapterItems;
    private ArrayList<String> mAdapterKeys;
    private Query mQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mFoodReference = ((MainActivity ) this.getActivity()).mRootRef.child("foods");
        rootView2 = inflater.inflate(R.layout.my_foods_fragment, container, false);
        handleInstanceState(savedInstanceState);
        setupFirebase();
        setupRecyclerview();


        return rootView2;
    }
    private void setupFirebase() {
        mQuery = mFoodReference.limitToLast(10);

//        Firebase.setAndroidContext(this);
//        String firebaseLocation = getResources().getString(R.string.firebase_location);
//        mQuery = new Firebase(firebaseLocation);
    }
    private void handleInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(SAVED_ADAPTER_ITEMS) &&
                savedInstanceState.containsKey(SAVED_ADAPTER_KEYS)) {
            mAdapterItems = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_ADAPTER_ITEMS));
            mAdapterKeys = savedInstanceState.getStringArrayList(SAVED_ADAPTER_KEYS);
        } else {
            mAdapterItems = new ArrayList<Food>();
            mAdapterKeys = new ArrayList<String>();
        }
    }
    private void setupRecyclerview() {
        mRecyclerView = (RecyclerView) rootView2.findViewById(R.id.foodListRecyclerView);
        mMyAdapter = new MyFirebaseAdapter(mQuery, Food.class, mAdapterItems, mAdapterKeys);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setAdapter(mMyAdapter);
    };



    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
