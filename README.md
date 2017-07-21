# Hadoop-Multi-Node-Tutorial

# Environment

- Ubuntu 14.04 LTS 3 (1 Master, 2 Slaves)
- Hadoop 2.6.5 
- Java 1.8.0_141


Hadoop consists of one master node and several slave nodes.    

Hadoop can be divided into HDFS (Hadoop File System) for storing and managing files and Yarn for executing map & reduce job. In practice, both run and terminate separately.     

In this article, I will cover everything from basic to actual use including SSH key registration, Java installation, Hadoop installation and setup, Hadoop execution and testing.    
 
# Install order

## (1) Ubuntu apt-get update
    
    $ apt-get install update   
    $ sudo vi /etc/resolv.conf  
    
change nameserver IP  
      nameserver 8.8.8.8  

    $ apt-get install update  


## (2) Generate ssh key and exchange key

When connecting from one node to another node, you need to create and register an SSH key so that you can login without a password. 

When you run Hadoop, you log in from all nodes and run Hadoop on each node. At this time, you can not log in with a password, so you have to register your SSH key.


    $ ssh-keygen -t rsa
    $ cd .ssh
    
    drwx------ 2 ubuntu ubuntu 4096 Jul 20 07:25 ./
    drwxr-xr-x 8 ubuntu ubuntu 4096 Jul 20 08:40 ../
    -rw-r--r-- 1 ubuntu ubuntu 1608 Jul 20 05:20 authorized_keys
    -rw------- 1 ubuntu ubuntu 1679 Jul 20 05:16 id_rsa
    -rw-r--r-- 1 ubuntu ubuntu  402 Jul 20 05:19 id_rsa.pub
    -rw-r--r-- 1 ubuntu ubuntu 1554 Jul 20 07:33 known_hosts
    
If you do not have a known_hosts file, create a file with touch known_hosts.


    $ chmod 700 ./
    $ chmod 755 ../
    $ chmod 600 id_rsa
    $ chmod 644 id_rsa.pub  
    $ chmod 644 authorized_keys
    $ chmod 644 known_hosts
    
Copy the id_rsa.pub key and put it in the authorized_keys file of each slave node.

The id_rsa.pub key of the master node is also put in the authorized_keys file of the slave node.

Each node tries to connect to each other through ssh.

## (3) Install java

    $ apt-get install default-jdk
    
    $ vi /etc/environment
    
    Add JAVA_HOME="/usr/lib/jvm/default-java"
    
    $ source /etc/environment
    
    $ java -version
    
    java version "1.7.0_131"
    OpenJDK Runtime Environment (IcedTea 2.6.9) (7u131-2.6.9-0ubuntu0.14.04.2)
    OpenJDK 64-Bit Server VM (build 24.131-b00, mixed mode)
    
## (4) Install Hadoop 2.6.5

    $ wget http://apache.mesi.com.ar/hadoop/common/hadoop-2.6.5/hadoop-2.6.5.tar.gz
    
    $ tar -xvzf hadoop-2.6.5.tar.gz
    
    $ mv hadoop-2.6.5 hadoop
    
    $ vi .bashrc
    
    ############################################################
    ### Hadoop
    ############################################################
    export HADOOP_HOME=$HOME/hadoop
    export PATH=$PATH:$HADOOP_HOME/bin
    export PATH=$PATH:$HADOOP_HOME/sbin
    export HADOOP_MAPRED_HOME=$HADOOP_HOME
    export HADOOP_COMMON_HOME=$HADOOP_HOME
    export HADOOP_HDFS_HOME=$HADOOP_HOME
    export YARN_HOME=$HADOOP_HOME
    export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
    export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib"
    export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar
    export CLASSPATH=$HADOOP_HOME/share/hadoop/common/hadoop-common-
    2.6.5.jar:$HADOOP_HOME/share/hadoop/mapreduce/:$HADOOP_HOME/share/hadoop/common/lib/commons-cli-1.2.jar
    
    $ source .bashrc

In your home directory, 

    $ mkdir -p hadoop_tmp/hdfs/namenode 
    $ mkdir -p hadoop_tmp/hdfs/datanode
    
All other nodes are set equal.


## (5) Set Hadoop 2.6.5

    $ cd ~/hadoop/etc/hadoop
    
Only modify 

    hadoop-env.sh, core-site.xml, hdfs-site.xml, mapred-site.xml, yarn-site.xml, slaves, masters

files in that directory.

