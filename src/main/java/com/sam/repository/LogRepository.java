package com.sam.repository;


import com.sam.pojo.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(rollbackFor = Exception.class)
public interface LogRepository extends MongoRepository<Log, String> {

}