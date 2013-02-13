package com.ebuild.leap.repository.jpa;

import java.util.List;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ebuild.leap.pojo.HomeUnit;
import com.ebuild.leap.pojo.HomeUnitVersion;


public interface HomeUnitVersionRepository extends JpaRepository<HomeUnitVersion, Long> {

	//@Query("SELECT huVersion FROM HomeUnitVersion huVersion WHERE huVersion.homeUnit.id=?1")
	public List<HomeUnitVersion> findByHomeUnit_Id(Long homeUnitId);
}
