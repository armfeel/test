package upload.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这是个配置文件操作类，用来读取和设置ini配置文件
 * 
 * @author cici
 * @da2011-2-18 @t上午11:36:45
 */
public final class IniUtil
{
	// private static final Logger logger =
	// Logger.getLogger(ConfigIniUtil.class);

	/** */
	/**
	 * 从ini配置文件中读取变量的�?
	 * 
	 * @param file
	 *            配置文件的路�?
	 * @param section
	 *            要获取的变量�?��段名�?
	 * @param variable
	 *            要获取的变量名称
	 * @param defaultValue
	 *            变量名称不存在时的默认�?
	 * @return 变量的�?
	 * @throws IOException
	 *             抛出文件操作可能出现的io异常
	 */
	public static String getProfileString(String file, String section,
			String variable, String defaultValue) throws IOException
	{
		// 参数校验
		if (file == null || "".equals(file))
		{
			return defaultValue;
		}

		if (section == null || "".equals(section))
		{
			return defaultValue;
		}

		if (variable == null || "".equals(variable))
		{
			return defaultValue;
		}

		String strLine, value = "";
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), "UTF-8"));
		boolean isInSection = false;
		Scanner scanner = new Scanner(bufferedReader);

		try
		{
			while (scanner.hasNext())
			{
				strLine = scanner.nextLine();
				strLine = strLine.trim();
				strLine = strLine.split("[;]")[0];
				Pattern p;
				Matcher m;
				p = Pattern.compile("\\[\\s*.*\\s*\\]");
				m = p.matcher((strLine));
				if (m.matches())
				{
					p = Pattern.compile("\\[\\s*" + section + "\\s*\\]");
					m = p.matcher(strLine);
					if (m.matches())
					{
						isInSection = true;
					} else
					{
						isInSection = false;
					}
				}
				if (isInSection == true)
				{
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					if (strArray.length == 1)
					{
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variable))
						{
							value = "";
							return value;
						}
					} else if (strArray.length == 2)
					{
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variable))
						{
							value = strLine.substring(strLine.indexOf("=") + 1)
									.trim();
							return value;
						}
					} else if (strArray.length > 2)
					{
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variable))
						{
							value = strLine.substring(strLine.indexOf("=") + 1)
									.trim();
							return value;
						}
					}
				}
			}
		} finally
		{
			bufferedReader.close();
		}
		return defaultValue;
	}

	/**
	 * @param file
	 * @param section
	 * @return
	 * @throws IOException
	 */
	public static ConcurrentHashMap<String, String> getProfileStrings(
			String file, String section) throws Exception
	{
		// 参数校验
		if (file == null || "".equals(file))
		{
			return null;
		}

		if (section == null || "".equals(section))
		{
			return null;
		}

		ConcurrentHashMap<String, String> results = new ConcurrentHashMap<String, String>();
		String strLine = "";
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), "UTF-8"));
		boolean isInSection = false;
		boolean isBreak = false;
		Scanner scanner = new Scanner(bufferedReader);

		try
		{
			while (scanner.hasNext())
			{
				strLine = scanner.nextLine();
				strLine = strLine.trim();
				strLine = strLine.split("[;]")[0];
				Pattern p;
				Matcher m;
				p = Pattern.compile("\\[\\s*.*\\s*\\]");
				m = p.matcher((strLine));
				if (m.matches())
				{
					if (isBreak)
					{
						break;
					}

					p = Pattern.compile("\\[\\s*" + section + "\\s*\\]");
					m = p.matcher(strLine);
					if (m.matches())
					{
						isInSection = true;
						isBreak = true;
					} else
					{
						isInSection = false;
						isBreak = false;
					}
				}
				if (isInSection == true)
				{
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");

					if (strArray.length == 1)
					{
						results.put(strArray[0].trim(), "");
					} else if (strArray.length == 2)
					{
						results.put(strArray[0].trim(),
								strLine.substring(strLine.indexOf("=") + 1)
										.trim());
					} else if (strArray.length > 2)
					{
						results.put(strArray[0].trim(),
								strLine.substring(strLine.indexOf("=") + 1)
										.trim());
					}
				}
			}
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			bufferedReader.close();
		}
		return results;
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getAllProfileStrings(
			String file) throws Exception
	{
		// 参数校验
		if (file == null || "".equals(file))
		{
			return null;
		}

		ConcurrentHashMap<String, ConcurrentHashMap<String, String>> results = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();
		String section = "", strLine = "";
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), "UTF-8"));
		Scanner scanner = new Scanner(bufferedReader);

		try
		{
			while (scanner.hasNext())
			{
				strLine = scanner.nextLine();
				// logger.error("@@@@@@@@@@@@@@@" + strLine);
				// logger.error("@@@@@@@@@@@@@@@-------------------------"
				// + new String(strLine.getBytes(), "utf-8"));
				strLine = strLine.trim();
				strLine = strLine.split("[;]")[0];
				Pattern p;
				Matcher m;
				p = Pattern.compile("\\[\\s*.*\\s*\\]");
				m = p.matcher((strLine));
				if (m.matches())
				{
					section = strLine.substring(1, strLine.length() - 1);
					ConcurrentHashMap<String, String> temp = new ConcurrentHashMap<String, String>();
					results.put(section, temp);

				} else
				{
					strLine = strLine.trim();
					if (strLine == null || "".equalsIgnoreCase(strLine))
					{
						continue;
					}
					String[] strArray = strLine.split("=");

					if (strArray.length == 1)
					{
						results.get(section).put(strArray[0].trim(), "");
					} else if (strArray.length == 2)
					{
						results.get(section).put(
								strArray[0].trim(),
								strLine.substring(strLine.indexOf("=") + 1)
										.trim());
					} else if (strArray.length > 2)
					{
						results.get(section).put(
								strArray[0].trim(),
								strLine.substring(strLine.indexOf("=") + 1)
										.trim());
					}
				}
			}
		} catch (Exception e)
		{
			throw e;

		} finally
		{
			bufferedReader.close();
		}
		return results;
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getSections(String file) throws IOException
	{
		// 参数校验
		if (file == null || "".equals(file))
		{
			return null;
		}

		Set<String> results = new CopyOnWriteArraySet<String>();

		String strLine = "";
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), "UTF-8"));
		Scanner scanner = new Scanner(bufferedReader);

		try
		{
			while (scanner.hasNext())
			{
				strLine = scanner.nextLine();
				strLine = strLine.trim();
				strLine = strLine.split("[;]")[0];
				Pattern p;
				Matcher m;
				p = Pattern.compile("\\[\\s*.*\\s*\\]");
				m = p.matcher((strLine));
				if (m.matches())
				{
					results.add(strLine.substring(1, strLine.length() - 1));
				}

			}
		} finally
		{
			bufferedReader.close();
		}
		return results;
	}

	/** */
	/**
	 * 修改ini配置文件中变量的�?
	 * 
	 * @param file
	 *            配置文件的路�?
	 * @param section
	 *            要修改的变量�?��段名�?
	 * @param variable
	 *            要修改的变量名称
	 * @param value
	 *            变量的新�?
	 * @throws IOException
	 *             抛出文件操作可能出现的io异常
	 */
	public static boolean setProfileString(String file, String section,
			String variable, String value) throws IOException
	{
		// 参数校验
		if (file == null || "".equals(file))
		{
			return false;
		}

		if (section == null || "".equals(section))
		{
			return false;
		}

		if (variable == null || "".equals(variable))
		{
			return false;
		}

		String fileContent, allLine, strLine, newLine, remarkStr;
		String getValue;
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), "utf-8"));

		boolean isInSection = false;
		fileContent = "";
		try
		{

			while ((allLine = bufferedReader.readLine()) != null)
			{
				allLine = allLine.trim();
				if (allLine.split("[;]").length > 1)
					remarkStr = ";" + allLine.split(";")[1];
				else
					remarkStr = "";
				strLine = allLine.split(";")[0];
				Pattern p;
				Matcher m;
				p = Pattern.compile("\\[\\s*.*\\s*\\]");
				m = p.matcher((strLine));
				if (m.matches())
				{
					p = Pattern.compile("\\[\\s*" + section + "\\s*\\]");
					m = p.matcher(strLine);
					if (m.matches())
					{
						isInSection = true;
					} else
					{
						isInSection = false;
					}
				}
				if (isInSection == true)
				{
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					getValue = strArray[0].trim();
					if (getValue.equalsIgnoreCase(variable))
					{
						newLine = getValue + "=" + value + remarkStr;
						fileContent += newLine + "\r\n";
						while ((allLine = bufferedReader.readLine()) != null)
						{
							fileContent += allLine + "\r\n";
						}
						bufferedReader.close();
						// BufferedWriter bufferedWriter = new BufferedWriter(
						// new FileWriter(file, false));

						BufferedWriter bufferedWriter = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(
										file), "utf-8"));

						bufferedWriter.write(fileContent);
						bufferedWriter.flush();
						bufferedWriter.close();

						return true;
					}
				}
				fileContent += allLine + "\r\n";
			}
		} catch (IOException ex)
		{
			throw ex;
		} finally
		{
			bufferedReader.close();
		}
		return false;
	}

	/**
	 * @param relativePath
	 * @param filename
	 * @return
	 */
	public static String getFullFilename(String relativePath, String filename)
	{
		String filename1 = null;
		String filename2 = null;
		String filename3 = null;
		String filename4 = null;

		String classesPath = IniUtil.class.getResource("/").toString();
		String webinfoPath = classesPath.substring(0, classesPath.length() - 9);
		webinfoPath = webinfoPath.replaceAll("file:/", "");

		String os = System.getProperty("file.separator");
		if (os.equals("/"))
			webinfoPath = "/" + webinfoPath;

		if (relativePath != null && !"".equals(relativePath))
		{
			filename1 = webinfoPath + File.separator + relativePath
					+ File.separator + filename;
		} else
		{
			filename1 = webinfoPath + File.separator + filename;
		}

		if (relativePath != null && !"".equals(relativePath))
		{
			filename2 = System.getProperty("user.dir") + File.separator
					+ relativePath + File.separator + filename;
		} else
		{
			filename2 = System.getProperty("user.dir") + File.separator
					+ filename;
		}

		filename3 = System.getProperty("user.dir") + File.separator + filename;

		filename4 = System.getProperty("user.dir") + File.separator
				+ "WebContent" + File.separator + "WEB-INF" + File.separator
				+ relativePath + File.separator + filename;

		String realFilename = filename;

		if (new File(filename1).exists())
		{
			realFilename = filename1;
		} else if (new File(filename2).exists())
		{
			realFilename = filename2;
		} else if (new File(filename3).exists())
		{
			realFilename = filename3;
		} else if (new File(filename4).exists())
		{
			realFilename = filename4;
		} else
		{
			realFilename = filename;
		}

		return realFilename;
	}
}
