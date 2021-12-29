package com.pensarcomodev.script;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class ScriptCodeRunner {

    private final Map<String, Script> scriptCache = new HashMap<>();

    public synchronized Object runScriptCode(String code, Map<String, Object> parameters) {
        Script script = getScript(code);
        Binding binding = new Binding(parameters);
        script.setBinding(binding);
        return script.run();
    }

    private Script getScript(String code) {
        Script script = scriptCache.get(code);
        if (script == null) {
            GroovyShell groovyShell = buildGroovyShell();
            script = groovyShell.parse(code);
            scriptCache.put(code, script);
        }
        return script;
    }

    private GroovyShell buildGroovyShell() {
        SecureASTCustomizer astCustomizer = new SecureASTCustomizer();
        astCustomizer.setImportsWhitelist(Arrays.asList(
                Math.class.getCanonicalName(),
                Object.class.getCanonicalName(),
                ApplicationMethods.class.getCanonicalName()
        ));
        astCustomizer.setStatementsBlacklist(Arrays.asList(
                WhileStatement.class,
                DoWhileStatement.class,
                ForStatement.class
        ));
        astCustomizer.setAllowedReceivers(Arrays.asList(
                Object.class.getCanonicalName()
        ));
        astCustomizer.setIndirectImportCheckEnabled(true);
        CompilerConfiguration conf = new CompilerConfiguration();
        conf.addCompilationCustomizers(astCustomizer);
        return new GroovyShell(conf);
    }
}
