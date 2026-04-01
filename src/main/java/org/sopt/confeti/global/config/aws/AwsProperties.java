package org.sopt.confeti.global.config.aws;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@ConfigurationProperties(prefix = "aws")
public record AwsProperties(CloudFrontProperties cloudfront) {

    @PostConstruct
    public void init() {
        log.debug(cloudfront.domain.fileDomain);
    }

    public record CloudFrontProperties(CdnProperties domain) {

        public record CdnProperties(String fileDomain) {

        }
    }
}
