/*
 * Copyright (c) 2015 by vincent.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.vincent.taste.spicy.helper.basic;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文件信息
 *
 * @author vincent
 * @version 1.0 2017/8/20 02:33
 */
@Getter
@Setter
public class FilePath implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String FOLDER_SEPARATOR = "/";
    private static final String EXTENSION_SEPARATOR = ".";

    /** 文件路径 */
    private String path;
    /** 不带后缀的文件名 */
    private String fileName;
    /** 文件后缀 */
    private String fileExtension;

    public FilePath(String fullPath) {
        this(fullPath, EXTENSION_SEPARATOR);
    }

    public FilePath(String fullPath, String extensionSeparator) {
        tripFilePath(fullPath, extensionSeparator);
    }

    /**
     * 萃取分割文件信彷
     *
     * @param fullPath 文件全路径
     */
    private void tripFilePath(String fullPath, String extensionSeparator) {
        if (fullPath == null || fullPath.trim().length() == 0) {
            return;
        }

        int folderIndex = fullPath.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > 0) {
            path = fullPath.substring(0, folderIndex);
        }

        int extIndex = fullPath.lastIndexOf(extensionSeparator);
        if (extIndex > 0 && folderIndex < extIndex) {
            fileExtension = fullPath.substring(extIndex + 1);
            fileName = fullPath.substring(folderIndex + 1, extIndex);
        } else {
            fileName = fullPath.substring(folderIndex + 1);
        }
    }

    /**
     * 获取链接或者文件的后缀
     *
     * @param url String 连接或者文件路径
     * @return 后缀
     */
    public static String fileExtension(String url) {
        return fileExtension(url, EXTENSION_SEPARATOR);
    }

    /**
     * 获取链接或者文件的后缀
     *
     * @param url   String 连接或者文件路径
     * @param split String 分割符号
     * @return 后缀
     */
    public static String fileExtension(String url, String split) {
       return new FilePath(url).getFileExtension();
    }

}
