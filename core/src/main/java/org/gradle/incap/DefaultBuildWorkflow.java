package org.gradle.incap;

import java.io.IOException;

/** Default implementation of the {@code BuildWorkflow} interface. */
public class DefaultBuildWorkflow implements BuildWorkflow {

    // Called by Incap only.
    DefaultBuildWorkflow() {}

    @Override
    public PreBuildResult preBuild(BuildSpec spec) {
        // TODO:  Read PG from disk, if necessary.

        PreBuildResult result =
                new PreBuildResult() {
                    @Override
                    public PreBuildResultStatus status() {
                        return PreBuildResultStatus.FULL_REBUILD;
                    }

                    @Override
                    public String message() {
                        return null;
                    }
                };
        return result;
    }

    @Override
    public PostBuildResult postBuild() throws IOException {
        // TODO:  serialize PG
        return new PostBuildResult() {
            @Override
            public PostBuildResultStatus status() {
                return PostBuildResultStatus.SUCCESS;
            }

            @Override
            public String message() {
                return null;
            }
        };
    }
}
