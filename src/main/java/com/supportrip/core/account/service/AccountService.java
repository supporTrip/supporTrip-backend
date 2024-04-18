package com.supportrip.core.account.service;

import com.supportrip.core.account.domain.Bank;
import com.supportrip.core.account.domain.ForeignAccount;
import com.supportrip.core.account.dto.request.GenerateForeignAccountRequest;
import com.supportrip.core.account.dto.response.GenerateForeignAccountResponse;
import com.supportrip.core.account.exception.ForeignAccountDuplicateException;
import com.supportrip.core.account.repository.BankRepository;
import com.supportrip.core.account.repository.ForeignAccountRepository;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.exception.UserNotFoundException;
import com.supportrip.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final BankRepository bankRepository;
    private final ForeignAccountRepository foreignAccountRepository;
    public GenerateForeignAccountResponse generateForeignAccount(Long userId, GenerateForeignAccountRequest generateForeignAccountRequest) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if(foreignAccountRepository.findByUser(user).isPresent())
            throw new ForeignAccountDuplicateException();

        Bank bank = bankRepository.findByName(generateForeignAccountRequest.getBankName());
        ForeignAccount foreignAccount = ForeignAccount.of(user, bank, generateForeignAccountRequest.getAccountNumber());
        foreignAccountRepository.save(foreignAccount);
        return GenerateForeignAccountResponse.of(foreignAccount.getId());
    }
}
