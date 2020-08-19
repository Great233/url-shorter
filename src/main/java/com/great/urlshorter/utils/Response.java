package com.great.urlshorter.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Great
 */
public class Response<T> {

    public static <T> ResponseEntity<T> respond(T body,
                                                HttpStatus status,
                                                MultiValueMap<String, String> headers) {
        return new ResponseEntity<T>(body, headers, status);
    }

    public static <T> ResponseEntity<Map<String, Object>> ok(T body) {
        return ok("success", body);
    }

    public static <T> ResponseEntity<Map<String, Object>> ok(String message, T body) {
        Map<String, Object> response = new HashMap<>(2);
        response.put("message", message);
        response.put("data", body);
        return respond(response, HttpStatus.OK, null);
    }

    public static <T> ResponseEntity<Map<String, Object>> created(T body) {
        return created("success", body);
    }

    public static <T> ResponseEntity<Map<String, Object>> created(String message, T body) {
        Map<String, Object> response = new HashMap<>(2);
        response.put("message", message);
        response.put("data", body);
        return respond(response, HttpStatus.CREATED, null);
    }

    public static <T> ResponseEntity<T> noContent() {
        return respond(null, HttpStatus.NO_CONTENT, null);
    }

    public static ResponseEntity.BodyBuilder redirect(String url) throws URISyntaxException {
        URI uri = new URI(url);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(uri);
    }

    public static <T> ResponseEntity<Map<String, Object>> notFound(T body) {
        return notFound("resource not found", body);
    }

    public static <T> ResponseEntity<Map<String, Object>> notFound(String message, T body) {
        Map<String, Object> response = new HashMap<>(2);
        response.put("message", message);
        response.put("data", body);
        return respond(response, HttpStatus.NOT_FOUND, null);
    }

    public static <T> ResponseEntity<Map<String, Object>> badRequest(T body) {
        return badRequest("bad request", body);
    }

    public static <T> ResponseEntity<Map<String, Object>> badRequest(String message, T body) {
        Map<String, Object> response = new HashMap<>(2);
        response.put("message", message);
        response.put("data", body);
        return respond(response, HttpStatus.BAD_REQUEST, null);
    }

    public static <T> ResponseEntity<Map<String, Object>> serverError(T body) {
        return badRequest("server error", body);
    }

    public static <T> ResponseEntity<Map<String, Object>> serverError(String message, T body) {
        Map<String, Object> response = new HashMap<>(2);
        response.put("message", message);
        response.put("data", body);
        return respond(response, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
}
