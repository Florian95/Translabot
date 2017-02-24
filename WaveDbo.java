/*
 * This class creates the object that handles the translations. Translation details such as language are
 * part of this class. 
 */

package db.entities;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class WaveDbo {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String language;

    @Persistent
    private String waveId;
	/** 
     * Initializer
     * param waveID: String object to set the current waveID of this instance.
     * param langauge: String object to set the current language of this instance.
     */
    public WaveDbo(String waveId, String language) {
        this.waveId = waveId;
        this.language = language;
    }

    // Accessors for the fields.  JDO doesn't use these, but your application does.

    /** 
     * ID Getter method
     * @returns Long type object 
     */
    public Long getId() {
        return id;
    }

    /** 
     * Wave ID Getter method
     * @returns String type object 
     */
    public String getWaveId() {
        return waveId;
    }
    /** 
     * Wave ID Setter Method
     * @param waveID: String type object 
     */
    public void setWaveId(String waveId) {
        this.waveId = waveId;
    }

     /** Language Getter Method
     * @returns String type language
     */
    public String getLanguage() {
        return language;
    }

     /** Language Setter Method
     * @param language: String type object
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    
}