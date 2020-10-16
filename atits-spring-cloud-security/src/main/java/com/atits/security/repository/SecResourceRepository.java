package com.atits.security.repository;

import com.atits.security.entity.SecResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 资源
 *
 * @author zhangys
 * @date 2020-10-14 16:58
 **/
public interface SecResourceRepository extends JpaRepository<SecResourceEntity,String>, JpaSpecificationExecutor<SecResourceEntity> {


}