package com.onarinskyi.environment;

import java.util.Arrays;

public enum OperatingSystem {

    WINDOWS,
    MACOS;

    public static OperatingSystem current() {
        String systemOs = System.getProperty("os.name");

        if (systemOs.contains(" ")) {
            systemOs = systemOs.substring(0, systemOs.indexOf(" "));
        }

        final String os = systemOs;

        return Arrays.stream(OperatingSystem.values())
                .filter(constant -> constant.name().toLowerCase().contains(os.toLowerCase()))
                .findFirst().orElse(WINDOWS);
    }
}