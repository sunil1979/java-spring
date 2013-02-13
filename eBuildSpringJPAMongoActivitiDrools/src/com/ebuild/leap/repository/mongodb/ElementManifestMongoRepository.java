package com.ebuild.leap.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ebuild.leap.pojo.ElementManifest;

public interface ElementManifestMongoRepository extends MongoRepository<ElementManifest,Long> {

}
