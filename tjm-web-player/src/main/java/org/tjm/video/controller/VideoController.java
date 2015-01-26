/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjm.video.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tjm.identity.dao.FileIdentityDao;
import org.tjm.identity.entity.FileIdentity;
import org.tjm.identity.singleton.FileIdentityFactory;
import org.tjm.user.dao.UserDao;
import org.tjm.user.entity.Authority;
import org.tjm.user.entity.User;

/**
 *
 * @author larry
 */
@Controller
@RequestMapping("/video")
public class VideoController {

    private static Logger logger = LoggerFactory.getLogger(VideoController.class);
    Properties props = new Properties();
    @Resource(name = "userDao")
    private UserDao userDao;
    @Resource(name = "fileIdentityDao")
    private FileIdentityDao fileIdentityDao;

    @RequestMapping("/tree")
    @ResponseBody
    public String tree(HttpServletRequest request, String id) {

        logger.warn("--------------------------------- video controller /tree --------------------------------------");
        JSONArray infoArray = new JSONArray();
        User user = userDao.selectUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Set<Authority> authoritySet = userDao.selectAuthoritySetByUserId(user.getId());
        boolean could_see_inner_content = false;
        for (Authority a : authoritySet) {
            if (a.getName().equals("INNER_BROWSE")) {
                could_see_inner_content = true;
            }
        }

        try {
            props.load(VideoController.class
                    .getResourceAsStream("/config.properties"));

            // 根路徑
            String onclick_path = props.getProperty("root");

            if (id != null && !id.equals("")) {
                id = java.net.URLDecoder.decode(id, "UTF-8");
                System.out.println("id = " + id);
                onclick_path = onclick_path + id;
            }

            logger.warn("onclick_path = " + onclick_path);

            FileIdentity onclick_node_fi = getOnclickNodeFileIdentity(id, onclick_path);

            File root = new File(onclick_path);

            File[] under_root_files = root.listFiles();

            for (File file : under_root_files) {
                int dot_index = file.getName().lastIndexOf(".");

                if (file.isDirectory() || 
                        // (dot_index > -1 && file.getName().substring(dot_index).equals(".mp4"))
                        (dot_index > -1)
                        ) {

                    // children id
                    String children_id = file.getPath().replace(onclick_path, "");
                    if (id != null) {
                        children_id = id + file.getPath().replace(onclick_path, "");
                    }
                    logger.warn("children id :" + children_id);

                    JSONObject file_info = new JSONObject();
                    FileIdentity children_fi = fileIdentityDao.findByFilePath(children_id);

                    if (children_fi != null) { // 資料庫中查詢得到該資料夾
                        if (!children_fi.getIsPublic()) { // 如果是「對內的資料夾」
                            if (could_see_inner_content) { // 僅有可瀏覽對內的權限才
                                file_info = toReturnNodeInfo(request, children_id, file);
                                infoArray.add(file_info);
                            }
                        } else { // 「對外的資料夾」
                            file_info = toReturnNodeInfo(request, children_id, file);
                            infoArray.add(file_info);
                        }
                    } else { // 非資料夾
                        if (!file.isDirectory()) { // 確保是檔案之情況下
                            if (!onclick_node_fi.getIsPublic()) { // 若該檔案所屬的資料夾是「對內資料夾」
                                if (could_see_inner_content) { // 是內部人員
                                    file_info = toReturnNodeInfo(request, children_id, file);
                                    infoArray.add(file_info);
                                }
                            } else { // 「對外資料夾」
                                file_info = toReturnNodeInfo(request, children_id, file);
                                infoArray.add(file_info);
                            }
                        }
                    } // if (children_fi != null) {} else {}

                } // if (file.isDirectory() || (dot_index > -1 && file.getName().substring(dot_index).equals(".mp4"))) {}
            } // for (File file : under_root_files) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.warn("--------------------------------- end of video controller /tree --------------------------------------");
        return infoArray.toString();
    }

    public JSONObject toReturnNodeInfo(HttpServletRequest request, String children_id, File file) throws UnsupportedEncodingException {

        JSONObject file_info = new JSONObject();
        // put return info
        file_info.put("id", URLEncoder.encode(new String(
                children_id.getBytes("UTF-8"), "UTF-8"), "UTF-8"));
        file_info.put("text", file.getName());

        JSONObject attributes = new JSONObject();

        // 如果為資料夾，則顯示為「收起」狀態
        if (file.isDirectory()) {
            file_info.put("state", "closed");
            attributes.put("isDirectory", true);
        } else {
            attributes.put("isDirectory", false);
        }
        file_info.put("attributes", attributes);

        return file_info;

    }

    @RequestMapping("/stream")
    @ResponseBody
    public String stream(String path) {
        JSONObject result = new JSONObject();

        System.out.println("in stream.htm = " + path);
        try {
            String decode_path = java.net.URLDecoder.decode(path, "UTF-8");
            System.out.println("decode_path= " + decode_path);

            String ip = props.getProperty("host.ip");
            String port = props.getProperty("stream.port");
            String prefix_path = props.getProperty("prefix_path");
            String stream_type = props.getProperty("stream_type");

            String file_name = decode_path.substring(decode_path.lastIndexOf(File.separator) + 1);
            System.out.println("file_name = " + file_name);
            result.put("file", file_name);

            String path_before_file_name = decode_path.substring(0,
                    decode_path.lastIndexOf(File.separator));
            path_before_file_name = path_before_file_name.replaceAll("\\\\", "/");
            System.out.println("withoutFileName = " + path_before_file_name);

            String streamer = stream_type + "://" + ip + ":" + port + prefix_path + path_before_file_name;
            System.out.println("streamer = " + streamer);
            result.put("streamer", streamer);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public FileIdentity getOnclickNodeFileIdentity(String id, String onclick_path) {
        FileIdentity onclick_fi = null;
        if (id == null) {
            onclick_fi = new FileIdentity();
            onclick_fi.setFilepath("/");
            onclick_fi.setIsPublic(true);
        } else {
            String root_path = props.getProperty("root");
            onclick_fi = fileIdentityDao.findByFilePath(onclick_path.replace(root_path, ""));
        }
        return onclick_fi;
    }

	// @RequestMapping("/playlist")
    // @ResponseBody
    // public String toPlayList(HttpServletRequest request,
    // HttpServletResponse response, String path) {
    // JSONObject result = new JSONObject();
    // JSONArray array = new JSONArray();
    //
    // System.out.println("in playlist.htm = " + path);
    // try {
    // String decode_path = java.net.URLDecoder.decode(path, "UTF-8");
    // System.out.println("decode_path= " + decode_path);
    //
    //
    // result.put("file", request.getContextPath()
    // + "/video/read.htm?filepath=" + path);
    // result.put("title", decode_path.substring(decode_path
    // .lastIndexOf(File.separator) + 1));
    // result.put("type", "mp4");
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // array.add(result);
    // return array.toString();
    // }
    // @RequestMapping("/read")
    // public void read(String filepath, HttpServletRequest request,
    // HttpServletResponse response) {
    //
    // try {
    // System.out.println("in read.htm = " + filepath);
    // filepath = java.net.URLDecoder.decode(filepath, "UTF-8");
    //
    // File mediaFile = new File(filepath);
    //
    // if (mediaFile.exists()) {
    //
    // FileInputStream in = new FileInputStream(mediaFile);
    // BufferedInputStream bis = new BufferedInputStream(in);
    // BufferedOutputStream bos = new BufferedOutputStream(
    // response.getOutputStream());
    //
    // byte[] buffer = new byte[1024 * 1024 * 5];
    // response.addHeader("Accept-Ranges", "bytes");
    // response.setContentLength((int) mediaFile.length());
    // response.setContentType("video/mp4");
    //
    // int length = 0;
    // while ((length = bis.read(buffer)) != -1) {
    // bos.write(buffer, 0, length);
    // }
    //
    // response.getOutputStream().close();
    // in.close();
    //
    // } else {
    // response.sendError(404);
    // }
    // } catch (IOException ex) {
    // System.out.println("read image failed: " + ex);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // } // public void read(
}
