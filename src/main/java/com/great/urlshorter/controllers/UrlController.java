package com.great.urlshorter.controllers;

import com.great.urlshorter.entities.Url;
import com.great.urlshorter.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.great.urlshorter.utils.Response;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * @author Great
 */
@RestController
public class UrlController {

    private final UrlService urlService;

    @Value("${app.domain}")
    private String domain;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{id}")
    public void redirect(HttpServletResponse response,
                         @PathVariable String id) {
        try {
            if ("".equals(id) || id == null) {
                response.sendError(404);
                return;
            }
            Url url = urlService.getByEncodedId(id);
            if (url == null) {
                response.sendError(404);
                return;
            }
            response.sendRedirect(url.getUrl());
        } catch (Exception e) {
            response.setStatus(500);
        }
    }

    @PostMapping("/urls")
    public ResponseEntity<Map<String, Object>> compress(@RequestBody Url url) {
        try {
            String id = urlService.save(url);
            return Response.created(String.format("%s%s", domain, id));
        } catch (Exception e) {
            return Response.badRequest(e.getMessage());
        }
    }

}
