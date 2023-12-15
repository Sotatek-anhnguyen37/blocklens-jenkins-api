package web_core;

public enum DeviceMetrics {
    IPHONE_5("iPhone 5", 320, 568, 2.0, "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1"),
    IPHONE_6("iPhone 6", 375, 667, 2.0, "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1"),
    IPHONE_6_PLUS("iPhone 6 Plus", 414, 736, 3.0, "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1"),
    IPAD_MINI("iPad Mini", 768, 1024, 2.0, "Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1"),
    IPAD_PRO("iPad Pro", 1366, 1024, 2.0, "Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1"),
    NEXUS_6("Nexus 6", 412, 732, 3.5, "Mozilla/5.0 (Linux; Android 7.1.1; Nexus 6 Build/N6F26U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/%s Mobile Safari/537.36");

    private String deviceName, userAgent;
    private int width, height;
    private double ratio;

    DeviceMetrics(String deviceName, int width, int height, double ratio, String userAgent) {
        this.deviceName = deviceName;
        this.width = width;
        this.height = height;
        this.ratio = ratio;
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return deviceName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getRatio() {
        return ratio;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
