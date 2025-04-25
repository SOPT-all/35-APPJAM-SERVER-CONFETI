package org.sopt.confeti.domain.elastic_search;

import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Document(indexName = "search_terms")
public record SearchTermDocument(

        @Field(type = FieldType.Keyword)
        String searchTerm,

        @Field(type = FieldType.Date)
        LocalDateTime timestamp
) {
    public static SearchTermDocument create(String searchTerm) {
        return SearchTermDocument.builder()
                .searchTerm(searchTerm)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
