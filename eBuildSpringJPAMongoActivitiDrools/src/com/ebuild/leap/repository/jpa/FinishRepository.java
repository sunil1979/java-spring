package com.ebuild.leap.repository.jpa;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebuild.leap.pojo.Finish;


public interface FinishRepository extends JpaRepository<Finish, Long> {

}
