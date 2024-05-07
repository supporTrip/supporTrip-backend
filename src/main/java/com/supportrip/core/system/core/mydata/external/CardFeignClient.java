package com.supportrip.core.system.core.mydata.external;

import com.supportrip.core.system.core.mydata.external.response.UserCardApprovalListResponse;
import com.supportrip.core.system.core.mydata.external.response.UserCardListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(value = "card-service", url = "${mydata.api.card}")
public interface CardFeignClient {
    @GetMapping("/cards")
    UserCardListResponse getCardList(@RequestParam(value = "token") String token);

    @GetMapping("/cards/{card_id}/approval-overseas")
    UserCardApprovalListResponse getCardApprovalList(@PathVariable("card_id") Long cardId,
                                                     @RequestParam(value = "token") String token,
                                                     @RequestParam(value="from_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                     @RequestParam(value="to_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate);

}