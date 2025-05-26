package com.grupo_exito.microservicio_autenticacion.shared.infrastructure.config;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomLogHttpFilterChain implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                var httpRequest = (HttpServletRequest) request;
                var headers = "Headers: " + Collections.list(httpRequest.getHeaderNames()).stream().map(headerName -> {
                    return headerName + " : " + httpRequest.getHeader(headerName);
                }).collect(Collectors.joining(" | "));
                log.info(
                    String.format("INTERCEPTOR ContentType: %s | ServerName: %s " + 
                    "| Protocol: %s | LocalAddr: %s | ContextPath: %s | LocalPort: %s | Scheme: %s | Headers: %s", 
                        request.getContentType(), 
                        request.getServerName(),
                        request.getServletConnection().getProtocol(),
                        request.getLocalAddr(),
                        request.getServletContext().getContextPath(),
                        String.valueOf(request.getLocalPort()),
                        request.getScheme(),
                        headers));
                chain.doFilter(request, response);
    }
}
