#!/bin/bash
java -Xmx8g -cp ./target/classes/:/Users/guillaume.haben/.m2/repository/fr/inria/gforge/spoon/spoon-core/7.5.0/spoon-core-7.5.0.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/jdt/org.eclipse.jdt.core/3.16.0/org.eclipse.jdt.core-3.16.0.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.core.resources/3.13.500/org.eclipse.core.resources-3.13.500.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.core.expressions/3.6.500/org.eclipse.core.expressions-3.6.500.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.core.runtime/3.16.0/org.eclipse.core.runtime-3.16.0.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.osgi/3.15.0/org.eclipse.osgi-3.15.0.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.equinox.common/3.10.500/org.eclipse.equinox.common-3.10.500.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.core.jobs/3.10.500/org.eclipse.core.jobs-3.10.500.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.equinox.registry/3.8.500/org.eclipse.equinox.registry-3.8.500.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.equinox.preferences/3.7.500/org.eclipse.equinox.preferences-3.7.500.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.core.contenttype/3.7.400/org.eclipse.core.contenttype-3.7.400.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.equinox.app/1.4.300/org.eclipse.equinox.app-1.4.300.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.core.filesystem/1.7.500/org.eclipse.core.filesystem-1.7.500.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.text/3.9.0/org.eclipse.text-3.9.0.jar:/Users/guillaume.haben/.m2/repository/org/eclipse/platform/org.eclipse.core.commands/3.9.500/org.eclipse.core.commands-3.9.500.jar:/Users/guillaume.haben/.m2/repository/com/martiansoftware/jsap/2.1/jsap-2.1.jar:/Users/guillaume.haben/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:/Users/guillaume.haben/.m2/repository/commons-io/commons-io/2.6/commons-io-2.6.jar:/Users/guillaume.haben/.m2/repository/org/apache/maven/maven-model/3.5.0/maven-model-3.5.0.jar:/Users/guillaume.haben/.m2/repository/org/codehaus/plexus/plexus-utils/3.0.24/plexus-utils-3.0.24.jar:/Users/guillaume.haben/.m2/repository/org/apache/commons/commons-lang3/3.8.1/commons-lang3-3.8.1.jar:/Users/guillaume.haben/.m2/repository/org/tukaani/xz/1.8/xz-1.8.jar:/Users/guillaume.haben/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.9.9/jackson-databind-2.9.9.jar:/Users/guillaume.haben/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.jar:/Users/guillaume.haben/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.9.9/jackson-core-2.9.9.jar:/Users/guillaume.haben/.m2/repository/org/apache/commons/commons-compress/1.18/commons-compress-1.18.jar:/Users/guillaume.haben/.m2/repository/org/apache/maven/shared/maven-invoker/3.0.1/maven-invoker-3.0.1.jar:/Users/guillaume.haben/.m2/repository/org/apache/maven/shared/maven-shared-utils/3.2.1/maven-shared-utils-3.2.1.jar:/Users/guillaume.haben/.m2/repository/org/codehaus/plexus/plexus-component-annotations/1.7.1/plexus-component-annotations-1.7.1.jar:/Users/guillaume.haben/.m2/repository/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar:/Users/guillaume.haben/.m2/repository/junit/junit/4.10/junit-4.10.jar:/Users/guillaume.haben/.m2/repository/org/hamcrest/hamcrest-core/1.1/hamcrest-core-1.1.jar base.Main "$@"
