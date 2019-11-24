package org.poem.service.impl;

import org.poem.CommandEnum;
import org.poem.entity.ExecutResult;
import org.poem.exception.SqoopSessionException;
import org.poem.service.SqoopService;
import org.poem.utils.RemoteServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行操作
 */
public class SqoopServiceImpl implements SqoopService {

    private static final Logger logger = LoggerFactory.getLogger(SqoopServiceImpl.class);
    /**
     * 执行结果
     */
    private RemoteServerHandler remoteServerHandler;


    public SqoopServiceImpl(RemoteServerHandler remoteServerHandler) {
        this.remoteServerHandler = remoteServerHandler;
    }

    /**
     * 创建hive的数据库
     *
     * @param database 数据库名称
     * @return 执行结果
     */
    @Override
    public ExecutResult createHiveDatabase(String database) {
        logger.info("createHiveDatabase -- ");
        String command = CommandEnum.HIVE_CREATE_DATABASE.replaceAll("@database", database);
        try {
            return remoteServerHandler.exec(command);
        } catch (SqoopSessionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除hive的库
     *
     * @param schema 库
     * @param table  表
     * @return 执行结果
     */
    @Override
    public ExecutResult deleteHiveTable(String schema, String table) {
        logger.info("deleteHiveTable -- ");
        String command = CommandEnum.HIVE_DELETE_TABLE.replaceAll("@database", schema).replaceAll("@table", table);
        try {
            return remoteServerHandler.exec(command);
        } catch (SqoopSessionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除 hadoop 文件
     *
     * @param table 表
     * @return 执行结果
     */
    @Override
    public ExecutResult deleteHadoop(String table) {
        logger.info("deleteHadoop -- ");
        String command = CommandEnum.HADOOP_DELETE_FILE.replaceAll("@table", table);
        try {
            return remoteServerHandler.exec(command);
        } catch (SqoopSessionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sqoop 导入数据库
     *
     * @param mysqlUrl    mysql的链接信息
     * @param mysqlUser   mysql的 用户名
     * @param mysqlPasswd mysql 密码
     * @return 执行结果
     */
    @Override
    public ExecutResult sqoopImportUrl(String mysqlUrl, String mysqlUser, String mysqlPasswd, String table, String scheme) {
        logger.info("sqoopImportUrl -- ");
        String command = CommandEnum.SQOOP_IMPORT_DATA
                .replaceAll("@url", mysqlUrl)
                .replaceAll("@user", mysqlUser)
                .replaceAll("@password", mysqlPasswd)
                .replaceAll("@table", table)
                .replaceAll("@scheme", scheme);

        try {
            return remoteServerHandler.exec(command);
        } catch (SqoopSessionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sqoop 导入数据库
     *
     * @param ip          mysql的链接信息
     * @param mysqlUser   mysql的 用户名
     * @param mysqlPasswd mysql 密码
     * @param mysqlPort   mysql的端口
     * @return 执行结果
     */
    @Override
    public ExecutResult sqoopImportIp(String ip, String mysqlUser, String mysqlPasswd, Integer mysqlPort, String table, String scheme) {
        logger.info("sqoopImportIp -- ");
        return sqoopImportUrl("jdbc:mysql://" + ip + ":" + mysqlPort + "/" + scheme + "?characterEncoding=UTF-8", mysqlUser, mysqlPasswd, table, scheme);
    }

    /**
     * sqoop 导入数据库
     *
     * @param ip          mysql的链接信息
     * @param mysqlUser   mysql的 用户名
     * @param mysqlPasswd mysql 密码
     * @return 执行结果
     */
    @Override
    public ExecutResult sqoopImportIp(String ip, String mysqlUser, String mysqlPasswd, String table, String scheme) {
        logger.info("sqoopImportIp -- ");
        return sqoopImportUrl("jdbc:mysql://" + ip + ":3306/" + scheme + "?characterEncoding=UTF-8", mysqlUser, mysqlPasswd, table, scheme);
    }

}
