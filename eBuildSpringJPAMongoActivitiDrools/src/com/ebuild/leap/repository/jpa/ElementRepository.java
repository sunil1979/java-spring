package com.ebuild.leap.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ebuild.leap.pojo.Element;

public interface ElementRepository extends JpaRepository<Element, Long>, JpaSpecificationExecutor<Element> {

}
