package org.sopt.confeti.global.common;

import org.sopt.confeti.global.config.aws.AwsProperties;
import org.springframework.stereotype.Service;

@Service
public class CdnFileDomainResolveService {

    private final String fileDomain;

    public CdnFileDomainResolveService(AwsProperties awsProperties) {
        this.fileDomain = awsProperties.cloudfront().domain().fileDomain();
    }

    public String resolve(String filePath) {
        if (filePath == null) {
            return null;
        }

        return fileDomain + "/" + filePath;
    }
}
