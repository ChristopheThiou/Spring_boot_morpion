package com.boardgame.morpion.service;

import com.boardgame.morpion.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;



@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RestTemplate restTemplate;

    public boolean verifyUserId(String userId) {
        String url = "http://localhost:8081/users/" + userId;
        try {
            logger.info("Sending request to URL: {}", url);
            UserDto response = restTemplate.getForObject(url, UserDto.class);
            logger.info("Received response: {}", response);
            return response != null && response.getId().equals(userId);
        } catch (HttpClientErrorException.NotFound e) {
            logger.error("User not found: {}", userId);
            return false;
        } catch (Exception e) {
            logger.error("Error verifying user ID: {}", userId, e);
            return false;
        }
    }
}