1. hadoop-env.sh

    $ vi hadoop-env.sh
    
    export HADOOP_LOG_DIR={$HADOOP_HOME}/logs/hadoop/core
    export HADOOP_ROOT_LOGGE=INFO,console
    export HADOOP_PID_DIR=$HADOOP_LOG_DIR
    export HADOOP_SECURITY_LOGGER=INFO,NullAppender
    export HDFS_AUDIT_LOGGER=INFO,NullAppender
    
    export JAVA_HOME=${JAVA_HOME}
    export HADOOP_HOME=/home/ubuntu/hadoop

    export HADOOP_LOG_DIR=${HADOOP_HOME}/logs
    export HADOOP_PREFIX=${HADOOP_HOME}
    export HADOOP_COMMON_LIB_NATIVE_DIR=${HADOOP_HOME}/lib/native
    export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib/native"
    

2. core-site.xml
    
    $ vi core-site.xml
    
    <configuration>    
      <property>    
        <name>fs.defaultFS</name>     
        <value>hdfs://yechan-master:9000</value>      
      </property>     
      <property>      
	      <name>hadoop.tmp.dir</name>     
	      <value>/home/ubuntu/hadoop_tmp</value>      
	    </property>     
    </configuration>      
    
3. hdfs-site.xml  

    $ vi hdfs-site.xml
    
    <configuration> 
    <property>    
        <name>dfs.replication</name>  
        <value>2</value>  
    </property> 
    <property>  
        <name>dfs.webhdfs.enabled</name>  
        <value>true</value> 
    </property> 
    <property>  
        <name>dfs.namenode.name.dir</name>  
        <value>/home/ubuntu/hadoop_tmp/hdfs/namenode</value>  
    </property> 
    <property>  
        <name>dfs.datanode.data.dir</name>  
        <value>/home/ubuntu/hadoop_tmp/hdfs/datanode</value>  
    </property> 
    <property>  
       <name>dfs.datanode.hdfs-blocks-metadata.enabled</name> 
       <value>true</value>  
    </property> 
    </configuration>  


4. mapred-site.xml

    $ vi mapred-site.xml
    
    <configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
    <property>
        <name>mapreduce.jobtracker.hosts.exclude.filename</name>
        <value>/home/ubuntu/hadoop/etc/hadoop/exclude</value>
    </property>
    <property>
        <name>mapreduce.jobtracker.hosts.filename</name>
        <value>/home/ubuntu/hadoop/etc/hadoop/include</value>
    </property>
    </configuration>
    
    
5. yarn-site.xml
    
    $ vi yarn-site.xml
    
    <configuration>

    <!-- Site specific YARN configuration properties -->
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
        <property>
        <name>yarn.nodemanager.aux-services.mapreduce_shuffle.class</name>
        <value>org.apache.hadoop.mapred.ShuffleHandler</value>
        </property>
        <property>
        <name>yarn.nodemanager.local-dir</name>
        <value>/home/hadoop-2.6.5/data/yarn/nm-local-dir</value>
        </property>
        <property>
        <name>yarn.resourcemanager.fs.state-store.uri</name>
        <value>/home/hadoop-2.6.5/data/yarn/system/rmstore</value>
        </property>
        <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>yechan-master</value>
        </property>
    </configuration>
    
    
6. slaves

    $ vi slaves

    yechan-master
    yechan-slave1
    yechan-slave2
    
    
7. masters
    
    $vi masters
    
    yechan-master


All other nodes are set equal.


## (6) Execution Hadoop


1. format namenode

In Master node.


    $ hadoop namenode -format
    
2. execution (hdfs and yarn)

    $ start-all.sh
    
    
When you hit the jps command after a while, the master node

    $ jps
    
    25240 DataNode
    25443 SecondaryNameNode
    25069 NameNode
    25592 ResourceManager
    29776 Jps
    25739 NodeManager
    
    
    In datanode
    
    $ jps
    
    11799 DataNode
    12617 Jps
    11947 NodeManager
    
    
3. End

When shutting down Hadoop, 

    $ stop-all.sh
    
    
## (7) Test Hadoop

    $ hdfs dfs -mkdir /input
    
    $ hdfs dfs -lsr
    
    $ hdfs dfs -put /home/ubuntu/word.txt
    
    $ hadoop jar /home/ubuntu/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.6.5.jar wordcount /input/word.txt output
    
    $ hdfs dfs -cat /user/ubuntu/output
    
    
    
# The End.

# Thank you.

    
