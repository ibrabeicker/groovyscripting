package com.pensarcomodev.script;

import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ScriptCodeRunnerTest {

    private final ScriptCodeRunner scriptCodeRunner = new ScriptCodeRunner();

    @Test
    public void testDisallowedLoop() {

        String code = "while (true) {}";

        Assertions.assertThrows(MultipleCompilationErrorsException.class,
                () -> scriptCodeRunner.runScriptCode(code, Collections.emptyMap()));
    }

    @Test
    public void testDenyUseOfUnsafeCode() {

        String code = "def socket = new java.net.Socket()";
        Assertions.assertThrows(MultipleCompilationErrorsException.class,
                () -> scriptCodeRunner.runScriptCode(code, Collections.emptyMap()));
    }

    @Test
    public void testSumResult() {

        String code = "var1 + var2";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("var1", 3);
        parameters.put("var2", BigDecimal.valueOf(2.0));
        Object result = scriptCodeRunner.runScriptCode(code, parameters);
        assertTrue(result instanceof BigDecimal);
        assertEquals(5, ((BigDecimal) result).intValue());
    }

    @Test
    public void testConcatenateResult() {

        String code = "var1 + var2";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("var1", "foo");
        parameters.put("var2", "bar");
        Object result = scriptCodeRunner.runScriptCode(code, parameters);
        assertEquals("foobar", result);
    }

    @Test
    public void testCallingStringMethod() {

        ApplicationMethods mock = Mockito.mock(ApplicationMethods.class);

        String code = "app.logMethod('foo')";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("app", mock);
        scriptCodeRunner.runScriptCode(code, parameters);

        verify(mock).logMethod(eq("foo"));
    }

    @Test
    public void testCallingNumberMethod() {

        ApplicationMethods mock = Mockito.mock(ApplicationMethods.class);

        String code = "app.logMethod(1000)";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("app", mock);
        scriptCodeRunner.runScriptCode(code, parameters);

        verify(mock).logMethod(eq(1000));
    }

    @Test
    public void testCallingBigDecimalMethod() {

        ApplicationMethods mock = Mockito.mock(ApplicationMethods.class);

        String code = "app.logMethod((BigDecimal) 1000)";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("app", mock);
        scriptCodeRunner.runScriptCode(code, parameters);

        verify(mock).logMethod(eq(BigDecimal.valueOf(1000)));
    }
}
