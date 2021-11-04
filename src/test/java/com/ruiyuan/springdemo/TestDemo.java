package com.ruiyuan.springdemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test; //注意这里使用的是jupiter的Test注解！！
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.StringUtils;

import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;


public class TestDemo {

    @Test
    @DisplayName("第一次测试")
    public void firstTest() {
        System.out.println("hello world");
    }

    @Test
    @DisplayName("simple assertion")
    public void simple() {
        assertEquals(3, 1 + 2, "simple math");
        assertNotEquals(3, 1 + 1);

        assertNotSame(new Object(), new Object());
        Object obj = new Object();
        assertSame(obj, obj);

        assertFalse(1 > 2);
        assertTrue(1 < 2);

        assertNull(null);
        assertNotNull(new Object());
    }

    @DisplayName("前置条件")
    public class AssumptionsTest {
        private final String environment = "DEV";

        @Test
        @DisplayName("simple")
        public void simpleAssume() {
            assumeTrue(Objects.equals(this.environment, "DEV"));
            assumeFalse(() -> Objects.equals(this.environment, "PROD"));
        }

        @Test
        @DisplayName("assume then do")
        public void assumeThenDo() {
            assumingThat(
                    Objects.equals(this.environment, "DEV"),
                    () -> System.out.println("In DEV")
            );
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"one", "two", "three"})
    @DisplayName("参数化测试1")
    public void parameterizedTest1(String string) {
        System.out.println(string);
        Assertions.assertTrue(StringUtils.isNotBlank(string));
    }


    @ParameterizedTest
    @MethodSource("method")    //指定方法名
    @DisplayName("方法来源参数")
    public void testWithExplicitLocalMethodSource(String name) {
        System.out.println(name);
        Assertions.assertNotNull(name);
    }

    static Stream<String> method() {
        return Stream.of("apple", "banana");
    }
}

