package in.grasshoper.core.infra.email.template;

import static in.grasshoper.core.GrassHoperMainConstants.DefaultSiteName;

public class EmailTemplates {
	public static String activateUserEmailSubject(){
		return "Welcome to "+DefaultSiteName;
	}
	public static String activateUserEmailTemplate(final String toName, final String verificationLink){
		StringBuffer tmpl = new StringBuffer()
			.append("<font face='verdana'>Dear ").append(toName)
			.append(",<br/><br/><table border = '0' cellpadding='0' cellspacing='0' bgcolor='#FFFFFF'><tr>")
			.append("<td align='center'><h3>Welcome to ")
			.append(DefaultSiteName)
			.append("</h3> </td></tr><tr><td> </td></tr>")
			.append("<tr><td align='center' style ='height: 100px;'> ")
			.append("<a style='background-color:#73AD21;padding: 16px 100px;color: white;text-decoration: none;' href='")
			.append(verificationLink)
			.append("'><b> Confirm Email</b> </a> </td></tr>")
			.append("<tr><td> </td></tr>")
			.append("<tr><td align='left'>Thank you for chosing ")
			.append(DefaultSiteName)
			.append(", your account is awaiting email verification<br/> ")
			.append("Kindly verify your email by clicking on the above link <br/>")
			.append("or by pasting the below given url to a new browser tab.<br/><br/><br/>")
			.append(verificationLink)
			.append("</td></tr>")
			.append("</table><br/><br/>")
			.append("Thanks and Regards, <br/><br/>")
			.append("The ")
			.append(DefaultSiteName).append(" Team<br/><br/><br/>")
			.append("Note: This is an auto-generated mail. Please do not reply.</font>");
			
		return tmpl.toString();
	}
	
	public static String newOrderNotiSubject(){
		return "New Order";
	}
	
	public static String newOrderNotiEmailTemplate(){
		StringBuffer tmpl = new StringBuffer()
			.append("<font face='verdana'>Dear ").append("Admin")
			.append(",<br/><br/><table border = '0' cellpadding='0' cellspacing='0' bgcolor='#FFFFFF'><tr>")
			.append("<td align='center'><h3>You have a New Order ")
			.append("</h3> </td></tr><tr><td> </td></tr>")
			.append("<tr><td align='center' > ")
			.append("</td></tr>")
			.append("<tr><td> </td></tr>")
			.append("<tr><td align='left'> ")
			.append("The Order is waiting for your Action.<br>Kindly visit the Dash board and take necesaary actions.<br/> ")
			.append("</td></tr>")
			.append("</table><br/><br/>")
			.append("Thanks and Regards, <br/><br/>")
			.append("")
			.append(DefaultSiteName).append("<br/><br/><br/>")
			.append("Note: This is an auto-generated mail. Please do not reply.</font>");
			
		return tmpl.toString();
	}
	
	public static String orderStatusUpdateSubject(){
		return "Order Status Updated";
	}
	public static String orderStatusUpdateEmailTemplate(final String toName, final String status){
		StringBuffer tmpl = new StringBuffer()
			.append("<font face='verdana'>Dear ").append(toName)
			
			.append(",<br/><br/><table border = '0' cellpadding='0' cellspacing='0' bgcolor='#FFFFFF'><tr>")
			.append("<td align='center'><h3>Your Order status is changed, please find below the updates: ")
			.append("</h3> </td></tr><tr><td> </td></tr>")
			.append("<tr><td align='center' > ")
			.append("</td></tr>")
			.append("<tr><td> </td></tr>")
			.append("<tr><td align='left'> ")
			.append(status)
			.append("</td></tr>")
			.append("<tr><td> </td></tr>")
			.append("<tr><td>Order details</td></tr>")
			.append("<tr><td> </td></tr>")
			.append("</table><br/><br/>")
			.append("Thanks and Regards, <br/><br/>")
			.append("The ")
			.append(DefaultSiteName).append(" Team<br/><br/><br/>")
			.append("Note: This is an auto-generated mail. Please do not reply.</font>");
			
			
			return tmpl.toString();
	}
	
}
