/*
 * This class is used to create a modified Bot Profile to be used for MyBot, with custom (overriden) methods 
 */


import com.google.wave.api.ProfileServlet;


public class MyBotProfil extends ProfileServlet {
   
    /** 
     * Gets the Avatar URL from the translabot website
     * @throws NotFoundException if URL is outdated.
     */
	@Override
	public String getRobotAvatarUrl() {
			return "http://translabot.appspot.com/img/pic.png";
	}

    /** 
     * Gets the Robot name
     */
	@Override
	public String getRobotName() {
			return "Translabot";
	}

    /** 
     * Gets the Robot profile page URL
     * @throws NotFoundException if URL is outdated.
     */
	@Override
	public String getRobotProfilePageUrl() {
			return "http://translabot.appspot.com/";
	}

}
