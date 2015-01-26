/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjm.setting.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tjm.identity.dao.FileIdentityDao;
import org.tjm.identity.entity.FileIdentity;
import org.tjm.identity.singleton.FileIdentityFactory;

/**
 *
 * @author larry
 */
@Controller
@RequestMapping("/setting/visibility")
public class VisibilityController {

    Logger logger = LoggerFactory.getLogger(VisibilityController.class);

    Properties props = new Properties();
    @Resource(name = "fileIdentityDao")
    private FileIdentityDao fileIdentityDao;

//    @RequestMapping("/")
//    public ModelAndView mainPage() {
//        ModelAndView mav = new ModelAndView("/setting/visibility/main");
//
//        return mav;
//    }
    /**
     * 取得勾選對外的資料夾列表
     *
     * @param id
     * @return
     */
    @RequestMapping("/folder")
    @ResponseBody
    public String folder(String id) {
        JSONArray infoArray = new JSONArray();
        logger.warn("---------------------------------------------- folder -------------------------------------------------------------");
        try {

            props.load(VisibilityController.class
                    .getResourceAsStream("/config.properties"));

            // 第一次讀取列表
            String onclick_path = "/";

            // id 為當前點擊之節點 id，當第一次開啟 window 時，id 為 null ( 手動展開節點時，才會將 id 帶入並呼叫 url )
            logger.warn("id = " + id);

            // 當點擊展開節點時(即表示 不是第一次讀取列表)
            if (id != null && !id.equals("")) {
                FileIdentity fi = fileIdentityDao.findById(id);
                onclick_path = fi.getFilepath();
            } else { // 第一次讀取

                // 核對資料夾真實狀況及資料庫資料狀況
                checkOutCheckInFile();
            }

            // 取得目前點選之節點
            String root_path = props.getProperty("root");
            File onclick_dir = new File(root_path + onclick_path);

            // 取得目前點選之節點底下所有檔案
            File[] under_onclick_dir_files = onclick_dir.listFiles();

            // 個別讀出目前點選之節點底下之檔案
            for (File file : under_onclick_dir_files) {

                // 若該檔案為資料夾
                if (file.isDirectory()) {

                    FileIdentity fi = fileIdentityDao.findByFilePath(file.getPath().replace(root_path, ""));
                    String file_id = fi.getId();

                    String this_file_path_without_onclick_path = file.getPath().replace(onclick_path, "");
                    String this_file_path_without_onclick_path_utf8 = URLEncoder.encode(new String(this_file_path_without_onclick_path.getBytes("UTF-8"), "UTF-8"), "UTF-8");

                    JSONObject file_info = new JSONObject();
                    // 前端展開節點時會帶入 id 重新呼叫，以遞增整數作為 id
                    file_info.put("id", file_id);
                    // 列表顯示之名稱
                    file_info.put("text", file.getName());
                    // 節點關閉狀態
                    file_info.put("state", "closed");
                    if (fi.getIsPublic()) {
                        file_info.put("checked", true);
                    } else {
                        file_info.put("checked", false);
                    }
                    // 其它紀錄之屬性
                    JSONObject attributes = new JSONObject();

                    attributes.put("path_name", this_file_path_without_onclick_path_utf8);
                    file_info.put("attributes", attributes);

                    // 放入物件
                    infoArray.add(file_info);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.warn("---------------------------------------------- end of folder -------------------------------------------------------------");
        return infoArray.toString();
    }

    /**
     * 第一次讀取列表
     */
    public void checkOutCheckInFile() {
        // FileIdentityFactory 重新讀取並暫存 資料夾列表於 Map 中
        FileIdentityFactory.getInstance().refresh();
        // 取得暫存的 資料夾Map (即剛讀取的、現有的檔案資料夾)
        Map<FileIdentity, String> identityMap = FileIdentityFactory.getInstance().getIdentityMap();
        Set<FileIdentity> keys = identityMap.keySet();
        // 現有的檔案資料夾 List
        List<FileIdentity> realFileIdentityList = new ArrayList<FileIdentity>();
        // 裝入現有檔案資料夾暫存物件
        for (FileIdentity fi : keys) {
            realFileIdentityList.add(fi);
        }
        // 複製現有的檔案資料夾 List
        List<FileIdentity> clone_realFileIdentityList = new ArrayList<FileIdentity>(realFileIdentityList);
        // DB中舊有的檔案資料夾 List
        List<FileIdentity> dbFileIdentityList = fileIdentityDao.selectAll();
        // 以 現有的資料列表 移除 資料庫中的資料列表，即剩下新增的資料
        realFileIdentityList.removeAll(dbFileIdentityList);
        for (FileIdentity add_fileIdentity : realFileIdentityList) {
            logger.warn("找到新的資料夾 : " + add_fileIdentity.getFilepath() + "，即將寫入資料庫...");
            fileIdentityDao.save(add_fileIdentity);
            logger.warn("新的資料夾屬性 : " + add_fileIdentity.getFilepath() + " 寫入完成...");
        }

        // 以 資料庫中的資料列表 移除 現有的資料列表，即剩下資料庫中多餘的資料
        dbFileIdentityList.removeAll(clone_realFileIdentityList);

        for (FileIdentity delete_fileIdentity : dbFileIdentityList) {

            logger.warn("資料庫中找到多餘的資料夾 : " + delete_fileIdentity.getFilepath() + "，即將從資料庫中刪除...");
            fileIdentityDao.remove(delete_fileIdentity.getId());
            logger.warn("資料庫中的資料夾 : " + delete_fileIdentity.getFilepath() + " 刪除完成...");
        }
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(String checked_ids, String unchecked_ids) {
        JSONObject result = new JSONObject();
        try {
            if (checked_ids != null && !checked_ids.equals("")) {
                String[] ids = checked_ids.split(",");
                for (String id : ids) {
                    FileIdentity fi = fileIdentityDao.findById(id);
                    fi.setIsPublic(Boolean.TRUE);
                    fileIdentityDao.update(fi);
                }
            }
            if (unchecked_ids != null && !unchecked_ids.equals("")) {
                String[] ids = unchecked_ids.split(",");
                for (String id : ids) {
                    FileIdentity fi = fileIdentityDao.findById(id);
                    fi.setIsPublic(Boolean.FALSE);
                    fileIdentityDao.update(fi);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
            result.put("status", 500);
            result.put("message", "server error");
        }
        result.put("status", 200);
        result.put("message", "setting saved");
        return result.toString();
    }

}
