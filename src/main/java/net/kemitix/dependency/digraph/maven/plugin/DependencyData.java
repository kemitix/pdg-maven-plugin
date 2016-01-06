package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.logging.Log;

import java.util.List;

/**
 * Interface for storing package and class dependency data.
 *
 * @author pcampbell
 */
public interface DependencyData {

    /**
     * Records a dependency between the user class and the imported class.
     *
     * @param userPackage     the package that is using the import
     * @param userClass       the class within the package that is using the
     *                        imported class
     * @param importedPackage the package that contains the class that is being
     *                        imported
     * @param importedClass   the class within the package that is being
     *                        imported
     */
    void addDependency(String userPackage, String userClass,
            String importedPackage, String importedClass);

    /**
     * Dumps the dependencies to stdout.
     *
     * @param log the log to write to
     */
    void dumpDependencies(final Log log);

    /**
     * Returns a list of packages that use other packages.
     *
     * @return the list of package names
     */
    List<String> getUserPackages();

    /**
     * Returns a list of packages that are used by the user package.
     *
     * @param user the package to filter on
     *
     * @return the list of package names
     */
    List<String> getUsedPackages(String user);

}