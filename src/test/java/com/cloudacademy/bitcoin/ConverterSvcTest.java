package com.cloudacademy.bitcoin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;

import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.ParseException;

/**
 * Unit test for simple ConverterSvc.
 */
public class ConverterSvcTest 
{
    private static final double DELTA = 0;

    private CloseableHttpClient client;
    private CloseableHttpResponse response;
    private StatusLine statusLine;
    private HttpEntity entity;
    private InputStream stream;

    private ConverterSvc converterSvc;

    @BeforeEach
    public void setUp() {
        client = mock(CloseableHttpClient.class);
        response = mock(CloseableHttpResponse.class);
        statusLine = mock(StatusLine.class);
        entity = mock(HttpEntity.class);

        stream = new ByteArrayInputStream("{\"bpi\":{\"USD\":{\"code\":\"USD\",\"rate\":\"10,095.9106\",\"description\":\"United States Dollar\",\"rate_float\":10095.9106},\"NZD\":{\"code\":\"NZD\",\"rate\":\"15,095.5670\",\"description\":\"New Zealand Dollar\",\"rate_float\":15095.567}}}".getBytes());

        converterSvc = new ConverterSvc(client);
    }

    @Test
    public void shouldGetNZDExchangeRate() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);

        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.GetExchangeRate(ConverterSvc.Currency.NZD);

        //assert
        double expected = 15095.5670;
        Assertions.assertEquals(expected, actual, DELTA);
    }

    @Test
    public void shouldGetUSDExchangeRate() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.GetExchangeRate(ConverterSvc.Currency.USD);
    
        //assert
        double expected = 10095.9106;
        Assertions.assertEquals(expected, actual, DELTA);
    }

    @Test
    public void shouldConvert1BitcoinToUSD() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.ConvertBitcoins(ConverterSvc.Currency.USD, 1);
    
        //assert
        double expected = 10095.9106;
        Assertions.assertEquals(expected, actual, DELTA);
    }
    
    @Test
    public void shouldConvert2BitcoinsToUSD() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.ConvertBitcoins(ConverterSvc.Currency.USD, 2);
    
        //assert
        double expected = 20191.8212;
        Assertions.assertEquals(expected, actual, DELTA);
    }
    
    @Test
    public void shouldConvert1BitcoinToNZD() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.ConvertBitcoins(ConverterSvc.Currency.NZD, 1);
    
        //assert
        double expected = 15095.5670;
        Assertions.assertEquals(expected, actual, DELTA);
    }
    
    @Test
    public void shouldConvert2BitcoinsToNZD() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.ConvertBitcoins(ConverterSvc.Currency.NZD, 2);
    
        //assert
        double expected = 30191.1340;
        Assertions.assertEquals(expected, actual, DELTA);
    }

    @Test
    public void shouldThrowExceptionWhenBitcoinsLessThanZero() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        ConverterSvc converterSvc = new ConverterSvc(client);
        Assertions.assertThrows(IllegalArgumentException.class, () -> converterSvc.ConvertBitcoins(ConverterSvc.Currency.NZD, -1));
    }

    @Test
    public void shouldReturnNegative1WhenServiceUnavailable() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(503);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.ConvertBitcoins(ConverterSvc.Currency.NZD, 2);
    
        //assert
        double expected = -1;
        Assertions.assertEquals(expected, actual, DELTA);
    }

    @Test
    public void shouldReturnNegative1WhenExecuteThrowsIOException() throws IOException {
        when(client.execute(any(HttpGet.class))).thenThrow(IOException.class);
    
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.GetExchangeRate(ConverterSvc.Currency.NZD);
    
        //assert
        double expected = -1;
        Assertions.assertEquals(expected, actual, DELTA);
    }

    @Test
    public void shouldReturnNegative1WhenCloseThrowsIOException() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);

        doThrow(IOException.class).when(response).close();
    
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.GetExchangeRate(ConverterSvc.Currency.NZD);
    
        //assert
        double expected = -1;
        Assertions.assertEquals(expected, actual, DELTA);
    }
}
