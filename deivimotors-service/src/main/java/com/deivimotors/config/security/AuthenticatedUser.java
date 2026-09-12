package com.deivimotors.config.security;

public record AuthenticatedUser(
        String userId,
        String name,
        String cpf,
        String email) {
}
