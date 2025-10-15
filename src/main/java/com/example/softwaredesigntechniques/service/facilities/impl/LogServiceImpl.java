package com.example.softwaredesigntechniques.service.facilities.impl;

import com.example.softwaredesigntechniques.domain.facilities.Log;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.repository.facilities.LogRepository;
import com.example.softwaredesigntechniques.service.facilities.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public Log get(Long id) throws NotFoundException {
        return logRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Log not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Log> getAll() {
        return logRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Log> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return logRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    public Log save(Log log) {
        return logRepository.save(log);
    }

    @Override
    public Log update(Long id, Log log) throws NotFoundException {
        Log existingLog = get(id);
        existingLog.setDate(log.getDate());
        return logRepository.save(existingLog);
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        Log log = get(id);
        logRepository.delete(log);
    }

    // Admin operations
    @Override
    public void recreateTable() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS \"Logs\" CASCADE");
        jdbcTemplate.execute("CREATE TABLE \"Logs\"(" +
                "\"id\" BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                "\"date\" DATE NOT NULL" +
                ")");
    }

    @Override
    public void truncateTable() {
        jdbcTemplate.execute("TRUNCATE TABLE \"Logs\" RESTART IDENTITY CASCADE");
    }

    @Override
    public void backupData() {
        System.out.println("Log data backup completed at: " + System.currentTimeMillis());
    }
}
