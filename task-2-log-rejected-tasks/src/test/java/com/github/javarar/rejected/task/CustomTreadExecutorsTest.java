package com.github.javarar.rejected.task;

import com.github.javarar.rejected.task.log.MyAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class CustomTreadExecutorsTest {
    @Test
    public void threadPoolDoesNotThrowExceptionOnQueueOverflow() {
        var lc = (LoggerContext) LogManager.getContext(false);
        var myAppender = new MyAppender("myAppender", null, PatternLayout.createDefaultLayout(), true, null);
        myAppender.start();
        lc.getConfiguration().addAppender(myAppender);
        lc.getRootLogger().addAppender(lc.getConfiguration().getAppender(myAppender.getName()));
        lc.updateLoggers();

        Assertions.assertEquals(0, myAppender.getEvents().size());
        var executor = CustomThreadExecutors.logRejectedThreadPoolExecutor();
        executor.execute(() -> {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        executor.execute(() -> {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Assertions.assertEquals(1, myAppender.getEvents().size());
        Assertions.assertEquals(Level.ERROR, myAppender.getEvents().get(0));
    }
}
