package com.stdatalabs.SparkWordcount

/*#############################################################################################
# Description: WordCount using Spark
##
# Input: 
#   1. /user/cloudera/MarkTwain.txt
#
# To Run this code use the command:    
# spark-submit --class com.stdatalabs.SparkWordcount.Driver \
#							 --master yarn-cluster \
#							 --num-executors 5 \
#							 --driver-memory 4g \
#							 --executor-memory 4g \
#							 --executor-cores 1 
#							 SparkWordcount-0.0.1-SNAPSHOT.jar \
#							/user/cloudera/MarkTwain.txt \
#							/user/cloudera/sparkWordCount
#############################################################################################*/

// Scala Imports

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
object Driver {

  def main(args: Array[String]) {
    
    //1.spark配置
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wc1")

    //2.spark入口
    val sc: SparkContext = new SparkContext(sparkConf)
    
    //todo 业务
    //1.一行一行的读取数据
    //{(hello spark),(hello scala)}
    val lines: RDD[String] = sc.textFile("datas")
    
    //2.对数据进行扁平化处理，打散成一个个单词
    //{(hello spark),(hello scala)} --> {hello,spark,hello,scala}
    val words: RDD[String] = lines.flatMap(_.split(" "))
    
    //3.将一个个单词映射成元组
    val wordtoOne: RDD[(String, Int)] = words.map((_, 1))

    //4.对RDD按key进行聚合
    val wordToSum: RDD[(String, Int)] = wordtoOne.reduceByKey(_ + _)

    //以数组Array的形式返回数据的所有元素
    //5.搜集数据到Driver端进行打印,慎用
    val tuples: Array[(String, Int)] = wordToSum.collect()
    tuples.foreach(println)
    sc.stop()
    
  }

}
