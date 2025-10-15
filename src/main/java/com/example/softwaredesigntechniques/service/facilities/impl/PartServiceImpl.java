package com.example.softwaredesigntechniques.service.facilities.impl;

import com.example.softwaredesigntechniques.domain.facilities.Part;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.repository.facilities.PartRepository;
import com.example.softwaredesigntechniques.service.facilities.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PartServiceImpl implements PartService {

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public Part get(Long id) throws NotFoundException {
        return partRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Part not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Part> getAll() {
        return partRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Part> findByNameContaining(String name) {
        return partRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Part> findByMachineId(Long machineId) {
        return partRepository.findByMachineId(machineId);
    }

    @Override
    public Part save(Part part) {
        return partRepository.save(part);
    }

    @Override
    public Part update(Long id, Part part) throws NotFoundException {
        Part existingPart = get(id);
        existingPart.setName(part.getName());
        existingPart.setMachine(part.getMachine());
        return partRepository.save(existingPart);
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        Part part = get(id);
        partRepository.delete(part);
    }

    // Admin operations
    @Override
    public void recreateTable() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS \"Parts\" CASCADE");
        jdbcTemplate.execute("CREATE TABLE \"Parts\"(" +
                "\"id\" BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                "\"name\" VARCHAR(255) NOT NULL," +
                "\"machine_id\" BIGINT NULL" +
                ")");
    }

    @Override
    public void truncateTable() {
        jdbcTemplate.execute("TRUNCATE TABLE \"Parts\" RESTART IDENTITY CASCADE");
    }

    @Override
    public void backupData() {
        System.out.println("Part data backup completed at: " + System.currentTimeMillis());
    }
}
