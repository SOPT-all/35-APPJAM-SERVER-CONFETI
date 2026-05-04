package org.sopt.confeti.domain.elastic_search;

import java.time.LocalDate;
import lombok.Builder;
import org.sopt.confeti.domain.view.performance.Performance;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

@Builder
@Document(indexName = "upcoming_performances")
@Setting(settingPath = "/elasticsearch/performance-settings.json")
public record PerformanceDocument(

    @Field(type = FieldType.Long)
    long id,

    @Field(type = FieldType.Keyword)
    String type,

    @Field(type = FieldType.Long)
    long typeId,

    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "search_analyzer"),
        otherFields = {
            @InnerField(suffix = "autocomplete", type = FieldType.Text,
                analyzer = "autocomplete_analyzer", searchAnalyzer = "search_analyzer"),
            @InnerField(suffix = "partial", type = FieldType.Text,      // 추가!
                analyzer = "partial_match_analyzer", searchAnalyzer = "search_analyzer"),
            @InnerField(suffix = "keyword", type = FieldType.Keyword)
        }
    )
    String title,

    @Field(type = FieldType.Date)
    LocalDate startAt,

    @Field(type = FieldType.Date)
    LocalDate endAt,

    @Field(type = FieldType.Text)
    String posterPath,

    @Field(type = FieldType.Text)
    String area
) {

    public static PerformanceDocument create(Performance performance) {
        return PerformanceDocument.builder()
            .id(performance.getId())
            .type(performance.getType().getName())
            .typeId(performance.getTypeId())
            .title(performance.getTitle())
            .startAt(performance.getStartAt())
            .endAt(performance.getEndAt())
            .posterPath(performance.getPosterPath())
            .area(performance.getArea())
            .build();
    }
}
