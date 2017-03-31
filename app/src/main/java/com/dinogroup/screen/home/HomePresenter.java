package com.dinogroup.screen.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dinogroup.actionbar.ActionBarConfig;
import com.dinogroup.actionbar.ActionBarOwner;
import com.dinogroup.model.TableItem;
import com.dinogroup.repository.PresenterSharedRepository;
import com.dinogroup.screen.status.StatusScreen;
import com.dinogroup.util.logging.Logger;
import com.dinogroup.util.mortar.BaseViewPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import flow.Flow;

/**
 * Created by EVL1HC on 1/18/2017.
 */

public class HomePresenter extends BaseViewPresenter<HomeView>{

    private static final Logger LOG = Logger.getLogger(HomePresenter.class);
    private final String TAG = "HomePresenter";
    private ActionBarOwner actionBarOwner;
    private final PresenterSharedRepository presenterSharedRepository;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private String curBuzID = "";

    private final Flow flow;

    @Inject
    HomePresenter(Flow flow, ActionBarOwner actionBarOwner, PresenterSharedRepository presenterSharedRepository) {
        this.flow = flow;
        this.actionBarOwner = actionBarOwner;
        this.presenterSharedRepository = presenterSharedRepository;
    }

    @Override
    public void dropView(HomeView view) {
        super.dropView(view);
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        HomeView view = getView();
        if (view != null) {
            configureActionBar();
            getTableData();
            view.addTableItemListener();
        }
    }

    private void configureActionBar() {
        ActionBarConfig config = new ActionBarConfig.Builder()
                .title("Tables")
                .visible(true)
                .enableHomeAsUp(false)
                .build();
        actionBarOwner.setConfig(config);
    }

    private void getTableData() {
        // Get database reference
        curBuzID = presenterSharedRepository.getCurrentBuzID();
        DatabaseReference databaseReference = database.getReference("businesses/"+curBuzID+"/tables");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                TableItem table = dataSnapshot.getValue(TableItem.class);
                if (table != null) {
                    Log.d(TAG, "Table name is: " + table.getName());
                    //display list of table
                    getView().updateTableList(table);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LOG.error(TAG, "Failed to read value.", databaseError.toException());
                Toast.makeText(getView().getContext(), "Failed to load data.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }

    public void StatusScreenNavigation(String tableID) {
        presenterSharedRepository.setCurrentTableID(tableID);
        flow.goTo(new StatusScreen());
    }
}
