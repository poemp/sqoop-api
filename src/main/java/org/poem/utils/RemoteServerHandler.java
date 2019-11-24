package org.poem.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.lang3.StringUtils;
import org.poem.entity.ExecutResult;
import org.poem.exception.SqoopSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 远程调用方式去执行操作
 */
public class RemoteServerHandler {

    /**
     * 服务器id
     */
    private String host;
    /**
     * 服务器登录密码
     */
    private String password;
    /**
     * 服务器登录用户名
     */
    private String userName;
    /**
     * 服务器登录端口
     */
    private Integer port;
    /**
     * session
     */
    private Session sess = null;

    /**
     * 链接
     */
    private Connection conn = null;
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(RemoteServerHandler.class);


    /**
     * 执行操作
     *
     * @param host     服务器地址
     * @param password 服务器密码
     * @return 返回结果
     */
    public RemoteServerHandler(String host, String password) {
        this.host = host;
        this.password = password;
        this.userName = "root";
        this.port = 22;
        try {
            initConnection();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * 执行操作
     *
     * @param host     服务器地址
     * @param password 服务器密码
     * @param userName 服务用户名
     * @return 返回结果
     */
    public RemoteServerHandler(String host, String password, String userName) {
        this.host = host;
        this.password = password;
        this.userName = userName;
        this.port = 22;
        try {
            initConnection();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * 执行操作
     *
     * @param host     服务器地址
     * @param password 服务器密码
     * @param userName 服务用户名
     * @param port     服务器 ssh 端口
     * @return 返回结果
     */
    public RemoteServerHandler(String host, String password, String userName, Integer port) {
        this.host = host;
        this.password = password;
        this.userName = userName;
        this.port = port;
        try {
            initConnection();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }


    /**
     * @param cmd 执行命令
     * @return 返回结果
     */
    public ExecutResult exec(String cmd) throws SqoopSessionException {
        logger.info(String.format("host: %s, password :%s, userName : %s, port :%d", this.host, this.password, this.userName, this.port));
        logger.info("Execute Shell Command:" + cmd);
        ExecutResult executResult = validatePar(cmd, host, password, userName, port);
        if (executResult != null) {
            return executResult;
        }
        return command(cmd);
    }

    /**
     * @param cmd      执行命令
     * @param host     服务器地址
     * @param password 服务器密码
     * @param userName 服务用户名
     * @param port     服务器 ssh 端口
     * @return 验证结果
     */
    private ExecutResult validatePar(String cmd, String host, String password, String userName, Integer port) {
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
     * 初始化连接
     */
    private void initConnection() throws IOException {
        /* Create a connection instance */
        this.conn = new Connection(this.host, this.port);
        /* Now connect */
        this.conn.connect();
        /* Authenticate */
        boolean isAuthenticated = this.conn.authenticateWithPassword(this.userName,
                this.password);
        if (!isAuthenticated)
            throw new IOException("Authentication failed.");
        /* Create a session */
        this.sess = this.conn.openSession();
        // sess.execCommand("uname -a && date && uptime && who");
    }

    /**
     * 执行
     *
     * @param cmd
     * @return
     */
    private ExecutResult command(String cmd) throws SqoopSessionException {
        if (this.sess == null) {
            throw new SqoopSessionException("");
        }
        InputStream stdout = null;
        InputStream stderr = null;
        ExecutResult executResult = new ExecutResult();
        StringBuilder error = new StringBuilder();
        StringBuilder std = new StringBuilder();
        try {
            sess.execCommand(cmd);
            logger.info("Here is some information about the remote host:");
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
            /* Show exit status, if available (otherwise "null") */
            logger.info("ExitCode: " + sess.getExitStatus());
            executResult.setSuccess(sess.getExitStatus() == 0);
            executResult.setErrorResult(error.toString());
            executResult.setErrorResult(std.toString());
        } catch ( Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace(System.err);
        }finally {
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

    /**
     * 关闭
     * 必须
     */
    @PreDestroy
    public void close(){
        if (sess != null) {
            sess.close();
        }
        if (conn != null) {
            conn.close();
        }
    }
}
