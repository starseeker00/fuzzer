import javafx.util.Pair;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Example usage of the JaCoCo core API. In this tutorial a single target class
 * will be instrumented and executed. Finally the coverage information will be
 * dumped.
 */
public class CoverageRunner implements Runner {

    private String param;
    private ArrayList<Integer> coverage = new ArrayList<>();

    private final OutputStream out;

    public CoverageRunner() {
        out = System.out;
    }

    public CoverageRunner(OutputStream out) {
        this.out = out;
    }

    /**
     * A class loader that loads classes from in-memory data.
     */
    public static class MemoryClassLoader extends ClassLoader {

        private final Map<String, byte[]> definitions = new HashMap<String, byte[]>();

        /**
         * Add a in-memory representation of a class.
         *
         * @param name  name of the class
         * @param bytes class definition
         */
        public void addDefinition(final String name, final byte[] bytes) {
            definitions.put(name, bytes);
        }

        @Override
        protected Class<?> loadClass(final String name, final boolean resolve)
                throws ClassNotFoundException {
            final byte[] bytes = definitions.get(name);
            if (bytes != null) {
                return defineClass(name, bytes, 0, bytes.length);
            }
            return super.loadClass(name, resolve);
        }

    }

    /**
     * Run this example.
     *
     * @throws Exception in case of errors
     */
    public String execute() throws Exception {
        final String targetName = PUT.class.getName();

        // For instrumentation and runtime we need a IRuntime instance to collect execution data:
        final IRuntime runtime = new LoggerRuntime();

        // The Instrumenter creates a modified version of our test target class that contains additional probes for execution data recording:
        final Instrumenter instr = new Instrumenter(runtime);
        InputStream original = getTargetClass(targetName);
        final byte[] instrumented = instr.instrument(original, targetName);
        original.close();

        // Now we're ready to run our instrumented class and need to startup the runtime first:
        final RuntimeData data = new RuntimeData();
        runtime.startup(data);

        // In this tutorial we use a special class loader to directly load the instrumented class definition from a byte[] instances.
        final MemoryClassLoader memoryClassLoader = new MemoryClassLoader();
        memoryClassLoader.addDefinition(targetName, instrumented);
        final Class<?> targetClass = memoryClassLoader.loadClass(targetName);

        // Here we execute our test target class through its Runnable interface:
        final Runnable targetInstance = (Runnable) targetClass.getConstructor(String.class).newInstance(this.param);
        try {
            targetInstance.run();
            return targetInstance.toString();
        } finally {

            // At the end of test execution we collect execution data and shutdown the runtime:
            final ExecutionDataStore executionData = new ExecutionDataStore();
            final SessionInfoStore sessionInfos = new SessionInfoStore();
            data.collect(executionData, sessionInfos, false);
            runtime.shutdown();

            // Together with the original class definition we can calculate coverage information:
            final CoverageBuilder coverageBuilder = new CoverageBuilder();
            final Analyzer analyzer = new Analyzer(executionData, coverageBuilder);
            original = getTargetClass(targetName);
            analyzer.analyzeClass(original, targetName);
            original.close();

            // Let's dump some metrics and line coverage information:
            coverage.clear();
            for (final IClassCoverage cc : coverageBuilder.getClasses()) {
                for (int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {
                    int status = cc.getLine(i).getStatus();
                    if (status == ICounter.PARTLY_COVERED || status == ICounter.FULLY_COVERED) {
                        coverage.add(i);
                    }
                }
            }
        }
    }

    private InputStream getTargetClass(final String name) {
        final String resource = '/' + name.replace('.', '/') + ".class";
        return getClass().getResourceAsStream(resource);
    }

    @Override
    public Pair<String, String> run(String inp) {
        param = inp;

        String k, v;
        try {
            k = this.execute();
            v = PASS;
        } catch (Exception e) {
            k = e.toString();
            v = FAIL;
            try {
                out.write(("input: " + param + "\n").getBytes(StandardCharsets.UTF_8));
                out.write((k + "\n\n").getBytes(StandardCharsets.UTF_8));
                out.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return new Pair<>(k, v);
    }

    public ArrayList<Integer> getCoverage() {
        return coverage;
    }

}
