package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Aspects;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import io.github.cdklabs.cdknag.AwsSolutionsChecks;

import java.util.Arrays;

public class AppApp {
        public static void main(final String[] args) {
                App app = new App();

               // new AppStack(app, "AppStack", StackProps.builder()

                           //     .build());
               new AppStack(app, "AppStack");
              new PipelineStack(app, "PipelineStack");
             /*   new PipelineStack(app, "PipelineStack", StackProps.builder()
        .env(Environment.builder().account("242332171291").region("us-east-1").build())
        .build());
        */

                //Aspects.of(app).add(new AwsSolutionsChecks());//cdk-nag

                app.synth();
        }
}
