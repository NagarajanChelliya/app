package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

public class AppApp {
        public static void main(final String[] args) {
                App app = new App();

               // new AppStack(app, "AppStack", StackProps.builder()

                           //     .build());
                new AppStack(app, "S3BucketStack");
                new PipelineStack(app, "PipelineStack");

                app.synth();
        }
}
