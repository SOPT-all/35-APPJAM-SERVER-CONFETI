package org.sopt.confeti.global.util;

import java.util.ArrayList;
import java.util.List;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.common.constant.PerformanceKeyword;
import org.sopt.confeti.global.common.constant.PerformanceType;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MorphemeAnalyzer {

    private static final Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

    public static KomoranResult getAnalyzeResult(String sentence) {
        return komoran.analyze(sentence);
    }

    public static PerformanceType getFirstMatchingPerformanceType(KomoranResult analyzeResult) {
        List<Token> tokens = analyzeResult.getTokenList();

        for (Token token : tokens) {
            log.debug(token.getMorph());
            if (PerformanceKeyword.isValid(token.getMorph())) {
                return PerformanceKeyword.getMatchingPerformanceType(token.getMorph());
            }
        }

        return PerformanceType.PERFORMANCE;
    }

    public static String getRemovedPerformanceTypesTerm(String sentence, KomoranResult analyzeResult) {
        List<Token> tokens = analyzeResult.getTokenList();
        List<int[]> performanceKeywordIndexes = new ArrayList<>();

        tokens.forEach(token -> {
            if (PerformanceKeyword.isValid(token.getMorph())) {
                performanceKeywordIndexes.add(new int[]{token.getBeginIndex(), token.getEndIndex()});
            }
        });

        if (performanceKeywordIndexes.isEmpty()) {
            return sentence;
        }

        StringBuilder result = new StringBuilder();
        int startIdx = 0;

        for (int[] performanceTypeIndex : performanceKeywordIndexes) {
            if (startIdx < performanceTypeIndex[0]) {
                result.append(sentence, startIdx, performanceTypeIndex[0]);
            }

            startIdx = performanceTypeIndex[1];
        }

        if (startIdx < sentence.length()) {
            result.append(sentence.substring(startIdx));
        }

        return result.toString()
                .replaceAll("\\s+", " ")
                .trim();
    }
}
