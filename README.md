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

## Getting Started

Initialize an `HTTPServer` object and call its `start` method.
`HTTPServer` takes two arguments, a port number integer e.g. `5000`, and a `Router` instance.

- You will need to implement `Route` and `Router` objects according to their respective interfaces. For more details, see the documentation below.
- Request logic is handled a `Route` implementation's `performRequest` method.
- `HTTPServer` creates `ClientHandler` instances, which use `Route` implementations to build responses. 

## Known Bugs

`RequestReader.getRequestAddress` throws `ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1` when request are made through Postman.

## Documentation

### Design Decisions

The first build of this library contained five small files:
- RequestReader (static helper methods)
- Response (state with getters and setters)
- Route (Interface)
- Codes (Enum)
- Headers (Enum)

While light on dependencies and small, it meant a lot of boilerplate was needed to implement the library.
Another significant issue was that implementations were clearly not SOLID, and as they were refactored, it became clear that a lot of the code that was on implementation side should be in the library.

Starting Implementation:
- Main
- HTTPServer (handles sockets with threading)
- ClientHandler (the logic controller, initialized by HTTPServer)
- Routes (directory of Route objects)
- RouteMatcher (single method class that matches paths to Route objects)
- ResponseBuilder (a mix of Route specific logic and abstract static methods)

Starting with the `ResponseBuilder`, it was clear that there was more than one responsibility here, as it handled specific cases like `/redirect`, as well as having knowledge of methods like `GET` and `POST`, otherwise containing static methods.
Adding a `performRequest` method to the `Route` interface allowed route-specific logic to be contained in the implementation of `Route`, allowing everything but static methods to be removed from `ResponseBuilder`. Those methods then moved into the library side as `ResponseHelper`. The two parts of the application were becoming more focused on their single responsibility, and routes were extensible.
It was clear now that the only responsibility implementation side should have was passing `Route`s through `RouteMatcher`, so it was a straightforward task to move `HTTPServer` and `ClientHandler` over, only having to hard-code a socket into `HTTPServer`.
Implementation side now only contains a 15 line `Main` class, `RouteMatcher`, a directory of `Route`s, and resources.
One thing that made this refactor easier is that the application as a whole was designed with many (but not all, case in `ResponseBuilder`) dependencies injected, so that there was great flexibility in designing the library. 

### Next Steps

This implementation of HTTP is not yet compliant, and although making it so is not the aim of this project, there are things that I would implement given more time:
- Adding some common response codes, primarily 201, 202, 400, and 403
- More complete headers, such as Date and Content-Size
- Removing the need to `printBody` as well as `printFile` by removing `file` as a distinct variable in `Response`, and making body a `byte[]`
- Testing the ability of the server to perform other methods like `PUT` and `DELETE` (implementation side)

### Classes

#### HTTPServer

Takes a port number and a `Router` object, and creates new `ClientHandler` objects with server sockets.
Has a single callable method, `start`.

#### ClientHandler

A threaded client socket handler, initialized by HTTPServer with a `Socket` and a `Router`.
Accepts via `InputStream` and outputs through `ByteArrayOutputStream`.
Is the controller class of the package, calling the remaining classes to process the incoming request and construct the outgoing response.
Has a single callable method, `run`.

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

Five static helper methods, which accept and break down a request into its components so that each can be processed individually when constructing a response.

- `getRequest` takes the `InputStream` and returns the request as a String.
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

#### Router

An Interface with a single `getRoute` method, which takes a string path and returns a `Route` object.
