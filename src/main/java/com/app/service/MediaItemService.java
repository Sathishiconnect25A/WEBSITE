package com.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.entity.BannerItem;
import com.app.entity.MediaSection;
import com.app.entity.PageType;
import com.app.repository.BannerItemRepository;
import com.app.util.FileUploadUtility;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor

public class MediaItemService {

    private final BannerItemRepository bannerItemRepository;

    private final FileUploadUtility fileUploadUtility;

    // ==============================

    // UPLOAD MEDIA

    // ==============================

    public BannerItem uploadMedia(

            PageType pageType,

            MediaSection section,

            MultipartFile image,

            MultipartFile video) {

        BannerItem item = new BannerItem();

        item.setPageType(pageType);

        item.setSection(section);

        // ---- Upload image ----

        if (image != null && !image.isEmpty()) {

            String imageUrl = fileUploadUtility.uploadFile(image, "images");

            item.setImageUrl(imageUrl);

        }

        // ---- Upload video ----

        if (video != null && !video.isEmpty()) {

            String videoUrl = fileUploadUtility.uploadFile(video, "videos");

            item.setVideoUrl(videoUrl);

        }

        return bannerItemRepository.save(item);

    }

    // ==============================

    // UPDATE MEDIA

    // ==============================

    public BannerItem updateMedia(

            Long id,

            PageType pageType,

            MediaSection section,

            MultipartFile image,

            MultipartFile video) {

        BannerItem item = bannerItemRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("Banner item not found"));

        // ---- Update only provided fields ----

        if (section != null)
            item.setSection(section);

        // ---- IMAGE update ----

        if (image != null && !image.isEmpty()) {

            String updatedImageUrl = fileUploadUtility.replaceFile(image, "images", item.getImageUrl());

            item.setImageUrl(updatedImageUrl);

        }

        // ---- VIDEO update ----

        if (video != null && !video.isEmpty()) {

            String updatedVideoUrl = fileUploadUtility.replaceFile(video, "videos", item.getVideoUrl());

            item.setVideoUrl(updatedVideoUrl);

        }

        return bannerItemRepository.save(item);

    }

    // // ==============================
    //
    // // GET ALL
    //
    // // ==============================
    //
    // public List<BannerItem> getAll() {
    //
    // return bannerItemRepository.findAllByOrderBySection();
    //
    // }
    //
    // // ==============================
    //
    // // GET BY SECTION
    //
    // // ==============================
    //
    // public List<BannerItem> getBySection(MediaSection section) {
    //
    // return bannerItemRepository.findBySection(section);
    //
    // }

    // update sections by pageType
    public List<BannerItem> getByPageAndSection(PageType pageType, MediaSection section) {
        return bannerItemRepository.findByPageTypeAndSection(pageType, section);
    }

}
