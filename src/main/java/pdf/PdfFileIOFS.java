package pdf;

import java.io.*;

public class PdfFileIOFS implements PdfFileIO {

    /**
     * reads file from local file system
     *
     * @param s {@link String} absolute path to file
     * @return {@link InputStream} pdf file
     * @throws IOException
     */
    public InputStream read(String s) throws IOException {
        File file = new File(s);

        if (!file.exists())
            throw new FileNotFoundException("there is not file on path: " + s);

        return new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * write file to local file system
     *
     * @param path {@link String} absolute path of file
     * @param pdf  {@link InputStream} of pdf file
     * @throws IOException
     */
    public void write(String path, InputStream pdf) throws IOException {

        File f = new File(path);

        byte[] buf = new byte[8192];

        OutputStream oos = new FileOutputStream(f);

        int c = 0;

        while ((c = pdf.read(buf, 0, buf.length)) > 0) {
            oos.write(buf, 0, c);
            oos.flush();
        }

        pdf.close();
        oos.close();
    }

}
