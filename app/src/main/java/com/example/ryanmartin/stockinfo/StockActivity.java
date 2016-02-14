package com.example.ryanmartin.stockinfo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StockActivity extends AppCompatActivity {

    private EditText mSearchStockSymbolField;
    private Button mSearchButton;
    private LinearLayout mStockLinearScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        mSearchStockSymbolField = (EditText) findViewById(R.id.SearchStockSymbolField);
        mSearchButton = (Button) findViewById(R.id.SearchButton);
        mStockLinearScrollView = (LinearLayout) findViewById(R.id.stockLinearScrollView);
    }

    public void onPressSearchButton(View view) {
        String searchText = mSearchStockSymbolField.getText().toString();
        if (searchText.length() != 0) {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            new StockDataRetriever().execute(searchText);
        }
        else {
            showAlertDialog("Error: Invalid Input", "Enter a valid stock symbol", "Ok");
        }
    }

    private final void showAlertDialog(String title, String message, String buttonMessage) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(buttonMessage, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class StockDataRetriever extends AsyncTask<String, Void, StockData> {

        private String beginningURL = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
        private String endingURL = "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

        @Override
        protected StockData doInBackground(String... params) {
            String stringUrl = beginningURL + params[0] + endingURL;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(stringUrl);
                connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return createStockDataObject(sb.toString());
                }
                else {
                    showAlertDialog("Error: Network Error", "Unexpected network error occurred", "Ok");
                }
            }
            catch (MalformedURLException me) {
                me.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(StockData stockData) {
            mStockLinearScrollView.removeAllViews();
            if (stockData != null && !stockData.isEmpty()) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View stockInfoView = layoutInflater.inflate(R.layout.stock_entry_row, null);

                TextView name = (TextView) stockInfoView.findViewById(R.id.NameTV);
                TextView symbol = (TextView) stockInfoView.findViewById(R.id.SymbolTV);
                TextView avgDailyVol = (TextView) stockInfoView.findViewById(R.id.AvgDailyVolTV);
                TextView change = (TextView) stockInfoView.findViewById(R.id.ChangeTV);
                TextView daysLo = (TextView) stockInfoView.findViewById(R.id.DaysLowTV);
                TextView daysHi = (TextView) stockInfoView.findViewById(R.id.DaysHighTV);
                TextView yrLo = (TextView) stockInfoView.findViewById(R.id.YearLowTV);
                TextView yrHi = (TextView) stockInfoView.findViewById(R.id.YearHighTV);
                TextView mrktCapt = (TextView) stockInfoView.findViewById(R.id.MarketCapitalizationTV);
                TextView stockExch = (TextView) stockInfoView.findViewById(R.id.StockExchangeTV);
                TextView daysRange = (TextView) stockInfoView.findViewById(R.id.DaysRangeTV);
                TextView lastTradePriceOnly = (TextView) stockInfoView.findViewById(R.id.LastTradePriceOnlyTV);
                TextView volume = (TextView) stockInfoView.findViewById(R.id.VolumeTV);

                name.setText(stockData.getName());
                symbol.setText(stockData.getSymbol());

                avgDailyVol.setText("Avg Daily Volume: " + ((stockData.getAverageDailyVolume().equals("null")) ? "" : stockData.getAverageDailyVolume()));
                change.setText("Change: " + ((stockData.getChange().equals("null") ? "" : stockData.getChange())));
                daysLo.setText("Days Low: " + ((stockData.getDaysLow().equals("null") ? "" : stockData.getDaysLow())));
                daysHi.setText("Days High: " + ((stockData.getDaysHigh().equals("null") ? "" : stockData.getDaysHigh())));
                yrLo.setText("Year Low: " + ((stockData.getYearLow().equals("null") ? "" : stockData.getYearLow())));
                yrHi.setText("Year High: " + ((stockData.getYearHigh().equals("null") ? "" : stockData.getYearHigh())));
                mrktCapt.setText("Market Capitalization: " + ((stockData.getMarketCapitalization().equals("null") ? "" : stockData.getMarketCapitalization())));
                stockExch.setText("Stock Exchange: " + ((stockData.getStockExchange().equals("null") ? "" : stockData.getStockExchange())));
                daysRange.setText("Days Range: " + ((stockData.getDaysRange().equals("null") ? "" : stockData.getDaysRange())));
                lastTradePriceOnly.setText("Last Trade Price Only: " + ((stockData.getLastTradePriceOnly().equals("null") ? "" : stockData.getLastTradePriceOnly())));
                volume.setText("Volume: " + ((stockData.getVolume().equals("null") ? "" : stockData.getVolume())));

                mStockLinearScrollView.addView(stockInfoView);
            }
            else {
                showAlertDialog("Error: Null Response", "No data to show", "Ok");
                mSearchStockSymbolField.setText("");
            }
        }

        private StockData createStockDataObject(String jsonData) throws JSONException {
            JSONObject json = new JSONObject(jsonData).getJSONObject("query").getJSONObject("results").getJSONObject("quote");

            String avgDailyVol = json.getString("AverageDailyVolume");
            String change = json.getString("Change");
            String daysLo = json.getString("DaysLow");
            String daysHi = json.getString("DaysHigh");
            String yrLo = json.getString("YearLow");
            String yrHi = json.getString("YearHigh");
            String marketCapitalization = json.getString("MarketCapitalization");
            String lastTradePriceOnly = json.getString("LastTradePriceOnly");
            String daysRange = json.getString("DaysRange");
            String name = json.getString("Name");
            String symbol = json.getString("Symbol");
            String volume = json.getString("Volume");
            String stockExchange = json.getString("StockExchange");

            StockData stockData = new StockData(symbol, avgDailyVol, change, daysLo, daysHi, yrLo, yrHi, marketCapitalization,
                    lastTradePriceOnly, daysRange, name, volume, stockExchange);
            return stockData;
        }
    }
}
