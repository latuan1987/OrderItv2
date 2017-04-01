package com.dinogroup.screen.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dinogroup.actionbar.ActionBarConfig;
import com.dinogroup.actionbar.ActionBarOwner;
import com.dinogroup.model.MenuCategory;
import com.dinogroup.model.OrderItem;
import com.dinogroup.model.TableItem;
import com.dinogroup.repository.PresenterSharedRepository;
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

public class OrderPresenter extends BaseViewPresenter<OrderView>{

    private static final Logger LOG = Logger.getLogger(OrderPresenter.class);
    private final String TAG = "OrderPresenter";
    private ActionBarOwner actionBarOwner;
    private PresenterSharedRepository presenterSharedRepository;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final Flow flow;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private OrderView view = getView();

    @Inject
    OrderPresenter(Flow flow, ActionBarOwner actionBarOwner, PresenterSharedRepository presenterSharedRepository) {
        this.flow = flow;
        this.actionBarOwner = actionBarOwner;
        this.presenterSharedRepository = presenterSharedRepository;
    }

    @Override
    public void dropView(OrderView view) {
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
        if (view != null) {
            configureActionBar();

            view.CreateListAdapter();

            // Build data
            makeMenuData();
            makeRecentData();
            makeOrderData();
            makeTopRankData();
        }
    }

    private void configureActionBar() {
        ActionBarConfig config = new ActionBarConfig.Builder()
                .title("Order")
                .visible(true)
                .enableHomeAsUp(false)
                .build();
        actionBarOwner.setConfig(config);
    }

    private void makeMenuGroupData() {
        // Get database reference
        String refChild = "businesses/"+presenterSharedRepository.getCurrentBuzID()+"/menus/categories";
        DatabaseReference databaseReference = database.getReference(refChild);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                MenuCategory menuCategory = dataSnapshot.getValue(MenuCategory.class);
                if (menuCategory != null) {
                    // Notify to update data
                    view.updateMenuData(menuCategory);
                    addMenuItemListener(menuCategory.getId());
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

    private void makeRecentData() {
        // Get database reference
        String refChild = "businesses/"+presenterSharedRepository.getCurrentBuzID()+"/tables/"+presenterSharedRepository.getCurrentTableID()+"/recent";
        DatabaseReference databaseReference = database.getReference(refChild);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                OrderItem orderItem = dataSnapshot.getValue(OrderItem.class);
                if (orderItem != null) {
                    // Notify to update data
                    view.updateRecentData(orderItem);
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

    private void makeOrderData() {
        // Get database reference
        String refChild = "businesses/"+presenterSharedRepository.getCurrentBuzID()+"/tables/"+presenterSharedRepository.getCurrentTableID()+"/orders";
        DatabaseReference databaseReference = database.getReference(refChild);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                OrderItem orderItem = dataSnapshot.getValue(OrderItem.class);
                if (orderItem != null) {
                    // Notify to update data
                    view.updateOrderData(orderItem);
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

    private void makeTopRankData() {
        // Get database reference
        String refChild = "businesses/"+presenterSharedResposity.getCurrentBuzID()+"/menus/toprank";
        DatabaseReference databaseReference = database.getReference(refChild);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                OrderItem orderItem = dataSnapshot.getValue(OrderItem.class);
                if (orderItem != null) {
                    // Notify to update data
                    view.updateTopRankData(orderItem);
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
}
