package com.seeyon.v3x.dee.common.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AuthorizeUtil {
	private static String SERIALNUMBER = "";

	/**
	 * 获得主板信息.
	 * 
	 * @return 返回主板信息
	 */
	private static String getH() {
		try {
			return getB2();
			// + getN();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private static String getB() throws Exception {
		String procCmd = "cmd /C " + System.getenv("windir")
				+ "//system32//wbem//wmic.exe BASEBOARD get SerialNumber";
		// 利用指定的操作系统程序和参数构造一个进程生成器
		Process process = new ProcessBuilder(procCmd.split(" ")).start();
		// 错误的信息
		StreamGobbler errorGobbler = new StreamGobbler(
				process.getErrorStream(), "ERROR");
		// 正确的信息
		StreamGobbler outputGobbler = new StreamGobbler(
				process.getInputStream(), "OUTPUT");
		// 启动线程
		errorGobbler.start();
		outputGobbler.start();
		// 关闭进程输出流
		process.getOutputStream().close();
		// 等待该进程执行完毕
		int exitVal = process.waitFor();
//		System.out.println("ExitValue: " + exitVal);
		return outputGobbler.getSerialNumber();
	}
	
	private static String getB2() {
        String result = "";
        FileWriter fw = null;
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            fw = new java.io.FileWriter(file);

            String vbs =
                    "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                            + "Set colItems = objWMIService.ExecQuery _ \n"
                            + "   (\"Select * from Win32_BaseBoard\") \n"
                            + "For Each objItem in colItems \n"
                            + "    Wscript.Echo objItem.SerialNumber \n"
                            + "    exit for  ' do the first cpu only! \n"
                            + "Next \n";

            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.trim();
    }

	private static String getN() throws UnknownHostException, SocketException {
		// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
		InetAddress ia = InetAddress.getLocalHost();// 获取本地IP对象
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		// 把mac地址拼装成String
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}
		return (sb.toString().toUpperCase());
	}

	public static String getK(String s, String t) {
		MD5 md5 = new MD5();
		return md5.getMD5ofStr(getH() + s + t);
	}

	public static String getS(String k, String licenseNum) {
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 3, 1);
		cal.add(1, 3);
		cal.add(6, -1);
		NumberFormat nf = new DecimalFormat("000");
		licenseNum = nf.format(Integer.valueOf(licenseNum));
		String verTime = (new StringBuilder("-"))
				.append((new SimpleDateFormat("yyMMdd")).format(cal.getTime()))
				.append("0").toString();
		String type = "YE3MP-";
		String need = (new StringBuilder(k.substring(0, 1))).append(type)
				.append("300").append(licenseNum).append(verTime).toString();
		String dx = (new StringBuilder(need)).append("yourself.").append(k)
				.toString();
		int suf = decode(dx);
		String code = (new StringBuilder(need)).append(String.valueOf(suf))
				.toString();
		return change(code);
	}

	private static int decode(String s) {
		int i = 0;
		char ac[] = s.toCharArray();
		int j = 0;
		for (int k = ac.length; j < k; j++)
			i = 31 * i + ac[j];
		return Math.abs(i);
	}

	private static String change(String s) {
		byte abyte0[] = s.getBytes();
		char ac[] = new char[s.length()];
		int i = 0;
		for (int k = abyte0.length; i < k; i++) {
			int j = abyte0[i];
			if (j >= 48 && j <= 57)
				j = ((j - 48) + 5) % 10 + 48;
			else if (j >= 65 && j <= 90)
				j = ((j - 65) + 13) % 26 + 65;
			else if (j >= 97 && j <= 122)
				j = ((j - 97) + 13) % 26 + 97;
			ac[i] = (char) j;
		}
		return String.valueOf(ac);
	}

	public static void main(String[] str) {
		try {
			System.out.println(getB());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String key = getK("?f.p?g?d?l>g(u?l`_/y", "?/--S_N}[#;:U(IT");
		// System.out.println(key);
		// System.out.println(getS("565B6EF95089294CE925906FE15B7B55", "250"));
		// try {
		// Enumeration<NetworkInterface> netEnums = NetworkInterface
		// .getNetworkInterfaces();
		// while (netEnums.hasMoreElements()) {
		// NetworkInterface net = netEnums.nextElement();
		// byte[] macAddress = net.getHardwareAddress();
		// if (!net.isVirtual() && macAddress != null
		// && macAddress.length > 0) {
		// StringBuffer sb = new StringBuffer();
		// for (int i = 0; i < macAddress.length; i++) {
		// if (i != 0) {
		// sb.append("-");
		// }
		// // mac[i] & 0xFF 是为了把byte转化为正整数
		// String s = Integer.toHexString(macAddress[i] & 0xFF);
		// sb.append(s.length() == 1 ? 0 + s : s);
		// }
		// System.out.println(net.getDisplayName() + ":"
		// + sb.toString().toUpperCase());
		// List<InterfaceAddress> addresses = net
		// .getInterfaceAddresses();
		// for (InterfaceAddress interfaceAddress : addresses) {
		// System.out.println(interfaceAddress.getAddress());
		// }
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}
}

class StreamGobbler extends Thread {
	private InputStream is;
	private String serialNumber;

	StreamGobbler(InputStream is, String type) {
		this.is = is;
	}

	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				// System.out.println(type + ">" + line);
				if (!line.trim().equals("") && !"SerialNumber".equals(line)) {
					this.serialNumber = line.trim();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}
}
