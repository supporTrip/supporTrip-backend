package com.supportrip.core.feign.service;

import com.supportrip.core.feign.controller.CardFeignClient;
import com.supportrip.core.user.dto.response.UserCardApprovalListResponse;
import com.supportrip.core.user.dto.response.UserCardListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CardClientService {
    private final CardFeignClient cardFeignClient;

    public UserCardListResponse getCardList(String token) {
        return cardFeignClient.getCardList(token);
    }

    public UserCardApprovalListResponse getCardApprovalList(Long cardId, String token, LocalDate fromDate, LocalDate toDate) {
        return cardFeignClient.getCardApprovalList(cardId, token, fromDate, toDate);
    }
}
