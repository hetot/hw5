package com.github.javarar.rejected.task;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public final class CustomThreadExecutors {

    private static final RejectedExecutionHandler DEFAULT_HANDLER = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.error("Задача не добавлена {}", r);
        }
    };

    public static Executor logRejectedThreadPoolExecutor(/*можно передать такие аргументы, которые помогут вам сконструировать нужный пул*/) {
        return new ThreadPoolExecutor(1, 1, 0, TimeUnit.MICROSECONDS,
                new SynchronousQueue<>(),
                DEFAULT_HANDLER
        );
    }
}
