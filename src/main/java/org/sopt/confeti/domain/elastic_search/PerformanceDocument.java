package org.sopt.confeti.domain.elastic_search;

import java.time.LocalDate;
import lombok.Builder;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Document(indexName = "performances")
public record PerformanceDocument(

        @Id
        long id,

        @Field(type = FieldType.Text, analyzer = "autocomplete_analyzer", searchAnalyzer = "search_analyzer")
        String title,

        @Field(type = FieldType.Date)
        LocalDate startAt,

        @Field(type = FieldType.Date)
        LocalDate endAt,

        @Field(type = FieldType.Text)
        String posterPath,

        @Field(type = FieldType.Text)
        String area,

        @Field(type = FieldType.Keyword)
        PerformanceType type
) {
    public static PerformanceDocument create(PerformanceDTO performance) {
        return PerformanceDocument.builder()
                .id(performance.id())
                .title(performance.title())
                .startAt(performance.startAt())
                .endAt(performance.endAt())
                .posterPath(performance.posterPath())
                .area(performance.area())
                .type(performance.type())
                .build();
    }
}