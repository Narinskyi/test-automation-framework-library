package com.onarinskyi.environment;

public class Timeout {

    private long implicitWait;
    private long explicitWait;

    public Timeout(String implicitWait, String explicitWait) {
        this.implicitWait = Long.valueOf(implicitWait);
        this.explicitWait = Long.valueOf(explicitWait);
    }

    public long implicitWait() {
        return implicitWait;
    }

    public long explicitWait() {
        return explicitWait;
    }
}
