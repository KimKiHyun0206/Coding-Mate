package com.codingmate.auth.repository;

import com.codingmate.auth.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Authority 엔티티에 대한 JPA 기능을 사용하기 위한 레포지토리
 *
 * @author duskafka
 * */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}