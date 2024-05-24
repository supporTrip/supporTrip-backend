package com.supportrip.core.system.core.account.internal.application;

import com.supportrip.core.context.error.exception.badrequest.ForeignAccountDuplicateException;
import com.supportrip.core.system.core.account.internal.domain.*;
import com.supportrip.core.system.core.account.internal.presentation.request.GenerateForeignAccountRequest;
import com.supportrip.core.system.core.account.internal.presentation.response.ForeignAccountInfoListResponse;
import com.supportrip.core.system.core.account.internal.presentation.response.ForeignAccountInfoResponse;
import com.supportrip.core.system.core.account.internal.presentation.response.ForeignAccountTransactionDetailResponse;
import com.supportrip.core.system.core.account.internal.presentation.response.GenerateForeignAccountResponse;
import com.supportrip.core.system.core.exchange.internal.domain.Country;
import com.supportrip.core.system.core.exchange.internal.domain.CountryRepository;
import com.supportrip.core.system.core.exchange.internal.domain.Currency;
import com.supportrip.core.system.core.exchange.internal.domain.CurrencyRepository;
import com.supportrip.core.system.core.user.internal.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final BankRepository bankRepository;
    private final ForeignAccountRepository foreignAccountRepository;
    private final ForeignCurrencyWalletRepository foreignCurrencyWalletRepository;
    private final ForeignAccountTransactionRepository foreignAccountTransactionRepository;
    private final CurrencyRepository currencyRepository;
    private final CountryRepository countryRepository;

    public GenerateForeignAccountResponse generateForeignAccount(User user, GenerateForeignAccountRequest generateForeignAccountRequest) {
        if (foreignAccountRepository.findByUser(user).isPresent())
            throw new ForeignAccountDuplicateException();

        Bank bank = bankRepository.findByName(generateForeignAccountRequest.getBankName());
        ForeignAccount foreignAccount = ForeignAccount.of(user, bank, generateForeignAccountRequest.getAccountNumber());
        foreignAccountRepository.save(foreignAccount);

        List<Currency> currencyList = currencyRepository.findAll();

        for (Currency currency : currencyList) {
            ForeignCurrencyWallet wallet = ForeignCurrencyWallet.of(foreignAccount, currency, 0L);
            foreignCurrencyWalletRepository.save(wallet);
        }

        return GenerateForeignAccountResponse.of(foreignAccount.getId());
    }

    public ForeignAccountInfoListResponse getForeignAccountInfo(User user) {
        Optional<ForeignAccount> optionalForeignAccount = foreignAccountRepository.findByUser(user);
        if (optionalForeignAccount.isEmpty()) {
            return ForeignAccountInfoListResponse.of(false, null);
        }

        ForeignAccount foreignAccount = optionalForeignAccount.get();

        List<ForeignCurrencyWallet> foreignCurrencyWallets = foreignCurrencyWalletRepository.findByForeignAccountAndTotalAmountGreaterThan(foreignAccount, -1L);
        foreignCurrencyWallets.remove(0);

        if (foreignCurrencyWallets.isEmpty())
            return ForeignAccountInfoListResponse.of(true, null);

        List<ForeignAccountInfoResponse> accountInfoResponses = new ArrayList<>();

        for (ForeignCurrencyWallet wallet : foreignCurrencyWallets) {
            List<ForeignAccountTransactionDetailResponse> details = new ArrayList<>();
            List<ForeignAccountTransaction> transactions = foreignAccountTransactionRepository.findByForeignCurrencyWalletOrderByCreatedAtDesc(wallet);

            double originTotalAmount = 0.0;
            double targetTotalAmount = wallet.getTotalAmount();

            for (ForeignAccountTransaction transaction : transactions) {
                details.add(ForeignAccountTransactionDetailResponse.of(transaction));
                originTotalAmount += transaction.getAmount() * transaction.getTargetExchangeRate();
            }

            double averageRate = originTotalAmount / targetTotalAmount;

            Country country = countryRepository.findByCurrency(wallet.getCurrency());

            accountInfoResponses.add(ForeignAccountInfoResponse.of(wallet, country, averageRate, details));
        }

        return ForeignAccountInfoListResponse.of(true, accountInfoResponses);
    }
}
