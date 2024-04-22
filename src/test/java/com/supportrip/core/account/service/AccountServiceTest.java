package com.supportrip.core.account.service;

import com.supportrip.core.account.domain.Bank;
import com.supportrip.core.account.domain.ForeignAccount;
import com.supportrip.core.account.domain.ForeignAccountTransaction;
import com.supportrip.core.account.domain.ForeignCurrencyWallet;
import com.supportrip.core.account.dto.response.ForeignAccountInfoListResponse;
import com.supportrip.core.account.repository.ForeignAccountRepository;
import com.supportrip.core.account.repository.ForeignAccountTransactionRepository;
import com.supportrip.core.account.repository.ForeignCurrencyWalletRepository;
import com.supportrip.core.exchange.domain.Country;
import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.repository.CurrencyRepository;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

class AccountServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ForeignAccountRepository foreignAccountRepository;

    @Mock
    private ForeignCurrencyWalletRepository foreignCurrencyWalletRepository;

    @Mock
    private ForeignAccountTransactionRepository foreignAccountTransactionRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("외화 계좌가 없을때 hasAccount가 false, accountInfo가 null로 반환된다.")
    void foreignAccountNotExist(){
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.initialUserOf("profile_img")));
        when(foreignAccountRepository.findByUser(any())).thenReturn(Optional.empty());

        // When
        ForeignAccountInfoListResponse response = accountService.getForeignAccountInfo(userId);

        // Then
        assertFalse(response.getHasAccount());
        assertNull(response.getAccountInfo());
    }

    @Test
    @DisplayName("외화 계좌가 있을 때, 계좌 존재여부, 각 거래내역을 반환해야 한다")
    void getforeignAccountInfo(){
        Long userId = 1L;
        User user = User.initialUserOf("profile_img");
        Bank bank = Bank.of("우리은행", "WOORI", "bank_img");
        ForeignAccount foreignAccount = ForeignAccount.of(user, bank, "1111");
        List<ForeignCurrencyWallet> walletList = new ArrayList<>();
        Country country = Country.of("미국", "미국국기", "미국달러");

        Currency currency = Currency.of(country, "미국달러", "USD", "$");
        ForeignCurrencyWallet wallet = ForeignCurrencyWallet.of(foreignAccount, currency, 10.0);
        walletList.add(wallet);

        List<ForeignAccountTransaction> transactions = new ArrayList<>();
        ForeignAccountTransaction transaction = ForeignAccountTransaction.of(10.0, 1200.0, 10.0,wallet,null);
        transactions.add(transaction);

        ReflectionTestUtils.setField(transaction, "createdAt", LocalDateTime.now());

        when(foreignAccountTransactionRepository.findByForeignCurrencyWalletOrderByCreatedAtDesc(any()))
                .thenReturn(transactions);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(foreignAccountRepository.findByUser(any())).thenReturn(Optional.of(foreignAccount));
        when(foreignCurrencyWalletRepository.findByForeignAccountAndTotalAmountGreaterThan(any(), anyDouble()))
                .thenReturn(walletList);


        // When
        ForeignAccountInfoListResponse response = accountService.getForeignAccountInfo(userId);

        // Then
        assertTrue(response.getHasAccount());
        assertNotNull(response.getAccountInfo());
        assertEquals(1, response.getAccountInfo().size());
        assertNotNull(response.getAccountInfo().get(0).getDetails());
        assertEquals(1, response.getAccountInfo().get(0).getDetails().size());
        assertEquals(10.0, response.getAccountInfo().get(0).getDetails().get(0).getTotalMoney());
    }
}