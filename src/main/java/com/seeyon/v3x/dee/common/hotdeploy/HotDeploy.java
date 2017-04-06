
package com.seeyon.v3x.dee.common.hotdeploy;

import com.seeyon.v3x.dee.DEEConstants;
import com.seeyon.v3x.dee.DirectoryWatcher;
import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.TransformFactory;
import com.seeyon.v3x.dee.common.db2cfg.GenerationCfgUtil;
import com.seeyon.v3x.dee.common.db2cfg.constant.GlobalConstant;
import com.seeyon.v3x.dee.common.importflow.ImportFlow;
import com.seeyon.v3x.dee.context.EngineController;
import com.seeyon.v3x.dee.enumerate.EnumerateConfig;
import com.seeyon.v3x.dee.schedule.QuartzManager;
import com.seeyon.v3x.dee.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;


public class HotDeploy extends DirectoryWatcher {
    private static Log log = LogFactory.getLog(HotDeploy.class);
	protected Map<String, String> digestMap;
	private ImportFlow imp = new ImportFlow();
	private GenerationCfgUtil deploy = GenerationCfgUtil.getInstance();
	private static boolean isInA8 = false;
	private static final String A8_HOME = "A8_HOME";
	
	public HotDeploy(String directoryToWatch, Pattern pattern) {
		this(new File(directoryToWatch), pattern);
	}

	public HotDeploy(File directoryToWatch, Pattern pattern) {
		super(directoryToWatch, pattern);
		digestMap = FileDigest.getDirDigest(directoryToWatch, false,
				FileDigest.ALGORITHMS_MD5);
		try {
			init();
		} catch (Throwable e) {
			log.error(e);
		}
	}

	private void init() throws Throwable {
		// TODO 导入所有资源包
		File[] files = super._directoryToWatch.listFiles(this);
		
		for (File file : files) {
			if(file == null) continue;
			log.debug("加载资源包：" + file.getPath());
			try{
				imp.doWriter(readFile(file));
				file.delete();
			}
			catch(Throwable e){
				log.error("加载资源包【"+file.getName()+"】异常："+e);
			}
		}
		deploy.generationMainFile(GenerationCfgUtil.getDEEHome());
		QuartzManager.getInstance().refresh();
		/*EngineController.getInstance(null).refreshContext();*/
//		DEEClient client = new DEEClient();
//		client.refreshContext();
		EngineController.getInstance(null).refreshContext();
	}

	@Override
	protected File processFile(File file) throws Exception {
		String fileMD5 = FileDigest.getFileDigest(file,
				FileDigest.ALGORITHMS_MD5);
		String mapMD5 = digestMap.get(file.getPath());
		if (mapMD5 == null || !fileMD5.equals(mapMD5)) {
			String data = readFile(file);
			if (data == null)
				throw new TransformException("读取资源包" + file.getPath() + "失败！");
			imp.doWriter(data);
			digestMap.put(file.getPath(), fileMD5);
			file.delete();
		}
		return file;
	}

	@Override
	protected boolean preProcess(File directoryToWatch) {
		File[] files = _directoryToWatch.listFiles(this);
		for (File file : files) {
			String fileMD5 = FileDigest.getFileDigest(file,
					FileDigest.ALGORITHMS_MD5);
			String mapMD5 = digestMap.get(file.getPath());
			if (mapMD5 == null || !fileMD5.equals(mapMD5))
				return true;
		}
		return false;
	}

	@Override
	protected void postProcess() {
		try {
			deploy.generationMainFile(GenerationCfgUtil.getDEEHome());
			QuartzManager.getInstance().refresh();
			EngineController.getInstance(null).refreshContext();
//			DEEClient client = new DEEClient();
//			client.refreshContext();
			EnumerateConfig ec = EnumerateConfig.getInstance();
			Properties propertie = ec.getPropertie();
			propertie = new Properties();
			try {
				FileInputStream inputFile = new FileInputStream(TransformFactory.getInstance()
						.getConfigFilePath("dictionary.properties"));
				propertie.load(new InputStreamReader(inputFile, DEEConstants.CHARSET_UTF8));
				inputFile.close();
			} catch (FileNotFoundException ex) {
				log.error("File read fail!"+ex.getMessage(),ex);
			} catch (IOException ex) {
				log.error("Load file error!"+ex.getMessage(),ex);
			}
			ec.setPropertie(propertie);
		} catch (Throwable e) {
			log.error(e);
		}
	}

