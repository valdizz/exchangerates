package com.valdizz.exchangerates.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valdizz.exchangerates.R;
import com.valdizz.exchangerates.model.db.entity.ExchangeRate;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RatesRecyclerViewAdapter extends RecyclerView.Adapter<RatesRecyclerViewAdapter.ViewHolder> {

    private List<ExchangeRate> exchangeRates;
    private final LayoutInflater layoutInflater;

    public RatesRecyclerViewAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setCurrencyRates(List<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.content_rates_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.tvCurrencyChar.setText(exchangeRates.get(position).getCharCode());
        viewHolder.tvCurrencyName.setText(exchangeRates.get(position).getScale() + " " + exchangeRates.get(position).getName());
        viewHolder.tvRate1.setText(Double.toString(exchangeRates.get(position).getRate1()));
        viewHolder.tvRate2.setText(Double.toString(exchangeRates.get(position).getRate2()));
    }

    @Override
    public int getItemCount() {
        return exchangeRates == null ? 0 : exchangeRates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCurrencyChar)
        TextView tvCurrencyChar;
        @BindView(R.id.tvCurrencyName)
        TextView tvCurrencyName;
        @BindView(R.id.tvRate1)
        TextView tvRate1;
        @BindView(R.id.tvRate2)
        TextView tvRate2;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
