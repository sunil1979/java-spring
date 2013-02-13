package com.ebuild.leap.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ebuild.leap.pojo.HomeUnitRevision;

public interface HomeUnitRevisionMongoRepository extends MongoRepository<HomeUnitRevision,Long>{
	
}
