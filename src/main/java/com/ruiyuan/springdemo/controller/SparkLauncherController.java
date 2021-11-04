package com.ruiyuan.springdemo.controller;

import com.ruiyuan.springdemo.util.RestUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.deploy.rest.CreateSubmissionResponse;
import org.apache.spark.deploy.rest.RestSubmissionClient;
import org.apache.spark.deploy.rest.RestSubmissionClientApp;
import org.apache.spark.deploy.rest.SubmissionStatusResponse;
import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

@RestController
public class SparkLauncherController {
    private static Logger log = LoggerFactory.getLogger(SparkLauncherController.class);

    @GetMapping(value = "/spark")
    public static void submit() {
        String appResource = "/software/spark_test-1.0-SNAPSHOT.jar";
        String mainClass = "sequenceFile/SequenceFileJava";
        //spark://manager2.bigdata:7077   spark://10.10.13.180:7077
        String master = "spark://10.10.13.180:18088";
        String[] args = {

        };  //spark程序需要的参数

        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster(master)//master地址
                .setSparkHome("/opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/spark")
                .setAppName("submitSparkTask" + " " + System.currentTimeMillis())
                .set("spark.total.executor.cores", "4")//执行需要的cores
                .set("spark.submit.deployMode", "cluster")//yarn模式
                .set("spark.jars", appResource)//执行jar地址
                .set("spark.executor.memory", "1")//执行需要的内存
                .set("spark.driver.memory", "1")//driver端需要的内存，有时任务会报内存溢出，加大driver端内存就可以
                //.set("spark.executor.extraClassPath", )//引用的其他jar地址
                //.set("spark.driver.extraClassPath", )//引用的其他jar地址
                .set("spark.driver.supervise", "false");


        CreateSubmissionResponse response = null;
        try {
            //任务提交
            RestSubmissionClientApp app = new RestSubmissionClientApp();
            response = (CreateSubmissionResponse)
                    app.run(appResource, mainClass, args, sparkConf, new scala.collection.immutable.HashMap<>());
        } catch (Exception e) {
            //异常打印;
            log.error("cuowu");
        }

        //获取submissionId
        String id = response.submissionId();
        log.info(response.submissionId());
        //获取执行状态
        RestSubmissionClient client = new RestSubmissionClient(master);
        SubmissionStatusResponse responseN = null;
        try {
            responseN = (SubmissionStatusResponse) client.requestSubmissionStatus(id, true);
        } catch (Exception e) {
            //异常打印;
            log.error("cuowu   id");
        }
        //获取执行状态RUNNING，FINISHED，ERROR, KILLED
        final String s =
                responseN.driverState();
        log.info(s);
    }
    /*public String submitTast() throws IOException, InterruptedException {

        HashMap env = new HashMap();
        //这两个属性必须设置
        env.put("HADOOP_CONF_DIR", "/etc/hadoop/conf");
        env.put("JAVA_HOME", "/opt/module/java");
        //env.put("SPARK_HOME", "/opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/spark/bin");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        SparkAppHandle handle = new SparkLauncher(env)
                //配置参数
                //\opt\cloudera\parcels\CDH-6.2.0-1.cdh6.2.0.p0.967373\lib\spark
                .setMaster("spark://10.10.13.180:7077")
                .setSparkHome("/opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/spark/")
                .setAppResource("/software/spark_test-1.0-SNAPSHOT.jar")
                .setMainClass("sequenceFile.SequenceFileJava")
                .setMaster("yarn")
                .setDeployMode("cluster")
                .setConf("spark.driver.memory", "1g")
                .setConf("spark.executor.memory", "1g")
                .setConf("spark.executor.instances", "2")
                .setConf("spark.executor.cores", "2")
                .setConf("spark.default.parallelism", "12")
                .setVerbose(true).startApplication(new SparkAppHandle.Listener() {
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
        System.out.println("The task is executing, please wait ....");
        //线程等待任务结束
        countDownLatch.await();
        System.out.println("The task is finished!");
        //通过Spark原生的监测api获取执行结果信息
        String restUrl = "http://master:18080/api/v1/applications/" + handle.getAppId();
        String resultJson = RestUtil.httpGet(restUrl, null);

        return resultJson;
    }*/

