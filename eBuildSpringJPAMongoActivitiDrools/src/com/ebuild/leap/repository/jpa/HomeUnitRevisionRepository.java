package com.ebuild.leap.repository.jpa;

import java.math.BigInteger;

import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.pojo.HomeUnitVersion;


public interface HomeUnitRevisionRepository extends JpaRepository<HomeUnitRevision, Long> {

	@Query(name="getLatestRevisionId",value="select revision.revision_id from revision where revision.homeUnitVersion=?1 ORDER BY revision.revisionNumber DESC LIMIT 1",nativeQuery=true)
	//@Query("select r from revision r where revision.homeUnitVersion=?1 ORDER BY revision.revisionNumber DESC LIMIT 1")
	public Long getLatestRevisionId(Long homeUnitVersionId);

}
