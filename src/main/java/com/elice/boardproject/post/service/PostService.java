package com.elice.boardproject.post.service;

import com.elice.boardproject.post.entity.Post;
import com.elice.boardproject.post.entity.PostDto;
import com.elice.boardproject.post.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostMapper postMapper;

    public List<Post> findAll(String filter, String description) {
        return postMapper.findAll(filter, description);
    }

    public Post detail(Long postId) {
        Post findPost = postMapper.detail(postId);
        if (findPost == null) {
            throw new RuntimeException();
        }
        return findPost;
    }

    public int insert(Post post) {
        int exists = postMapper.insert(post);

        if (exists == 0) {
            throw new RuntimeException();
        }

        return exists;
    }

    @Transactional
    public int update(Post post) {
        Post findPost = postMapper.detail(post.getPostId());

        if (findPost == null) {
            throw new RuntimeException();
        }
    /*
        Optional.ofNullable(post.getPostTitle())
                .ifPresent(findPost::setPostTitle);
        Optional.ofNullable(post.getPostContent())
                .ifPresent(findPost::setPostContent);
    */
        return postMapper.update(post);
    }

    public void delete(Long postId) {
        Post findPost = postMapper.detail(postId);

        if (findPost == null) {
            throw new RuntimeException();
        }

        postMapper.delete(findPost);
    }
}
