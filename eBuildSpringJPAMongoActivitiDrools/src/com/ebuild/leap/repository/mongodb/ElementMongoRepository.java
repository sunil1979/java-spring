package com.ebuild.leap.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ebuild.leap.pojo.Element;

public interface ElementMongoRepository extends MongoRepository<Element,Long> {

}
