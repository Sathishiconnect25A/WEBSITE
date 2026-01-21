package com.app.repository;

import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.BannerItem;
 
import com.app.entity.MediaSection;
import com.app.entity.PageType;

public interface BannerItemRepository extends JpaRepository<BannerItem, Long> {
 
//    List<BannerItem> findAllByOrderBySection();
// 
//    List<BannerItem> findBySection(MediaSection section);
    
    List<BannerItem> findByPageTypeAndSection(PageType pageType, MediaSection section);

}

 