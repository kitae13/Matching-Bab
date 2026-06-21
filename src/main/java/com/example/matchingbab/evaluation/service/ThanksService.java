package com.example.matchingbab.evaluation.service;

import com.example.matchingbab.evaluation.dto.*;
import com.example.matchingbab.evaluation.entity.Thanks;
import com.example.matchingbab.evaluation.entity.ThanksType;
import com.example.matchingbab.evaluation.repository.ThanksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThanksService {

    private final ThanksRepository thanksRepository;

    public ThanksResponse createThanks(CreateThanksRequest request) {

        if (thanksRepository.existsBySenderIdAndMatchId(
                request.getSenderId(), request.getMatchId())) {
            throw new IllegalStateException("이미 보은을 보냈습니다.");
        }

        Thanks thanks = Thanks.builder()
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .matchId(request.getMatchId())
                .type(request.getType())
                .build();

        Thanks saved = thanksRepository.save(thanks);

        return ThanksResponse.builder()
                .id(saved.getId())
                .senderId(saved.getSenderId())
                .receiverId(saved.getReceiverId())
                .matchId(saved.getMatchId())
                .type(saved.getType())
                .build();
    }

    public ThanksCountResponse getThanksCount(Long userId) {
        long count = thanksRepository.countByReceiverId(userId);

        return ThanksCountResponse.builder()
                .userId(userId)
                .totalCount(count)
                .build();
    }

    public List<ThanksTypeCountResponse> getThanksTypeCount(Long userId) {

        List<Object[]> result = thanksRepository.countByType(userId);

        return result.stream()
                .map(obj -> ThanksTypeCountResponse.builder()
                        .type((ThanksType) obj[0])
                        .count((Long) obj[1])
                        .build())
                .collect(Collectors.toList());
    }
}