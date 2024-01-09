package com.semicolonised.bluedit.service;

import com.semicolonised.bluedit.dto.SubredditDTO;
import com.semicolonised.bluedit.model.SubReddit;
import com.semicolonised.bluedit.repository.SubRedditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubRedditService {
    private final SubRedditRepository subRedditRepository;
    private final AuthService authService;

    @Transactional
    public SubredditDTO save(SubredditDTO subredditDTO) {
        SubReddit subReddit = subRedditRepository.save(mapToSubReddit(subredditDTO));
        subredditDTO.setId(subReddit.getId());
        return subredditDTO;
    }

    @Transactional(readOnly = true)
    public List<SubredditDTO> getAll() {
        return subRedditRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private SubredditDTO mapToDTO(SubReddit subReddit) {
        return SubredditDTO.builder()
                .id(subReddit.getId())
                .name(subReddit.getName())
                .description(subReddit.getDescription())
                .numberOfPosts(subReddit.getPosts().size())
                .build();
    }

    private SubReddit mapToSubReddit(SubredditDTO subredditDTO) {
        return SubReddit.builder()
                .name(subredditDTO.getName())
                .description(subredditDTO.getDescription())
                .build();
    }

}
