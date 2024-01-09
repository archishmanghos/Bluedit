package com.semicolonised.bluedit.controller;

import com.semicolonised.bluedit.dto.SubredditDTO;
import com.semicolonised.bluedit.service.SubRedditService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
public class SubRedditController {
    private final SubRedditService subRedditService;

    @PostMapping
    public SubredditDTO createSubReddit(@RequestBody SubredditDTO subredditDTO) {
        return subRedditService.save(subredditDTO);
    }

    @GetMapping
    public List<SubredditDTO> getAllSubReddits() {
        return subRedditService.getAll();
    }
}
