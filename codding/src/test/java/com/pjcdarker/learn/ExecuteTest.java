package com.pjcdarker.learn;

import com.pjcdarker.learn.reflection.Execute;
import org.junit.jupiter.api.Test;

/**
 * @author pjcdarker
 * @created 9/20/2017.
 */
public class ExecuteTest {

    private final Execute instance = new Execute();

    public class AProcess implements Execute.ProcessBase {
        public Boolean step_1() {
            return true;
        }

        public Boolean step_2() {
            return false;
        }
    }

    @Test
    public void getMethodFromStepid() throws Exception {
        final AProcess process = new AProcess();
        {
            final Execute.Step methodRef = instance.getMethodFromStepid(process, 1);
            final boolean result = methodRef.apply();
            assert (result);
        }
        {
            final Execute.Step methodRef = instance.getMethodFromStepid(process, 2);
            final boolean result = methodRef.apply();
            assert (result);
        }
    }


}
