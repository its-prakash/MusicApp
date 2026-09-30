package com.musicBackend.Authentication;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private Set<String> blackListedToken = ConcurrentHashMap.newKeySet();
    private Map<String, String> activeUserSession = new ConcurrentHashMap<>();

    public void blacklistToken(String token){
        blackListedToken.add(token);
    }

    public boolean isTokenBlackListed(String token){
        return   blackListedToken.contains(token);
    }

    public boolean hasActiveSession(String userName){
        return activeUserSession.containsKey(userName);
    }

    public void registerActiveSession(String username, String token) {
        activeUserSession.put(username, token);
    }

    public void removeAccountSession(String userName, String token){
        activeUserSession.remove(userName);
        blacklistToken(token);
    }



}
