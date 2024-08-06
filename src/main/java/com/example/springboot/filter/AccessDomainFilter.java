package com.example.springboot.filter;

import com.example.springboot.properties.AllowedDomainsProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccessDomainFilter extends OncePerRequestFilter {

    private final List<String> allowedDomains;

    public AccessDomainFilter(AllowedDomainsProperties allowedDomainsProperties) {
        this.allowedDomains = allowedDomainsProperties.getDomains().stream()
                .map(domain -> domain.replaceAll("/$", "")) // Ensure no trailing slash
                .collect(Collectors.toList());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String host = request.getHeader("Host");
        if (host == null || !isDomainAllowed(host)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isDomainAllowed(String origin) {
        return allowedDomains.stream().anyMatch(origin::equals);
    }
}