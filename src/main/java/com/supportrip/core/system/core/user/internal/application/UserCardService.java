package com.supportrip.core.system.core.user.internal.application;

import com.supportrip.core.context.error.exception.notfound.ExchangeRateNotFoundException;
import com.supportrip.core.context.error.exception.notfound.UserNotFoundException;
import com.supportrip.core.system.core.exchange.internal.domain.CountryRepository;
import com.supportrip.core.system.core.exchange.internal.domain.Currency;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRate;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRateRepository;
import com.supportrip.core.system.core.mydata.external.response.UserCardApproval;
import com.supportrip.core.system.core.mydata.external.response.UserCardApprovalListResponse;
import com.supportrip.core.system.core.mydata.external.response.UserCardListResponse;
import com.supportrip.core.system.core.mydata.external.MyDataCardClient;
import com.supportrip.core.system.core.user.internal.domain.*;
import com.supportrip.core.system.core.user.internal.presentation.response.CountryRank;
import com.supportrip.core.system.core.user.internal.presentation.response.CountryRankingResult;
import com.supportrip.core.system.core.user.internal.presentation.response.OverSeasHistory;
import com.supportrip.core.system.core.user.internal.presentation.response.OverseasListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserCardService {
    private final MyDataCardClient myDataCardClient;
    private final UserRepository userRepository;
    private final UserCIRepository userCIRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final CountryRepository countryRepository;

    public OverseasListResponse getOverseasList(Long userId, LocalDate fromDate, LocalDate toDate) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        UserCI userCI = userCIRepository.findByUser(user);

        UserCardListResponse userCards = myDataCardClient.getCardList(userCI.getToken());

        List<UserCardApprovalListResponse> approvalsPerCard = userCards.getCardResponseList()
                .stream()
                .map((card) -> myDataCardClient.getCardApprovalList(card.getCardId(), userCI.getToken(), fromDate, toDate))
                .toList();

        List<CountryRankingResult> countryRankingResults = calculateRanking(approvalsPerCard);
        List<CountryRank> ranking = countryRankingResults.stream()
                .map(CountryRank::from)
                .toList();

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
                            approval.getCurrencyCode(),
                            UserCardApprovalStatus.findByCode(approval.getStatus()).getValue()
                    );
                })
                .toList();

        return OverseasListResponse.of(ranking, overSeasHistories);
    }

    public List<CountryRankingResult> getCountryRankingResult(User user, LocalDate fromDate, LocalDate toDate) {
        UserCI userCI = userCIRepository.findByUser(user);

        UserCardListResponse userCards = myDataCardClient.getCardList(userCI.getToken());

        List<UserCardApprovalListResponse> approvalsPerCard = userCards.getCardResponseList()
                .stream()
                .map((card) -> myDataCardClient.getCardApprovalList(card.getCardId(), userCI.getToken(), fromDate, toDate))
                .toList();

        return calculateRanking(approvalsPerCard);
    }

    private List<CountryRankingResult> calculateRanking(List<UserCardApprovalListResponse> approvalsPerCard) {
        Map<String, Long> countsByCountryCode = approvalsPerCard.stream()
                .flatMap(approvalResponse -> approvalResponse.getCardResponseList().stream())
                .filter(approval -> "01".equals(approval.getStatus()))
                .collect(Collectors.groupingBy(UserCardApproval::getCountryCode, Collectors.counting()));

        Map<String, Long> amountsByCountryCode = approvalsPerCard.stream()
                .flatMap(approvalResponse -> approvalResponse.getCardResponseList().stream())
                .filter(approval -> "01".equals(approval.getStatus()))
                .collect(Collectors.groupingBy(UserCardApproval::getCountryCode, Collectors.summingLong(approval -> {
                    Currency currency = countryRepository.findByCode(approval.getCountryCode()).getCurrency();
                    ExchangeRate currentExchangeRate = exchangeRateRepository.findLatestExchange(currency).orElseThrow(ExchangeRateNotFoundException::new);
                    return ((long) (approval.getApprovedAmt() * currentExchangeRate.getDealBaseRate()) / currentExchangeRate.getTargetCurrencyUnit());
                })));

        Map<String, Long> sortedAmountsByCountryCode = amountsByCountryCode.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()).thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return convertToCountryRanking(sortedAmountsByCountryCode, countsByCountryCode);
    }

    private List<CountryRankingResult> convertToCountryRanking(Map<String, Long> sortedAmountsByCountryCode, Map<String, Long> countsByCountryCode) {
        List<CountryRankingResult> countryRanks = new ArrayList<>();

        int rank = 1;
        for (Map.Entry<String, Long> entry : sortedAmountsByCountryCode.entrySet()) {
            String code = entry.getKey();
            Long amountToKrw = entry.getValue();

            countryRanks.add(CountryRankingResult.of(rank++, countryRepository.findByCode(code), countsByCountryCode.get(code), amountToKrw));
        }

        return countryRanks;
    }
}
