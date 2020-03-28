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

package cn.vincent.taste.spicy.helper.object;

import cn.vincent.taste.spicy.helper.constant.Constant;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * java包工具
 *
 * @author vincent
 * @version 1.0 2017/8/20 01:37
 */
public class PackageHelper {

    private static final String FILE = "file";
    private static final String JAR = "jar";

    private PackageHelper() {
    }

    /**
     * 获取jar包中的文件,遍历子目录
     *
     * @param jarPath String jar包中目录的绝对路径
     * @return 子目录列表
     */
    public static List<JarEntry> getJarFiles(String jarPath) {
        String[] paths = jarPath.split("!");
        return getJarFiles(paths[0], paths.length == 1 ? null : paths[1], true);
    }

    /**
     * 获取jar包中的文件
     *
     * @param jarPath      String jar包中目录的绝对路径
     * @param childPackage boolean 是否遍历子目录
     * @return 文件列表
     */
    public static List<JarEntry> getJarFiles(String jarPath, boolean childPackage) {
        String[] paths = jarPath.split("!");
        return getJarFiles(paths[0], paths.length == 1 ? null : paths[1], childPackage);
    }

    /**
     * 获取jar包中的文件，遍历子目录
     *
     * @param jarPath     String jar包的路径，不包含子路径
     * @param packagePath String 需要查询的子路径
     * @return 文件列表
     */
    public static List<JarEntry> getJarFiles(String jarPath, String packagePath) {
        return getJarFiles(jarPath, packagePath, true);
    }

    /**
     * 获取jar包中的文件
     *
     * @param jarPath      String jar包的路径，不包含子路径
     * @param packagePath  String 需要查询的子路径
     * @param childPackage boolean 是否遍历子目录
     * @return 文件列表
     */
    public static List<JarEntry> getJarFiles(String jarPath, String packagePath, boolean childPackage) {
        if (StringUtils.isBlank(packagePath)) {
            packagePath = Constant.MARK_SLASH;
        } else if (!packagePath.endsWith(Constant.MARK_SLASH)) {
            packagePath += Constant.MARK_SLASH;
        }

        List<JarEntry> subs = new ArrayList<>();
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (!entryName.equals(packagePath) && entryName.startsWith(packagePath)) {
                    if (childPackage) {
                        subs.add(jarEntry);
                    } else if (!jarEntry.isDirectory()) {
                        String relativePath = entryName.substring(packagePath.length());
                        if (!relativePath.contains(Constant.MARK_SLASH)) {
                            subs.add(jarEntry);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return subs;
    }

    /**
     * 获取连接数组里的所有文件,包括子目录
     *
     * @param urls        URL[] 需要遍历的连接
     * @param packagePath String 包路径
     * @return 文件列表
     */
    public static List<JarEntry> getSubByJars(URL[] urls, String packagePath) {
        return getSubByJars(urls, packagePath, true);
    }

    /**
     * 获取连接数组里的所有文件
     *
     * @param urls         URL[] 需要遍历的连接
     * @param packagePath  String 包路径
     * @param childPackage boolean 是否遍历子目录
     * @return 文件列表
     */
    public static List<JarEntry> getSubByJars(URL[] urls, String packagePath, boolean childPackage) {
        List<JarEntry> subs = new ArrayList<>();
        if (urls == null || urls.length == 0) {
            return subs;
        }

        for (URL url : urls) {
            String urlPath = url.getPath();
            // 不必搜索classes文件夹
            if (urlPath.endsWith("classes/")) {
                continue;
            }

            subs.addAll(getJarFiles(urlPath, packagePath, childPackage));
        }
        return subs;
    }

    /**
     * 从项目文件获取某包下所有文件
     *
     * @param filePath     String 文件路径
     * @param childPackage boolean 是否遍历子包
     * @return 类的完整名称
     */
    public static List<File> getSubByFile(String filePath, boolean childPackage) {
        List<File> subs = new ArrayList<>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles != null && childFiles.length > 0) {
            for (File childFile : childFiles) {
                if (childPackage) {
                    subs.add(childFile);
                    subs.addAll(getSubByFile(childFile.getPath(), true));
                } else if (childFile.isFile()) {
                    subs.add(childFile);
                }
            }
        }

        return subs;
    }

    /**
     * 获取文件目录下的所有class
     *
     * @param url          URL 查找url
     * @param childPackage boolean 是否遍历子包
     * @return class列表
     */
    @SuppressWarnings("Duplicates")
    private static List<String> getFileClass(URL url, boolean childPackage) {
        List<String> fileNames = new ArrayList<>();
        List<File> list = getSubByFile(url.getPath(), childPackage);
        if (list.size() > 0) {
            for (File file : list) {
                String name = file.getName();
                if (file.isDirectory() || !name.endsWith(".class")) {
                    continue;
                }

                if (name.startsWith(Constant.MARK_SLASH)) {
                    name = name.substring(1);
                }

                name = name.substring(0, name.length() - ".class".length());
                fileNames.add(name.replace(Constant.MARK_SLASH, Constant.MARK_POINT) + Constant.MARK_POINT + "class");
            }
        }
        return fileNames;
    }

    /**
     * 获取jar包下的所有class
     *
     * @param url          URL 查找url
     * @param childPackage boolean 是否遍历子包
     * @return class列表
     */
    private static List<String> getJarClass(URL url, boolean childPackage) {
        String[] paths = url.getPath().split("!");
        URL[] urls = new URL[1];
        try {
            urls[0] = new URL(paths[0]);
            return getJarClass(urls, paths.length == 1 ? null : paths[1], childPackage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 获取jar包下的所有class
     *
     * @param urls         URL[] 查找url
     * @param packagePath  String 包路径
     * @param childPackage boolean 是否遍历子包
     * @return class列表
     */
    @SuppressWarnings("Duplicates")
    private static List<String> getJarClass(URL[] urls, String packagePath, boolean childPackage) {
        List<String> fileNames = new ArrayList<>();
        List<JarEntry> list = getSubByJars(urls, packagePath, childPackage);
        if (list.size() > 0) {
            for (JarEntry jarEntry : list) {
                String name = jarEntry.getName();
                if (jarEntry.isDirectory() || !name.endsWith(".class")) {
                    continue;
                }

                if (name.startsWith(Constant.MARK_SLASH)) {
                    name = name.substring(1);
                }

                name = name.substring(0, name.length() - ".class".length());
                fileNames.add(name.replace(Constant.MARK_SLASH, Constant.MARK_POINT) + Constant.MARK_POINT + "class");
            }
        }
        return fileNames;
    }

    /**
     * 获取项目中的所有类
     *
     * @param packageName  String 包名
     * @param childPackage boolean 是否遍历子包
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName, boolean childPackage) {
        List<String> fileNames = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String type = url.getProtocol();
            if (FILE.equals(type)) {
                fileNames.addAll(getFileClass(url, childPackage));
            } else if (JAR.equals(type)) {
                fileNames.addAll(getJarClass(url, childPackage));
            }
        } else {
            fileNames.addAll(getJarClass(((URLClassLoader) loader).getURLs(), packageName, childPackage));
        }
        return fileNames;
    }

    /**
     * 获取某包下（包括该包的所有子包）所有类
     *
     * @param packageName String 包名
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName) {
        return getClassName(packageName, true);
    }
}
