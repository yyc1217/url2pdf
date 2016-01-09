package cc.ncu.htmltopdf.security;

import static cc.ncu.htmltopdf.config.RequestMappingPath.url2pdf;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.startsWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import cc.ncu.htmltopdf.exception.BadParameterException;

public class AcceptTargetIpPrefixVerifyFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AcceptTargetIpPrefixVerifyFilter.class);

    private static final String ACCEPT_TARGET_IP_FILENAME = "acceptTargetIp.txt";

    private Set<String> acceptTargetIps = Collections.emptySet();

    private String forbiddenMessage = "%s is not a valid url or resolved ip is not allowed, acceptable ip prefixes  %s";
    
    private LoadingCache<String, Boolean> cachedTargetIpVerifyResult;
    
    public AcceptTargetIpPrefixVerifyFilter() {
        readAcceptTargetIps();
        cachedTargetIpVerifyResult = buildCachedTargetIps();
    }

    private void readAcceptTargetIps() {
        
        try (
                InputStream in = getClass().getClassLoader().getResourceAsStream(ACCEPT_TARGET_IP_FILENAME);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(in))
        ) {

            acceptTargetIps = buffer.lines().filter(StringUtils::isNotBlank).collect(toSet());
            logAfterReadSuccessful();

        } catch (IOException e) {
            logger.error("Read accept target ips failed; System will reject every request.", e);
        }
    }
    
    private void logAfterReadSuccessful() {
        if (acceptTargetIps.isEmpty()) {
            logger.warn("No accept target ips specified, system will reject every request!");
        } else {
            logger.info("Accept target Ip prefixs: {}", acceptTargetIps);
        }
    }
    
    private LoadingCache<String, Boolean> buildCachedTargetIps() {
        CacheLoader<String, Boolean> loader = new CacheLoader<String, Boolean>() {
            @Override
            public Boolean load(String url) {
                return isAcceptableTarget(url);
            }
        };
        return CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).build(loader);
    }
    
    private Boolean isAcceptableTarget(String target) {

        logger.info("Verifying target url: {}", target);
        
        if (isBlank(target)) {
            return false;
        }
        
        try {
            InetAddress inetAddress = InetAddress.getByName(target);
            String address = inetAddress.getHostAddress();
            return acceptTargetIps.parallelStream().anyMatch(ip -> startsWith(address, ip));
        } catch (UnknownHostException e) {
            logger.error("Verify target url failed.", e);
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        boolean hasTargetUrlPath = req.getRequestURI().contains(url2pdf);

        if (hasTargetUrlPath) {

            String target = req.getParameter("target");
            if (!isValidTarget(target)) {
                throw new BadParameterException(asForbiddenMessage(target));            
            }
        }

        chain.doFilter(request, response);
    }

    private Boolean isValidTarget(String target) {
        return cachedTargetIpVerifyResult.getUnchecked(target);
    }
    
    private String asForbiddenMessage(String target) {
       return String.format(forbiddenMessage, target, acceptTargetIps);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void destroy() {
    }

}
