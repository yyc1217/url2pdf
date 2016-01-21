package cc.ncu.htmltopdf.security;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Sets;

@RunWith(value = Parameterized.class)
public class AcceptTargetIpPrefixVerifyFilterAcceptableIPTest {

    private AcceptTargetIpPrefixVerifyFilter filter;
    
    private String target;
    
    private boolean expectedResult;
    
    public AcceptTargetIpPrefixVerifyFilterAcceptableIPTest(String target, boolean expectedResult) {
        this.target = target;
        this.expectedResult = expectedResult;
    }
    
    @Before
    public void setUp() throws Exception {
        filter = new AcceptTargetIpPrefixVerifyFilter();
        ReflectionTestUtils.setField(filter, "acceptTargetIps", Sets.newHashSet("140.115"));
    }

    @Parameters(name = "{index}: {0} is valid url: {1}")
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][] { 
            { "https://portal.ncu.edu.tw/login", true}, 
            { "http://portal.ncu.edu.tw/login", true},
            { "www.ncu.edu.tw", true},
            { "www.google.com.tw", false},
            { "www", false},
            { " ", false}
        });
    }
    
    @Test
    public void testIsAcceptableIp() {
        boolean actualResult = filter.isAcceptableTarget(this.target);
        assertEquals(expectedResult, actualResult);
    }

}
