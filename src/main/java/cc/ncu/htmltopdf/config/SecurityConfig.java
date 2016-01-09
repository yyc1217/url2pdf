package cc.ncu.htmltopdf.config;

import static cc.ncu.htmltopdf.config.RequestMappingPath.url2pdf;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import cc.ncu.htmltopdf.security.AcceptTargetIpPrefixVerifyFilter;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        
            .addFilterBefore(new AcceptTargetIpPrefixVerifyFilter(), ChannelProcessingFilter.class)
        
            .authorizeRequests()
                .antMatchers(url2pdf).anonymous()
                .anyRequest().authenticated();
    }

}
