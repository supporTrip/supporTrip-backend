package com.supportrip.core.user.service;

import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.exception.ExchangeRateNotFoundException;
import com.supportrip.core.exchange.repository.CountryRepository;
import com.supportrip.core.exchange.repository.ExchangeRateRepository;
import com.supportrip.core.feign.service.CardClientService;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.domain.UserCI;
import com.supportrip.core.user.dto.response.*;
import com.supportrip.core.user.exception.UserNotFoundException;
import com.supportrip.core.user.repository.UserCIRepository;
import com.supportrip.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserCardService {
    private final CardClientService cardClientService;
    private final UserRepository userRepository;
    private final UserCIRepository userCIRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final CountryRepository countryRepository;

    public OverseasListResponse getOverseasList(Long userId, LocalDate fromDate, LocalDate toDate) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        UserCI userCI = userCIRepository.findByUser(user);

        UserCardListResponse userCards = cardClientService.getCardList(userCI.getToken());

        List<UserCardApprovalListResponse> approvalsPerCard = userCards.getCardResponseList()
                .stream()
                .map((card) -> cardClientService.getCardApprovalList(card.getCardId(), userCI.getToken(), fromDate, toDate))
                .toList();

        List<CountryRank> ranking = calculateRanking(approvalsPerCard);

        List<UserCardApproval> userCardApprovals = approvalsPerCard.stream()
                .flatMap(approvalResponse -> approvalResponse.getCardResponseList().stream())
                .toList();

        List<OverSeasHistory> overSeasHistories = userCardApprovals.stream()
                .sorted(Comparator.comparing(UserCardApproval::getApprovedAt).reversed())
                .map((approval) -> {
                    String code = approval.getCountryCode();
                    return OverSeasHistory.of(countryRepository.findByCode(code).getName(),
                            approval.getApprovedAt(),
                            approval.getApprovedAmt(),
                            approval.getCurrencyCode()
                    );
                })
                .toList();

        return OverseasListResponse.of(ranking, overSeasHistories);
    }

    private List<CountryRank> calculateRanking(List<UserCardApprovalListResponse> approvalsPerCard) {
        Map<String, Long> countsByCountryCode = approvalsPerCard.stream()
                .flatMap(approvalResponse -> approvalResponse.getCardResponseList().stream())
                .collect(Collectors.groupingBy(UserCardApproval::getCountryCode, Collectors.counting()));

        Map<String, Double> amountsByCountryCode = approvalsPerCard.stream()
                .flatMap(approvalResponse -> approvalResponse.getCardResponseList().stream())
                .collect(Collectors.groupingBy(UserCardApproval::getCountryCode, Collectors.summingDouble(UserCardApproval::getApprovedAmt)));

        Map<String, Double> sortedAmountsByCountryCode = amountsByCountryCode.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()).thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        List<CountryRank> countryRanks = new ArrayList<>();

        int rank = 1;
        for (Map.Entry<String, Double> entry : sortedAmountsByCountryCode.entrySet()) {
            String code = entry.getKey();
            Double amount = entry.getValue();

            Currency currency = countryRepository.findByCode(code).getCurrency();
            ExchangeRate currentExchangeRate = exchangeRateRepository.findLatestExchange(currency).orElseThrow(ExchangeRateNotFoundException::new);
            Long amountToKrw = (long) (amount * currentExchangeRate.getDealBaseRate()) / currentExchangeRate.getTargetCurrencyUnit();

            countryRanks.add(CountryRank.builder()
                        .countryName(countryRepository.findByCode(code).getName())
                        .rank(rank++)
                        .count(countsByCountryCode.get(code))
                            .amount(amountToKrw)
                        .build()
            );
        }

        return countryRanks;
    }
}
