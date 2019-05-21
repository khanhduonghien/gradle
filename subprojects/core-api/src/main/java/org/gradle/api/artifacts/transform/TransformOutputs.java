/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.artifacts.transform;

import org.gradle.api.Incubating;
import org.gradle.internal.HasInternalProtocol;

import java.io.File;

/**
 * The outputs of the artifact transform.
 *
 * <p>
 *     The registered output {@link File}s will appear in the transformed variant in the order they were registered.
 * </p>
 *
 * @since 5.3
 */
@Incubating
@HasInternalProtocol
public interface TransformOutputs {
    /**
     * Registers an output directory.
     *
     * <p>
     *     For an <strong>absolute path</strong>, the location is registered as an output directory of the {@link TransformAction}.
     *     The path is required to point to the {@link InputArtifact}, or point inside the input artifact if it is a directory.
     *     Example:
     * </p>
     * <pre>
     * Provider&lt;FileSystemLocation&gt; inputArtifact = ...
     * outputs.dir(inputArtifact.get().getAsFile());
     * outputs.dir(new File(inputArtifact.get().getAsFile(), "sub-dir"));
     * </pre>
     *
     * <p>
     *     For a <strong>relative path</strong>, a location for the output directory is provided by Gradle, so that the {@link TransformAction} can produce its outputs at that location.
     *     The directory is created automatically when calling the method.
     *     Example:
     * </p>
     * <pre>
     * File myOutput = outputs.dir("my-output");
     * Files.write(myOutput.toPath().resolve("file.txt"), "Generated text");
     * </pre>
     *
     * <p>
     *     The registered directory needs to exist when the {@link TransformAction#transform(TransformOutputs)} method finishes.
     * </p>
     *
     * @param path path of the output directory
     * @return determined location of the output
     */
    File dir(Object path);

    /**
     * Registers an output file.
     *
     * <p>
     *     For an absolute path, the location is registered as an output file of the {@link TransformAction}.
     *     The path is required to point to the {@link InputArtifact} or be inside it if the input artifact is a directory.
     *     Example:
     * </p>
     * <pre>
     * Provider&lt;FileSystemLocation&gt; inputArtifact = ...
     * File input = inputArtifact.get().getAsFile();
     * if (input.isFile()) {
     *     outputs.file(input);
     * } else {
     *     outputs.file(new File(input, "file-in-input-artifact.txt"));
     * }
     * </pre>
     *
     * <p>
     *     For a relative path, a location for the output file is provided by Gradle, so that the {@link TransformAction} can produce its outputs at that location.
     *     The parent directory of the provided location is created automatically when calling the method.
     *     Example:
     * </p>
     * <pre>
     * File myOutput = outputs.file("my-output.transformed");
     * Files.write(myOutput.toPath(), "Generated text");
     * </pre>
     *
     * <p>The registered file needs to exist when the {@link TransformAction#transform(TransformOutputs)} method finishes.</p>
     *
     * @param path path of the output file
     * @return determined location of the output
     */
    File file(Object path);
}
