package com.elice.boardproject.post.service;

import com.elice.boardproject.post.entity.PostDto;
import com.elice.boardproject.post.mapper.PostMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PostService {
    @Autowired
    private PostMapper postMapper;

    // 게시글 전체 목록 조회 page
    public List<PostDto> findAll(Long boardId, int page, int pageSize, String orderBy) {
        int offset = (page - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        params.put("pageSize", pageSize);
        params.put("orderBy", orderBy);
        params.put("offset", offset);

        return postMapper.findAll(params);
    }

    // 게시글 필터
    public List<PostDto> postFilter(String filter, String description, Long boardId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        params.put("filter", filter);
        params.put("description", description);
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        return postMapper.postFilter(params);
    }

    // 게시글 단건 조회
    public PostDto detail(Long postId) {
        int updateViewCount = postMapper.updateViewCount(postId);

        if (updateViewCount == 0) {
            throw new RuntimeException("조회수 업데이트에 실패했습니다.");
        }
        return postMapper.detail(postId)
                .orElseThrow(() -> {
                    throw new RuntimeException("게시글을 찾을 수 없습니다.");
                });
    }

    // 게시글 등록
    public void insert(PostDto post) {
        int exists = postMapper.insert(post);

        if (exists == 0) {
            throw new RuntimeException("게시글 등록에 실패했습니다.");
        }
    }

    // 게시글 수정
    @Transactional
    public void update(PostDto post) {
        PostDto findPost = postMapper.detail(post.getPostId())
                .orElseThrow(() -> {
                    throw new RuntimeException("게시글을 찾을 수 없습니다.");
                });

        int exists = postMapper.update(post);

        if (exists == 0) {
            throw new RuntimeException("게시글 수정에 실패했습니다.");
        }
    }

    // 게시글 삭제
    public void delete(Long postId) {
        PostDto findPost = postMapper.detail(postId)
                .orElseThrow(() -> {
                    throw new RuntimeException("게시글을 찾을 수 없습니다");
                });

        int exists = postMapper.delete(findPost);

        if (exists == 0) {
            throw new RuntimeException("게시글 삭제에 실패했습니다.");
        }
    }
}
