/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyWebSpider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author mazhenhao
 */
public class MyHtmlParser {
    private String urltobepar;
    public MyHtmlParser(String url)
    {
        urltobepar=url;
    }
    
    public  byte[] GetHtmlText() throws IOException {
			URL url = new URL(urltobepar);
			HttpURLConnection conn = 
                                (HttpURLConnection)url.openConnection();
			InputStream inputStream = conn.getInputStream();            //通过输入流获得网站数据
			byte[] getData = readInputStream(inputStream);              //获得网站的二进制数据
			//return new String(getData);
                        return getData;
		}

    private  byte[] readInputStream(InputStream inputStream) 
            throws IOException {
			byte[] buffer = new byte[1024];
			int len = 0;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while((len = inputStream.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			
			bos.close();
			return bos.toByteArray();
		}
   
}


