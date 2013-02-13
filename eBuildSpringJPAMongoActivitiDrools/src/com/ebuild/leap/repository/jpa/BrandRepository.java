package com.ebuild.leap.repository.jpa;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebuild.leap.pojo.Brand;


public interface BrandRepository extends JpaRepository<Brand, Long> {

}
