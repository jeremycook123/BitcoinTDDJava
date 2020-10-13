package com.cloudacademy.bitcoin.converter;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.NumberFormat;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.ByteArrayInputStream;

public class App 
{
    private final String BITCOIN_NZDUSD_URL = "https://api.coindesk.com/v1/bpi/currentprice/NZD.json";
    private final HttpGet httpget = new HttpGet(BITCOIN_NZDUSD_URL);

    private CloseableHttpClient httpclient;

    /*
    //curl -s https://api.coindesk.com/v1/bpi/currentprice/NZD.json | jq .
    //example json response from coindesk api:
    {
        "time": {
          "updated": "Oct 13, 2020 20:24:00 UTC",
          "updatedISO": "2020-10-13T20:24:00+00:00",
          "updateduk": "Oct 13, 2020 at 21:24 BST"
        },
        "disclaimer": "This data was produced from the CoinDesk Bitcoin Price Index (USD)",
        "bpi": {
          "USD": {
            "code": "USD",
            "rate": "11,431.5133",
            "description": "United States Dollar",
            "rate_float": 11431.5133
          },
          "NZD": {
            "code": "NZD",
            "rate": "17,193.0989",
            "description": "New Zealand Dollar",
            "rate_float": 17193.0989
          }
        }
      }
    */

    public App()
    {
        this.httpclient = HttpClients.createDefault();
    }

    public App(CloseableHttpClient httpClient)
    {
        this.httpclient = httpClient;
    }

    public double GetExchangeRate(String currency) throws IOException
    {
        double rate = 0;

        try {
            CloseableHttpResponse res = this.httpclient.execute(httpget);
            switch (res.getStatusLine().getStatusCode())
            {
                case 200:
                    HttpEntity entity = res.getEntity();

                    InputStream inputStream = res.getEntity().getContent();
                    var json = new BufferedReader(new InputStreamReader(inputStream));

                    @SuppressWarnings( "deprecation" )
                    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                    String n = jsonObject.get("bpi").getAsJsonObject().get(currency).getAsJsonObject().get("rate").getAsString();
                    NumberFormat nf = NumberFormat.getInstance();
                    rate = nf.parse(n).doubleValue();

                    break;
                default:
                    rate = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            rate = -1;
        }
        finally {
            httpclient.close();
        }

        return rate;
    }

    public double ConvertBitcoins(String currency, int coins)
    {
        double dollars = 0;

        try {
            var exchangeRate = GetExchangeRate(currency);

            if(exchangeRate >= 0)
            {
                dollars = exchangeRate * coins;
            }
            else
            {
                dollars = -1;
            }
        }
        catch (IOException ex) {
            dollars = -1;
        }

        return dollars;
    }
}
