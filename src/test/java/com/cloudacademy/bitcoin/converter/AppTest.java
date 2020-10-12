package com.cloudacademy.bitcoin.converter;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
@RunWith(MockitoJUnitRunner.class)
public class AppTest 
{
    private static final double DELTA = 0;

    private CloseableHttpClient client;
    private CloseableHttpResponse response;
    private StatusLine statusLine;
    private HttpEntity entity;
    private InputStream stream;

    private App app;

    @Before
    public void setUp()
    {
        client = mock(CloseableHttpClient.class);
        response = mock(CloseableHttpResponse.class);
        statusLine = mock(StatusLine.class);
        entity = mock(HttpEntity.class);

        stream = new ByteArrayInputStream("{\"bpi\":{\"USD\":{\"code\":\"USD\",\"rate\":\"10,095.9106\",\"description\":\"United States Dollar\",\"rate_float\":10095.9106},\"NZD\":{\"code\":\"NZD\",\"rate\":\"15,095.5670\",\"description\":\"New Zealand Dollar\",\"rate_float\":15095.567}}}".getBytes());

        app = new App(client);
    }

    @Test
    public void shouldGetNZDExchangeRate() throws IOException
    {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);

        App app = new App(client);
        var actual = app.GetExchangeRate("NZD");

        //assert
        double expected = 15095.5670;
        assertEquals(expected, actual, DELTA);
    }

    @Test
    public void shouldGetUSDExchangeRate() throws IOException
    {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);

        App app = new App(client);
        var actual = app.GetExchangeRate("USD");

        //assert
        double expected = 10095.9106;
        assertEquals(expected, actual, DELTA);
    }

    @Test
    public void shouldConvert1BitcoinToUSD() throws IOException
    {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);

        App app = new App(client);
        var actual = app.ConvertBitcoins("USD", 1);

        //assert
        double expected = 10095.9106;
        assertEquals(expected, actual, DELTA);
    }

    @Test
    public void shouldConvert2BitcoinsToUSD() throws IOException
    {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);

        App app = new App(client);
        var actual = app.ConvertBitcoins("USD", 2);

        //assert
        double expected = 20191.8212;
        assertEquals(expected, actual, DELTA);
    }

    @Test
    public void shouldConvert1BitcoinToNZD() throws IOException
    {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);

        App app = new App(client);
        var actual = app.ConvertBitcoins("NZD", 1);

        //assert
        double expected = 15095.5670;
        assertEquals(expected, actual, DELTA);
    }

    @Test
    public void shouldConvert2BitcoinsToNZD() throws IOException
    {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);

        App app = new App(client);
        var actual = app.ConvertBitcoins("NZD", 2);

        //assert
        double expected = 30191.1340;
        assertEquals(expected, actual, DELTA);
    }

    @Test
    public void shouldReturnNegative1WhenServiceUnavailable() throws IOException
    {
        when(statusLine.getStatusCode()).thenReturn(503);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(client.execute(any(HttpGet.class))).thenReturn(response);

        App app = new App(client);
        var actual = app.ConvertBitcoins("NZD", 2);

        //assert
        double expected = -1;
        assertEquals(expected, actual, DELTA);
    }

}
