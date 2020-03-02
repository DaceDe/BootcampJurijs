package lv.accenture.bootcamp.spring;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("smsService")
public class smsService implements NotificationChannel{

	@Value("${notification.sms.operator}")
	private String operator;

	public void notifyUser(User user){
		System.out.println(user.getFullName()+ " is notified by operator " + operator);
		
		

}
}