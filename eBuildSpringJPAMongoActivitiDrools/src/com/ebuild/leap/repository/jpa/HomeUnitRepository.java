package com.ebuild.leap.repository.jpa;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebuild.leap.pojo.HomeUnit;


public interface HomeUnitRepository extends JpaRepository<HomeUnit, Long> {

}
