package cn.futures.data.importor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SendMail {
	
	private Properties props=new Properties();//发送邮件的props文件
	private Session session = null;//邮件session
	private MimeMessage message = null;
	private String fromUser = "chengtm2011@126.com";//发件人
	private String toUser = "852246648@qq.com";//收件人
	private String subject = "logger";//主题
	private static List<File> attachment = new ArrayList<File>();//附件
	static{
		attachment.add(new File("D:\\crawlers\\Logs\\priceDownLoad\\logs\\priceDownLoad.log"));
		attachment.add(new File("D:\\Crawlers\\Logs\\crawlnew\\server.log"));
	}
	
	public void init(){
		 props.put("mail.smtp.host","smtp.126.com");//发件人使用发邮件的电子信箱服务器我使用的是163的服务器
	     props.put("mail.smtp.auth","true"); //这样才能通过验证
	     session=Session.getInstance(props);
	     session.setDebug(true);
	     message=new MimeMessage(session);
	}
	
//	@Scheduled
//	(cron=CrawlScheduler.CRON_SEND_MAIL)
	public void sendMailByTime(){
		init();
        try{
	        //给消息对象设置发件人/收件人/主题/发信时间
	        InternetAddress from=new InternetAddress(fromUser);  //发邮件的出发地（发件人的信箱），这是我的邮箱地址，使用请改成你的有效地址
	        message.setFrom(from);
	        InternetAddress to=new InternetAddress(toUser);// tto为发邮件的目的地（收件人信箱）
	
	        message.setRecipient(Message.RecipientType.TO,to);
	        message.setSubject(subject);// ttitle为邮件的标题
	        message.setSentDate(new Date());
	        Multipart multipart=new MimeMultipart();//新建一个MimeMultipart对象用来存放BodyPart对象(事实上可以存放多个)
	        // 添加附件的内容
            if (attachment.size() > 0){
            	for(File file:attachment){
            		if(file.exists()){
            			BodyPart attachmentBodyPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(file);
                        attachmentBodyPart.setDataHandler(new DataHandler(source));
                        attachmentBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
                        multipart.addBodyPart(attachmentBodyPart);
            		}
            	}
            }
	        message.setContent(multipart);//把multipart作为消息对象的内容
	        message.saveChanges();
	        Transport transport=session.getTransport("smtp");
	        transport.connect("smtp.126.com","chengtm2011","18709836273");//发邮件人帐户密码,此外是我的帐户密码，使用时请修改。
	        transport.sendMessage(message,message.getAllRecipients());
	        transport.close();
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	public static void main(String[] args){
		new SendMail().sendMailByTime();
	}
}
