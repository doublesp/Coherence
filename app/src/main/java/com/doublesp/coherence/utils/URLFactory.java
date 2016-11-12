package com.doublesp.coherence.utils;

import okhttp3.HttpUrl;

/**
 * Created by pinyaoting on 10/11/16.
 */

public class URLFactory {

    static final String API_PROTOCOL = "https";
    static final String IMAGE_API_HOST = "image.tmdb.org";
    static final String POSTER_PATH = "t/p/w342%s";
    static final String BACKDROP_PATH = "t/p/w1280%s";

    public static String getPosterRequestURL(String posterPath) {
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder().scheme(API_PROTOCOL).host(
                IMAGE_API_HOST).addPathSegments(String.format(POSTER_PATH, posterPath));
        return urlBuilder.build().toString();
    }

    public static String getBackdropRequestURL(String backdropPath) {
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder().scheme(API_PROTOCOL).host(
                IMAGE_API_HOST).addPathSegments(String.format(BACKDROP_PATH, backdropPath));
        return urlBuilder.build().toString();
    }
}
