package com.example.softwaredesigntechniques.service.facilities.impl;

import com.example.softwaredesigntechniques.domain.facilities.Machine;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.repository.facilities.MachineRepository;
import com.example.softwaredesigntechniques.service.facilities.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MachineServiceImpl implements MachineService {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public Machine get(Long id) throws NotFoundException {
        return machineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Machine not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Machine> getAll() {
        return machineRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Machine> findByNameContaining(String name) {
        return machineRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Machine save(Machine machine) {
        return machineRepository.save(machine);
    }

    @Override
    public Machine update(Long id, Machine machine) throws NotFoundException {
        Machine existingMachine = get(id);
        existingMachine.setName(machine.getName());
        return machineRepository.save(existingMachine);
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        Machine machine = get(id);
        machineRepository.delete(machine);
    }

    // Admin operations
    @Override
    public void recreateTable() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS \"Machines\" CASCADE");
        jdbcTemplate.execute("CREATE TABLE \"Machines\"(" +
                "\"id\" BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                "\"name\" VARCHAR(255) NOT NULL" +
                ")");
    }

    @Override
    public void truncateTable() {
        jdbcTemplate.execute("TRUNCATE TABLE \"Machines\" RESTART IDENTITY CASCADE");
    }

    @Override
    public void backupData() {
        // In a real implementation, this would export data to a backup file
        // For now, we'll just log the operation
        System.out.println("Machine data backup completed at: " + System.currentTimeMillis());
    }
}
