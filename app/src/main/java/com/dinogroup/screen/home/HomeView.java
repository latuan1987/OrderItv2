package com.dinogroup.screen.home;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.dinogroup.R;
import com.dinogroup.model.*;
import com.dinogroup.model.TableItem;
import com.dinogroup.util.logging.Logger;
import com.dinogroup.util.mortar.BaseView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Presenter;

/**
 * Created by EVL1HC on 1/18/2017.
 */
public class HomeView extends BaseView {
    private static final Logger LOG = Logger.getLogger(HomePresenter.class);
    private final String TAG = "HomePresenter";
    @Inject HomePresenter presenter;

    /**
     * Variables
     */
    private TableItemAdapter tableItemAdapter;
    @InjectView(R.id.TableList) public ListView listView;
    private List<TableItem> globTableItems;
    AlertDialog alertDialog;

    public HomeView(Context context, AttributeSet attrs) {
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

    public void updateTableList(TableItem table) {
        if (tableItemAdapter != null) {
            globTableItems.add(table);
            tableItemAdapter.setTableItemListList(globTableItems);
            tableItemAdapter.notifyDataSetChanged();
        } else {
            createTableListView(table);
        }
    }

    public void createTableListView(TableItem tableItem) {
        if (tableItemAdapter != null) {
            tableItemAdapter = null;
        }
        globTableItems = new ArrayList<>();
        globTableItems.add(tableItem);
        tableItemAdapter = new TableItemAdapter(this.getContext(), globTableItems);
        listView.setAdapter(tableItemAdapter);
    }

    public void addTableItemListener() {
        /** Set event listener to ListView */
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Item click " + globTableItems.get(position).getId());
                presenter.StatusScreenNavigation(globTableItems.get(position).getId());
            }
        });
    }
}
