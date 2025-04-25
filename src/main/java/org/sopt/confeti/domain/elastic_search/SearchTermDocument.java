package org.sopt.confeti.domain.elastic_search;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Document(indexName = "search_terms")
public record SearchTermDocument(

        @Id
        @Field(type = FieldType.Keyword)
        String uuid,

        @Field(type = FieldType.Keyword)
        String searchTerm,

        @Field(type = FieldType.Date)
        LocalDateTime timestamp
) {
    public static SearchTermDocument create(String searchTerm) {
        return SearchTermDocument.builder()
                .uuid(UUID.randomUUID().toString())
                .searchTerm(searchTerm)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
