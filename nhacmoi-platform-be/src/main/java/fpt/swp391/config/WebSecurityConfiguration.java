package fpt.swp391.config;

import fpt.swp391.security.CustomAccessDeniedHandler;
import fpt.swp391.security.JwtAuthenticationTokenFilter;
import fpt.swp391.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception {
        JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter = new JwtAuthenticationTokenFilter();
        jwtAuthenticationTokenFilter.setAuthenticationManager(authenticationManager());
        return jwtAuthenticationTokenFilter;
    }

    @Bean
    public RestAuthenticationEntryPoint restServicesEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/api/**");

        http.antMatcher("/api/**").httpBasic().authenticationEntryPoint(restServicesEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //list ALl songs and specific song
                .antMatchers(HttpMethod.GET, "/api/song/*").permitAll()
                //stream song
                .antMatchers(HttpMethod.GET, "/api/song/stream/*").permitAll()
                //add song, download song
                .antMatchers("/api/song/**").access("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR','ROLE_ADMIN')")
                //edit ans delete song
                .antMatchers(HttpMethod.PUT, "/api/song").access("hasAnyRole('ROLE_MODERATOR','ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/song/*").access("hasAnyRole('ROLE_MODERATOR','ROLE_ADMIN')")
                //login and sign up
                .antMatchers("/api/user/register").permitAll()
                .antMatchers("/api/user/login").permitAll()
                //get user by id, edit user
                .antMatchers(HttpMethod.GET, "/api/user/*").access("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR','ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/user").access("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR','ROLE_ADMIN')")
                //get all users, delete user
                .antMatchers("/api/user/**").access("hasAnyRole('ROLE_MODERATOR','ROLE_ADMIN')")
                //change user role
                .antMatchers(HttpMethod.PUT, "/api/user/changeRole").access("hasRole('ROLE_ADMIN')")
                //crud category
                .antMatchers("/api/category/**").access("hasAnyRole('ROLE_MODERATOR','ROLE_ADMIN')")
                //get all playlists
                .antMatchers(HttpMethod.GET, "/api/playlist").permitAll()
                //crud specific playlist, add song and remove song from playlists
                .antMatchers("/api/playlist/**").access("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR','ROLE_ADMIN')")
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        System.out.println(bCryptPasswordEncoder.encode("admin"));
    }
}