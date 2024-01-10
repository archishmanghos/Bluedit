package com.semicolonised.bluedit.controller;

import com.semicolonised.bluedit.dto.SubBlueditDTO;
import com.semicolonised.bluedit.service.SubBlueditService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subbluedit")
@AllArgsConstructor
public class SubBlueditController {
    private final SubBlueditService subBlueditService;

    @PostMapping
    public ResponseEntity<SubBlueditDTO> createSubBluedit(@RequestBody SubBlueditDTO subBlueditDTO) {
        return new ResponseEntity<>(subBlueditService.save(subBlueditDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public List<SubBlueditDTO> getAllSubBluedits() {
        return subBlueditService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubBlueditDTO> getSubBluedit(@PathVariable Long id) {
        return new ResponseEntity<>(subBlueditService.getSubBlueditById(id), HttpStatus.OK);
    }
}
