package me.yonghwan.myapp.aws.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {
    private final AwsS3Properties awsS3Properties;

//    @Bean
//    public S3Client s3Client() {
//        AwsCredentialsProvider provider = StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Properties.getAccessKey(), awsS3Properties.getSecretKey()));
//       return S3Client.builder()
//                .region(Region.AP_NORTHEAST_2)
//                .credentialsProvider(provider)
//                .build();
//    }
    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsS3Properties.getAccessKey(), awsS3Properties.getSecretKey())))
                .withRegion(awsS3Properties.getRegion()).build();
    }
}
