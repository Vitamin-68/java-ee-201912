package com.yuriy_shaynuk.spring_security.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
