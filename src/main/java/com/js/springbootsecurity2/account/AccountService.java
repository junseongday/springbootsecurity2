package com.js.springbootsecurity2.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Account createAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        System.out.println(account.toString());
        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Account> byUserName = accountRepository.findByUserName(s);
        Account account = byUserName.orElseThrow(() -> new UsernameNotFoundException(s));
        return new User(account.getUserName(),account.getPassword(),authority());
    }

    private Collection<? extends GrantedAuthority> authority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
