package pdf;

import util.Conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PdfStorage {

    private String rootPath;
    private PdfFileIO pdfIO;

    public PdfStorage() {
        rootPath = Conf.getInstance().getPdfRoot();
        pdfIO = new PdfFileIOFS();
    }

    /**
     * This method makes empty user personal storage
     *
     * @param userid name of storage or relative path to storage
     * @throws IOException throws if storage already exists
     */
    public void initializeUserStorage(String userid) throws IOException {
        File file = new File(rootPath + userid);
        if (!file.exists())
            file.mkdir();
        else
            throw new IOException("File already exists");
    }

    /**
     * Check is storage exists for given relative path to user personal storage
     *
     * @param storageRel relative path (name) of personal storage
     * @return boolean if storage is already exists (if initialized)
     */
    public boolean isInitializedStorage(String storageRel) {
        File file = new File(rootPath + storageRel);
        return file.exists();
    }

    /**
     * read one pdf from user personal storage
     *
     * @param userRel  user storage
     * @param fileName file name
     * @return
     * @throws IOException
     */
    public InputStream readOnePdf(String userRel, String fileName) throws IOException {
        return pdfIO.read(rootPath + userRel + "/" + fileName);

    }

    /**
     * Checks if storage is initialized and then writes pdf to file
     *
     * @param userRel  user storage
     * @param fileName file name
     * @param pdf
     * @throws IOException
     */
    public void writeOnePdf(String userRel, String fileName, InputStream pdf) throws IOException {
        if (isInitializedStorage(userRel))
            pdfIO.write(rootPath + userRel + "/" + fileName, pdf);
        else
            throw new FileNotFoundException("Storage is not initialized");
    }

    public static void main(String[] args) {
        PdfStorage storage = new PdfStorage();

        try {
            //read
            InputStream inPdf = storage.readOnePdf("user_1", "sample.pdf");

            //initialize user_2
            storage.initializeUserStorage("user_2");

            //write
            storage.writeOnePdf("user_2", "sample.pdf", inPdf);

            inPdf.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
