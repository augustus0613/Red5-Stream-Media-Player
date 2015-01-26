/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjm.identity.singleton;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tjm.identity.entity.FileIdentity;

/**
 *
 * @author larry
 */
@Singleton
@Startup
public class FileIdentityFactory {

    private static Logger logger = LoggerFactory.getLogger(FileIdentityFactory.class);
    private static Properties props = new Properties();
    // 根目路路徑
    private static String root_path;
    private static Map<FileIdentity, String> identityIdMap = new HashMap<FileIdentity, String>();
    private static FileIdentityFactory instance = null;

    public FileIdentityFactory() {
        logger.warn("singleton startup .....");
        try {
            props.load(FileIdentityFactory.class
                    .getResourceAsStream("/config.properties"));
            root_path = props.getProperty("root");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        logger.warn("現有資料夾內容「重新暫存」 開始　.....");

        File root = new File(root_path);
        if (!identityIdMap.isEmpty()) {
            identityIdMap = new HashMap<FileIdentity, String>();
        }

        logger.warn("資料夾搜尋 .....");
        identityIdMap = findDirectories(root, identityIdMap);
        logger.warn("資料夾搜尋結束 .....");

        logger.warn("現有資料夾內容「重新暫存」　結束 .....");

    }

    /**
     * 資料夾搜尋
     *
     * @param folder
     * @param passportIdMap
     * @return
     */
    private static Map<FileIdentity, String> findDirectories(File folder,
            Map<FileIdentity, String> passportIdMap) {

        File[] under_root_files = folder.listFiles();
        for (File file : under_root_files) {
            if (file.isDirectory()) {
                // 取得檔案路徑
                String filepath = file.getAbsolutePath().replace(root_path, "");
                logger.warn("找到檔案路徑 : " + filepath + " , 放入暫存 .....");
                // 產生暫存物件ID
                String file_id = UUID.randomUUID().toString();

                // 新增暫存物件
                FileIdentity f = new FileIdentity();
                f.setId(file_id);
                f.setFilepath(filepath);
                f.setIsPublic(Boolean.FALSE);

                passportIdMap.put(f, file_id);

                passportIdMap = findDirectories(file, passportIdMap);

            } // if (file.isDirectory()) {}
        } // for (File file : under_root_files) {}

        return passportIdMap;
    }

    public Map<FileIdentity, String> getIdentityMap() {
        return identityIdMap;
    }

    /**
     * 取得 FileIdentityFactory 物件
     *
     * @return
     */
    public static FileIdentityFactory getInstance() {
        if (instance == null) {
            instance = new FileIdentityFactory();
        }
        return instance;
    }

    /**
     * 以 檔案路徑 取得 對應暫存物件 的 ID
     *
     * @param filepath
     * @return
     */
    public static String findFileId(String filepath) {
        FileIdentity fp = new FileIdentity();
        // 因已改寫 equals 方法，只需放入 filepath 即可用 List 的 contains 方法
        fp.setFilepath(filepath);

        String file_id = identityIdMap.get(fp);
        if (file_id == null) {
            logger.warn("該路徑未被記錄在暫存物件中");
        }
        return file_id;
    }
}
