package com.valdizz.exchangerates.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.valdizz.exchangerates.R;
import com.valdizz.exchangerates.ui.adapter.ItemTouchHelperCallback;
import com.valdizz.exchangerates.ui.adapter.OnStartDragListener;
import com.valdizz.exchangerates.ui.adapter.SettingsRecyclerViewAdapter;
import com.valdizz.exchangerates.viewmodel.SettingsViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements OnStartDragListener{

    @BindView(R.id.currenciesList) RecyclerView currenciesList;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private SettingsViewModel settingsViewModel;
    private SettingsRecyclerViewAdapter settingsRecyclerViewAdapter;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initToolbar();
        initRecyclerView();
        initViewModel();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initRecyclerView() {
        settingsRecyclerViewAdapter = new SettingsRecyclerViewAdapter(this);
        currenciesList.setAdapter(settingsRecyclerViewAdapter);
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(settingsRecyclerViewAdapter));
        itemTouchHelper.attachToRecyclerView(currenciesList);
        currenciesList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void initViewModel() {
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsViewModel.getCurrencies().observe(this, currencies -> settingsRecyclerViewAdapter.setCurrencies(currencies));
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateUpTo(new Intent(this, ExchangeRatesActivity.class));
                return true;
            case R.id.action_save:
                settingsViewModel.saveCurrencies(settingsRecyclerViewAdapter.getCurrencies());
                navigateUpTo(new Intent(this, ExchangeRatesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