    /*public static String submit() {
        String appResource = "hdfs://cluster1/queryLog-1.0-SNAPSHOT.jar";
        String mainClass = "SelectLog";
        String[] args = {

        };  //spark程序需要的参数

        SparkConf sparkConf = new SparkConf();

        sparkConf.setMaster("spark://192.168.1.107:6066");
        sparkConf.set("spark.submit.deployMode", "cluster");
        sparkConf.set("spark.jars", appResource);
        sparkConf.set("spark.driver.supervise", "false");
        sparkConf.setAppName("queryLog"+ System.currentTimeMillis());

        CreateSubmissionResponse response = null;

        try {
            response = (CreateSubmissionResponse)
                    RestSubmissionClient.run(appResource, mainClass, args, sparkConf, new HashMap<String,String>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.submissionId();
    }

    private static RestSubmissionClient client = new RestSubmissionClient("spark://192.168.1.107:6066");

    public static boolean monitory(String appId){
        SubmissionStatusResponse response = null;
        boolean finished =false;
        try {
            response = (SubmissionStatusResponse) client.requestSubmissionStatus(appId, true);
            if("FINISHED" .equals(response.driverState()) || "ERROR".equals(response.driverState())){
                finished = true;
            }
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finished;
    }
    public static void main(String[] args) {
        String id = SparkLauncherController.submit();
        boolean flag;
        while (true) {
            flag = SparkLauncherController.monitory(id);
            if (flag) {
                break;
            }
        }
        System.out.println("spark执行完成");
    }*/

/*
        HashMap env = new HashMap();
        //这两个属性必须设置
        env.put("HADOOP_CONF_DIR", "/usr/local/hadoop/etc/overriterHaoopConf");
        env.put("JAVA_HOME", "/usr/local/java/jdk1.8.0_151");
        //env.put("YARN_CONF_DIR","");

        SparkLauncher handle = new SparkLauncher(env)
                .setSparkHome("\\opt\\cloudera\\parcels\\CDH-6.2.0-1.cdh6.2.0.p0.967373\\lib\\spark")
                .setAppResource("/opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/spark/bin/target/spark_test-1.0-SNAPSHOT.jar")
                .setMainClass("sequenceFile.SequenceFileJava")
                .setMaster("yarn")
                .setDeployMode("cluster")
                .setConf("spark.driver.memory", "2g")
                .setConf("spark.akka.frameSize", "200")
                .setConf("spark.executor.memory", "1g")
                .setConf("spark.executor.instances", "32")
                .setConf("spark.executor.cores", "3")
                .setConf("spark.default.parallelism", "10")
                .setConf("spark.driver.allowMultipleContexts", "true")
                .setVerbose(true);


        Process process = handle.launch();
        InputStreamReaderRunnable inputStreamReaderRunnable = new InputStreamReaderRunnable(process.getInputStream(), "input");
        Thread inputThread = new Thread(inputStreamReaderRunnable, "LogStreamReader input");
        inputThread.start();

        InputStreamReaderRunnable errorStreamReaderRunnable = new InputStreamReaderRunnable(process.getErrorStream(), "error");
        Thread errorThread = new Thread(errorStreamReaderRunnable, "LogStreamReader error");
        errorThread.start();

        System.out.println("Waiting for finish...");
        int exitCode = process.waitFor();
        System.out.println("Finished! Exit code:" + exitCode);
*/

/*        Process spark = new SparkLauncher()                .setSparkHome("\\opt\\cloudera\\parcels\\CDH-6.2.0-1.cdh6.2.0.p0.967373\\lib\\spark")

                .setAppResource("/opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/spark/bin/target/spark_test-1.0-SNAPSHOT.jar")
                .setMainClass("sequenceFile.SequenceFileJava")
                .setMaster("local")
                .setConf(SparkLauncher.DRIVER_MEMORY, "2g")
                .launch();
        spark.waitFor();*/

/*        HashMap env = new HashMap();
        //hadoop、spark环境变量读取
        env.put("HADOOP_CONF_DIR" ,  System.getenv().getOrDefault("HADOOP_CONF_DIR","/etc/hadoop/conf"));
        env.put("JAVA_HOME", System.getenv().getOrDefault("JAVA_HOME","/opt/module/java"));
        //创建SparkLauncher启动器
        SparkLauncher handle = new SparkLauncher(env)
                ///opt/cloudera/parcels/CDH-6.2.0-1.cdh6.2.0.p0.967373/lib/spark
                .setSparkHome("\\opt\\cloudera\\parcels\\CDH-6.2.0-1.cdh6.2.0.p0.967373\\lib\\spark")
                .setAppResource("/software/target/spark_test-1.0-SNAPSHOT.jar")
                .setMainClass("sequenceFile.SequenceFileJava")
                .setMaster("yarn")
                .setDeployMode("client")
                .setConf("spark.yarn.queue","sino")
                .setConf("spark.app.id", "luncher-test")
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
        }*/


    @GetMapping(value = "/hellospark")
    public String hello() {
        return "this is hello page!";
    }

}
