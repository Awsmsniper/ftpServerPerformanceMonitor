package test.org.after90.service;

import lombok.extern.slf4j.Slf4j;
import org.after90.service.FileService;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * FileService Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Dec 23, 2016</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FileService.class)
@Slf4j
public class FileServiceTest {

    @Autowired
    private FileService file;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: makeFile(String strFilePath, long lLength)
     */
    @Test
    public void testMakeFile() throws Exception {
        long lLength = 1024;
        String strFile = "test.txt";
        file.makeFile(strFile, lLength);
        File file = new File(strFile);
        assertEquals(file.length(), lLength);
        file.delete();
    }


} 
