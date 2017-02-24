/*
 * Bot Object that handles callback events and writes the response of the bot  
 */

import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.google.wave.api.*;
import db.PMF;
import db.entities.WaveDbo;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class MyBot extends AbstractRobotServlet {

    public static PersistenceManager pm = null;
    private WaveDbo waveObj;
    private Language langToTranslate;
    private static final Logger log = Logger.getLogger(MyBot.class.getName());

    /** 
     * Processes events whenever a change occurs
     * @param bundle of object RobotMessageBundle
     * @returntype void
     */
    @Override
    public void processEvents(RobotMessageBundle bundle) {

        Wavelet wavelet = bundle.getWavelet();

        if (pm == null) {
            pm = PMF.get().getPersistenceManager();
        }
		
		// create new gadget object from online config file if bundle was self added
        if (bundle.wasSelfAdded()) {
            Gadget gadget = new Gadget("http://translabot.appspot.com/gadget/config.xml");
            gadget.setField("wid", wavelet.getWaveId());
            TextView text = wavelet.appendBlip().getDocument();
            text.getGadgetView().append(gadget);
        }

		// iterate through each event. If a response is required, add it as a new Blip object
        for (Event e : bundle.getEvents()) {

            if (e.getType() == EventType.BLIP_SUBMITTED) {

                Blip b = e.getBlip();
                Query query = pm.newQuery(WaveDbo.class);
                query.setFilter("waveId == waveidParam");
                query.declareParameters("String waveidParam");

                try { // guard against empty lists
                    List<WaveDbo> results = (List<WaveDbo>) query.execute(wavelet.getWaveId().replace("+", " "));
                    waveObj = results.get(0);
                } finally { query.closeAll(); }
				
				// if a response is required, it is if the following condition is met
                if (waveObj != null) {

                    Translate.setHttpReferrer("http://translabot.appspot.com/");
                    String translatedText = null;

                    try { // guard against uncaught exception
                        langToTranslate = Language.fromString(waveObj.getLanguage());
                        translatedText = Translate.execute(b.getDocument().getText(), Language.AUTO_DETECT, langToTranslate);
                    } catch (Exception ex) {
                        Logger.getLogger(MyBot.class.getName()).log(Level.SEVERE, null, ex);
                    }
					
					// if server responded with a translated text
                    if (translatedText != null) {
                        String styledText = translatedText + "<br/><br/><i>Original: " + b.getDocument().getText() + "</i>";
                        b.getDocument().replace("");
                        b.getDocument().appendMarkup(styledText);
                    }



                }


            }
        }
    }
}
