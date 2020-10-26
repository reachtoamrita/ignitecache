package com.example.ignitecachedurablememory.dto;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Validated
@Data
public class HttpRequestData
{
    @QuerySqlField(index = true)
    Long requestId;

    String coapMethod; // Need to be CoAPMethod

    String uriHost;

    int uriPort;

    String uriPath;

    String uriQuery;

    String accept;

    String payload;
}
