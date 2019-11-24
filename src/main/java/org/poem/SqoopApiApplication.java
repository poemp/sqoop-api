package org.poem;

import org.poem.entity.ExecutResult;
import org.poem.exception.SqoopSessionException;
import org.poem.service.SqoopService;
import org.poem.service.impl.SqoopServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 */
@SpringBootApplication
public class SqoopApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(SqoopApiApplication.class);

    public static void main(String[] args) {
//        SpringApplication.run(SqoopApiApplication.class, args);
        String host = "142service";
        String password = "zgph123";
        String userName = "root";
        Integer port = 32201;
        String schame = "employee_service";
        String table = "admin_organization";
        SqoopService sqoopService = new SqoopServiceImpl(host, password, userName, port);
        ExecutResult executResult;
        try {
            executResult = sqoopService.createHiveDatabase(schame);
            if (executResult.getSuccess()){
                executResult = sqoopService.deleteHiveTable(schame ,table);
            }else{
                logger.error(executResult.getErrorResult());
            }
            if (executResult.getSuccess()){
                executResult = sqoopService.deleteHadoop(table);
            }else{
                logger.error(executResult.getErrorResult());
            }
            if (executResult.getSuccess()){
                executResult = sqoopService.sqoopImportIp("90service", "root", "123456", 2019 , table, schame);
            }else{
                logger.error(executResult.getErrorResult());
            }
        } catch (SqoopSessionException e) {
            e.printStackTrace();
        }
    }



}
