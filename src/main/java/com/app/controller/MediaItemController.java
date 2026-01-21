package com.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.entity.BannerItem;
import com.app.entity.MediaSection;
import com.app.entity.PageType;
import com.app.service.MediaItemService;

@RestController
 
@RequestMapping("/media")
 
public class MediaItemController {

    private final MediaItemService service;

    public MediaItemController(MediaItemService service) {
 
        this.service = service;
 
    }

    // ==========================
 
    // UPLOAD MULTIPLE FILES
 
    // ==========================
 
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 
    public ResponseEntity<BannerItem> uploadMedia(
    		
            @RequestParam PageType pageType,

            @RequestPart(value = "image", required = false) MultipartFile image,
 
            @RequestPart(value = "video", required = false) MultipartFile video,

            @RequestParam MediaSection section
 
 
    ) throws IOException {

        BannerItem saved = service.uploadMedia(pageType, section, image, video);

        return ResponseEntity.ok(saved);
 
    }


    // ==========================
 
    // GET ALL
 
    // ==========================
 
//    @GetMapping
// 
//    public ResponseEntity<List<BannerItem>> getAll() {
// 
//        return ResponseEntity.ok(service.getAll());
// 
//    }

    // ==========================
 
    // GET BY SECTION
 
    // ==========================
 
//    @GetMapping("/section/{section}")
// 
//    public ResponseEntity<List<BannerItem>> getBySection(@PathVariable MediaSection section) {
// 
//        return ResponseEntity.ok(service.getBySection(section));
// 
//    }
// 
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 
    public ResponseEntity<BannerItem> updateMedia(

            @PathVariable Long id,
            
            @RequestParam(required = false) PageType pageType,

            @RequestPart(value = "image", required = false) MultipartFile image,
 
            @RequestPart(value = "video", required = false) MultipartFile video,

            @RequestParam(required = false) MediaSection section
 
    ) throws IOException {

        BannerItem updated = service.updateMedia(
 
                id, pageType, section, image, video
 
        );

        return ResponseEntity.ok(updated);
 
    }


    
    @GetMapping("/page/{pageType}/section/{section}")
    public ResponseEntity<List<BannerItem>> getByPageAndSection(
            @PathVariable PageType pageType,
            @PathVariable MediaSection section
    ) {
        return ResponseEntity.ok(
                service.getByPageAndSection(pageType, section)
        );
    }

}

 