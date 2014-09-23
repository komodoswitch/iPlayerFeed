package com.linxmap.iplayerfeed.feed.http;


public class HttpConnectionResponse {
    private String httpResponseBody;
    private HttpConnectionManager.HttpConnectionResponseResult httpConnectionResponseResult;

    public HttpConnectionResponse(String httpResponseBody, HttpConnectionManager.HttpConnectionResponseResult httpConnectionResponseResult) {
        this.httpResponseBody = httpResponseBody;
        this.httpConnectionResponseResult = httpConnectionResponseResult;

    }

    public String getHttpResponseBody() {
        return httpResponseBody;
    }

    public HttpConnectionManager.HttpConnectionResponseResult getHttpConnectionResponseResult() {
        return httpConnectionResponseResult;
    }


}
