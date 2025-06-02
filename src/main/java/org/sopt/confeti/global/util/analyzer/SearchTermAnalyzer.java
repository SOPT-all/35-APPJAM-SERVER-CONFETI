package org.sopt.confeti.global.util.analyzer;

import kr.co.shineware.nlp.komoran.model.KomoranResult;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.MorphemeAnalyzer;
import org.sopt.confeti.global.util.analyzer.dto.PerformanceSearchTermAnalyzeResult;

public class SearchTermAnalyzer {

    public static PerformanceSearchTermAnalyzeResult analyzePerformance(String term) {
        KomoranResult analyzeResult = MorphemeAnalyzer.getAnalyzeResult(term);
        PerformanceType performanceType = MorphemeAnalyzer.getFirstMatchingPerformanceType(analyzeResult);
        String processedTerm = MorphemeAnalyzer.getRemovedPerformanceTypesTerm(term, analyzeResult);

        return PerformanceSearchTermAnalyzeResult.of(processedTerm, performanceType);
    }
}
