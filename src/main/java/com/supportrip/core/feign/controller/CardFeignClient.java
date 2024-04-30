package com.supportrip.core.feign.controller;

import com.supportrip.core.user.dto.response.UserCardApprovalListResponse;
import com.supportrip.core.user.dto.response.UserCardListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(value = "card-service", url = "http://localhost:8081/v2/card")
public interface CardFeignClient {
    @GetMapping("/cards")
    UserCardListResponse getCardList(@RequestParam(value = "token") String token);

    @GetMapping("/cards/{card_id}/approval-overseas")
    UserCardApprovalListResponse getCardApprovalList(@PathVariable("card_id") Long cardId,
                                                     @RequestParam(value = "token") String token,
                                                     @RequestParam(value="from_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                     @RequestParam(value="to_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate);

}