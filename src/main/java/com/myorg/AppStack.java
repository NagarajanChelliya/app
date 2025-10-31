package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.s3.Bucket;

import java.util.Arrays;

import io.github.cdklabs.cdknag.AwsSolutionsChecks;
import io.github.cdklabs.cdknag.NagPackSuppression;
import io.github.cdklabs.cdknag.NagSuppressions;



public class AppStack extends Stack {
    public AppStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AppStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // Create an S3 bucket
        Bucket bucket = Bucket.Builder.create(this, "MyDemoBucket")
        .bucketName("nagarajan-2-true-" + this.getAccount() + "-" + this.getRegion())
                .versioned(true) // optional, enables versioning
                .removalPolicy(software.amazon.awscdk.RemovalPolicy.DESTROY) // optional, for dev/testing
                .build();
    
        NagSuppressions.addStackSuppressions(
                this,
                Arrays.asList(
                        NagPackSuppression.builder()
                                .id("AwsSolutions-S1")
                                .reason("Access logs disabled for all demo buckets.")
                                .build(),
                        NagPackSuppression.builder()
                                .id("AwsSolutions-S10")
                                .reason("SSL enforcement not needed for all demo buckets.")
                                .build()),
                true
        );
    
   
    }
}
