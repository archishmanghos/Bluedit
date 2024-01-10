package com.semicolonised.bluedit.mapper;

import com.semicolonised.bluedit.dto.SubBlueditDTO;
import com.semicolonised.bluedit.model.Post;
import com.semicolonised.bluedit.model.SubBluedit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubBlueditMapper {
    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subReddit.getPosts()))")
    SubBlueditDTO mapSubBlueditToDTO(SubBluedit subReddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    SubBluedit mapDTOtoSubBluedit(SubBlueditDTO subredditDTO);
}
