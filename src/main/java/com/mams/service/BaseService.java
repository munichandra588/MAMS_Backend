package com.mams.service;

import com.mams.entity.Base;
import com.mams.exception.ResourceNotFoundException;
import com.mams.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseService {

    private final BaseRepository baseRepository;

    public BaseService(BaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    public List<Base> getAllBases() {
        return baseRepository.findAll();
    }

    public Base getBaseById(Long id) {
        return baseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Base not found with id: " + id));
    }

    public Base createBase(Base base) {
        return baseRepository.save(base);
    }

    public Base updateBase(Long id, Base updated) {
        Base existing = getBaseById(id);
        existing.setBaseCode(updated.getBaseCode());
        existing.setBaseName(updated.getBaseName());
        existing.setLocation(updated.getLocation());
        existing.setStatus(updated.getStatus());
        return baseRepository.save(existing);
    }
}
