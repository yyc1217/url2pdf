package cc.ncu.htmltopdf.security;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;

public class AcceptTargetIpPrefixVerifyFilterTest {

    private AcceptTargetIpPrefixVerifyFilter filter;
    
    private MockFilterChain mockChain;
    
    private MockHttpServletRequest req;
    
    private MockHttpServletResponse res;
    
    @Before
    public void setUp() throws Exception {
        filter = new AcceptTargetIpPrefixVerifyFilter();
        
        ReflectionTestUtils.setField(filter, "cachedTargetIpVerifyResult", mockCache());
        
        mockChain = new MockFilterChain();
        res = new MockHttpServletResponse();
    }
    
    private Cache<String, Boolean> mockCache() {
        Cache<String, Boolean> mockCache = CacheBuilder.newBuilder().build();
        mockCache.put("www.test.target.com.tw", true);
        return mockCache;
    }
    
    @Test
    public void testIsUrl2Pdf() throws IOException, ServletException {
        req = new MockHttpServletRequest("GET", "/url2pdf?target=www.test.target.com.tw");
        assertTrue(filter.isUrl2Pdf(req));
    }

}
