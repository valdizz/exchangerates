package com.valdizz.exchangerates.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.valdizz.exchangerates.R;
import com.valdizz.exchangerates.ui.adapter.RatesRecyclerViewAdapter;
import com.valdizz.exchangerates.viewmodel.ExchangeRatesViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeRatesActivity extends AppCompatActivity {

    @BindView(R.id.ratesList) RecyclerView ratesList;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvDate1) TextView tvDate1;
    @BindView(R.id.tvDate2) TextView tvDate2;
    private ExchangeRatesViewModel exchangeRatesViewModel;
    private RatesRecyclerViewAdapter ratesRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rates);
        ButterKnife.bind(this);
        initToolbar();
        initRecyclerView();
        initViewModel();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initRecyclerView() {
        ratesRecyclerViewAdapter = new RatesRecyclerViewAdapter(this);
        ratesList.setAdapter(ratesRecyclerViewAdapter);
        ratesList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void initViewModel() {
        exchangeRatesViewModel = ViewModelProviders.of(this).get(ExchangeRatesViewModel.class);
        exchangeRatesViewModel.getCurrencyRates().observe(this, exchangeRates -> ratesRecyclerViewAdapter.setCurrencyRates(exchangeRates));
        exchangeRatesViewModel.getRefreshing().observe(this, isRefreshing -> swipeContainer.setRefreshing(isRefreshing));
        exchangeRatesViewModel.getServiceError().observe(this, isServiceError -> {
            MenuItem menuItem = toolbar.getMenu().findItem(R.id.action_settings);
            if (isServiceError) {
                Snackbar.make(ratesList, R.string.service_error_msg, Snackbar.LENGTH_LONG).show();
                if (menuItem != null) {
                    toolbar.getMenu().findItem(R.id.action_settings).setEnabled(false);
                }
            }
            else {
                if (menuItem != null) {
                    menuItem.setEnabled(true);
                }
            }
        });
        exchangeRatesViewModel.getDate1().observe(this, s -> tvDate1.setText(s));
        exchangeRatesViewModel.getDate2().observe(this, s -> tvDate2.setText(s));
        swipeContainer.setOnRefreshListener(() -> exchangeRatesViewModel.getCurrencyRates());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exchange_rates_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
