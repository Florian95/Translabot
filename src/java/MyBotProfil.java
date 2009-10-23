/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.google.wave.api.ProfileServlet;

/**
 *
 * @author macbookpro
 */
public class MyBotProfil extends ProfileServlet {
   
    //Avatar
        @Override
        public String getRobotAvatarUrl() {
                return "http://translabot.appspot.com/img/pic.png";
        }

        //Name
        @Override
        public String getRobotName() {
                return "Translabot";
        }

        //URL
        @Override
        public String getRobotProfilePageUrl() {
                return "http://translabot.appspot.com/";
        }

}
