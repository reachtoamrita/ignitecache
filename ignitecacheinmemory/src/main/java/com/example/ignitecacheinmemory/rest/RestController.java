package com.example.ignitecacheinmemory.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ignitecacheinmemory.dto.HttpRequestData;
import com.example.ignitecacheinmemory.repository.HttpRequestDataRepository;

@org.springframework.web.bind.annotation.RestController
public class RestController
{
    @Autowired
    private HttpRequestDataRepository httpRequestDataRepository;

    @PostMapping("saveRequestData")
    public String saveRequestData(@RequestBody HttpRequestData httpRequestData) {
        System.out.println(httpRequestData);
        httpRequestDataRepository.save(httpRequestData.getRequestId(),httpRequestData);
        return httpRequestData.toString();
    }

    @GetMapping("getRequestData")
    public String getRequestData(@RequestParam Long requestId) {
        System.out.println(requestId);
        HttpRequestData httpRequestData = httpRequestDataRepository.findByRequestId(requestId);
        return httpRequestData.toString();
    }
}
