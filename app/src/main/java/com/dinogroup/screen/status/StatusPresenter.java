package com.dinogroup.screen.status;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dinogroup.actionbar.ActionBarConfig;
import com.dinogroup.actionbar.ActionBarOwner;
import com.dinogroup.model.TableItem;
import com.dinogroup.repository.PresenterSharedRepository;
import com.dinogroup.screen.bill.BillScreen;
import com.dinogroup.screen.order.OrderScreen;
import com.dinogroup.util.logging.Logger;
import com.dinogroup.util.mortar.BaseViewPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import flow.Flow;

/**
 * Created by EVL1HC on 1/18/2017.
 */

public class StatusPresenter extends BaseViewPresenter<StatusView>{

    private static final Logger LOG = Logger.getLogger(StatusPresenter.class);
    private final String TAG = "StatusPresenter";
    private ActionBarOwner actionBarOwner;
    private final PresenterSharedRepository presenterSharedRepository;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final Flow flow;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TableItem processingTable;

    @Inject
    StatusPresenter(Flow flow, ActionBarOwner actionBarOwner, PresenterSharedRepository presenterSharedRepository) {
        this.flow = flow;
        this.actionBarOwner = actionBarOwner;
        this.presenterSharedRepository = presenterSharedRepository;
    }

    @Override
    public void dropView(StatusView view) {
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
        StatusView view = getView();
        if (view != null) {
            getProcessingTableID();
        }
    }

    private void getProcessingTableID() {
        DatabaseReference databaseRef = database.getReference("businesses/"+presenterSharedRepository.getCurrentBuzID()+"/tables");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                processingTable = dataSnapshot.child(presenterSharedRepository.getCurrentTableID()).getValue(TableItem.class);
                if(processingTable != null) {
                    Log.d(TAG, "Processing Table Name: " + processingTable.getName());
                    ActionBarConfig config = new ActionBarConfig.Builder()
                            .title(processingTable.getName())
                            .visible(true)
                            .enableHomeAsUp(false)
                            .build();
                    actionBarOwner.setConfig(config);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void navigatingToOrder() {
        LOG.info("Navigating to order screen");
        flow.goTo(new OrderScreen());
    }

    public void navigatingToBill() {
        LOG.info("Navigating to bill screen");
        flow.goTo(new BillScreen());
    }
}
