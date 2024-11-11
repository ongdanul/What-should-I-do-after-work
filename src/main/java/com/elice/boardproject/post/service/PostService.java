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
    public List<PostDto> findAll(Long boardId, int page, int pageSize) {
        log.info("[findAll 호출]");

        int offset = (page - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        return postMapper.findAll(params);
    }

    // 게시글 필터
    public List<PostDto> postFilter(String filter, String description, Long boardId, int page, int pageSize) {
        log.info("[filter 호출]");

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
        log.info("[detail 호출] 게시글 아이디 : {}", postId);

        int updateViewCount = postMapper.updateViewCount(postId);

        if (updateViewCount == 0) {
            log.warn("조회수 수정 실패 : {}", postId);
            throw new RuntimeException("조회수 업데이트에 실패했습니다.");
        }
        return postMapper.detail(postId)
                .orElseThrow(() -> {
                    log.warn("게시글 아이디 : {}을 찾을 수 없습니다.", postId);
                    return new RuntimeException("게시글을 찾을 수 없습니다.");
                });
    }

    // 게시글 등록
    public void insert(PostDto post) {
        log.info("[insert 호출] 게시글 등록 : {}", post);
        log.info("post userId : {}", post.getUserId());

        int exists = postMapper.insert(post);

        if (exists == 0) {
            log.warn("게시글 등록 실패 : {}", post);
            throw new RuntimeException("게시글 등록에 실패했습니다.");
        }

        log.info("게시판 등록 성공 : {}", post);
    }

    // 게시글 수정
    @Transactional
    public void update(PostDto post) {
        log.info("[update 호출] 게시글 수정 : {}", post);

        PostDto findPost = postMapper.detail(post.getPostId())
                .orElseThrow(() -> {
                    log.warn("게시글 아이디 : {}을 찾을 수 없습니다.", post.getPostId());
                    throw new RuntimeException("게시글을 찾을 수 없습니다.");
                });

        int exists = postMapper.update(post);

        if (exists == 0) {
            log.warn("게시글 수정 실패 : {}", post);
            throw new RuntimeException("게시글 수정에 실패했습니다.");
        }

        log.info("게시글 수정 성공 : {}", post);
    }

    // 게시글 삭제
    public void delete(Long postId) {
        log.info("[delete 호출] 게시판 아이디 : {}", postId);
        PostDto findPost = postMapper.detail(postId)
                .orElseThrow(() -> {
                    log.warn("게시글 아이디 : {}을 찾을 수 없습니다.", postId);
                    throw new RuntimeException("게시글을 찾을 수 없습니다");
                });

        int exists = postMapper.delete(findPost);


        if (exists == 0) {
            log.warn("게시글 삭제 실패 : {}", postId);
            throw new RuntimeException("게시글 삭제에 실패했습니다.");
        }

        log.info("게시글 삭제 성공 : {}", postId);
    }
}
