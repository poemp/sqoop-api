package org.poem.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.lang3.StringUtils;
import org.poem.entity.ExecutResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 远程调用方式去执行操作
 */
public class RemoteServerHandler {


    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(RemoteServerHandler.class);


    /**
     * @param cmd      执行命令
     * @param host     服务器地址
     * @param password 服务器密码
     * @param userName 服务用户名
     * @return 验证结果
     */
    public static ExecutResult exec(String cmd, String host, String password, String userName) {
        return exec(cmd, host, password, userName, 22);
    }


    /**
     * @param cmd      执行命令
     * @param host     服务器地址
     * @param password 服务器密码
     * @param userName 服务用户名
     * @param port     服务器 ssh 端口
     * @return 验证结果
     */
    public static ExecutResult exec(String cmd, String host, String password, String userName, Integer port) {
        logger.info(String.format("host: %s, password :%s, userName : %s, port :%d", host, password, userName, port));
        logger.info("Execute Shell Command:" + cmd);
        ExecutResult executResult = validatePar(cmd, host, password, userName, port);
        if (executResult != null) {
            return executResult;
        }
        return command(cmd, host, password, userName, port);
    }

    /**
     * @param cmd      执行命令
     * @param host     服务器地址
     * @param password 服务器密码
     * @param userName 服务用户名
     * @param port     服务器 ssh 端口
     * @return 验证结果
     */
    private static ExecutResult validatePar(String cmd, String host, String password, String userName, Integer port) {
        if (StringUtils.isBlank(cmd)) {
            return new ExecutResult(false, "cmd not empty", "");
        }
        if (StringUtils.isBlank(host)) {
            return new ExecutResult(false, "host not empty", "");
        }
        if (StringUtils.isBlank(password)) {
            return new ExecutResult(false, "password not empty", "");
        }
        if (StringUtils.isBlank(userName)) {
            return new ExecutResult(false, "userName not empty", "");
        }
        if (port == null || port < 0) {
            return new ExecutResult(false, "port not empty and  more than 0", "");
        }
        return null;
    }

    /**
     * 执行
     *
     * @param cmd      命令
     * @param host     服务地址
     * @param password 服务器密码
     * @param userName 服务器用户名
     * @param port     服务器端口
     * @return 执行结果
     */
    private static ExecutResult command(String cmd, String host, String password, String userName, Integer port) {
        Connection conn = null;
        Session sess = null;
        InputStream stdout = null;
        InputStream stderr = null;
        ExecutResult executResult = new ExecutResult();
        StringBuilder error = new StringBuilder();
        StringBuilder std = new StringBuilder();
        try {
            /* Create a connection instance */
            conn = new Connection(host, port);
            /* Now connect */
            conn.connect();
            /* Authenticate */
            boolean isAuthenticated = conn.authenticateWithPassword(userName,
                    password);
            if (!isAuthenticated)
                throw new IOException("Authentication failed.");
            /* Create a session */
            sess = conn.openSession();
            sess.execCommand(cmd);
            stderr = new StreamGobbler(sess.getStderr());
            BufferedReader brerr = new BufferedReader(new InputStreamReader(stderr));
            Thread.sleep(500);
            while (true) {
                String line = brerr.readLine();
                if (line == null)
                    break;
                error.append(line);
            }

            stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            while (true) {
                String line = br.readLine();
                if (line == null)
                    break;
                std.append(line);
            }
            logger.info("ExitCode: " + sess.getExitStatus());
            executResult.setSuccess(sess.getExitStatus() == 0);
            executResult.setErrorResult(error.toString());
            executResult.setErrorResult(std.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace(System.err);
        } finally {
            if (sess != null) {
                sess.close();
            }
            if (conn != null) {
                conn.close();
            }
            if (stdout != null) {
                try {
                    stdout.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    e.printStackTrace(System.err);
                }
            }
            if (stderr != null) {
                try {
                    stderr.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    e.printStackTrace(System.err);
                }
            }
        }
        return executResult;
    }

}
