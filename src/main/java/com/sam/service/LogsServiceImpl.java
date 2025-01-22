package com.sam.service;

import com.sam.dto.FilterDto;
import com.sam.dto.LogDto;
import com.sam.dto.MetaDataDto;
import com.sam.exception.ApiException;
import com.sam.exception.ErrorCode;
import com.sam.pojo.Log;
import com.sam.mapper.LogMapper;
import com.sam.repository.LogRepository;
import com.sam.util.TimeStampConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;

import static com.sam.constants.Constants.GROUP_ID;
import static com.sam.constants.Constants.TOPIC_NAME;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class LogsServiceImpl implements LogsService {

    LogRepository logRepository;
    LogMapper logMapper;
    MongoTemplate mongoTemplate;
    TimeStampConverter timeStampConverter;
    KafkaTemplate<String, Log> kafkaTemplate;
    @Override
    public void produceLog(LogDto logDto) {
        Log log = logMapper.getLog(logDto);
        kafkaTemplate.send(TOPIC_NAME, log);
    }
    @Override
    @Async
    @KafkaListener(topics = {TOPIC_NAME}, groupId = GROUP_ID)
    public void consumeAndSaveLogs(List<Log> logs) {
        logRepository.saveAll(logs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogDto> seacrhLogs(String searchString, FilterDto filter) throws ApiException {
        Map<String, Object> filterMap = getFilterMap(filter);
        Criteria criteria = new Criteria();
        if(searchString != null) {
            this.applyRegex(searchString, criteria);
        }
        this.applyTimeRange(filter, criteria);
        this.applyFilter(filterMap, criteria);
        List<Log> logs = mongoTemplate.find(new Query(criteria), Log.class);
        return logMapper.getLogDtos(logs);
    }


    private Map<String, Object> getFilterMap(FilterDto filter) throws ApiException {
        Map<String, Object> filterMap = new HashMap<>();
        for (Field field : FilterDto.class.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue = null;
            try {
                fieldValue = field.get(filter);
            } catch (IllegalAccessException e) {
                throw new ApiException("Illegal Access", ErrorCode.INTERNAL_SERVER_ERROR);
            }
            if(fieldValue == null) {
                continue;
            }
            filterMap.put(fieldName, fieldValue);
        }
        return filterMap;
    }

    private void applyRegex(String searchString, Criteria criteria) {
        List<Criteria> fieldCriteria = new ArrayList<>();
        for(Field field : Log.class.getDeclaredFields()) {
            String fieldName = field.getName();
            if(fieldName.equals("metadata")) {
                fieldCriteria.add(Criteria.where("metadata.parentResourceId").regex(searchString, "i"));continue;
            }
            if(fieldName.equals("timestamp")) {
                continue;
            }
            fieldCriteria.add(Criteria.where(fieldName).regex(searchString, "i"));
        }
        criteria.orOperator(fieldCriteria);
    }

    private void applyFilter(Map<String, Object> filterMap, Criteria criteria) {
        for(Map.Entry<String, Object> entry : filterMap.entrySet())  {
            String fieldName = entry.getKey();
            Object value = entry.getValue();
            if(fieldName.equals("metadata")) {
                MetaDataDto metaData = (MetaDataDto) value;
                if (metaData.getParentResourceId() != null) {
                    criteria.and("metadata.parentResourceId").is(metaData.getParentResourceId());
                }
                continue;
            }
            if(fieldName.equals("startTime") || fieldName.equals("endTime")) {
                continue;
            }
            criteria.and(fieldName).is(value);
        }
    }

    private void applyTimeRange(FilterDto filterDto, Criteria criteria) {
        if(filterDto.getStartTime() == null || filterDto.getEndTime() == null) {
            return;
        }
        criteria.and("timestamp").gte(timeStampConverter.convert(filterDto.getStartTime()))
                .lte(timeStampConverter.convert(filterDto.getEndTime()));

    }

}