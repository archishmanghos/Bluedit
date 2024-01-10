package com.semicolonised.bluedit.repository;

import com.semicolonised.bluedit.model.SubBluedit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubBlueditRepository extends JpaRepository<SubBluedit, Long> {
}
