package com.willyb0t.dreamshops.service.image;

import com.willyb0t.dreamshops.dto.ImageDto;
import com.willyb0t.dreamshops.exception.ResultNotFoundException;
import com.willyb0t.dreamshops.model.Image;
import com.willyb0t.dreamshops.model.Product;
import com.willyb0t.dreamshops.repository.ImageRepository;
import com.willyb0t.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;
    private final IProductService productService;
    /**
     * @param id 
     * @return
     */
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResultNotFoundException("Image not found"));
    }

    /**
     * @param id 
     */
    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,()-> {
            throw new ResultNotFoundException("Image not found" + id);
        });
    }

    /**
     * @param files
     * @param productId
     * @return
     */
    @Override
    public List<ImageDto> savedImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImagesDto = new ArrayList<>();
        for (MultipartFile file : files){
            try{
                Image image = new Image();
                image.setFilename(file.getOriginalFilename());
                image.setFiletype(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);
                String buildDownloadUrl = "/api/v1/images/image/download/";
                String donwloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(donwloadUrl);
                Image saved = imageRepository.save(image);
                saved.setDownloadUrl(buildDownloadUrl+saved.getId());
                imageRepository.save(saved);
                ImageDto imageDto = new ImageDto();
                imageDto.setId(saved.getId());
                imageDto.setName(saved.getFilename());
                imageDto.setUrl(saved.getDownloadUrl());
                savedImagesDto.add(imageDto);
            } catch (IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImagesDto;
    }

    /**
     * @param id 
     * @param file
     */
    @Override
    public void updateImage(Long id, MultipartFile file) {
        Image image = getImageById(id);
        try {
            image.setFilename(file.getOriginalFilename());
            image.setFilename(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }catch (IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
