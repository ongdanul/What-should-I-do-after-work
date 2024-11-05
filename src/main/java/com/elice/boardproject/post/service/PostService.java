package com.elice.boardproject.post.service;

import com.elice.boardproject.post.entity.Post;
import com.elice.boardproject.post.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostMapper postMapper;

    public List<Post> findAll() {
        return postMapper.findAll();
    }
}
