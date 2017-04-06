package com.seeyon.v3x.dee.common.a8version;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.a8version.model.A8VersionItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A8版本管理器
 *
 * @author zhangfb
 */
public class A8VersionManager {
    private final static Log log = LogFactory.getLog(A8VersionManager.class);

    private static A8VersionManager instance = new A8VersionManager();

    /**
     * A8版本项列表
     */
    private List<A8VersionItem> a8VersionItems = new ArrayList<A8VersionItem>();

    private A8VersionManager() {
        init();
    }

    /**
     * 单例模式
     *
     * @return 当前类的单例
     */
    public static A8VersionManager getInstance() {
        return instance;
    }

    /**
     * <p>初始化操作，加载a8version.xml，将其内容载入到A8版本项列表中。</p>
     * <p>A8版本项列表，可用于版本比较操作</p>
     */
    private void init() {
        SAXReader saxReader = new SAXReader();
        URL fileURL = A8VersionManager.class.getResource("/com/seeyon/v3x/dee/conf/a8version.xml");

        try {
            Document document = saxReader.read(fileURL);
            Element root = document.getRootElement();
            List<Element> itemElements = (List<Element>) root.selectNodes("item");
            for (Element element : itemElements) {
                if (element != null) {
                    A8VersionItem item = new A8VersionItem();
                    item.setId(element.attributeValue("id"));
                    item.setVersion(element.attributeValue("version"));
                    item.setSort(Integer.parseInt(element.attributeValue("sort")));
                    a8VersionItems.add(item);
                }
            }
            Collections.sort(a8VersionItems);
        } catch (DocumentException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 源版本是否在目标版本中
     *
     * @param srcVersion   源版本
     * @param descVersions 目标版本
     * @return true：包含，false：不包含
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public boolean include(String srcVersion, List<String> descVersions) throws TransformException {
        A8VersionItem srcItem = exchange(srcVersion);
        if (srcItem == null) {
            throw new TransformException("A8版本<" + srcVersion + ">不存在！");
        }

        List<A8VersionItem> descItems = exchange(descVersions);
        if (descItems.size() != descVersions.size()) {
            throw new TransformException("A8<" + descVersions.toArray() + ">存在版本不一致！");
        }

        for (A8VersionItem descItem : descItems) {
            if (srcItem.equals(descItem)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 源版本是否排除在目标版本之外
     *
     * @param srcVersion   源版本
     * @param descVersions 目标版本
     * @return true：排除，false：不排除
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public boolean exclude(String srcVersion, List<String> descVersions) throws TransformException {
        return !include(srcVersion, descVersions);
    }

    /**
     * 源版本是否在目标版本之间
     *
     * @param srcVersion   源版本
     * @param descVersion1 目标版本1
     * @param descVersion2 目标版本2
     * @return true：是，false：不是
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public boolean between(String srcVersion, String descVersion1, String descVersion2) throws TransformException {
        String fromVersion;
        String toVersion;
        if (lessThan(descVersion1, descVersion2)) {
            fromVersion = descVersion1;
            toVersion = descVersion2;
        } else {
            fromVersion = descVersion2;
            toVersion = descVersion1;
        }

        boolean flag = false;
        for (A8VersionItem item : a8VersionItems) {
            if (item.getVersion().equals(fromVersion)) {
                flag = true;
            }
            if (flag && item.getVersion().equals(srcVersion)) {
                return true;
            }
            if (item.getVersion().equals(toVersion)) {
                flag = false;
            }
        }

        return false;
    }

    /**
     * 是否“源版本等于目标版本”
     *
     * @param srcVersion  源版本
     * @param descVersion 目标版本
     * @return true：是，false：否
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public boolean equal(String srcVersion, String descVersion) throws TransformException {
        return 0 == compareVersion(srcVersion, descVersion);
    }

    /**
     * 是否“源版本大于目标版本”
     *
     * @param srcVersion  源版本
     * @param descVersion 目标版本
     * @return true：是，false：否
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public boolean greatThan(String srcVersion, String descVersion) throws TransformException {
        return compareVersion(srcVersion, descVersion) > 0;
    }

    /**
     * 是否“源版本小于目标版本”
     *
     * @param srcVersion  源版本
     * @param descVersion 目标版本
     * @return true：是，false：否
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public boolean lessThan(String srcVersion, String descVersion) throws TransformException {
        return compareVersion(srcVersion, descVersion) < 0;
    }

    /**
     * 是否“源版本大于等于目标版本”
     *
     * @param srcVersion  源版本
     * @param descVersion 目标版本
     * @return true：是，false：否
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public boolean greatEqualThan(String srcVersion, String descVersion) throws TransformException {
        return compareVersion(srcVersion, descVersion) >= 0;
    }

    /**
     * 是否“源版本小于等于目标版本”
     *
     * @param srcVersion  源版本
     * @param descVersion 目标版本
     * @return true：是，false：否
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public boolean lessEqualThan(String srcVersion, String descVersion) throws TransformException {
        return compareVersion(srcVersion, descVersion) <= 0;
    }

    /**
     * 源版本和目标版本的比较结果
     *
     * @param srcVersion  源版本
     * @param descVersion 目标版本
     * @return 0：相等，1：源版本大于目标版本，-1：源版本小于目标版本
     * @throws com.seeyon.v3x.dee.TransformException
     */
    private int compareVersion(String srcVersion, String descVersion) throws TransformException {
        A8VersionItem item1 = exchange(srcVersion);
        if (item1 == null) {
            throw new TransformException("A8版本<" + srcVersion + ">不存在！");
        }

        A8VersionItem item2 = exchange(descVersion);
        if (item2 == null) {
            throw new TransformException("A8版本<" + descVersion + ">不存在！");
        }

        return item1.compareTo(item2);
    }

    /**
     * 获取版本Item列表
     *
     * @param versions 版本字符串列表
     * @return
     */
    private List<A8VersionItem> exchange(List<String> versions) {
        List<A8VersionItem> items = new ArrayList<A8VersionItem>();

        for (A8VersionItem a8VersionItem : a8VersionItems) {
            for (String version : versions) {
                if (a8VersionItem.getVersion().equals(version)) {
                    items.add(a8VersionItem);
                }
            }
        }

        return items;
    }

    /**
     * 获取版本Item
     *
     * @param version 版本
     * @return
     */
    public A8VersionItem exchange(String version) {
        if (version != null) {
            for (A8VersionItem item : a8VersionItems) {
                if (item.getVersion().equals(version)) {
                    return item;
                }
            }
        }
        return null;
    }
}
