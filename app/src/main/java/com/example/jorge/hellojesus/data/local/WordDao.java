package com.example.jorge.hellojesus.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import com.example.jorge.hellojesus.data.local.control.Control;
import com.example.jorge.hellojesus.data.local.help.Help;

import java.util.List;

/**
 * Created by jorge on 11/04/2018.
 * Data Access Object for the Word table.
 */

@Dao
public interface  WordDao {
    /**
     * Select all Purchase from the Purchase table.
     */
    @Query("SELECT * FROM Word ")
    List<Word> getWord();

    /**
     * Select a Purchase by id.
     */
    @Query("SELECT * FROM Word WHERE word = :word  ")
    Word getWordByName(String word);

    /**
     * Delete a Purchase by id.
     */
    @Query("DELETE FROM Word WHERE word = :word")
    int deleteWordByName(String word);

    /**
     * Update the complete status of a Word Write
     */
    @Query("UPDATE Word SET statuswrite = :statusWrite WHERE word = :word")
    int updateSatusWrite(String statusWrite, String word);

    /**
     * Update the complete status of a Word Sais
     */
    @Query("UPDATE Word SET statussaid = :statusSaid WHERE word = :word")
    int updateSatusSaid(String statusSaid, String word);

    /**
     * Insert a Purchase in the database. If the Purchase already exists, replace it.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertWord(Word word);

    /**
     * Delete all Word.
     */
    @Query("DELETE FROM Word ")
    void deleteAllWords();

    /**
     * Select a Word
     */
    @Query("SELECT * FROM Word WHERE word = :word ")
    Word getWordsByWord(String word);

    /**
     * Select a Purchase by id.
     */
    @Query("SELECT * FROM Word WHERE sourcename = :sourceName ")
    Word getWordByWordSourceName(String sourceName);


    /**
     * Select a Purchase by id.
     */
    @Query("SELECT * FROM Word WHERE word = :word ")
    Word getWordByCarId(String word);


    /**
     * Update for finalize of the shopping
     */
    @Query("UPDATE Word SET statussaid = :statusSaid WHERE word = :word")
    int updateFinalizeWords(String statusSaid, String word);

    @Query("SELECT * FROM Help ")
    List<Help> getHelp();

    @Query("SELECT * FROM Control ")
    List<Control> getControl();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertHelp(Help help);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertControl(Control control);

    /**
     * Select a Purchase by id.
     */
    @Query("SELECT * FROM Help WHERE key = :key ")
    Help getHelpByHelpId(String key);

    @Query("SELECT * FROM Control WHERE key = :key ")
    Control getControlByHelpId(String key);

}
