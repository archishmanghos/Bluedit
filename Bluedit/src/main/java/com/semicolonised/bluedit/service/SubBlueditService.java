package com.semicolonised.bluedit.service;

import com.semicolonised.bluedit.dto.SubBlueditDTO;
import com.semicolonised.bluedit.exception.BlueditException;
import com.semicolonised.bluedit.mapper.SubBlueditMapper;
import com.semicolonised.bluedit.model.SubBluedit;
import com.semicolonised.bluedit.repository.SubBlueditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubBlueditService {
    private final SubBlueditRepository subBlueditRepository;
    private final SubBlueditMapper subBlueditMapper;

    @Transactional
    public SubBlueditDTO save(SubBlueditDTO subBlueditDTO) {
        SubBluedit subBluedit = subBlueditRepository.save(subBlueditMapper.mapDTOtoSubBluedit(subBlueditDTO));
        subBlueditDTO.setId(subBluedit.getId());
        return subBlueditDTO;
    }

    @Transactional(readOnly = true)
    public List<SubBlueditDTO> getAll() {
        return subBlueditRepository.findAll()
                .stream()
                .map(subBlueditMapper::mapSubBlueditToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubBlueditDTO getSubBlueditById(Long id) {
        SubBluedit subBluedit = subBlueditRepository.findById(id).orElseThrow(() -> new BlueditException("Subbluedit not found with id -" + id));
        return subBlueditMapper.mapSubBlueditToDTO(subBluedit);
    }
}
