package com.seeyon.v3x.dee;

import com.seeyon.v3x.dee.common.a8rest.RestServiceManager;
import com.seeyon.v3x.dee.common.db.resource.util.DeeResourceEnum;
import com.seeyon.v3x.dee.common.hotdeploy.HotDeploy;
import com.seeyon.v3x.dee.context.AdapterKeyName;
import com.seeyon.v3x.dee.datasource.JDBCDataSource;
import com.seeyon.v3x.dee.resource.DbDataSource;
import com.seeyon.v3x.dee.srv.rmi.DEERmiServer;
import com.seeyon.v3x.dee.util.DataChangeUtil;
import com.seeyon.v3x.dee.util.FileUtil;
import com.seeyon.v3x.dee.util.UuidUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * DEE引擎内部数据源管理，目前仅用于管理DEE元数据的数据源。<br/>
 * 元数据源配置信息引用名为dee_meta的DataSource，<br/>
 * 其连接信息缺省从操作系统和JVM的环境变量中获取。
 *
 * @author wangwenyou
 */
public class DataSourceManager {
    private static final Log log = LogFactory.getLog(DataSourceManager.class);

    public static final String DEE_META = "dee_meta";

    private static ConcurrentMap<String, DbDataSource> registry = new ConcurrentHashMap<String, DbDataSource>();

    private static volatile DataSourceManager INSTANCE = null;

