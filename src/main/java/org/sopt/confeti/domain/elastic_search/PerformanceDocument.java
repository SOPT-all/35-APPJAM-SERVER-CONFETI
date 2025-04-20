package org.sopt.confeti.domain.elastic_search;

import java.time.LocalDate;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Document(indexName = "performances")
public record PerformanceDocument(

        @Id
        long id,

        @Field(type = FieldType.Text)
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
}