package com.example.community.manager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.community.domain.CoinAssetInfo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class UpbitApiManager {
    private static String accessKey = "";
    private static String secretKey = "";

    private static String createGetAuthenticationToken() {
        String jwtToken = Jwts.builder()
                .claim("access_key", accessKey)
                .claim("nonce", UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
        return ("Bearer " + jwtToken);
    }

    private static String createPostAuthenticationToken(Map<String, String> params) {
        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String jwtToken = "";
        try {
            String queryString = String.join("&", queryElements.toArray(new String[0]));
            MessageDigestManager messageDigest = MessageDigestManager.getInstance("SHA-512");
            messageDigest.update(queryString.getBytes("UTF-8"));
            String queryHash = String.format("%0128x", new BigInteger(1, messageDigest.digest()));

            jwtToken = Jwts.builder()
                    .claim("access_key", accessKey)
                    .claim("nonce", UUID.randomUUID().toString())
                    .claim("query_hash", queryHash)
                    .claim("query_hash_alg", "SHA512")
                    .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                    .compact();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return ("Bearer " + jwtToken);
    }

    public static void initialize() {
        accessKey = SharedPrefManager.getUpbitAccessApiKey();
        secretKey = SharedPrefManager.getUpbitSecretApiKey();
    }

    public static StringRequest getMyAssetStringRequest(Response.Listener<String> onResponse, Response.ErrorListener onError) {
        String requestUrl = "https://api.upbit.com/v1/accounts";

        return new StringRequest(Request.Method.GET, requestUrl, onResponse, onError) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", createGetAuthenticationToken());
                return params;
            }
        };
    }

    public static StringRequest sellCoinStringRequest(String marketName, String volume, Response.Listener<String> onResponse, Response.ErrorListener onError) {
        String requestUrl = "https://api.upbit.com/v1/orders";
        Map<String, String> entityParams = new HashMap<>();
        entityParams.put("market", marketName);
        entityParams.put("side", "ask");
        entityParams.put("volume", volume);
        entityParams.put("ord_type", "market");

        return new StringRequest(Request.Method.POST, requestUrl, onResponse, onError) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", createPostAuthenticationToken(entityParams));
                return params;
            }
            @Override
            public byte[] getBody() {
                return (new Gson().toJson(entityParams).getBytes());
            }
        };
    }

    public static StringRequest buyCoinStringRequest(String marketName, String price, Response.Listener<String> onResponse, Response.ErrorListener onError) {
        String requestUrl = "https://api.upbit.com/v1/orders";
        Map<String, String> entityParams = new HashMap<>();
        entityParams.put("market", marketName);
        entityParams.put("side", "bid");
        entityParams.put("price", price);
        entityParams.put("ord_type", "price");

        return new StringRequest(Request.Method.POST, requestUrl, onResponse, onError) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", createPostAuthenticationToken(entityParams));
                return params;
            }
            @Override
            public byte[] getBody() {
                return (new Gson().toJson(entityParams).getBytes());
            }
        };
    }

    public static StringRequest getCoinPriceStringRequest(String marketName, Response.Listener<String> onResponse, Response.ErrorListener onError) {
        String requestUrl = "https://api.upbit.com/v1/candles/minutes/1?count=1&market=" + marketName;
        return new StringRequest(Request.Method.GET, requestUrl, onResponse, onError);
    }

    public static StringRequest getCoinPriceHistoryStringRequest(String marketName, int minutes, int count, Response.Listener<String> onResponse, Response.ErrorListener onError) {
        String requestUrl = "https://api.upbit.com/v1/candles/minutes/" + minutes + "?count=" + count + "&market=" + marketName;
        return new StringRequest(Request.Method.GET, requestUrl, onResponse, onError);
    }

    public static List<String> getMarketNameList() {
        List<String> coinList = new ArrayList<>();
        coinList.add("KRW-BTC"); coinList.add("KRW-ETH"); coinList.add("KRW-CVC");
        coinList.add("KRW-SOL"); coinList.add("KRW-EGLD"); coinList.add("KRW-GAS");
        // coinList.add("KRW-APT"); coinList.add("KRW-THETA"); coinList.add("KRW-MATIC");
        return coinList;
    }

    public static String marketNameToCoinName(String marketName) {
        HashMap<String, String> coinNameMapper = new HashMap<>();
        coinNameMapper.put("KRW-BTC", "비트코인"); coinNameMapper.put("KRW-ETH", "이더리움"); coinNameMapper.put("KRW-CVC", "시빅");
        coinNameMapper.put("KRW-SOL", "솔라나"); coinNameMapper.put("KRW-EGLD", "멀티버스엑스"); coinNameMapper.put("KRW-GAS", "가스");
        // coinNameMapper.put("KRW-APT", "앱토스"); coinNameMapper.put("KRW-THETA", "쎄타토큰"); coinNameMapper.put("KRW-MATIC", "폴리곤");

        return coinNameMapper.get(marketName);
    }

    public static Double getCoinPriceFromResponse(String response) {
        double coinPrice = -1f;

        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            coinPrice = jsonObject.getDouble("trade_price");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return coinPrice;
    }

    public static List<Double> getCoinPriceHistoryFromResponse(String response) {
        List<Double> coinPriceHistory = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                coinPriceHistory.add(jsonObject.getDouble("trade_price"));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return coinPriceHistory;
    }

    public static List<CoinAssetInfo> getAssetInfoFromResponse(String response) {
        List<CoinAssetInfo> coinAssetInfoList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                coinAssetInfoList.add(
                        new CoinAssetInfo(
                                jsonObject.getString("currency"),
                                Double.valueOf(jsonObject.getString("balance")),
                                Double.valueOf(jsonObject.getString("avg_buy_price"))
                        )
                );
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return coinAssetInfoList;
    }

}
