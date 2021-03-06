package app.crossword.yourealwaysbe.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.logging.Level;

import app.crossword.yourealwaysbe.io.KingFeaturesPlaintextIO;
import app.crossword.yourealwaysbe.versions.DefaultUtil;


/**
 * King Features Syndicate Puzzles
 * URL: http://[puzzle].king-online.com/clues/YYYYMMDD.txt
 * premier = Sunday
 * joseph = Monday-Saturday
 * sheffer = Monday-Saturday
 */
public class KFSDownloader extends AbstractDownloader {
    DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
    NumberFormat nf = NumberFormat.getInstance();
    private String author;
    private String fullName;
    private DayOfWeek[] days;

    public KFSDownloader(String shortName, String fullName, String author, DayOfWeek[] days) {
        super("http://puzzles.kingdigital.com/javacontent/clues/"+shortName+"/", DOWNLOAD_DIR, fullName);
        this.fullName = fullName;
        this.author = author;
        this.days = days;
        nf.setMinimumIntegerDigits(2);
        nf.setMaximumFractionDigits(0);
    }

    public DayOfWeek[] getDownloadDates() {
        return days;
    }

    public String getName() {
        return fullName;
    }

    public File download(LocalDate date) {
        File downloadTo = new File(this.downloadDirectory, this.createFileName(date));

        if (downloadTo.exists()) {
            return null;
        }

        File plainText = downloadToTempFile(this.getName(), date);

        if (plainText == null) {
            return null;
        }

        String copyright = "\u00a9 " + date.getYear() + " King Features Syndicate.";

        try {
            InputStream is = new FileInputStream(plainText);
            DataOutputStream os = new DataOutputStream(new FileOutputStream(downloadTo));
            boolean retVal = KingFeaturesPlaintextIO.convertKFPuzzle(is, os, fullName + ", " + df.format(date), author,
                    copyright, date);
            os.close();
            is.close();
            plainText.delete();

            if (!retVal) {
                LOG.log(Level.SEVERE, "Unable to convert KFS puzzle into Across Lite format.");
                downloadTo.delete();
                downloadTo = null;
            }
        } catch (Exception ioe) {
            LOG.log(Level.SEVERE, "Exception converting KFS puzzle into Across Lite format.", ioe);
            downloadTo.delete();
            downloadTo = null;
        }

        return downloadTo;
    }

    @Override
    protected String createUrlSuffix(LocalDate date) {
        return date.getYear() + nf.format(date.getMonthValue()) + nf.format(date.getDayOfMonth()) + ".txt";
    }


}
