
import com.google.api.detect.Detect;
import com.google.api.detect.DetectResult;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

import com.google.wave.api.*;
import com.google.wave.api.StyledText;
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


  @Override
  public void processEvents(RobotMessageBundle bundle) {
    Wavelet wavelet = bundle.getWavelet();

    if (pm == null) {
        pm = PMF.get().getPersistenceManager();
    }

    if (bundle.wasSelfAdded()) {
      Gadget gadget =
        new Gadget("http://translabot.appspot.com/gadget/config.xml");
      gadget.setField("wid", wavelet.getWaveId());
      TextView text = wavelet.appendBlip().getDocument();
      text.getGadgetView().append(gadget);
        
    }

    for (Event e: bundle.getEvents()) {
      

      if (e.getType() == EventType.BLIP_SUBMITTED) {

        Blip b = e.getBlip();

        //WaveDbo waveObj = pm.getObjectById(WaveDbo.class, wavelet.getWaveId());
        Query query = pm.newQuery(WaveDbo.class);
        query.setFilter("waveId == waveidParam");
        query.declareParameters("String waveidParam");

        try {
            List<WaveDbo> results = (List<WaveDbo>) query.execute(wavelet.getWaveId().replace("+", " "));
            log.info("List of Wave = " + results.size());
            waveObj = results.get(0);
        } finally {
            query.closeAll();
        }

        if (waveObj != null) {

            Translate.setHttpReferrer("http://translabot.appspot.com/");

            String translatedText = null;
            String msgOrigin = null;
            try {
                langToTranslate = Language.fromString(waveObj.getLanguage());
                translatedText = Translate.execute(b.getDocument().getText(), Language.AUTO_DETECT, langToTranslate);
//                msgOrigin = Translate.execute("Message original", Language.FRENCH, orginLang.getLanguage());
           } catch (Exception ex) {
                Logger.getLogger(MyBot.class.getName()).log(Level.SEVERE, null, ex);
            }

            String styledText = translatedText + "<br/><br/><i>Original: " + b.getDocument().getText() + "</i>";

            b.getDocument().replace("");
            b.getDocument().appendMarkup(styledText);


//            StyledText textStyled = new StyledText("(" + b.getDocument().getText() + ")", StyleType.ITALIC);
//            textStyled.insertText(0, translatedText+ "\n");
//            b.getDocument().replaceStyledText(textStyled);
//            b.getDocument().insert(0, translatedText + "\n");
           // b.getDocument().replaceStyledText(textStyled);


        }


      }
    }
  }
}