package io.plagov.loandecisionengine.client;

public record UserProfile(
        String personalCode,
        boolean hasDebt,
        Integer creditModifier
) {
}