	private String readFile(File file) throws Exception {
	    String tmpDirectory = GenerationCfgUtil.getDEEHome() + File.separator + "temp";
        String deeDrpPath = file.getAbsolutePath();
        String deeNewPath = tmpDirectory + "/dee.drp";
        DesUtil desUtil = new DesUtil("drp_encrypt");
        //判断是否为A8环境
	    isInA8 = FileUtil.isA8Home();
	    //如果为A8环境直接进行解密后导入
	    if (isInA8) {
	        try {
	            desUtil.decryptFile(deeDrpPath, deeNewPath);
	            ZipUtil.unZip(new File(deeNewPath), tmpDirectory);
	        } catch (Exception e) {
	            log.error("解密异常:" + e.getLocalizedMessage(), e);
	        } finally{
	            DeleteFolder(deeNewPath);
	        }
	    } else {
    	    //如果为dee环境
    	    ZipUtil.unZip(file, tmpDirectory);//尝试解压
    		if (!(new File(tmpDirectory + "/dee.xml")).exists()) {//解压失败则对该文件进行解密后解压
    		    try {
    		        desUtil.decryptFile(deeDrpPath, deeNewPath);
    		        ZipUtil.unZip(new File(deeNewPath), tmpDirectory);
    		    } catch (Exception e){
    		        log.error("解密异常:" + e.getLocalizedMessage(), e);
    		    } finally {
    		        DeleteFolder(deeNewPath);
    		    }
    		}
	    }
		File dataFile = new File(tmpDirectory + "/dee.xml");
		File propertyFile = new File(tmpDirectory + "/dee-resource.properties");
		if (dataFile.exists()){
			FileInputStream fis = new FileInputStream(dataFile);
			byte[] b = new byte[fis.available()];// 创建一个字节数组，数组长度与file中获得的字节数相等
			while (fis.read(b) != -1) {
				log.debug("读取资源包内容：" + new String(b));// 打印出从file文件读取的内容
			}
			fis.close();
			dataFile.delete();

			// 加载资源包中的properties合并到系统properties中
			if (propertyFile.exists()) {
				Properties pro = new Properties();
				FileInputStream tempProFis = new FileInputStream(propertyFile);
				InputStreamReader inputStreamReader = new InputStreamReader(
						tempProFis, GlobalConstant.CHARSET);

				pro.load(inputStreamReader);

				File deepropFile = new File(TransformFactory.getInstance()
						.getConfigFilePath("dee-resource.properties"));
				if (!deepropFile.exists()) {
					deepropFile.createNewFile();
				}
				FileInputStream deepropertyFis = new FileInputStream(
						deepropFile);
				inputStreamReader = new InputStreamReader(deepropertyFis,
						GlobalConstant.CHARSET);
				pro.load(inputStreamReader);

				DataChangeUtil.storeProperties(pro,deepropFile.getPath());
//				FileOutputStream deeproperytFos = new FileOutputStream(
//						deepropFile);
//				OutputStreamWriter writer = new OutputStreamWriter(
//						deeproperytFos, GlobalConstant.CHARSET);
//				pro.store(writer, null);

				tempProFis.close();
				deepropertyFis.close();
//				deeproperytFos.close();
				propertyFile.delete();
			}
			return new String(b);
		} else {
			return null;
		}
	    
	}
	
	//文件导出后调用方法删除加密后的本地文件
    /** 
     *  根据路径删除指定的目录或文件，无论存在与否 
     *@param sPath  要删除的目录或文件 
     *@return 删除成功返回 true，否则返回 false。 
     */  
    private boolean DeleteFolder(String sPath) {
        boolean flag = false;  
        File file = new File(sPath);
        // 判断目录或文件是否存在  
        if (!file.exists()) {  // 不存在返回 false  
            return flag;  
        } else {  
            // 判断是否为文件  
            if (file.isFile()) {  // 为文件时调用删除文件方法  
                return deleteFile(sPath);  
            } else {  // 为目录时调用删除目录方法  
                return deleteDirectory(sPath);  
            }  
        }  
    }  
    
    
    
    //删除文件夹的方法
    /** 
     * 删除目录（文件夹）以及目录下的文件 
     * @param   sPath 被删除目录的文件路径 
     * @return  目录删除成功返回true，否则返回false 
     */  
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }  
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {  
            return false;  
        }  
        boolean flag = true;  
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {  
            //删除子文件  
            if (files[i].isFile()) {  
                flag = deleteFile(files[i].getAbsolutePath());  
                if (!flag) break;  
            } //删除子目录  
            else {  
                flag = deleteDirectory(files[i].getAbsolutePath());  
                if (!flag) break;  
            }  
        }  
        if (!flag) return false;  
        //删除当前目录  
        if (dirFile.delete()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
    
    //删除文件的方法
    /** 
     * 删除单个文件 
     * @param   sPath    被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    private boolean deleteFile(String sPath) {
        boolean flag = false;  
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {  
            file.delete();  
            flag = true;  
        }  
        return flag;  
    }  
}
