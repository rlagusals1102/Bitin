package com.example.community.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.community.R;
import com.example.community.adapter.CoinRecyclerViewAdapter;
import com.example.community.domain.CoinAmountEditTextWatcher;
import com.example.community.domain.CoinAssetInfo;
import com.example.community.domain.CoinInfo;
import com.example.community.domain.MoneyAmountEditTextWatcher;
import com.example.community.manager.RequestManager;
import com.example.community.manager.SharedPrefManager;
import com.example.community.manager.ToastManager;
import com.example.community.manager.UpbitApiManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class AssetFragment extends Fragment {
    private Context context;
    private View contentView;
    private RecyclerView.Adapter coinInfoRecylerViewAdapter;
    private RecyclerView coinInfoRecyclerView;
    private ArrayList<CoinInfo> coinInfoList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_asset, container, false);
        context = contentView.getContext();

        initialize();

        return contentView;
    }

    private void initialize() {
        Handler handler = new Handler(); // for smooth animation
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeCoinInfoRecyclerView();
            }
        }, 300);

        TextView titleTextView = contentView.findViewById(R.id.titleTextView);
        titleTextView.setText("안녕하세요! " + SharedPrefManager.getUserName() + "님");
    }

    private void initializeCoinInfoRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        coinInfoRecyclerView = contentView.findViewById(R.id.view);
        coinInfoRecyclerView.setLayoutManager(linearLayoutManager);
        initializeView();

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(((direction == ItemTouchHelper.LEFT) ? (R.layout.dialog_sell) : (R.layout.dialog_buy)));
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

                EditText coinAmountEditText = dialog.findViewById(R.id.editTextCoinAmount);
                EditText moneyAmountEditText = dialog.findViewById(R.id.editTextMoneyAmount);
                double coinPrice = coinInfoList.get(position).getCoinPrice();

                coinAmountEditText.addTextChangedListener(new CoinAmountEditTextWatcher(coinAmountEditText, moneyAmountEditText, coinPrice));
                moneyAmountEditText.addTextChangedListener(new MoneyAmountEditTextWatcher(moneyAmountEditText, coinAmountEditText, coinPrice));
                coinInfoRecylerViewAdapter = getRecyclerViewAdapter(coinInfoList);
                coinInfoRecyclerView.setAdapter(coinInfoRecylerViewAdapter);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightActionIcon(R.drawable.ic_input)
                        .addSwipeLeftActionIcon(R.drawable.ic_output)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(coinInfoRecyclerView);
    }

    private void initializeView() {
        coinInfoList = new ArrayList<>();
        List<String> marketNameList = UpbitApiManager.getMarketNameList();
        List<Double> fixedAssetList = new ArrayList<>();
        List<Double> floatingAssetList = new ArrayList<>();

        StringRequest getAssetRequest = UpbitApiManager.getMyAssetStringRequest(new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                  List<CoinAssetInfo> assetInfoList = UpbitApiManager.getAssetInfoFromResponse(response);

                  for (CoinAssetInfo assetInfo : assetInfoList) {
                      String coinSymbol = assetInfo.getCoinSymbol();
                      String marketName = "KRW-" + coinSymbol;

                      if (coinSymbol.equals("KRW")) {
                          fixedAssetList.add(assetInfo.getBalance());
                          continue;
                      }
                      if (!marketNameList.contains(marketName)) {
                          ToastManager.showError(context, "일부 코인은 자산에 포함되지 않아요(" + marketName + ")");
                          continue;
                      }

                      final int historyCount = 10;
                      StringRequest stringRequest = UpbitApiManager.getCoinPriceHistoryStringRequest(marketName, 1, historyCount,
                          new Response.Listener<String>() {
                              @Override
                              public void onResponse(String response) {
                                  List<Double> coinPriceHistory = UpbitApiManager.getCoinPriceHistoryFromResponse(response);

                                  floatingAssetList.add(assetInfo.getBalance() * coinPriceHistory.get(0));
                                  coinInfoList.add(new CoinInfo(
                                          UpbitApiManager.marketNameToCoinName(marketName), // market name
                                          assetInfo.getCoinSymbol(), // symbol name
                                          coinPriceHistory.get(0), // coin price
                                          Math.round((coinPriceHistory.get(historyCount - 1) - coinPriceHistory.get(0)) / coinPriceHistory.get(0) * 10000) / 100.0, // price change percent
                                          (ArrayList<Double>) coinPriceHistory, // chart data
                                          assetInfo.getBalance() * coinPriceHistory.get(0), // property amount
                                          assetInfo.getBalance()) // property size
                                  );
                                  if (coinInfoList.size() == assetInfoList.size() - 1) {
                                      coinInfoRecylerViewAdapter = getRecyclerViewAdapter(coinInfoList);
                                      coinInfoRecyclerView.setAdapter(coinInfoRecylerViewAdapter);

                                      DecimalFormat decimalFormat = new DecimalFormat("#,###원");
                                      TextView totalAssetTextView = contentView.findViewById(R.id.totalAssetTextView);
                                      TextView fixedAssetTextView = contentView.findViewById(R.id.fixedAssetTextView);
                                      TextView floatingAssetTextView = contentView.findViewById(R.id.floatingAssetTextView);

                                      Double fixedAsset = fixedAssetList.stream().reduce((x, y) -> x + y).get();
                                      Double floatingAsset = floatingAssetList.stream().reduce((x, y) -> x + y).get();

                                      totalAssetTextView.setText(decimalFormat.format(fixedAsset + floatingAsset));
                                      fixedAssetTextView.setText(decimalFormat.format(fixedAsset));
                                      floatingAssetTextView.setText(decimalFormat.format(floatingAsset));
                                  }
                              }
                          },
                          new Response.ErrorListener() {
                              @Override public void onErrorResponse(VolleyError error) {
                                  ArrayList<Double> tempArrayList = new ArrayList<>();
                                  for (int i = 0; i < historyCount; i++) {
                                      tempArrayList.add(0.0);
                                  }
                                  coinInfoList.add(new CoinInfo(
                                          UpbitApiManager.marketNameToCoinName(marketName), // market name
                                          marketName.replaceAll("KRW-", ""), // symbol name
                                          0.0, // coin price
                                          0.0, // price change percent
                                          (ArrayList<Double>) tempArrayList, // chart data
                                          0.0, // property amount
                                          0.0) // property size
                                  );
                                  if (coinInfoList.size() == marketNameList.size()) {
                                      coinInfoRecylerViewAdapter = getRecyclerViewAdapter(coinInfoList);
                                      coinInfoRecyclerView.setAdapter(coinInfoRecylerViewAdapter);
                                  }
                                  ToastManager.showError(context, "코인 정보를 불러오지 못했어요 (" + marketName + ")");
                              }
                          }
                      );
                      RequestManager.sendStringRequest(context, stringRequest);
                  }
              }
          },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastManager.showError(context, "자산 정보를 가져오지 못했어요 :(");
                Log.e("Bitin", new String(error.networkResponse.data));
            }
        });
        RequestManager.sendStringRequest(context, getAssetRequest);
    }

    private CoinRecyclerViewAdapter getRecyclerViewAdapter(ArrayList<CoinInfo> coinInfoList) {
        return new CoinRecyclerViewAdapter(coinInfoList, new CoinRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (AssetFragment.this.coinInfoList == null) {
                    return;
                }
                String chartUrl = "https://upbit.com/exchange?code=CRIX.UPBIT.KRW-" + coinInfoList.get(position).getCoinSymbol();
                showWebViewDialog(chartUrl);
            }
        });
    }

    private void showWebViewDialog(String url) {
        Dialog webViewDialog = new Dialog(getContext());
        webViewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        webViewDialog.setContentView(R.layout.dialog_webview);
        webViewDialog.show();

        WebView webView = webViewDialog.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.loadUrl(url);
    }
}