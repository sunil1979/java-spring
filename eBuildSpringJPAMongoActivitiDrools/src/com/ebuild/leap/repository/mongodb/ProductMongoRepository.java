package com.ebuild.leap.repository.mongodb;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.Project;

public interface ProductMongoRepository extends MongoRepository<Product,Long> {
	List<Product> findByProject(Project project);
}
