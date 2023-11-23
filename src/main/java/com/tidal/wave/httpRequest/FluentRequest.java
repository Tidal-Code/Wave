package com.tidal.wave.httpRequest;

import com.tidal.utils.exceptions.PropertyHandlerException;
import com.tidal.utils.exceptions.RuntimeTestException;
import com.tidal.utils.propertieshandler.PropertiesFinder;
import okhttp3.*;
import okhttp3.Request;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class FluentRequest {

    private final Function<String, String> timeOut = PropertiesFinder::getProperty;
    private final String BASE_URI = "baseURI";
    private final String MEDIA_TYPE = "mediaType";
    private final String PAYLOAD = "payload";
    private final String RESPONSE_STRING = "responseString";
    private final String QUERY_PARAM_ONE_KEY = "queryParamOneKey";
    private final String QUERY_PARAM_TWO_KEY = "queryParamTwoKey";

    private OkHttpClient CLIENT;
    private Response RESPONSE;
    private okhttp3.Request HTTP_REQUEST;
    private HashMap<String, Object> DATA_MAP;
    private Map<String, Object> HEADER_MAP;
    private Headers REQUEST_HEADERS;
    private HttpUrl.Builder builder;

    private final UnaryOperator<String> readTimeOut = s -> {
        try {
            return timeOut.apply(s) == null ? "10" : timeOut.apply(s);
        } catch (PropertyHandlerException ignored) {
            return "10";
        }
    };

    public FluentRequest() {
        createMap();
        CLIENT = getNewOkHttpClient();
    }

    /**
     * Method to set your own custom HttpRequest in case the built-in builder is not sufficient
     * for your request
     */
    public void setHttpRequest(okhttp3.Request builtRequest) {
        HTTP_REQUEST = builtRequest;
    }

    /**
     * This method sets the Base URI with the RequestSpecification and in turn overrides
     * the Base URI value if it is already present
     *
     * @param baseUri the base uri for the request to be made
     */
    public FluentRequest set(String baseUri) {
        DATA_MAP.put(BASE_URI, baseUri);
        return this;
    }

    /**
     * Sets the base Uri as a rest assured property instead of a request specification instance.
     * This would enable the creation of a new instance of request specification without deleting
     * the base Uri
     *
     * @param baseUri the base uri or end point
     */
    public FluentRequest setBaseUri(String baseUri) {
        DATA_MAP.put(BASE_URI, baseUri);
        return this;
    }

    /**
     * Sets the media type as json or xml or other types
     *
     * @param mediaType media type
     */
    public FluentRequest setMediaType(String mediaType) {
        DATA_MAP.put(MEDIA_TYPE, mediaType);
        return this;
    }

    private OkHttpClient getNewOkHttpClient() {
        return new OkHttpClient()
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(Integer.parseInt(readTimeOut.apply("connection.timeout"))))
                .readTimeout(Duration.ofSeconds(Integer.parseInt(readTimeOut.apply("read.timeout"))))
                .writeTimeout(Duration.ofSeconds(Integer.parseInt(readTimeOut.apply("write.timeout"))))
                .callTimeout(Duration.ofSeconds(Integer.parseInt(readTimeOut.apply("call.timeout"))))
                .build();
    }

    /**
     * Sets header values to the request. There is no limit to the number of headers
     *
     * @param key   header key
     * @param value header value
     */
    public FluentRequest setHeader(String key, Object value) {
        HEADER_MAP.put(key, value);
        return this;
    }

    /**
     * Sets the query params. Can only add a maximum of two
     *
     * @param key   query param key
     * @param value query param value
     */
    public FluentRequest setQueryParams(String key, Object value) {
        if (DATA_MAP.get(QUERY_PARAM_ONE_KEY) == null) {
            DATA_MAP.put(QUERY_PARAM_ONE_KEY, key);
            DATA_MAP.put("queryParamOneValue", value);
        } else if (DATA_MAP.get(QUERY_PARAM_TWO_KEY) == null) {
            DATA_MAP.put(QUERY_PARAM_TWO_KEY, key);
            DATA_MAP.put("queryParamTwoValue", value);
        }
        return this;
    }

    public FluentRequest setQueryParams2(String key, Object value) {
        builder.addQueryParameter((String) DATA_MAP.get(QUERY_PARAM_ONE_KEY), (String) DATA_MAP.get("queryParamOneValue"));
        return this;
    }

    public FluentRequest setPayload(String payload) {
        DATA_MAP.put(PAYLOAD, payload);
        return this;
    }

    /**
     * A data context map set up to carry data across steps
     *
     * @param key   key for assigning value
     * @param value assigned value
     * @param <T>   type of 'value' set
     */
    public <T> FluentRequest setData(String key, T value) {
        DATA_MAP.put(key, value);
        return this;
    }

    public <T> FluentRequest setData(DataEnum data, T value) {
        DATA_MAP.put(data.getValue(), value);
        return this;
    }

    /**
     * Retrieves the data from a static context map and at the same time ensured thread safety
     *
     * @param key key to assign the value
     * @param <T> object type
     * @return a static map in context of the current thread
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key) {
        return (T) DATA_MAP.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getData(DataEnum data) {
        T value = (T) DATA_MAP.get(data.getValue());
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }


    /**
     * Sends the request to the end point with a Uri path
     *
     * @param reqType specifies Get, Post, Delete etc...
     */
    public FluentRequest send(ReqType reqType) {

        MediaType mediaType = MediaType.parse("application/json");
        if (DATA_MAP.get(MEDIA_TYPE) != null) {
            mediaType = MediaType.parse((String) DATA_MAP.get(MEDIA_TYPE));
        }
        RequestBody body = RequestBody.create("", mediaType);
        if (DATA_MAP.get(PAYLOAD) != null) {
            body = RequestBody.create((String) DATA_MAP.get(PAYLOAD), mediaType);
        }
        applyHeaders();

        if (HTTP_REQUEST == null) {
            Request.Builder requestBuilder = new Request.Builder().url(queryBuilder().build());

            switch (reqType) {
                case GET:
                    HTTP_REQUEST = requestBuilder
                            .get()
                            .headers(REQUEST_HEADERS)
                            .build();
                    break;
                case HEAD:
                    HTTP_REQUEST = requestBuilder
                            .head()
                            .headers(REQUEST_HEADERS)
                            .build();
                    break;
                case DELETE:
                    HTTP_REQUEST = requestBuilder
                            .delete()
                            .headers(REQUEST_HEADERS)
                            .build();
                    break;
                default:
                    HTTP_REQUEST = requestBuilder
                            .method(reqType.getReqType().toUpperCase(Locale.ROOT), body)
                            .headers(REQUEST_HEADERS)
                            .build();
            }
        }

        try {
            RESPONSE = CLIENT.newCall(HTTP_REQUEST).execute();
        } catch (IOException e) {
            throw new RuntimeTestException("IOException with request" + e.getMessage());
        }
        return this;
    }

    private HttpUrl.Builder queryBuilder() {
        HttpUrl.Builder builder = HttpUrl.get((String) DATA_MAP.get(BASE_URI)).newBuilder();
        if (DATA_MAP.get(QUERY_PARAM_ONE_KEY) != null) {
            builder.addQueryParameter((String) DATA_MAP.get(QUERY_PARAM_ONE_KEY), (String) DATA_MAP.get("queryParamOneValue"));
        }
        if (DATA_MAP.get(QUERY_PARAM_TWO_KEY) != null) {
            builder.addQueryParameter((String) DATA_MAP.get(QUERY_PARAM_TWO_KEY), (String) DATA_MAP.get("queryParamTwoValue"));
        }
        return builder;
    }

    private void applyHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        for (String key : HEADER_MAP.keySet()) {
            headerBuilder.add(key, (String) Objects.requireNonNull(HEADER_MAP.get(key)));
        }

        REQUEST_HEADERS = headerBuilder.build();
    }

    public Response response() {
        if (RESPONSE == null) {
            throw new RuntimeTestException("Response is null : Check if the request is sent");
        }
        return RESPONSE;
    }

    public int getStatusCode() {
        if (RESPONSE == null) {
            throw new RuntimeTestException("Status code is null : Check if the request is sent");
        }
        return RESPONSE.code();
    }

    public String getResponseString() {
        if (RESPONSE == null) {
            throw new RuntimeTestException("Response string is null : Check if the request is sent");
        }

        try {
            //The response has to be stored because the default response.body().string() is auto-closeable
            if (DATA_MAP.get(RESPONSE_STRING) == null) {
                DATA_MAP.put(RESPONSE_STRING, RESPONSE.body().string());
            }
            return (String) DATA_MAP.get(RESPONSE_STRING);
        } catch (IOException e) {
            throw new RuntimeTestException("IO Exception with response: " + e.getMessage());
        }
    }

    private void createMap() {
        if (DATA_MAP == null) {
            DATA_MAP = new HashMap<>();
        }

        if (HEADER_MAP == null) {
            HEADER_MAP = new HashMap<>();
        }
    }
}
