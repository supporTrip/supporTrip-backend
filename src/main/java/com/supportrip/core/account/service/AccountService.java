package com.supportrip.core.account.service;

import com.supportrip.core.account.domain.Bank;
import com.supportrip.core.account.domain.ForeignAccount;
import com.supportrip.core.account.dto.request.ForeignAccountRequest;
import com.supportrip.core.account.dto.response.ForeignAccountResponse;
import com.supportrip.core.account.exception.ForeignAccountDuplicateException;
import com.supportrip.core.account.repository.BankRepository;
import com.supportrip.core.account.repository.ForeignAccountRepository;
import com.supportrip.core.auth.jwt.JwtProvider;
import com.supportrip.core.auth.jwt.JwtUtil;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.exception.UserNotFoundException;
import com.supportrip.core.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final BankRepository bankRepository;
    private final ForeignAccountRepository foreignAccountRepository;
    public ForeignAccountResponse generateForeignAccount(Long userId, ForeignAccountRequest foreignAccountRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());

        if(foreignAccountRepository.findByUser(user)!=null)
            throw new ForeignAccountDuplicateException();

        Bank bank = bankRepository.findByName(foreignAccountRequest.getBankName());
        ForeignAccount foreignAccount = ForeignAccount.of(user, bank, foreignAccountRequest.getAccountNumber());
        foreignAccountRepository.save(foreignAccount);
        return ForeignAccountResponse.of(foreignAccount.getId());
    }
}
