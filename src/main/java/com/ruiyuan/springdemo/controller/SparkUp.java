package com.ruiyuan.springdemo.controller;


import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


@RestController
public class SparkUp {

    @GetMapping(value = "/sparkPI")
    public String submitTast() throws IOException {

        //hadoop、spark环境变量读取
        HashMap env = new HashMap();
        env.put("HADOOP_CONF_DIR", "/etc/hadoop/conf");
        env.put("YARN_CONF_DIR", "/etc/hadoop/conf");
        env.put("SPARK_CONF_DIR", "/etc/spark/conf");
        env.put("JAVA_HOME", "/opt/module/java");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

        //创建spark启动对象，并设置监听，spark启动的各参数
        SparkAppHandle handler = new SparkLauncher(env)
                .setSparkHome("/opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/spark")
                .setAppResource("/software/pi.py")
                .setMainClass("pi")
                .setAppName("python_pi" + " " + df.format(new Date()))
                .setMaster("yarn")
                .setDeployMode("cluster")
                .setConf("spark.driver.memory", "2g")
                .setConf("spark.executor.memory", "1g")
                .setConf("spark.executor.cores", "3")
                .setVerbose(true)
                .startApplication(new SparkAppHandle.Listener() {
                    @Override
                    public void stateChanged(SparkAppHandle handle) {
                        System.out.println("**********  state  changed  **********");
                    }

                    @Override
                    public void infoChanged(SparkAppHandle handle) {
                        System.out.println("**********  info  changed  **********");
                    }
                });

        while (!"FINISHED".equalsIgnoreCase(handler.getState().toString()) && !"FAILED".equalsIgnoreCase(handler.getState().toString()))
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return handler.getAppId();
    }

}

