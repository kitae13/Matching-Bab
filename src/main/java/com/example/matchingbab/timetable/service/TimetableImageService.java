package com.example.matchingbab.timetable.service;

import com.example.matchingbab.timetable.dto.TimetableImageUploadResponse;
import com.example.matchingbab.timetable.entity.TimetableImage;
import com.example.matchingbab.timetable.repository.TimetableImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimetableImageService {

    private final TimetableImageRepository repository;

    private final String uploadDir = "uploads/";

    public TimetableImageUploadResponse uploadImage(Long userId, MultipartFile file) throws IOException {

        validateFile(file);

        String originalName = file.getOriginalFilename();
        String storedName = UUID.randomUUID() + "_" + originalName;

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        File target = new File(uploadDir + storedName);
        file.transferTo(target);

        String imageUrl = "/uploads/" + storedName;

        TimetableImage saved = repository.save(
                TimetableImage.builder()
                        .userId(userId)
                        .imageUrl(imageUrl)
                        .originalFileName(originalName)
                        .storedFileName(storedName)
                        .build()
        );

        String dummyOcr = "OCR_RESULT_PENDING";

        return TimetableImageUploadResponse.builder()
                .imageId(saved.getId())
                .imageUrl(saved.getImageUrl())
                .ocrText(dummyOcr)
                .build();
    }

    private void validateFile(MultipartFile file) {

        String name = file.getOriginalFilename();

        if (name == null ||
                !(name.endsWith(".png") ||
                        name.endsWith(".jpg") ||
                        name.endsWith(".jpeg"))) {
            throw new IllegalArgumentException("INVALID_FILE_TYPE");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("FILE_SIZE_EXCEEDED");
        }
    }
}