/*
 * Copyright 2009 the original author or authors.
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
package org.gradle.api.internal.artifacts.ivyservice.moduleconverter.dependencies

import org.gradle.api.InvalidUserDataException
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.internal.component.local.model.OpaqueComponentIdentifier
import org.gradle.internal.component.model.LocalOriginDependencyMetadata
import spock.lang.Specification

public class DefaultDependencyDescriptorFactoryTest extends Specification {
    def configurationName = "conf"
    def projectDependency = Stub(ProjectDependency)
    def componentId = new OpaqueComponentIdentifier("foo")

    def "delegates to internal factory"() {
        given:
        def ivyDependencyDescriptorFactory1 = Mock(IvyDependencyDescriptorFactory)
        def ivyDependencyDescriptorFactory2 = Mock(IvyDependencyDescriptorFactory)
        def result = Stub(LocalOriginDependencyMetadata)

        when:
        def dependencyDescriptorFactory = new DefaultDependencyDescriptorFactory(
                ivyDependencyDescriptorFactory1, ivyDependencyDescriptorFactory2
        );
        def created = dependencyDescriptorFactory.createDependencyDescriptor(componentId, configurationName, null, projectDependency)

        then:
        created == result

        and:
        1 * ivyDependencyDescriptorFactory1.canConvert(projectDependency) >> false
        1 * ivyDependencyDescriptorFactory2.canConvert(projectDependency) >> true
        1 * ivyDependencyDescriptorFactory2.createDependencyDescriptor(componentId, configurationName, null, projectDependency) >> result
    }

    def "fails where no internal factory can handle dependency type"() {
        def ivyDependencyDescriptorFactory1 = Mock(IvyDependencyDescriptorFactory);

        when:
        ivyDependencyDescriptorFactory1.canConvert(projectDependency) >> false

        and:
        def dependencyDescriptorFactory = new DefaultDependencyDescriptorFactory(
                ivyDependencyDescriptorFactory1
        );
        dependencyDescriptorFactory.createDependencyDescriptor(componentId, configurationName, null, projectDependency)

        then:
        thrown InvalidUserDataException
    }
}
