package net.media.spamserver.config;

import net.media.spamserver.verifiers.*;

import java.util.HashMap;

public class VerifierConfig {

    public static String[] priorities = {
            BlockingColoListVerifier.class.getName(),
            NonBlockingColoListVerifier.class.getName(),
            BotListVerifier.class.getName(),
            IPAdVerifier.class.getName(),
            DomainIPVerifier.class.getName(),
            VisitIdVerifier.class.getName(),
            VisitorIdAdVerifier.class.getName(),
            DomainVisitorIdVerifier.class.getName(),
    };

    public static HashMap<String, Class> shortFormVerifiers = new HashMap<>();
    static {
        shortFormVerifiers.put("BCOLO", BlockingColoListVerifier.class);
        shortFormVerifiers.put("FCLICK", FastClickVerifier.class);
        shortFormVerifiers.put("NCOLO", NonBlockingColoListVerifier.class);
        shortFormVerifiers.put("BOT", BotListVerifier.class);
        shortFormVerifiers.put("IPAD", IPAdVerifier.class);
        shortFormVerifiers.put("DOMIP", DomainIPVerifier.class);
        shortFormVerifiers.put("VISITID", VisitIdVerifier.class);
        shortFormVerifiers.put("VIDAD", VisitorIdAdVerifier.class);
        shortFormVerifiers.put("DOMVID", DomainVisitorIdVerifier.class);
    }

}
