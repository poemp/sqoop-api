package org.poem;

import org.poem.entity.ExecutResult;
import org.poem.entity.table.DataBases;
import org.poem.entity.table.Table;
import org.poem.exception.SqoopSessionException;
import org.poem.service.SqoopService;
import org.poem.service.impl.SqoopServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author poem
 */
public class SqoopApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(SqoopApiApplication.class);

    /**
     * java main function
     *
     * @param args par
     */
    public static void main(String[] args) {
        List<DataBases> init = InitDatabaseTables.getInit();
        String host = "142service";
        String password = "zgph123";
        String userName = "root";
        Integer port = 32201;
        for (DataBases dataBases : init) {
            for (Table table : dataBases.getTables()) {
                execute(dataBases.getDatabases(), table.getTableName(), host, password, userName, port);
            }
        }
    }


    /**
     * @param schame   数据库
     * @param table    表
     * @param host     端口
     * @param password 密码
     * @param userName 用户名
     * @param port     端口号
     */
    private static void execute(String schame, String table, String host, String password, String userName, Integer port) {
        SqoopService sqoopService = new SqoopServiceImpl(host, password, userName, port);
        ExecutResult executResult;
        try {
            logger.info("*********************** Create Hive Start*********************");
            executResult = sqoopService.createHiveDatabase(schame);
            if (executResult.getSuccess()) {
                executResult = sqoopService.deleteHiveTable(schame, table);
                logger.info(executResult.getSuccessResult());
                logger.error(executResult.getErrorResult());
            }
            logger.info("*********************** Create Hive End *********************");
            logger.info("*********************** Delete Hadoop File Start *********************");
            if (executResult.getSuccess()) {
                executResult = sqoopService.deleteHadoop(table);
                logger.info(executResult.getSuccessResult());
                logger.error(executResult.getErrorResult());
            }
            logger.info("*********************** Delete Hadoop File End *********************");
            logger.info("*********************** Import Data Start *********************");
            if (executResult.getSuccess()) {
                executResult = sqoopService.sqoopImportIp("90service", "root", "123456", 2019, table, schame);
                logger.info(executResult.getSuccessResult());
                logger.error(executResult.getErrorResult());
            }
            logger.info("*********************** Import Data End *********************");
        } catch (SqoopSessionException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

}
