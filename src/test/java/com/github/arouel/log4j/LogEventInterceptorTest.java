package com.github.arouel.log4j;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LogEventInterceptorTest {

    private static Logger logger = LoggerFactory.getLogger(LogEventInterceptorTest.class);

    @Test
    void test1() {
        LogEventInterceptor interceptor = LogEventInterceptor.register(LogEventInterceptorTest.class);
        LoggerFactory.getLogger("Logger2").info("message1");
        logger.info("message2");
        interceptor.close();
        logger.info("message3");

        assertThat(interceptor.messages()).containsExactly("message2");
        assertThat(interceptor.events()).isNotEmpty();
    }

    @Test
    void test2() {
        LogEventInterceptor interceptor = LogEventInterceptor.register("Logger1");
        LoggerFactory.getLogger("Logger2").info("message1");
        LoggerFactory.getLogger("Logger1").info("message2");
        interceptor.close();
        LoggerFactory.getLogger("Logger1").info("message3");

        assertThat(interceptor.messages()).containsExactly("message2");
        assertThat(interceptor.events()).isNotEmpty();
    }

}
