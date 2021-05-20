# HTTP Server - Java

A hand-rolled HTTP Library.

## Installation
 
 - Clone this repository to your directory
 - Navigate to `HTTP-Library-Java`
 
## Testing
 
 - In the console, run `./gradlew test`
 - For test coverage report, run `./gradlew jacocoTestReport`
 - Results are found in `/build/reports/jacoco/test/html/index.html`, and can be read in browser
 
## Compiling
 
Run: `./gradlew build`

Find the output file in: `./build/libs/HTTP-Library-Java-1.0-SNAPSHOT.jar`  

## Documentation

### Contents

#### Response

An Object that constructs and formats responses to requests, containing:

- String `params`, containing the HTTP version and response code
- String `headers`, containing other pre-body information such as content type and allowed methods
- String `body`, for returning text as response body
- Byte array `file`, for returning anything other than text as response body
- Getter and setter methods for these variables
- Methods starting with `get` return Strings - for testing
- Methods starting with `print` return byte arrays - for constructing responses

#### RequestReader

Four static helper methods, which break down a request into its components so that each can be processed individually when constructing a response.

- `getRequestParams` takes the whole request as a String, and returns the first line also as a String, containing the request method and address/route
- `getRequestMethod` and `getRequestAddress` take the String returned by `getRequestParams` and return the method and address respectively
- `getRequestBody` takes the whole request as a String, and returns the body also as a String

The results of `getRequestMethod`, `getRequestAddress`, and `getRequestBody`, along with an instance of `Response`, are everything needed to construct a response with `ResponseHelper` and an implementation of `Route`.

#### ResponseHelper

Six static helper methods, which handle the construction of response headers.
For code examples, check `ResponseHelperTest.java`.

- `responseHandler` takes the method, path, and body, as well as a `Response` and `Route`, and returns the final response. The other methods are all contained in `responseHandler`, so should only be needed if this method is not used
- `checkRouteNotFound` takes a `Response` instance and a `Route` implementation, and returns whether or not the `Route` is `null`. If it is, it sets the response to a `404`, to be immediately returned
- Example usage `if (ResponseHelper.checkRouteNotFound(response, route)) return response;`
- `checkRouteParamsFound` works similarly, tracking whether route params are invalid
- `getResponseCode` returns either a `200` or a `405` header for a valid `Route` and method
- `setResponseHeaders` populates `Response` headers with `Route` headers
- `setRouteNotFound` is the method called by `checkRouteNotFound` if the `Route` is invalid, taking a `Response` and setting it to a `404`

#### Route

An Interface for route logic, mostly containing getters and setters for:

- A string body, initialisation optional (e.g. `Hello world!`)
- An blank list of headers (default `new ArrayList<>()`)
- A fixed list of allowed methods `Arrays.asList("GET", "POST", "HEAD")`
- A boolean for tracking validity of routes (default `null`)

It also contains the `performRequest` method, which should handle the request method (GET, POST, ...). This is the method that should handle setting the body of the response.
