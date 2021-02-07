package org.tosi29;

public class EndPoint {
    private String path;
    private String method;

    public EndPoint(String path, String method) {
        this.path = path;
        this.method = method;
    }

    @Override
    public String toString() {
        return "EndPoint{" +
                "path='" + path + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
