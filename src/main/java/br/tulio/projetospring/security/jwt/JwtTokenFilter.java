package br.tulio.projetospring.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    //Serve para filtrar e injetar o Usuario no contexto de segurança da aplicação
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        var token = tokenProvider.resolveToken((HttpServletRequest) servletRequest);

        if (StringUtils.isNotBlank(token) && tokenProvider.validateToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            if (authentication != null) { //cria a autenticação desse usuario nesse contexto
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }


        filterChain.doFilter(servletRequest, servletResponse); //retorna a resposta
    }
}
