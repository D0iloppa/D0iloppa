package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


public class ProcProperties {
	static ConcurrentHashMap<String, Properties> propertiesMap = new ConcurrentHashMap<String, Properties>();
	
	static String fileName = ""; 
//			(ProcProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(
//							0
//							, ProcProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath().lastIndexOf("/"))
//							+File.separator
//							+ "conf/console.properties."+LogManagerLauncher.consolePropNum).replaceAll("%20", " ").replaceAll("/bin", "");
	
	// 파일명을 지정해주는 함수
	public static void setFileName(String num){ // 파일명은 num으로 지정해준다.
		// conf라는 디렉토리상에서
		// console.properties.라는 이름을 가진 num 파일을 대상으로 한다.
		fileName = ProcProperties.getPropertiesFilePath() + "/" + "conf/console.properties." + num;
	}
	
	public static void reload(){
		synchronized (propertiesMap) {
			propertiesMap = new ConcurrentHashMap<String, Properties>();
		}
		
	}
	
	/**
	 * properties �뙆�씪濡쒕��꽣 媛곸쥌 �젙蹂대�� �씫�뒗�떎.<br>
	 * 
	 * @param fileName		property �뙆�씪 �씠由�
	 * @param key			�씫�뼱�삱 key
	 * @return				二쇱뼱吏� key �빐�떦�븯�뒗 value
	 */
	public static String readProperty(String key) {
		// 키값에 의해 설정값을 가져오는 함수
		
		Properties properties = propertiesMap.get(fileName);
		
		if(properties == null){
			synchronized (propertiesMap) {
				properties = propertiesMap.get(fileName);
				if(properties == null){

					// Read properties file.
					try {
						properties = readProperties();
						propertiesMap.put(fileName,properties); 
					} catch (IOException e) {
						throw new IllegalStateException("Could not read properties file " + fileName + 
								" : " + e.getMessage(), e);
					}
				}
			}
		}
		
		String property = properties.getProperty(key);
		return property == null ? null : property.trim(); //프로퍼티가 null이 아니라면 trim(공백제거)해서 넘겨준다.
	}
	
	public static String readSQL(String key){
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			while(( line = br.readLine()) != null){
				if(line.equals(key)){
					while(( line = br.readLine()) != null){
						if(line.equals("[SQL END]")){
							break;
						}
						else{
							sb.append(line+"\n");
						}
					}
				}
			}
			br.close();
		}catch (Exception e) {
			throw new IllegalStateException("Could not read properties file " + fileName + 
					" : " + e.getMessage(), e);
		}
		
		
		return sb.toString();
	}
	
	// 
	public static Properties readProperties() throws IOException {
		
		// 해쉬테이블의 하위 클래스 Properties
		// 파일 입출력을 지원하는 해쉬맵이라고 보면된다.
		// key = value 형식으로 매핑
		
		Properties result = null;
		
		synchronized (propertiesMap) {
			result = propertiesMap.get(fileName);
			
			if(result != null)
				return result; 

				FileInputStream fis = null;	
				try { // 파일인풋스트림을 통해 파일을 불러옴
					fis = new FileInputStream(fileName);
					result = new Properties();
					result.load(fis);
				} finally {
					if(fis != null)
						fis.close();		
				}
				
				// 불러온 파일을 맵에 연결
				propertiesMap.put(fileName, result);
		}
		return result;
	}
	
	public static String getPropertiesFilePath() {
		String str = null;

		try {
			str = new File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return str;
	}
}
