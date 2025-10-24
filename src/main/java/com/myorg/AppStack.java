package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.s3.Bucket;

public class AppStack extends Stack {
    public AppStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AppStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // Create an S3 bucket
        Bucket bucket = Bucket.Builder.create(this, "MySimpleBucket")
                .versioned(true) // optional, enables versioning
                .removalPolicy(software.amazon.awscdk.RemovalPolicy.DESTROY) // optional, for dev/testing
                .build();
    }
}
