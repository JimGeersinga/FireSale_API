package com.firesale.api.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UrlUtil {
    private UrlUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getBaseUrl() {
        var uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return uri.endsWith("/") ? uri : uri + "/";
    }
}
