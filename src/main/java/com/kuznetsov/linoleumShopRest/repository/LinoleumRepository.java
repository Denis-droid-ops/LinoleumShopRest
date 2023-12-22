package com.kuznetsov.linoleumShopRest.repository;

import com.kuznetsov.linoleumShopRest.entity.Linoleum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinoleumRepository extends JpaRepository<Linoleum,Integer> {

}
