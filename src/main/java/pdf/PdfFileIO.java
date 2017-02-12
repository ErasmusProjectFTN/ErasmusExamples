package pdf;

import java.io.IOException;
import java.io.InputStream;

public interface PdfFileIO {

    /**
     * reads file from given path
     *
     * @param s {@link String} absolute path to file
     * @return {@link InputStream} of pdf file
     * @throws IOException if file does not exists
     */
    public InputStream read(String s) throws IOException;

    /**
     * writes file to given path
     *
     * @param path {@link String} absolute path of file
     * @param pdf  {@link InputStream} of pdf file
     * @throws IOException
     */
    public void write(String path, InputStream pdf) throws IOException;

}
