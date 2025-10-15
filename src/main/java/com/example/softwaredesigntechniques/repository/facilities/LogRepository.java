package com.example.softwaredesigntechniques.repository.facilities;

import com.example.softwaredesigntechniques.domain.facilities.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
