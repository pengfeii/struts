package com.opensymphony.xwork2.util;

import com.opensymphony.xwork2.FileManager;
import com.opensymphony.xwork2.FileManagerFactory;
import com.opensymphony.xwork2.XWorkTestCase;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * FileManager Tester.
 *
 * @author <Lukasz>
 * @since <pre>02/18/2009</pre>
 * @version 1.0
 */
public class DefaultFileManagerTest extends XWorkTestCase {

    private FileManager fileManager;
    private long lastModified;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        fileManager = container.getInstance(FileManagerFactory.class).getFileManager();
    }

    public void testGetFileInJar() throws Exception {
        testLoadFile("xwork-jar.xml");
        testLoadFile("xwork - jar.xml");
        testLoadFile("xwork-zip.xml");
        testLoadFile("xwork - zip.xml");
        testLoadFile("xwork-jar2.xml");
        testLoadFile("xwork - jar2.xml");
        testLoadFile("xwork-zip2.xml");
        testLoadFile("xwork - zip2.xml");
    }

    private void testLoadFile(String fileName) {
        fileManager.setReloadingConfigs(true);
        URL url = ClassLoaderUtil.getResource(fileName, DefaultFileManagerTest.class);
        InputStream file = fileManager.loadFile(url);
        assertNotNull(file);
        assertTrue(fileManager.fileNeedsReloading(fileName));
    }

    public void testReloadingConfigs() throws Exception {
        // given
        container.getInstance(FileManagerFactory.class).setReloadingConfigs("false");
        FileManager fm = container.getInstance(FileManagerFactory.class).getFileManager();
        String resourceName = "xwork-sample.xml";
        URL url = ClassLoaderUtil.getResource(resourceName, DefaultFileManagerTest.class);
        assertFalse(fm.fileNeedsReloading(url));

        // when
        container.getInstance(FileManagerFactory.class).setReloadingConfigs("true");
        changeLastModified(resourceName);

        // then
        url = ClassLoaderUtil.getResource(resourceName, DefaultFileManagerTest.class);
        fm = container.getInstance(FileManagerFactory.class).getFileManager();
        assertTrue(fm.fileNeedsReloading(url));
        restoreLastModified(resourceName);
    }

    private void changeLastModified(String resourceName) throws Exception {
        URL url = ClassLoaderUtil.getResource(resourceName, DefaultFileManagerTest.class);
        lastModified = new File(url.toURI()).lastModified();
        new File(url.toURI()).setLastModified(new Date().getTime() - 1000*10);
        Thread.sleep(1100);
    }

    private void restoreLastModified(String resourceName) throws Exception {
        URL url = ClassLoaderUtil.getResource(resourceName, DefaultFileManagerTest.class);
        new File(url.toURI()).setLastModified(lastModified);
        Thread.sleep(1100);
    }

}
