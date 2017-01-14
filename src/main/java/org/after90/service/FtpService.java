package org.after90.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;

/**
 * ftp操作相关服务
 * Created by zhaogj on 22/12/2016.
 */
@Service
@Slf4j
public class FtpService {
    @Value("${ftp.strRemoteHost}")
    private String strRemoteHost;
    @Value("${ftp.nPort}")
    private int nPort;
    @Value("${ftp.strUserName}")
    private String strUserName;
    @Value("${ftp.strPassword}")
    private String strPassword;

    @Autowired
    private FileService file;

    public void doTest() {
        while (true) {
            connectOnlyTest();
            uploadOnlyTest();
            uploadTest();
            try {
                Thread.sleep(1000 * 10);
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    /**
     * 连接测试，分别计算建立和拆的成功率和时间消耗
     */
    public void connectOnlyTest() {
        int nTotal = 3;
        long lConnectTime = 0;//建立
        long lLogin = 0;//登录
        long lDisconnectTime = 0;//拆
        int nSucc = 0;
        for (int i = 0; i < nTotal; i++) {
            long lStart = System.currentTimeMillis();
            FTPClient ftp = new FTPClient();
            try {
                ftp.connect(strRemoteHost, nPort);
                lConnectTime += System.currentTimeMillis() - lStart;
                lStart = System.currentTimeMillis();
                // logger.info("Connected to " + strRemoteHost);
                // logger.info("ftp.getReplyString():" + ftp.getReplyString());
                ftp.login(strUserName, strPassword);
                lLogin += System.currentTimeMillis() - lStart;
                lStart = System.currentTimeMillis();
                int nReplyCode = ftp.getReplyCode();
                // logger.info("ftp.getReplyCode():" + nReplyCode);
                if (!FTPReply.isPositiveCompletion(nReplyCode)) {
                    ftp.disconnect();
                    log.error("FTP server refused connection.strRemoteHost={}", strRemoteHost);
                } else {
                    log.info("连接成功");
                    nSucc++;
                }
            } catch (Exception e) {
                log.error("", e);
            } finally {
                try {
                    if (ftp.isConnected()) {
                        ftp.disconnect();
                        lDisconnectTime += System.currentTimeMillis() - lStart;
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
        log.info("建立连接成功率:{}%, 建立连接:{}, 登录:{}, 拆连接:{}, 总时间:{}ms", nSucc * 100 / nTotal, lConnectTime / nTotal, lLogin / nTotal, lDisconnectTime / nTotal,
                (lConnectTime + lLogin + lDisconnectTime) / nTotal);
    }

    /**
     * 上传测试，仅仅计算上传数据的速度
     */
    public void uploadOnlyTest() {
        FTPClient ftp = new FTPClient();
        FileInputStream fis = null;
        try {
            ftp.connect(strRemoteHost, nPort);
            ftp.login(strUserName, strPassword);
            int nReplyCode = ftp.getReplyCode();
            // logger.info("ftp.getReplyCode():" + nReplyCode);
            if (!FTPReply.isPositiveCompletion(nReplyCode)) {
                ftp.disconnect();
                log.error("FTP server refused connection.strRemoteHost={}", strRemoteHost);
            } else {
                String strFile = "test.txt";
                file.makeFile(strFile, 1024 * 1024);
                File fileSrc = new File(strFile);
                int nTotal = 3;
                int nSucc = 0;
                long lStartTime = System.currentTimeMillis();
                for (int i = 0; i < nTotal; i++) {
                    ftp.setBufferSize(1024);
                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                    fis = new FileInputStream(fileSrc);
                    ftp.enterLocalPassiveMode();
                    if (ftp.storeFile(i + fileSrc.getName(), fis)) {
                        //上传成功
                        nSucc++;

                    } else {
                        //上传失败
                    }
                }
                long lTime = System.currentTimeMillis() - lStartTime;
                log.info("纯上传时间:{}, 纯上传速度:{}KB/s, 成功率:{}%", lTime / nTotal, (1024) / (lTime / nTotal), nSucc * 100 / nTotal);
                (new File(strFile)).delete();
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            try {
                if (ftp.isConnected()) {
                    ftp.disconnect();
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }

    }

    /**
     * 建立连接，上传，拆连接完整测试
     */
    public void uploadTest() {
        int nTotal = 3;
        int nSucc = 0;
        String strFile = "test.txt";
        file.makeFile(strFile, 1024 * 1024);
        long lStartTime = System.currentTimeMillis();
        FTPClient ftp = new FTPClient();
        FileInputStream fis = null;
        try {
            ftp.connect(strRemoteHost, nPort);
            ftp.login(strUserName, strPassword);
            int nReplyCode = ftp.getReplyCode();
            // logger.info("ftp.getReplyCode():" + nReplyCode);
            if (!FTPReply.isPositiveCompletion(nReplyCode)) {
                ftp.disconnect();
                log.error("FTP server refused connection.strRemoteHost={}", strRemoteHost);
            } else {
                File fileSrc = new File(strFile);
                for (int i = 0; i < nTotal; i++) {
                    ftp.setBufferSize(1024);
                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                    fis = new FileInputStream(fileSrc);
                    ftp.enterLocalPassiveMode();
                    if (ftp.storeFile(i + fileSrc.getName(), fis)) {
                        //上传成功
                        nSucc++;

                    } else {
                        //上传失败
                    }
                }

            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            try {
                if (ftp.isConnected()) {
                    ftp.disconnect();
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        long lTime = System.currentTimeMillis() - lStartTime;
        log.info("上传时间:{}, 上传速度:{}KB/s, 成功率:{}%", lTime / nTotal, (1024) / (lTime / nTotal), nSucc * 100 / nTotal);
        (new File(strFile)).delete();
    }

}
