package cc.ncu.htmltopdf.security;

import org.springframework.stereotype.Component;

/**
 * Every target url is restricted to 140.115.0.0/16.
 * @author Yeh-Yung
 *
 */
@Component("NCUTargetUrlVerifier")
public class NCUTargetUrlVerifier {
    public boolean isTargetToNCU(String target) {
        System.out.println("isTargetToNCU: " + target);
        return false;
    }
}
