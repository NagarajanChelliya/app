package com.myorg;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.codepipeline.*;
import software.amazon.awscdk.services.codepipeline.actions.*;
import software.amazon.awscdk.services.codebuild.*;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.*;

public class PipelineStack extends Stack {

    public PipelineStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public PipelineStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // -------------------------------
        // Define a new CodePipeline
        // -------------------------------
        Pipeline pipeline = Pipeline.Builder.create(this, "MyPipeline")
                .pipelineName("S3BucketPipeline")
                .build();

        // -------------------------------
        // GitHub Source Stage
        // -------------------------------
        Artifact sourceOutput = new Artifact("SourceOutput");

        CodeStarConnectionsSourceAction sourceAction = CodeStarConnectionsSourceAction.Builder.create()
                .actionName("GitHub_Source")
                .owner("NagarajanChelliya") // replace
                .repo("app")        // replace
                .branch("main")
                .connectionArn("arn:aws:codeconnections:us-east-1:242332171291:connection/a9730499-9831-4922-a23e-79ab6c36cea2")
                .output(sourceOutput)
                .build();

        pipeline.addStage(StageOptions.builder()
                .stageName("Source")
                .actions(List.of(sourceAction))
                .build());

        // -------------------------------
        // Build & Deploy Stage
        // -------------------------------
        PipelineProject buildProject = PipelineProject.Builder.create(this, "CDKBuildProject")
                .buildSpec(BuildSpec.fromObject(Map.of(
                        "version", "0.2",
                        "phases", Map.of(
                                "install", Map.of("commands", List.of(
                                        "npm install -g aws-cdk",
                                        "mvn clean install"
                                )),
                                "build", Map.of("commands", List.of(
                                        "cdk synth",
                                        "cdk deploy --require-approval=never"
                                ))
                        )
                )))
                .build();

        CodeBuildAction buildAction = CodeBuildAction.Builder.create()
                .actionName("CDK_Build_Deploy")
                .project(buildProject)
                .input(sourceOutput)
                .build();

        pipeline.addStage(StageOptions.builder()
                .stageName("Build_Deploy")
                .actions(List.of(buildAction))
                .build());
    }
}
