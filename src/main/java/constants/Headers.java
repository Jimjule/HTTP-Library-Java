package constants;

public enum Headers {
    ALLOW("Allow: "),
    CONTENT_TYPE("Content-Type: "),
    CONTENT_TYPE_GIF(CONTENT_TYPE.getHeader() + "image/gif"),
    CONTENT_TYPE_HTML(CONTENT_TYPE.getHeader() + "text/html;charset=utf-8"),
    CONTENT_TYPE_JPEG(CONTENT_TYPE.getHeader() + "image/jpeg"),
    CONTENT_TYPE_JSON(CONTENT_TYPE.getHeader() + "application/json;charset=utf-8"),
    CONTENT_TYPE_PNG(CONTENT_TYPE.getHeader() + "image/png"),
    CONTENT_TYPE_TEXT(CONTENT_TYPE.getHeader() + "text/plain;charset=utf-8"),
    CONTENT_TYPE_XML(CONTENT_TYPE.getHeader() + "application/xml;charset=utf-8"),
    LOCATION("Location: ");

    private final String header;

    Headers(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
