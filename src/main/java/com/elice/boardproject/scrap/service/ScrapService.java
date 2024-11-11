package com.elice.boardproject.scrap.service;

import com.elice.boardproject.scrap.entity.ScrapDto;
import com.elice.boardproject.scrap.mapper.ScrapMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ScrapService {
    @Autowired
    private ScrapMapper scrapMapper;

    // 즐겨찾기 목록 전체 조회
    public List<ScrapDto> findAll(String userId) {
        log.info("[findAll 호출]");
        log.info("userId: {}", userId);

        List<ScrapDto> list = scrapMapper.findAll(userId);
        log.info("scrap list : {}", list);
        return list;
    }

    // 즐겨찾기 단건 조회
    public ScrapDto detail(Long scrapId) {
        log.info("[detail 호출 스크랩 아이디 : {}", scrapId );
        return scrapMapper.detail(scrapId)
                .orElseThrow(() -> {
                    log.warn("스크랩 아이디 : {}을 찾을 수 없습니다.", scrapId);
                    return new RuntimeException("즐겨찾기 게시물을 찾을 수 없습니다.");
                });
    }

    // 즐겨찾기 등록
    public void insert(ScrapDto scrap) {
        log.info("[insert 호출] 즐겨찾기 등록 : {}", scrap);
        log.info("scrap: {}", scrap.toString());

        int exists = scrapMapper.insert(scrap);

        if (exists == 0) {
            log.warn("즐겨찾기 등록 실패 : {}", scrap);
            throw new RuntimeException("게시물 즐겨찾기를 실패했습니다.");
        }

        log.info("즐겨찾기 등록 성공 : {}", scrap);
    }

    // 즐겨찾기 삭제
    public void delete(ScrapDto scrap) {
        log.info("[delete 호출] 스크랩 아이디 : {}", scrap.getScrapId());

        ScrapDto findScrap = scrapMapper.detail(scrap.getScrapId())
                .orElseThrow(() -> {
                    log.warn("스크랩 아이디 : {}을 찾을 수 없습니다.", scrap.getScrapId());
                    throw new RuntimeException("즐겨찾기 게시물을 찾을 수 없습니다.");
                });

        int exists = scrapMapper.delete(findScrap);

        if (exists == 0) {
            log.warn("즐겨찾기 취소 실패 : {}", findScrap);
            throw new RuntimeException("즐겨찾기 취소에 실패했습니다.");
        }

        log.info("즐겨찾기 취소 성공 : {}", findScrap);
    }
}
