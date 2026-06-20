package com.example.matchingbab.guide.service;

import com.example.matchingbab.global.exception.BusinessException;
import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.guide.dto.GuideDetailResponse;
import com.example.matchingbab.guide.dto.GuideListResponse;
import com.example.matchingbab.guide.entity.Guide;
import com.example.matchingbab.guide.repository.GuideRepository;
import com.example.matchingbab.guide.type.GuideCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuideService {

    private final GuideRepository guideRepository;

    public List<GuideListResponse> getGuides(
            GuideCategory category
    ) {
        List<Guide> guides =
                category == null
                        ? guideRepository
                          .findAllByOrderByDisplayOrderAscIdAsc()
                        : guideRepository
                          .findAllByCategoryOrderByDisplayOrderAscIdAsc(
                                  category
                          );

        return guides.stream()
                .map(GuideListResponse::from)
                .toList();
    }

    public GuideDetailResponse getGuide(
            Long guideId
    ) {
        Guide guide = guideRepository
                .findById(guideId)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.GUIDE_NOT_FOUND
                        )
                );

        return GuideDetailResponse.from(guide);
    }
}