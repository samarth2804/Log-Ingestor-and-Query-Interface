package com.sam.service;

import com.sam.dto.FilterDto;
import com.sam.dto.LogDto;
import com.sam.exception.ApiException;
import com.sam.pojo.Log;

import java.io.IOException;
import java.util.List;


public interface LogsService {

    void produceLog(LogDto logDto) throws ApiException;
    void consumeAndSaveLogs(List<Log> log) throws ApiException, IOException;
    List<LogDto> seacrhLogs(String searchString, FilterDto filter) throws ApiException;
}
