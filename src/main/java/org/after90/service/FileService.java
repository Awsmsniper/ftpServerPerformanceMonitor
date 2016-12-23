package org.after90.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by zhaogj on 23/12/2016.
 */
@Service
@Slf4j
public class FileService {

    public void makeFile(String strFilePath, long lLength) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(strFilePath, "rw");
            raf.setLength(lLength);
        } catch (FileNotFoundException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        } finally {
            if (raf == null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }

        }
    }
}
