package com.dinogroup.screen.order;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import com.dinogroup.R;
import com.dinogroup.model.ExpandableListAdapter;
import com.dinogroup.model.MenuGroup;
import com.dinogroup.model.MenuItem;
import com.dinogroup.model.OrderItem;
import com.dinogroup.util.logging.Logger;
import com.dinogroup.util.mortar.BaseView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.InjectView;
import mortar.Presenter;

/**
 * Created by EVL1HC on 1/18/2017.
 */
public class OrderView extends BaseView {
    private static final Logger LOG = Logger.getLogger(OrderPresenter.class);
    private final String TAG = "HomePresenter";
    @Inject
    OrderPresenter presenter;

    /**
     * Variables
     */
    private ExpandableListAdapter mExpListAdapter;
    @InjectView(R.id.OrderList) private ExpandableListView mExpListView;

    private Map<String, OrderItem> mMapOrderItem;
    private List<MenuGroup> mListMenuGroup;
    private Map<String, List<MenuItem>> mMapMenuItem;
    private Map<String, List<MenuItem>> mMapFilteredMenuItem;

    AlertDialog alertDialog;

    public OrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    public void onNumberInputError(Throwable thrown) {
        alertDialog = showBasicAlertDialog(getString(R.string.dialog_error_title), thrown.getMessage());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public void updateMenuData(MenuItem menuItem) {

        this.mExpListAdapter.notifyDataSetChanged();
    }

    public void updateRecentData(OrderItem orderItem) {

        this.mExpListAdapter.notifyDataSetChanged();
    }

    public void updateMenuData(OrderItem orderItem) {

        this.mExpListAdapter.notifyDataSetChanged();
    }

    public void updateTopRankData(OrderItem orderItem) {

        this.mExpListAdapter.notifyDataSetChanged();
    }

    public void CreateListAdapter() {
        mListMenuGroup = new ArrayList<MenuGroup>();
        mMapMenuItem = new LinkedHashMap<String, List<MenuItem>>();
        mMapFilteredMenuItem = new LinkedHashMap<String, List<MenuItem>>();
        mMapOrderItem = new LinkedHashMap<String, OrderItem>();

        this.mExpListAdapter = new ExpandableListAdapter(this, mListMenuGroup, mMapMenuItem, mMapFilteredMenuItem, mMapOrderItem);
        this.mExpListView.setAdapter(this.mExpListAdapter);

        this.mExpListView.setGroupIndicator(null);
    }
}
