package com.jwt.integration.configuration;

import com.jwt.integration.constant.SecurityConstant;
import com.jwt.integration.filter.JwtAccessDeniedHandler;
import com.jwt.integration.filter.JwtAuthenticationEntryPoint;
import com.jwt.integration.filter.JwtAuthorizationFilter;
// import com.jwt.integration.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.filter.OncePerRequestFilter;

// import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled= true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

    public JwtAuthorizationFilter jwtAuthorizationFilter;

    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private UserDetailsService userDetailsService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfiguration(JwtAuthorizationFilter jwtAuthorizationFilter,
                                         JwtAccessDeniedHandler jwtAccessDeniedHandler,
                                         JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                         UserDetailsService userDetailsService,
                                         BCryptPasswordEncoder bCryptPasswordEncoder)
                                        //  @Qualifier("userDetailsService")
    {
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.userDetailsService=userDetailsService;
        this.jwtAccessDeniedHandler=jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint=jwtAuthenticationEntryPoint;
        this.jwtAuthorizationFilter=jwtAuthorizationFilter;

    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http
        .csrf()
        .disable()
        .cors()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(SecurityConstant.PUBLIC_URLS)
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .accessDeniedHandler(jwtAccessDeniedHandler)
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .and()
        .addFilterBefore(jwtAuthorizationFilter,  UsernamePasswordAuthenticationFilter.class);

    }
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception
    {
        return super.authenticationManagerBean();
    }

}
