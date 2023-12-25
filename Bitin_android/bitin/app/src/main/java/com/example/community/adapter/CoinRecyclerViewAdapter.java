package com.example.community.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.community.domain.CoinInfo;
import com.example.community.R;
import com.example.community.manager.UpbitApiManager;
import com.majorik.sparklinelibrary.SparkLineLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CoinRecyclerViewAdapter extends RecyclerView.Adapter<CoinRecyclerViewAdapter.Viewholder> {
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    ArrayList<CoinInfo> cryptoWallets;
    OnItemClickListener onItemClickListener;
    DecimalFormat formatter;
    int itemCount = 0;
    int marketCount;

    public CoinRecyclerViewAdapter(ArrayList<CoinInfo> cryptoWallets, OnItemClickListener onItemClickListener) {
        this.cryptoWallets = cryptoWallets;
        this.onItemClickListener = onItemClickListener;
        formatter = new DecimalFormat("###,###,###");
        marketCount = UpbitApiManager.getMarketNameList().size();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_wallet, parent, false);
        CoinRecyclerViewAdapter.Viewholder viewholder = new CoinRecyclerViewAdapter.Viewholder(inflate);

        int position = itemCount;
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });
        itemCount += 1;
        if (itemCount == marketCount) {
            itemCount = 0;
        }

        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.cryptoNameText.setText(cryptoWallets.get(position).getCoinName());
        holder.cryptoPriceText.setText(formatter.format(cryptoWallets.get(position).getCoinPrice()) + "원");
        holder.changePercentText.setText(cryptoWallets.get(position).getChangePercent() + "%");
        if (cryptoWallets.get(position).getPropertySize() < 0) {
            holder.propertySizeText.setText("");
            holder.propertyAmountText.setText("");
            ViewGroup.LayoutParams layoutParams = holder.lineChart.getLayoutParams();
            layoutParams.width += 100;
            holder.lineChart.setLayoutParams(layoutParams);
        }
        else {
            holder.propertySizeText.setText(formatter.format(cryptoWallets.get(position).getPropertySize()) + "개");
            holder.propertyAmountText.setText(formatter.format(cryptoWallets.get(position).getPropertyAmount()) + "원");
        }

        if(cryptoWallets.get(position).getChangePercent() > 0) {
            holder.changePercentText.setTextColor(Color.parseColor("#12C737"));
        }
        else if(cryptoWallets.get(position).getChangePercent() < 0) {
            holder.changePercentText.setTextColor(Color.parseColor("#FC0000"));
        }
        else {
            holder.changePercentText.setTextColor(Color.parseColor("#FFFFFF"));
        }

        holder.lineChart.setData(cryptoWallets.get(position).getLineData());

        int drawableResourceId = 0;
        switch (cryptoWallets.get(position).getCoinName()) {
            case "비트코인": drawableResourceId = R.drawable.btc_symbol; break;
            case "이더리움": drawableResourceId = R.drawable.eth_symbol; break;
            case "시빅": drawableResourceId = R.drawable.cvc_symbol; break;
            case "솔라나": drawableResourceId = R.drawable.sol_symbol; break;
            case "멀티버스엑스": drawableResourceId = R.drawable.egld_symbol; break;
            case "가스": drawableResourceId = R.drawable.gas_symbol; break;
            case "앱토스": drawableResourceId = R.drawable.apt_symbol; break;
            case "쎄타토큰": drawableResourceId = R.drawable.theta_symbol; break;
            case "폴리곤": drawableResourceId = R.drawable.matic_symbol; break;

        }

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.logoCrypto);
    }

    @Override
    public int getItemCount() {
        return cryptoWallets.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView cryptoNameText, cryptoPriceText, changePercentText, propertySizeText, propertyAmountText;
        ImageView logoCrypto;
        SparkLineLayout lineChart;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            cryptoNameText = itemView.findViewById(R.id.cryptoNameText);
            cryptoPriceText = itemView.findViewById(R.id.cryptoPriceText);
            changePercentText = itemView.findViewById(R.id.changePercentText);
            propertySizeText = itemView.findViewById(R.id.propertySizeText);
            propertyAmountText = itemView.findViewById(R.id.propertyAmountText);
            logoCrypto = itemView.findViewById(R.id.logoImg);
            lineChart = itemView.findViewById(R.id.sparkLineLayout);
        }
    }
}
