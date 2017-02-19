package net.kemitix.dependency.digraph.maven.plugin.stubs;

import lombok.Getter;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Project stub.
 *
 * @author pcampbell
 */
public class DigraphProjectStub extends MavenProjectStub {

    @Getter
    private final File basedir;

    /**
     * Default constructor.
     *
     * @param path the path to the project
     */
    public DigraphProjectStub(final String path) {
        basedir = new File(super.getBasedir() + path);
    }

    /**
     * Initialise the stub.
     */
    public void init() {
        MavenXpp3Reader pomReader = new MavenXpp3Reader();
        Model model;
        try {
            model = pomReader.read(ReaderFactory.newXmlReader(new File(getBasedir(), "pom.xml")));
            setModel(model);
        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }

        setGroupId(model.getGroupId());
        setArtifactId(model.getArtifactId());
        setVersion(model.getVersion());
        setName(model.getName());
        setUrl(model.getUrl());
        setPackaging(model.getPackaging());

        final String srcMainJava = getBasedir() + "/src/main/java";
        final String srcTestJava = getBasedir() + "/src/test/java";

        Build build = new Build();
        build.setFinalName(model.getArtifactId());
        build.setDirectory(getBasedir() + "/target");
        build.setSourceDirectory(srcMainJava);
        build.setOutputDirectory(getBasedir() + "/target/classes");
        build.setTestSourceDirectory(srcTestJava);
        build.setTestOutputDirectory(getBasedir() + "/target/test-classes");
        setBuild(build);

        List<String> compileSourceRoots = new ArrayList<>();
        compileSourceRoots.add(srcMainJava);
        setCompileSourceRoots(compileSourceRoots);

        List<String> testCompileSourceRoots = new ArrayList<>();
        testCompileSourceRoots.add(srcTestJava);
        setTestCompileSourceRoots(testCompileSourceRoots);
    }

}
