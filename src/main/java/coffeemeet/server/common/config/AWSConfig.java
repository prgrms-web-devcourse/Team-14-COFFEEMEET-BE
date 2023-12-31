package coffeemeet.server.common.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

  private final String accessKey;
  private final String secretKey;
  private final String region;

  public AWSConfig(
      @Value("${cloud.aws.credentials.access-key}") String accessKey,
      @Value("${cloud.aws.credentials.secret-key}") String secretKey,
      @Value("${cloud.aws.region.static}") String region) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
  }

  @Bean
  public AWSCredentials awsCredentials() {
    return new BasicAWSCredentials(accessKey, secretKey);
  }

  @Bean
  public AmazonS3 amazonS3Client(AWSCredentials awsCredentials) {
    return AmazonS3ClientBuilder
        .standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .build();
  }

}
