package HTTPServer;

public class Response {

    private String params;
    private String headers = "";
    private String body = "";
    private byte[] file = new byte[0];

    private String CRLF = "\r\n";
    private String HTTP_VERSION = "HTTP/1.1 ";

    public void setParams(String code) {
        this.params = HTTP_VERSION + code + CRLF;
    }

    public void addHeader(String header) {
        this.headers += header + CRLF;
    }

    public void setBody(String body) {
        if (body != null) {
            this.body += body;
        }
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getHeaders() {
        return headers;
    }

    public String getStringResponse() {
        return this.params + this.headers + CRLF + this.body;
    }

    public byte[] printHeaders() {
        return (this.params + this.headers + CRLF).getBytes();
    }

    public byte[] printBody() {
        return body.getBytes();
    }

    public byte[] printFile() {
        return file;
    }
}
