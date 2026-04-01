package com.willyb0t.dreamshops.service.image;

import com.willyb0t.dreamshops.dto.ImageDto;
import com.willyb0t.dreamshops.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> savedImages(List<MultipartFile> files, Long productId);
    void updateImage(Long id, MultipartFile file);
}
