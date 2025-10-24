package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.codepipeline.Pipeline;
import software.amazon.awscdk.services.codepipeline.PipelineProps;
import software.amazon.awscdk.services.codepipeline.actions.GitHubSourceAction;
import software.amazon.awscdk.services.codepipeline.actions.CodeBuildAction;
import software.amazon.awscdk.services.codepipeline.actions.GitHubTrigger;
import software.amazon.awscdk.services.codebuild.PipelineProject;
import software.amazon.awscdk.services.codebuild.BuildSpec;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.constructs.Construct;

public class PipelineStack extends Stack {
    public PipelineStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public PipelineStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // Create a CodePipeline
        Pipeline pipeline = Pipeline.Builder.create(this, "MyPipeline")
                .pipelineName("S3BucketPipeline")
                .build();

        // GitHub Source Action
        GitHubSourceAction source = GitHubSourceAction.Builder.create()
                .actionName("GitHub_Source")
                .owner("NagarajanChelliya") // replace with your GitHub username
                .repo("PTSBDevops")        // replace with your repo
                .oauthToken("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") // store token in Secrets Manager
                //.oauthToken(SecretValue.secretsManager("GITHUB_TOKEN")) // store token in Secrets Manager
                .branch("main")
                .trigger(GitHubTrigger.WEBHOOK)
                //.output(new Artifact_())
                .build();

        pipeline.addStage(stage -> stage.stageName("Source").actions(Arrays.asList(source)));

        // CodeBuild project to run CDK deploy
        PipelineProject buildProject = PipelineProject.Builder.create(this, "CDKBuild")
                .buildSpec(BuildSpec.fromObject(Map.of(
                        "version", "0.2",
                        "phases", Map.of(
                                "install", Map.of(
                                        "commands", Arrays.asList(
                                                "npm install -g aws-cdk",
                                                "mvn install"
                                        )
                                ),
                                "build", Map.of(
                                        "commands", Arrays.asList(
                                                "cdk synth",
                                                "cdk deploy --require-approval=never"
                                        )
                                )
                        )
                )))
                .build();

        CodeBuildAction buildAction = CodeBuildAction.Builder.create()
                .actionName("CDK_Build_Deploy")
                .project(buildProject)
                .input(source.getOutput())
                .build();

        pipeline.addStage(stage -> stage.stageName("BuildDeploy").actions(Arrays.asList(buildAction)));
    }
}
