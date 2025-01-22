package com.sam.controller;

import com.sam.dto.FilterDto;
import com.sam.dto.LogDto;
import com.sam.exception.ApiException;
import com.sam.service.LogsService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Log Operations")
public class LogController {

    LogsService logsService;

    @Hidden
    @PostMapping
    public ResponseEntity<Void> saveLog(@RequestBody @Valid LogDto logDto) throws ApiException {
        logsService.produceLog(logDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Log Search",
            description = "Search logs using search string and filter." +
                    "To add filter for a field, specify the value in the request body, otherwise keep it null"
    )
    @PostMapping("logs-search")
    public ResponseEntity<List<LogDto>> getLogsFilter(@RequestParam(value = "searchString", required = false) String searchString,
                                                      @RequestBody @Valid FilterDto filter)
            throws ApiException {
        return new ResponseEntity<>(logsService.seacrhLogs(searchString, filter), HttpStatus.OK);
    }

}

