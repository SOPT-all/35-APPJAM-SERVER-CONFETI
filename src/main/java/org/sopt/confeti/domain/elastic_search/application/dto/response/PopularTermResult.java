package org.sopt.confeti.domain.elastic_search.application.dto.response;

public record PopularTermResult(
        int rank,
        String popularTerm
) {
    public static PopularTermResult of(int rank, String popularTerm) {
        return new PopularTermResult(
                rank,
                popularTerm
        );
    }
}
