package com.ebuild.leap.repository.jpa;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebuild.leap.pojo.CostVersion;


public interface CostVersionRepository extends JpaRepository<CostVersion, Long>{

}
