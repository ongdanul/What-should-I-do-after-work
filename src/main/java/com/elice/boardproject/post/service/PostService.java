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

    // 게시글 전체 목록 조회
    public List<PostDto> findAll(long boardId) {
        return postMapper.findAll(boardId);
    }

    // 게시글 필터
    public List<PostDto> postFilter(String filter, String description, long boardId) {
        return postMapper.postFilter(filter, description, boardId);
    }

    // 게시글 단건 조회
    public PostDto detail(Long postId) {
        PostDto findPost = postMapper.detail(postId);

        if (findPost == null) {
            throw new RuntimeException();
        }

        return findPost;
    }

    // 게시글 등록
    public int insert(PostDto post) {
        int exists = postMapper.insert(post);

        if (exists == 0) {
            throw new RuntimeException();
        }

        return exists;
    }

    // 게시글 수정
    @Transactional
    public int update(PostDto post) {
        PostDto findPost = postMapper.detail(post.getPostId());

        if (findPost == null) {
            throw new RuntimeException();
        }

        return postMapper.update(post);
    }

    // 게시글 삭제
    public void delete(Long postId) {
        PostDto findPost = postMapper.detail(postId);

        if (findPost == null) {
            throw new RuntimeException();
        }

        postMapper.delete(findPost);
    }
}
