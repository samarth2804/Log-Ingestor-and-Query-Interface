package com.sam.mapper;

import com.sam.pojo.Log;
import com.sam.pojo.MetaData;
import com.sam.dto.LogDto;
import com.sam.dto.MetaDataDto;
import com.sam.util.TimeStampConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LogMapper {

    TimeStampConverter timeStampConverters;

    public Log getLog(LogDto dto) {
        MetaData metadata = MetaData.builder()
                .parentResourceId(dto.getMetadata().getParentResourceId())
                .build();
        return  Log.builder()
                .level(dto.getLevel())
                .message(dto.getMessage())
                .resourceId(dto.getResourceId())
                .timestamp(timeStampConverters.convert(dto.getTimestamp()))
                .spanId(dto.getSpanId())
                .commit(dto.getCommit())
                .metadata(metadata)
                .build();
    }

    public LogDto getLogDto(Log log) {
        MetaDataDto metadata = MetaDataDto.builder()
                .parentResourceId(log.getMetadata().getParentResourceId())
                .build();
        return  LogDto.builder()
                .level(log.getLevel())
                .message(log.getMessage())
                .resourceId(log.getResourceId())
                .timestamp(timeStampConverters.convert(log.getTimestamp()))
                .spanId(log.getSpanId())
                .commit(log.getCommit())
                .metadata(metadata)
                .build();
    }

    public List<LogDto> getLogDtos(List<Log> logs) {
        return logs.stream().map(this::getLogDto).collect(Collectors.toList());
    }

    public List<Log> getLogs(List<LogDto> logDtos) {
        return logDtos.stream().map(this::getLog).collect(Collectors.toList());
    }

}
