//package com.app.config;
//
//import java.util.concurrent.Executor;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//@Configuration
//@EnableAsync
//public class AsyncConfig {
//
//    @Bean(name = "emailExecutor")
//    public Executor emailExecutor() {
//
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//
//        executor.setCorePoolSize(5);     // minimum threads
//        executor.setMaxPoolSize(10);     // maximum threads
//        executor.setQueueCapacity(50);   // queue
//        executor.setThreadNamePrefix("EMAIL-THREAD-");
//
//        executor.initialize();
//        return executor;
//    }
//}

package com.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
}