    public static DataSourceManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DataSourceManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataSourceManager();
                }
            }
        }
        return INSTANCE;
    }

    private DataSourceManager() {
        initDeePath();         //初始化DEE_HOME下的各个文件夹
        initSeeyonFormXSLT();  //初始化默认提供的XSLT模板
        initDeeResourceConf(); //初始化dee-resource.properties
        initDeeQuartzConf();   //初始化dee-quartz.properties
        isStartRMISrv();       //判断是否启动RMI服务
        bindMetaDataSource();
        initDeeDb();
        initDeeMeta();
        RestServiceManager.getInstance();
        DirectoryWatcher fm = new HotDeploy(getProperty("DEE_HOME") + "/hotdeploy", Pattern.compile("(?:.+\\.drp)"));
        Timer timer = new Timer();
        timer.schedule(fm, 30000, 60000);
        log.info("初始化DEE配置完毕.");
    }

    private String getProperty(String name) {
        // Property优先
        String value = System.getProperty(name);
        if (value == null) {
            value = System.getenv(name);
        }
        return value;
    }

    private void bindMetaDataSource() {
        // 尝试从系统变量取元数据连接信息
        String driver = this.getProperty("dee.meta.datasource.driver");
        String url = this.getProperty("dee.meta.datasource.url");
        String userName = this.getProperty("dee.meta.datasource.userName");
        String password = this.getProperty("dee.meta.datasource.password");
        // Boolean isRemote = true;
        // 如果设置了上述四个参数，以系统环境设置为准（远程），未设置尝试访问DEE_HOME的数据库（本地）
        if (url == null) {
            driver = "org.h2.Driver";
            userName = "sa";
            password = "seeyondee";

            String deeHome = this.getProperty("DEE_HOME");
            // 连接本地DEE_HOME下的dee数据库
            if (deeHome != null) {
                // 混合模式连接H2，保证tcp服务能自动启动
                url = "jdbc:h2:" + deeHome + "/data/dee;AUTO_RECONNECT=TRUE;AUTO_SERVER=TRUE";
            }
            // isRemote = false;
        }
        if (driver != null && url != null) {
            log.debug("启动DEE元数据数据库：" + url);
            JDBCDataSource ds = new JDBCDataSource(driver, url, userName, password);
            bind(DEE_META, ds);
        } else {
            log.error("没有指定连接信息，无法启动DEE元数据数据库。");
        }
    }

    public static DbDataSource lookup(String name) {
        DbDataSource ds = registry.get(name);
        if (ds == null && DEE_META.equals(name) && INSTANCE != null) {
            INSTANCE.bindMetaDataSource();
            return registry.get(name);
        }
        return ds;
    }

    public void bind(String name, DbDataSource ds) {
        registry.put(name, ds);
    }

    /**
     * 在DEE配置根目录下，创建相关的文件夹
     */
    private void initDeePath() {
        String a8Home = this.getProperty(DEEConstants.A8_HOME);
        String deeHome = this.getProperty(DEEConstants.DEE_HOME);
        AdapterKeyName ak = AdapterKeyName.getInstance();
        ak.setA8Home(a8Home);
        ak.setDeeHome(deeHome);

        if (deeHome == null && a8Home != null) {
            deeHome = a8Home + File.separator + "base" + File.separator + "dee";
            System.setProperty("DEE_HOME", deeHome);
        }

        if (deeHome != null && !"".equalsIgnoreCase(deeHome)) {
            FileUtil.createDir(deeHome + "/conf");
            FileUtil.createDir(deeHome + "/hotdeploy");
            FileUtil.createDir(deeHome + "/temp");
            FileUtil.createDir(deeHome + "/logs");
            FileUtil.createDir(deeHome + "/licence");
            FileUtil.createDir(deeHome + "/codelib");
        }
    }

    /**
     * 通过传入连接执行 SQL 文件
     *
     * @param jDs     JDBCDataSource
     * @param sqlFile SQL 脚本文件
     * @author dkywolf
     */
    private void initCreateDb(JDBCDataSource jDs, String sqlFile) throws TransformException {
        List<String> sList = loadSql(sqlFile);
//		Connection conn = null;
//		PreparedStatement st = null;
//		InputStream in = null;
        try {
            if (jDs == null || sList == null || sList.size() < 1)
                return;
            jDs.initDbBatch(sList);
//			conn = jDs.getConnection();
//			if (this.isExistsA8Meta(conn)) {
//			} else {
//				// 初始化metaflow
//				jDs = (JDBCDataSource) lookup("dee_meta");
//				in = this.getClass().getResourceAsStream(
//						"/com/seeyon/v3x/dee/data/A8meta");
//				String sql = "INSERT INTO DEE_METAFLOW (METAFLOW_ID,METAFLOW_NAME,METAFLOW_CODE) VALUES (?,?,?);";
//				st = conn.prepareStatement(sql);
//				st.setString(1, "001");
//				st.setString(2, "元数据获取FLOW");
//				st.setString(3, IOUtility.toString(in));
//				st.executeUpdate();
//			}
        } catch (Exception e) {
            log.error("执行创建表sql脚本出错：" + e.getMessage(), e);
        } finally {
            try {
//				if (null != in) {
//					in.close();
//				}
//				if (st != null) {
//					st.close();
//				}
//				if (conn != null) {
//					jDs.close(conn);	
//				}
            } catch (Exception e) {
                log.error("初始化脚本关闭数据库连接异常" + e.getLocalizedMessage());
            } catch (Throwable e) {
                log.error("初始化脚本关闭JDBCDataSource异常" + e.getLocalizedMessage());
            }
        }
    }

    private void initDeeDb() {
        String sqlFile = "/com/seeyon/v3x/dee/data/crebas.sql";
        try {
            DbDataSource ds1 = lookup(DEE_META);
            if (ds1 == null)
                throw new TransformException("初始脚本时，没有找到dee的数据源dee_meta。");
            if (ds1 instanceof JDBCDataSource) {
                initCreateDb((JDBCDataSource) ds1, sqlFile);
            }
        } catch (Exception e) {
            log.error("执行创建表脚本异常：" + e.getMessage(), e);
        }
    }

    /**
     * init the table of dee_metaflow
     */
    private void initDeeMeta() {
        try {
            DbDataSource ds1 = lookup(DEE_META);
            if (ds1 != null) {
                String metaFlowCode = ((JDBCDataSource) ds1).executeQuery(
                        "select METAFLOW_CODE from DEE_METAFLOW where METAFLOW_ID = '001'",
                        new JDBCDataSource.ResultSetCallback<String>() {
                            @Override
                            public String execute(ResultSet rs) throws SQLException {
                                if (rs.next()) {
                                    return rs.getString("METAFLOW_CODE");
                                }
                                return null;
                            }
                        });
                if (metaFlowCode != null) {
                    org.dom4j.Document doc = org.dom4j.DocumentHelper.parseText("<root>" + metaFlowCode + "</root>");
                    List<org.dom4j.Element> list = doc.getRootElement().selectNodes("//datasource");
                    String sql = "insert into DEE_RESOURCE(" +
                            "RESOURCE_ID, RESOURCE_NAME, RESOURCE_TEMPLATE_ID, DIS_NAME, RESOURCE_CODE, EXT1) " +
                            "values(?, ?, ?, ?, ?, ?)";
                    for (int i = 0; i < list.size(); i++) {
                        org.dom4j.Element e = list.get(i);
                        if (e != null) {
                            String[] params = new String[6];
                            String uuid = UuidUtil.uuid();
                            params[0] = uuid;
                            params[1] = uuid;
                            params[2] = String.valueOf(DeeResourceEnum.A8MetaDatasource.ordinal());
                            params[3] = e.attributeValue("name");
                            e.attribute("name").setValue(uuid);
                            e = e.addAttribute("class", "com.seeyon.v3x.dee.datasource.JDBCDataSource");
                            params[4] = e.asXML();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            params[5] = formatter.format(new Date(System.currentTimeMillis()));
                            ((JDBCDataSource) ds1).execute(sql, params);
                        }
                    }
                    ((JDBCDataSource) ds1).execute("delete from DEE_METAFLOW where METAFLOW_ID = '001'", new String[0]);
                }
            }
        } catch (Exception e) {
            log.error("dee_metaflow数据迁移异常：" + e.getMessage(), e);
        }
    }

    /**
     * 读取 SQL 文件，获取 SQL 语句
     *
     * @param sqlFile SQL 脚本文件
     * @return List<sql> 返回所有 SQL 语句的 List
     * @author dkywolf
     */
    private List<String> loadSql(String sqlFile) throws TransformException {
        List<String> sqlList = new ArrayList<String>();

        try {
            InputStream sqlFileIn = this.getClass()
                    .getResourceAsStream(sqlFile);
            StringBuffer sqlSb = new StringBuffer();
            String oLine = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    sqlFileIn, DEEConstants.CHARSET_UTF8));
            while ((oLine = br.readLine()) != null) {
                sqlSb.append(oLine + "\n");
            }
            Pattern p = Pattern.compile("/\\*.+?\\*/");
            String pRet = p.matcher(sqlSb.toString()).replaceAll("");
            String[] sqlArr = pRet.split("(;\\s*\\r\\n)|(;\\s*\\n)");
            for (int i = 0; i < sqlArr.length; i++) {
                if (sqlArr[i] == null)
                    continue;
                String sSql = sqlArr[i].trim();
                if (!"".equalsIgnoreCase(sSql)) {
                    sqlList.add(sSql);
                }
            }
            return sqlList;
        } catch (Exception e) {
            log.error("获取sql脚本出错：" + e.getMessage(), e);
            throw new TransformException(e);
        }
    }

    /**
     * 仅用于在插入A8元数据前检验是否存在数据<br>
     * 与JDBCDataSource中检查内容方法类似<br>
     * 方法内无需关闭连接，外部关闭
     *
     * @param conn
     * @return true存在，不需要插入;false不存在，需要插入
     */
    private Boolean isExistsA8Meta(Connection conn) {
        Boolean b = false;
        ResultSet rs;
        try {
            rs = conn.createStatement().executeQuery(
                    "SELECT 1 FROM DEE_METAFLOW WHERE METAFLOW_ID = '001'");
            if (rs.next())
                b = true;
        } catch (SQLException e) {
        } // 捕获这个异常但不处理
        return b;
    }

    /**
     * 加入xslt模板拷贝，文件列表如下：
     * <ul>
     * <li>SeeyonForm2_0.xsl 流程表单</li>
     * <li>SeeyonForm2_1.xsl 无流程表单</li>
     * <li>SeeyonOrgInput2_0.xsl 组织机构同步模板</li>
     * </ul>
     */
    private void initSeeyonFormXSLT() {
        String deeHome = this.getProperty("DEE_HOME");
        String seeyonForm2_0 = "/com/seeyon/v3x/dee/data/SeeyonForm2_0.xsl";
        String targetForm2_0 = deeHome + "/conf/SeeyonForm2_0.xsl";
        String seeyonForm2_1 = "/com/seeyon/v3x/dee/data/SeeyonForm2_1.xsl";
        String targetForm2_1 = deeHome + "/conf/SeeyonForm2_1.xsl";
        String seeyonOrgInput2_0 = "/com/seeyon/v3x/dee/data/SeeyonOrgInput2_0.xsl";
        String targetOrgInput2_0 = deeHome + "/conf/SeeyonOrgInput2_0.xsl";
        this.fileCopy(seeyonForm2_0, targetForm2_0);
        this.fileCopy(seeyonForm2_1, targetForm2_1);
        this.fileCopy(seeyonOrgInput2_0, targetOrgInput2_0);
    }

    /**
     * 拷贝dee-resource.properties，config.properties等文件到DEE_HOME的conf
     */
    private void initDeeResourceConf() {
        String deeHome = getProperty("DEE_HOME");
        String deeResourceConf_source = "/com/seeyon/v3x/dee/conf/dee-resource.properties";
        String deeResourceConf_target = deeHome + "/conf/dee-resource.properties";
        this.fileCopy(deeResourceConf_source, deeResourceConf_target);
        //拷贝config.properties
        String config_source = "/com/seeyon/v3x/dee/conf/config.properties";
        String config_target = deeHome + "/conf/config.properties";
        File destFile = new File(config_target);
        if (destFile.exists()) {
            //文件如果存在则不进行拷贝
            InputStream in = null;
            try {
                Properties prpSource = new Properties();
                in = this.getClass().getResourceAsStream(config_source);
                prpSource.load(in);
                Properties prpTarget = DataChangeUtil.loadProperties(config_target);
                if (prpSource != null && prpTarget != null) {
                    if (prpSource.size() > prpTarget.size()) {
                        Set<Object> keys = prpSource.keySet();
                        for (Object key : keys) {
                            if (key == null) continue;
                            if (prpTarget.containsKey(key)) continue;
                            prpTarget.setProperty((String) key, prpSource.getProperty((String) key));
                        }
                        //保存信息
                        DataChangeUtil.storeProperties(prpTarget, config_target);
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error("获取config.properties路径出错：" + e.getMessage(), e);
            } finally {
                try {
                    if (in != null) in.close();
                } catch (IOException e) {
                }//写文件IO异常，catch不处理
            }
        } else {
            //文件如果不存在则直接拷贝
            this.fileCopy(config_source, config_target);
        }

    }

    private void initDeeQuartzConf() {
        String deeHome = getProperty("DEE_HOME");
        String config_source = "/com/seeyon/v3x/dee/conf/dee-quartz.properties";
        String config_target = deeHome + "/conf/dee-quartz.properties";
        this.fileCopy(config_source, config_target);
    }

    /**
     * 设置是否启动RMI远程调用
     */
    private void isStartRMISrv() {
        String deeHome = this.getProperty("DEE_HOME");
        String deeRMI_target = deeHome + "/conf/config.properties";
        Properties prop = new Properties();

        //获取RMI远程调用配置信息
        InputStream config = null;//this.getClass().getResourceAsStream(deeRMI_target);
        try {
            config = new FileInputStream(deeRMI_target);
            prop.load(config);
            //启动RMI远程服务
            if (Boolean.parseBoolean(prop.getProperty("rmisrv"))) {
                try {
                    if (StringUtils.isBlank(prop.getProperty("rmiport"))) {
                        log.error("DEE RMI 服务启动失败：系统参数rmiport为空");
                        return;
                    }
                    DEERmiServer dSrv = new DEERmiServer();
                    dSrv.setRmiPort(prop.getProperty("rmiport"));
                    dSrv.StartRmiSrv();
                    log.debug("DEE RMI 服务启动!");
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    log.error("DEE RMI 服务启动失败：" + e.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            log.error("DEE RMI启动配置信息读取异常：" + e.getMessage(), e);
        } finally {
            try {
                if (config != null) config.close();
            } catch (IOException e) {
            }//写文件IO异常，catch不处理
        }
    }

    /**
     * 从dee-common.jar中复制文件到磁盘
     *
     * @param srcFilePath：源路径，既JAR包中的资源文件，路径相对于CLASSPATH
     * @param destFilePath：目标路径，磁盘上的任意路径，绝对路径
     */
    private void fileCopy(String srcFilePath, String destFilePath) {
        File destFile = new File(destFilePath);
        if (destFile.exists()) {//先不判断是否是文件夹，可以考虑作为拷贝文件夹的方法
            return;//文件如果存在则不进行拷贝
        }
        BufferedInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new BufferedInputStream(this.getClass().getResourceAsStream(srcFilePath));
            fos = new FileOutputStream(destFile);
            byte[] buf = new byte[1024];
            int c = 0;
            while ((c = fis.read(buf)) != -1) {
                fos.write(buf, 0, c);
            }
            fos.flush();
        } catch (IOException e) {
            log.error("拷贝文件异常" + e.getLocalizedMessage());
        } finally {
            try {
                if (fos != null) fos.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
            }//写文件IO异常，catch不处理
        }
    }
}
