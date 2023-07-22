package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@EnableWebSecurity
@Configuration
public class WebAuthorization{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/web/index.html","web/style/**","web/javascript/**","/api/login","/web/register.html").permitAll()
                .antMatchers("/web/news.html","/web/aboutUs.html").permitAll()
                .antMatchers("/api/accounts.html","/web/accounts.html","/web/prueba-cards.html","/web/pay-loan.html","/web/create-account.html","/web/disable-account.html","/web/disable-card.html","/web/transfer.html","/web/account.html","/web/loan-aplication.html","/web/cards.html","/web/create-cards.html","/web/news.html","/web/aboutUs.html","/api/clients/current","/api/client-loans/payment").hasAuthority("CLIENT")
                .antMatchers("/h2-console/**","/rest/**","/web/**","/api/accounts.html").hasAuthority("ADMIN")
                .antMatchers("/web/create-loan.html").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/clients/current/account","/api/loans","/api/clients/current/cards","api/transactions","/api/transactions/PDF").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH,"/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH,"/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll();

// deniega cualquier peticion que no esten contempladas en el antMatchers
//.anyRequest().denyAll(); /clients/current/accounts
//"/web/account.html?id={id}"  "/web/accounts.html","/web/account.html","/web/account.html?id={id}",
        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        //Elimina Cookies al cerrar sesion
        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        // desactivar la comprobación de tokens CSRF
        http.csrf().disable();

        // deshabilitar frameOptions para que se pueda acceder a h2-console
        http.headers().frameOptions().disable();

//        HttpSecurity.headers().frameOptions().disable();

        // si el usuario no está autenticado, simplemente envíe una respuesta de falla de autenticación (401 - Unauthorized)

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // si el inicio de sesión es exitoso, simplemente borre las banderas que solicitan autenticación

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // si falla el inicio de sesión, simplemente envíe una respuesta de falla de autenticación

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // si el cierre de sesión es exitoso, simplemente envíe una respuesta exitosa

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    //    eliminar los atributos relacionados con la autenticación de una sesión HTTP
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
        // Al eliminar este atributo, se borra cualquier información relacionada con
        // la excepción de autenticación almacenada en la sesión.
    }

}

