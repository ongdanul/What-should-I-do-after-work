package com.elice.boardproject.scrap.controller;

import com.elice.boardproject.scrap.entity.ScrapDto;
import com.elice.boardproject.scrap.service.ScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/bookmark")
@Controller
public class ScrapController {
    @Autowired
    private ScrapService scrapService;

    // 즐겨찾기 전체 목록 조회
    @GetMapping("/list/{userId}")
    public String getList(@PathVariable String userId, Model model) {
        List<ScrapDto> scrapLists = scrapService.findAll(userId);
        model.addAttribute("scraps", scrapLists);
        model.addAttribute("userId", userId);

        return "bookmark/list";
    }

    // 즐겨찾기 단건 조회
    @GetMapping("/{scrapId}")
    public String getDetail(@PathVariable Long scrapId, Model model) {
        ScrapDto findScrap = scrapService.detail(scrapId);
        model.addAttribute("scrap", findScrap);

        return "bookmark/detail";
    }

    // 즐겨찾기 등록
    @PostMapping("/{postId}")
    public String register(@PathVariable Long postId) {
        ScrapDto newScrap = new ScrapDto();
        newScrap.setPostId(postId);
        scrapService.insert(newScrap);

        return "redirect:/bookmark/list/" + newScrap.getUserId();
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/{scrapId}")
    public String delete(@PathVariable Long scrapId) {
        ScrapDto removeScrap = scrapService.detail(scrapId);
        String userId = removeScrap.getUserId();
        scrapService.delete(removeScrap);

        return "redirect:/bookmark/list/" + userId;
    }
}
