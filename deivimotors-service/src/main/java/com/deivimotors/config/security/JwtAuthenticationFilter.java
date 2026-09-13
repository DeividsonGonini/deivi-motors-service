package com.deivimotors.config.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * Filtro que intercepta o Bearer token JWT e extrai as claims "sub" (userId), name, cpf e "email"
 * populando o SecurityContext com um AuthenticatedUser como principal.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorized(
                    response,
                    "Missing or invalid Authorization header"
            );
            return;
        }

        String token = authHeader.substring(7);

        try {
            /*
             * O API Gateway é responsável por validar a autenticidade
             * e a assinatura do JWT emitido pelo Cognito.
             *
             * Aqui apenas extraímos as claims necessárias para a aplicação.
             */
            AuthenticatedUser user = extractUserFromJwt(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            List.of(
                                    new SimpleGrantedAuthority("ROLE_USER")
                            )
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (Exception e) {
            log.warn(
                    "Failed to extract claims from JWT: {}",
                    e.getMessage()
            );

            sendUnauthorized(
                    response,
                    "Invalid JWT token"
            );
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Extrai as claims do payload do JWT.
     * <p>
     * A validação da assinatura/autenticidade do token é realizada
     * pelo API Gateway antes da requisição chegar ao serviço.
     */
    private AuthenticatedUser extractUserFromJwt(String token)
            throws IOException {

        String[] parts = token.split("\\.");

        if (parts.length < 2) {
            throw new IllegalArgumentException(
                    "JWT must have at least 2 parts"
            );
        }

        byte[] payloadBytes =
                Base64.getUrlDecoder().decode(parts[1]);

        JsonNode payload =
                objectMapper.readTree(payloadBytes);

        String userId = getRequiredClaim(payload,"sub");
        String name = getRequiredClaim(payload,"name");
        String cpf = getRequiredClaim(payload,"custom:cpf");
        String email = getRequiredClaim(payload, "email");

        return new AuthenticatedUser(userId,name,cpf,email);
    }

    private String getRequiredClaim(JsonNode payload,String claim) {

        JsonNode node = payload.get(claim);

        if (node == null || node.isNull()) {
            throw new IllegalArgumentException(
                    "Missing required JWT claim: " + claim
            );
        }

        return node.asText();
    }

    private void sendUnauthorized(
            HttpServletResponse response,
            String message
    ) throws IOException {

        response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
        );

        response.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );

        response.getWriter().write(
                "{\"error\":\"Unauthorized\",\"message\":\""
                        + message
                        + "\"}"
        );
    }

//    Libera endpoints que não exigem o header Authorization
    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request
    ) {

        String servletPath = request.getServletPath();

        String path =
                (servletPath != null && !servletPath.isEmpty())
                        ? servletPath
                        : request.getRequestURI();

        return path.startsWith("/v1/payments/")
                || path.startsWith("/actuator")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");
    }
}

