package com.company.business.controller.admin;

import com.company.server.dto.ChapterDto;
import com.company.server.dto.PageDto;
import com.company.server.dto.ResponseDto;
import com.company.server.service.ChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/chapter")
@Slf4j
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @RequestMapping("/list")
    //@RequestBody接收流数据
    public ResponseDto list(@RequestBody PageDto pageDto) {
        log.info("pageDto:{}", pageDto);
        ResponseDto responseDto = new ResponseDto();
        chapterService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @RequestMapping("/save")
    //@RequestBody接收流数据
    public ResponseDto save(@RequestBody ChapterDto chapterDto) {
        log.info("chapterDto:{}", chapterDto);
        ResponseDto responseDto = new ResponseDto();
        chapterService.save(chapterDto);
        responseDto.setContent(chapterDto);
        return responseDto;
    }
}
