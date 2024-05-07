package com.supportrip.core.system.core.mydata.internal.application;

import com.supportrip.core.system.core.mydata.external.CardFeignClient;
import com.supportrip.core.system.core.mydata.external.response.UserCardApprovalListResponse;
import com.supportrip.core.system.core.mydata.external.response.UserCardListResponse;
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
