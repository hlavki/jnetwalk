/*
 * ImageHelper.java
 *
 * Created on Nede\u013Ea, 2007, marec 18, 22:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.easyedu.jnetwalk.utils;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 *
 * @author hlavki
 */
public class ImageHelper {

    private static final Map<String, ImageIcon> iconCache = initCache();
    private static ImageHelper instance;

    /** Creates a new instance of ImageHelper */
    private ImageHelper() {
    }

    public static synchronized ImageHelper getInstance() {
        if (instance == null) {
            instance = new ImageHelper();
        }
        return instance;
    }

    private static Map<String, ImageIcon> initCache() {
        return new HashMap<String, ImageIcon>();
    }

    public synchronized ImageIcon getIcon(String name) {
        ImageIcon result = iconCache.get(name);
        if (result == null) {
            result = new javax.swing.ImageIcon(getClass().getResource(name));
            iconCache.put(name, result);
        }
        return result;
    }
}