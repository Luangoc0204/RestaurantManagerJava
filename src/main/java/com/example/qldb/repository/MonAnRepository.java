package com.example.qldb.repository;

import com.example.qldb.entity.MonAn;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonAnRepository extends CrudRepository<MonAn, Integer> {
    @Query("select u from MonAn u where u.isActive = true")
    List<MonAn> findAllByActiveTrue();
}
