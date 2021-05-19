package HTTPServer;

public class ResponseHelper {
    public static boolean checkRouteNotFound(Response response, Route route) {
        if (route == null) {
            setRouteNotFound(response);
            return true;
        }
        return false;
    }

    public static void checkRouteParamsInvalid(Response response, Route route) {
        if (!route.getRouteIsFound()) {
            setRouteNotFound(response);
        }
    }

    public static String getResponseCode(String method, Route route) {
        String responseCode;
        if (!route.getAllow().contains(method)) {
            responseCode = Codes._405.getCode();
        } else {
            responseCode = Codes._200.getCode();
        }
        return responseCode;
    }

    public static Response responseHandler(String method, String path, String body, Response response, Route route) {
        if (ResponseHelper.checkRouteNotFound(response, route)) return response;

        String responseCode = ResponseHelper.getResponseCode(method, route);
        response.setParams(responseCode);

        ResponseHelper.setResponseHeaders(response, route);
        route.performRequest(method, response, body, path);
        ResponseHelper.checkRouteParamsInvalid(response, route);

        return response;
    }

    public static void setResponseHeaders(Response response, Route route) {
        for (String header : route.getHeaders()) {
            response.addHeader(header);
        }
    }

    public static void setRouteNotFound(Response response) {
        response.setParams(Codes._404.getCode());
        response.setBody("");
    }
}
