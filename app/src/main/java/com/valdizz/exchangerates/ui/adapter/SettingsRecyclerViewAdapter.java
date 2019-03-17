package com.valdizz.exchangerates.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.valdizz.exchangerates.R;
import com.valdizz.exchangerates.model.db.entity.Currency;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsRecyclerViewAdapter extends RecyclerView.Adapter<SettingsRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter{

    private List<Currency> currencies;
    private final LayoutInflater layoutInflater;
    private final OnStartDragListener onStartDragListener;

    public SettingsRecyclerViewAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        onStartDragListener = (OnStartDragListener) context;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
        notifyDataSetChanged();
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    @NonNull
    @Override
    public SettingsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.content_currency, viewGroup, false);
        return new SettingsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void onBindViewHolder(@NonNull SettingsRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tvCurrencyChar.setText(currencies.get(position).getCharCode());
        viewHolder.tvCurrencyName.setText(currencies.get(position).getScale() + " " + currencies.get(position).getName());
        viewHolder.switchVisible.setChecked(currencies.get(position).getVisible() == 1);
        viewHolder.switchVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currencies.get(position).setVisible(((Switch)v).isChecked() ? 1 : 0);
            }
        });
        viewHolder.moveCurrency.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return currencies == null ? 0 : currencies.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(currencies, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(currencies, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @BindView(R.id.tvCurrencyChar)
        TextView tvCurrencyChar;
        @BindView(R.id.tvCurrencyName)
        TextView tvCurrencyName;
        @BindView(R.id.switchVisible)
        Switch switchVisible;
        @BindView(R.id.moveCurrency)
        ImageView moveCurrency;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
