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

//    public List<Post> findAll(long boardId) {
//        return postMapper.findAll(boardId);
//    }
//    // 필터
//    public List<Post> postFilter(String postTitle, String postContent, long boardId) {
//        System.out.println("boardId");
//        return postMapper.postFilter(postTitle, postContent, boardId);
//    }

    public List<Post> findAll(long boardId) {
        return postMapper.findAll(boardId);
    }
    // 필터
    public List<Post> postFilter(String filter, String description, long boardId) {
        return postMapper.postFilter(filter, description, boardId);
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
        System.out.println("exists : " + exists);
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
