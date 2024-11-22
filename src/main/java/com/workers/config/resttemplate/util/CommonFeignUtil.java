package com.workers.config.resttemplate.util;

public class CommonFeignUtil {

    public static String getSpecificMessage(Exception ex) {
        var lastOne = ex.getMessage().split(":").length - 1;
        return ex.getMessage().split(":")[lastOne].trim()
                .replaceAll("\\]", "")
                .replaceAll("\\[", "")
                .replace("\"", "");
    }
}
