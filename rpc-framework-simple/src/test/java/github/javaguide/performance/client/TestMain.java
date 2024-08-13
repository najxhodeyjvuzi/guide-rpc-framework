package github.javaguide.performance.client;

import github.javaguide.annotation.RpcReference;
import github.javaguide.annotation.RpcScan;
import github.javaguide.performance.service.TestService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


/**
 * a simple test for performance, referring to the test plan in dubbo doc
 *
 * @author najxhodeyjvuzi
 * @createTime 2024/8/13
 */
@RpcScan(basePackage = {"github.javaguide.performance.client"})
@Component
public class TestMain {

    @RpcReference(group = "test1", version = "version1")
    private TestService testService;

    private static final int THREAD_POOL_SIZE = 10;
    private static final int REQUEST_TIMES = 1000;
    private static int MESSAGE_LENGTH = 50000;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private final HashMap<Integer, FutureTask<Float>> futureTaskMap = new HashMap<>();
    private static int totalStatisticTime = 0;

    private static class GenerateString {
        public static String withLength(int length) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append("a");
            }
            return sb.toString();
        }
    }

    private class StringTestCallable implements Callable<Float> {
        @Override
        public Float call() throws Exception {
            String message = GenerateString.withLength(MESSAGE_LENGTH);
            long start = System.nanoTime();
            String result = testService.hello(message);
            long end = System.nanoTime();
            long delta = end - start;
            float milliTime = (float) delta / 1000000F;
            return milliTime;
        }
    }

    private void performRpcCall(int requestId) {

        Callable<Float> callable = new StringTestCallable();
        futureTaskMap.putIfAbsent(requestId, new FutureTask<>(callable));
        executorService.submit(futureTaskMap.get(requestId));
    }

    @org.junit.Test
    public void testPerformanceWithString1k() {
        totalStatisticTime = 0;
        MESSAGE_LENGTH = 1000;

        for (int i = 0; i < REQUEST_TIMES; i++) {
            performRpcCall(i);
        }

        for (int i = 0; i < REQUEST_TIMES; i++) {
            try {
                float time = futureTaskMap.get(i).get();
                totalStatisticTime += time;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @org.junit.Test
    public void testPerformanceWithString50k() {
        totalStatisticTime = 0;
        MESSAGE_LENGTH = 50000;

        for (int i = 0; i < REQUEST_TIMES; i++) {
            performRpcCall(i);
        }

        for (int i = 0; i < REQUEST_TIMES; i++) {
            try {
                float time = futureTaskMap.get(i).get();
                totalStatisticTime += time;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static long REQUEST_TIME_200K = 0;

    public void testPerformanceWithString200k() {
        totalStatisticTime = 0;
        MESSAGE_LENGTH = 200000;

//        long start = System.currentTimeMillis();
        for (int i = 0; i < REQUEST_TIMES; i++) {
            performRpcCall(i);
        }
//        long end = System.currentTimeMillis();
//        REQUEST_TIME_200K = end - start;

        for (int i = 0; i < REQUEST_TIMES; i++) {
            try {
                float time = futureTaskMap.get(i).get();
                totalStatisticTime += time;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestMain.class);
//        TestController testController = context.getBean(TestController.class);
//        testController.test();
        TestMain testMain = context.getBean(TestMain.class);
        testMain.testPerformanceWithString200k();
        System.out.println("average time: " + (float) totalStatisticTime / REQUEST_TIMES + "ms");
//        System.out.println("TPS: " + (float) REQUEST_TIMES / (REQUEST_TIME_200K / 1000));
    }

}
