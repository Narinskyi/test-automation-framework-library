package com.onarinskyi.environment;

import java.util.Arrays;

public enum Browser {

    CHROME,
    IE,
    EDGE,
    FIREFOX,
    HEADLESS,
    MOBILE_EMULATOR_CHROME,
    TABLET_EMULATOR_CHROME;

    public static Browser of(String type) {
        return Arrays.stream(Browser.values())
                .filter(constant -> constant.name().toLowerCase().contains(type))
                .findFirst().get();
    }
}
