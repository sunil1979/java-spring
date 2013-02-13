package com.ebuild.leap.repository.jpa;

import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebuild.leap.pojo.Theme;


public interface ThemeRepository extends JpaRepository<Theme, Long> {

}
