package org.poem.service;

import org.poem.entity.ExecutResult;
import org.poem.exception.SqoopSessionException;

public interface SqoopService {

    /**
     * 创建hive的数据库
     *
     * @param database 数据库名称
     */
    ExecutResult createHiveDatabase(String database) throws SqoopSessionException;


    /**
     * 删除hive的库
     *
     * @param schema 库
     * @param table  表
     * @return 执行结果
     */
    ExecutResult deleteHiveTable(String schema, String table) throws SqoopSessionException;


    /**
     * 删除 hadoop 文件
     *
     * @param table
     * @return
     */
    ExecutResult deleteHadoop(String table) throws SqoopSessionException;


    /**
     * sqoop 导入数据库
     *
     * @param mysqlUrl    mysql的链接信息
     * @param mysqlUser   mysql的 用户名
     * @param mysqlPasswd mysql 密码
     * @param table       表
     * @param scheme      库
     * @return
     */
    ExecutResult sqoopImportUrl(String mysqlUrl, String mysqlUser, String mysqlPasswd, String table, String scheme);

    /**
     * sqoop 导入数据库
     *
     * @param mysqlUser   mysql的 用户名
     * @param mysqlPasswd mysql 密码
     * @param table       表
     * @param scheme      库
     * @return
     */
    public ExecutResult sqoopImportIp(String ip, String mysqlUser, String mysqlPasswd, String table, String scheme);

    /**
     * sqoop 导入数据库
     * <p>
     * sqoop 导入数据库
     *
     * @param mysqlUser   mysql的 用户名
     * @param mysqlPasswd mysql 密码
     * @param table       表
     * @param scheme      库
     * @return
     */
    ExecutResult sqoopImportIp(String ip, String mysqlUser, String mysqlPasswd, Integer mysqlPort, String table, String scheme);

}
