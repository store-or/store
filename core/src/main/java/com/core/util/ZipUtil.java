package com.core.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * Author: laizy
 */
public final class ZipUtil {

    private ZipUtil(){}

    /**
     * 解压缩文件(适用于含中文文件名的文件的压缩包).
     * @param file
     * @param dir
     * @throws java.io.IOException
     */
    public static void unzip(File file, File dir) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        try {
            Enumeration enumeration = zipFile.getEntries();
            while(enumeration.hasMoreElements()) {
                ZipEntry zipEntry;
                int length;
                byte[] buffer = new byte[8192];
                while (enumeration.hasMoreElements()) {
                    zipEntry = (ZipEntry) enumeration.nextElement();
                    File loadFile = new File(dir, zipEntry.getName());
                    if (zipEntry.isDirectory()) {
                        loadFile.mkdirs();
                    } else {
                        if (!loadFile.getParentFile().exists()) {
                            loadFile.getParentFile().mkdirs();
                        }
                        OutputStream outputStream = new FileOutputStream(loadFile);
                        InputStream inputStream = zipFile.getInputStream(zipEntry);
                        try {
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }
                        } finally {
                            inputStream.close();
                            outputStream.close();
                        }
                    }
                }
            }

        } finally {
            zipFile.close();
        }
    }

    public static void compress(File src, File dst) throws IOException {
        List<File> files = new ArrayList<File>();
        getAllFile(src, files);
        byte[] buffer = new byte[8192];
        int length;
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(dst));
            for (File file : files) {
                BufferedInputStream bis = null;
                try {
                    zos.putNextEntry(new java.util.zip.ZipEntry(getEntryName(src, file)));
                    bis = new BufferedInputStream(new FileInputStream(file));
                    while ((length = bis.read(buffer)) != -1) {
                        zos.write(buffer, 0, length);
                    }
                } finally {
                    if (bis != null) {
                        bis.close();
                    }
                    zos.closeEntry();
                }
            }
        } finally {
            if (zos != null) {
                zos.close();
            }
        }
    }

    private static void getAllFile(final File src, final List<File> list) {
        if (src.isFile()) {
            list.add(src);
        } else if (src.isDirectory()) {
            File[] files = src.listFiles();
            if (files != null) {
                for (File file : files) {
                    getAllFile(file, list);
                }
            }
        }
    }

    private static String getEntryName(File src, File entry) throws IOException {
        return entry.getCanonicalPath().substring(src.getCanonicalPath().length() + 1, entry.getCanonicalPath().length());
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void decompress(File file,File dir) throws IOException {
        java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(file);
        try {
            Enumeration enumeration = zipFile.entries();
            java.util.zip.ZipEntry zipEntry;
            int length;
            byte[] buffer = new byte[8192];
            while (enumeration.hasMoreElements()) {
                zipEntry = (java.util.zip.ZipEntry) enumeration.nextElement();
                File loadFile = new File(dir,zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    loadFile.mkdirs();
                } else {
                    if (!loadFile.getParentFile().exists()) {
                        loadFile.getParentFile().mkdirs();
                    }
                    OutputStream outputStream = new FileOutputStream(loadFile);
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    try {
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    } finally {
                        inputStream.close();
                        outputStream.close();
                    }
                }

            }
        } finally {
            zipFile.close();
        }

    }
}
