package com.supportrip.core.account.service;

import com.supportrip.core.account.domain.Bank;
import com.supportrip.core.account.domain.ForeignAccount;
import com.supportrip.core.account.domain.ForeignAccountTransaction;
import com.supportrip.core.account.domain.ForeignCurrencyWallet;
import com.supportrip.core.account.dto.request.GenerateForeignAccountRequest;
import com.supportrip.core.account.dto.response.ForeignAccountInfoListResponse;
import com.supportrip.core.account.dto.response.ForeignAccountInfoResponse;
import com.supportrip.core.account.dto.response.ForeignAccountTransactionDetailResponse;
import com.supportrip.core.account.dto.response.GenerateForeignAccountResponse;
import com.supportrip.core.account.exception.ForeignAccountDuplicateException;
import com.supportrip.core.account.repository.BankRepository;
import com.supportrip.core.account.repository.ForeignAccountRepository;
import com.supportrip.core.account.repository.ForeignCurrencyWalletRepository;
import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.repository.CurrencyRepository;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.exception.UserNotFoundException;
import com.supportrip.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.supportrip.core.account.repository.ForeignAccountTransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final BankRepository bankRepository;
    private final ForeignAccountRepository foreignAccountRepository;
    private final ForeignCurrencyWalletRepository foreignCurrencyWalletRepository;
    private final ForeignAccountTransactionRepository foreignAccountTransactionRepository;
    private final CurrencyRepository currencyRepository;
    public GenerateForeignAccountResponse generateForeignAccount(Long userId, GenerateForeignAccountRequest generateForeignAccountRequest) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if(foreignAccountRepository.findByUser(user).isPresent())
            throw new ForeignAccountDuplicateException();

        Bank bank = bankRepository.findByName(generateForeignAccountRequest.getBankName());
        ForeignAccount foreignAccount = ForeignAccount.of(user, bank, generateForeignAccountRequest.getAccountNumber());
        foreignAccountRepository.save(foreignAccount);

        List<Currency> currencyList = currencyRepository.findAll();

        for(Currency currency : currencyList){
            ForeignCurrencyWallet wallet = ForeignCurrencyWallet.of(foreignAccount, currency, 0.0);
            foreignCurrencyWalletRepository.save(wallet);
        }

        return GenerateForeignAccountResponse.of(foreignAccount.getId());
    }

    public ForeignAccountInfoListResponse getForeignAccountInfo(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Optional<ForeignAccount> optionalForeignAccount = foreignAccountRepository.findByUser(user);
        if (optionalForeignAccount.isEmpty()) {
            return ForeignAccountInfoListResponse.of(false, null);
        }

        ForeignAccount foreignAccount = optionalForeignAccount.get();

        List<ForeignCurrencyWallet> foreignCurrencyWallets = foreignCurrencyWalletRepository.findByForeignAccountAndTotalAmountGreaterThan(foreignAccount,0.0);

        if(foreignCurrencyWallets == null)
            return ForeignAccountInfoListResponse.of(true, null);

        List<ForeignAccountInfoResponse> accountInfoResponses = new ArrayList<>();

        for(ForeignCurrencyWallet wallet : foreignCurrencyWallets){
            List<ForeignAccountTransactionDetailResponse> details = new ArrayList<>();
            List<ForeignAccountTransaction> transactions = foreignAccountTransactionRepository.findByForeignCurrencyWalletOrderByCreatedAtDesc(wallet);

            double originTotalAmount = 0.0;
            double targetTotalAmount = wallet.getTotalAmount();

            for(ForeignAccountTransaction transaction : transactions){
                details.add(ForeignAccountTransactionDetailResponse.of(transaction));
                originTotalAmount += transaction.getAmount() * transaction.getTargetExchangeRate();
            }

            double averageRate = originTotalAmount / targetTotalAmount;

            accountInfoResponses.add(ForeignAccountInfoResponse.of(wallet, averageRate, details));
        }

        return ForeignAccountInfoListResponse.of(true, accountInfoResponses);
    }
}
