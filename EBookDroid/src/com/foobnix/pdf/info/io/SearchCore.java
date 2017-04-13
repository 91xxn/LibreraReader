package com.foobnix.pdf.info.io;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.foobnix.dao2.FileMeta;
import com.foobnix.pdf.info.ExtUtils;
import com.foobnix.ui2.FileMetaCore;
import com.foobnix.ui2.adapter.FileMetaAdapter;

public class SearchCore {
    public static boolean endWith(String name, List<String> exts) {
        for (String ext : exts) {
            if (name.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    public static boolean findOnce = false;

    public static void search(List<FileMeta> items, File root, List<String> exts) {
        findOnce = false;
        search(root, exts, items);
    }

    private static void search(File root, List<String> exts, List<FileMeta> items) {
        if (root.isFile() && endWith(root.getName(), exts)) {
            items.add(new FileMeta(root.getPath()));
            return;
        } else if (root.isFile()) {
            return;
        }

        File[] listFiles = root.listFiles();

        if (listFiles == null) {
            return;
        }
        for (File file : listFiles) {
            if (file.isDirectory()) {
                if (!findOnce && file.getPath().equals("/mnt/sdcard/Android")) {
                    findOnce = true;
                    continue;
                }
                search(file, exts, items);
            } else if (endWith(file.getName(), exts)) {
                items.add(new FileMeta(file.getPath()));
            }
        }
        return;

    }

    public static List<FileMeta> getFilesAndDirs(String path) {
        File file = new File(path);
        if (!file.isDirectory()) {
            return Collections.emptyList();
        }
        ArrayList<FileMeta> files = new ArrayList<FileMeta>();

        File[] listFiles = file.listFiles(SUPPORTED_EXT_AND_DIRS_FILTER);

        if (listFiles == null || listFiles.length == 0) {
            return Collections.emptyList();
        }

        List<File> res = new ArrayList<File>(Arrays.asList(listFiles));
        Collections.sort(res, FILES_AND_DIRS_COMPARATOR);

        for (File it : res) {
            FileMeta meta = new FileMeta(it.getPath());
            FileMetaCore.get().upadteBasicMeta(meta, it);
            if (it.isDirectory()) {
                meta.setCusType(FileMetaAdapter.DISPLAY_TYPE_DIRECTORY);
            }
            files.add(meta);
        }

        return files;
    }

    public static SupportedExtAndDirsFilter SUPPORTED_EXT_AND_DIRS_FILTER = new SupportedExtAndDirsFilter();

    public static class SupportedExtAndDirsFilter implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                return true;
            }
            for (String s : ExtUtils.browseExts) {
                if (pathname.getName().toLowerCase(Locale.US).endsWith(s)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static Comparator<File> FILES_AND_DIRS_COMPARATOR = new Comparator<File>() {
        @Override
        public int compare(File o1, File o2) {
            if (o1.isDirectory() && o2.isFile())
                return -1;
            if (o1.isFile() && o2.isDirectory())
                return 1;
            return o1.getName().compareTo(o2.getName());
        }
    };

}
