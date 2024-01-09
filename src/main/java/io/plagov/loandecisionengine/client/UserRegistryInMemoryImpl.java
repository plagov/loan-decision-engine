package io.plagov.loandecisionengine.client;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UserRegistryInMemoryImpl implements UserRegistryApi {

    @Override
    public UserProfile findUser(String personalCode) {
        return USERS.stream().filter(user -> personalCode.equals(user.personalCode()))
                .findFirst()
                .orElseThrow();
    }

    private final static List<UserProfile> USERS = List.of(
            new UserProfile("49002010965", true, null),
            new UserProfile("49002010976", false, 1),
            new UserProfile("49002010987", false, 2),
            new UserProfile("49002010998", false, 3));

    private final static Map<Integer, Integer> SEGMENTS = Map.of(
            1, 100,
            2, 300,
            3, 1000);
}
