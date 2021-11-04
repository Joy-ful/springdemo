package com.ruiyuan.springdemo.controller;


import com.ruiyuan.springdemo.util.InputStreamReaderRunnable;
import org.apache.spark.launcher.SparkLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
//submitTast


@RestController
public class Sparkupp {
    @GetMapping(value = "/sparkp")
    public String submitTast() throws IOException {
        HashMap env = new HashMap();
        //hadoop、spark环境变量读取
        env.put("HADOOP_CONF_DIR", "/opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/hadoop/etc/hadoop");
        env.put("JAVA_HOME", "/opt/module/java");
        env.put("YARN_CONF_DIR", "/opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/hadoop/etc/hadoop");
        //env.put("SparkHome", System.getenv().getOrDefault("SparkHome", "/opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/spark"));
        CountDownLatch countDownLatch = new CountDownLatch(1);

        //创建SparkLauncher启动器
        SparkLauncher handler = new SparkLauncher(env)
                /*.setSparkHome("/opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/spark")
                .setAppResource("/software/cook.py")*/
                .setSparkHome("F:\\spark\\spark-2.3.0-bin-hadoop2.7")
                .setAppResource("F:\\python_work\\test1\\cook.py")
                .setMainClass("cook")
                .setMaster("yarn")
                .setDeployMode("client")
                .setConf("spark.driver.memory", "2g")
                .setConf("spark.executor.memory", "1g")
                .setConf("spark.executor.cores", "3")
                .setConf("spark.driver.allowMultipleContexts", "true");
        //.setVerbose(true)
                /*.startApplication(new SparkAppHandle.Listener(){
                    @Override
                    public void stateChanged(SparkAppHandle handle) {
                        System.out.println("**********  state  changed  **********");
                    }

                    @Override
                    public void infoChanged(SparkAppHandle handle) {
                        System.out.println("**********  info  changed  **********");
                    }
                });


        while(!"FINISHED".equalsIgnoreCase(handler.getState().toString()) && !"FAILED".equalsIgnoreCase(handler.getState().toString())){
            System.out.println("id    "+handler.getAppId());
            System.out.println("state "+handler.getState());

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

                /*.startApplication(new SparkAppHandle.Listener() {
                    //这里监听任务状态，当任务结束时（不管是什么原因结束）,isFinal（）方法会返回true,否则返回false
                    @Override
                    public void stateChanged(SparkAppHandle sparkAppHandle) {
                        if (sparkAppHandle.getState().isFinal()) {
                            countDownLatch.countDown();
                        }
                        System.out.println("state:" + sparkAppHandle.getState().toString());
                    }


                    @Override
                    public void infoChanged(SparkAppHandle sparkAppHandle) {
                        System.out.println("Info:" + sparkAppHandle.getState().toString());
                    }
                });
        System.out.println("id*****************************    "+handler.getAppId()+"id*****************************    ");
        System.out.println("state--------------------- "+handler.getState()+"id*****************************    ");
        System.out.println("The task is executing, please wait ....");
        //线程等待任务结束
        countDownLatch.await();
        System.out.println("The task is finished!");
        return handler.getState().toString();*/
        //任务提交
        Process process = handler.launch();
        try {
            //日志跟踪子线程
            InputStreamReaderRunnable inputStreamReaderRunnable = new InputStreamReaderRunnable(process.getInputStream(), "input");
            Thread inputThread = new Thread(inputStreamReaderRunnable, "LogStreamReader input");
            inputThread.start();
            System.out.println("name 1 : "+inputThread.getName());
            System.out.println("state1: "+inputThread.getState());
            System.out.println("id1: "+inputThread.getId());
            System.out.println("name1: "+inputThread.getName());

            InputStreamReaderRunnable errorStreamReaderRunnable = new InputStreamReaderRunnable(process.getErrorStream(), "error");
            Thread errorThread = new Thread(errorStreamReaderRunnable, "LogStreamReader error");
            errorThread.start();

            System.out.println("Waiting for finish...");
            int exitCode = process.waitFor();
            System.out.println("Finished! Exit code:" + exitCode);
            return "status: " + exitCode;

        } catch (Exception e) {
            e.printStackTrace();
            return "status: " + 1;
        }
    }
/*    public String submitTast(){
        HashMap env = new HashMap();
        //hadoop、spark环境变量读取
        env.put("HADOOP_CONF_DIR", "/etc/hadoop/conf");
        env.put("JAVA_HOME", "/opt/module/java");
        env.put("SparkHome","/opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/spark");
        *//*env.put("HADOOP_CONF_DIR" ,  System.getenv().getOrDefault("HADOOP_CONF_DIR","/usr/local/hadoop/etc/overriterHaoopConf"));
        env.put("JAVA_HOME", System.getenv().getOrDefault("JAVA_HOME","/usr/local/java/jdk1.8.0_151"));*//*
        //创建SparkLauncher启动器
        SparkLauncher handle = new SparkLauncher(env)
                //.setSparkHome("/opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/spark")
                .setAppResource("/software/spark_test-1.0-SNAPSHOT.jar")
                .setMainClass("sequenceFile/SequenceFileJava")
                .setMaster("yarn")
                //.setDeployMode("client")
                //.setConf("spark.yarn.queue","sino")
                //.setConf("spark.app.id", "luncher-test")
                .setConf("spark.driver.memory", "1g")
                .setConf("spark.executor.memory", "1g")
                .setConf("spark.executor.instances", "2")
                .setConf("spark.executor.cores", "2")
                .setConf("spark.default.parallelism", "12")
                .setConf("spark.driver.allowMultipleContexts","true")
                .setVerbose(true);

        try {
            //任务提交
            Process process = handle.launch();
            //日志跟踪子线程
            InputStreamReaderRunnable inputStreamReaderRunnable = new InputStreamReaderRunnable(process.getInputStream(), "input");
            Thread inputThread = new Thread(inputStreamReaderRunnable, "LogStreamReader input");
            inputThread.start();

            InputStreamReaderRunnable errorStreamReaderRunnable = new InputStreamReaderRunnable(process.getErrorStream(), "error");
            Thread errorThread = new Thread(errorStreamReaderRunnable, "LogStreamReader error");
            errorThread.start();

            System.out.println("Waiting for finish...");
            int exitCode = process.waitFor();
            System.out.println("Finished! Exit code:" + exitCode);
            return "status: "+exitCode;

        }catch (Exception e){
            e.printStackTrace();
            return "status: "+1;
        }

    }*/
    @GetMapping(value = "/hello")
    public String hello(){
        return "this is hello page!";
    }

}

