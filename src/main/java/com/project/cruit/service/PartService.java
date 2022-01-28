package com.project.cruit.service;

import com.project.cruit.entity.Part;
import com.project.cruit.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartService {
    private final PartRepository partRepository;

    public void saveParts(List<Part> partList) {
        partRepository.saveAll(partList);
    }
}
