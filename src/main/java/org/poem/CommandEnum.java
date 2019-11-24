package org.poem;

/**
 * @author poem
 */
public class CommandEnum {

    /**
     * hive home
     */
    public static final String HIVE_HOME = "/opt/idagent/services/hive";

    /**
     * hadoop home
     */
    public static final String HADOOP_HOME = "/opt/idagent/services/hadoop";

    /**
     * sql
     * 操作
     */
    public static final String SQOOP_HOME = "/opt/idagent/services/sqoop";

    /**
     * hive 创建数据库
     */
    public static final String HIVE_CREATE_DATABASE = HIVE_HOME + "/bin/hive -e 'create database if not exists @database'";

    /**
     * hive 删除表
     */
    public static final String HIVE_DELETE_TABLE = HIVE_HOME + "/bin/hive -e 'use @database; drop table @table'";

    /**
     * 删除 hadoop  hdfs 表文件
     */
    public static final String HADOOP_DELETE_FILE = HADOOP_HOME + "/bin/hadoop dfs -rm -f -r -skipTrash  /user/root/@table";

    /**
     * sqoop 导入数据到hvie
     */
    public static final String SQOOP_IMPORT_DATA = SQOOP_HOME + "/bin/sqoop  import --connect @url --username @user --password '@password' " +
            "  --table  @table -m 1  --hive-database @scheme  --hive-import --hive-overwrite --create-hive-table --hive-table  @table";

    public static final String SQOOP_IMPORT_DATA_CHECK = HIVE_HOME + "/bin/hive -e 'use @scheme; select 1 from  @table'";
}
