package August_24;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tarena on 2016/8/26.
 */
public class TestGet{
    public static void main(String[] args) {
            new ReadBy().start();
    }
   static class ReadBy extends Thread{
        @Override
        public void run() {
            try {

                URL url=new URL("http://www.4399.com/flash/159974.htm");
                URLConnection connection =url.openConnection();
                InputStream is=connection.getInputStream();
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);

                String line;
                StringBuilder builder=new StringBuilder();
                while((line=br.readLine())!=null){
                    System.out.println(line.toString());
                }
                br.close();
                isr.close();
                is.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
