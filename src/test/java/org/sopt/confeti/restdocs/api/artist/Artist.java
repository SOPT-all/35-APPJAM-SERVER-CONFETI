package org.sopt.confeti.restdocs.api.artist;

import org.sopt.confeti.restdocs.base.APIBaseTest;

public class Artist extends APIBaseTest {

    @Override
    protected boolean shouldResetESSearchTermData() {
        return true;
    }

    @Override
    protected boolean shouldResetESPerformanceData() {
        return false;
    }
}
