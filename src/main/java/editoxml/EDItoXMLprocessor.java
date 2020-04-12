package editoxml;

import com.berryworks.edireader.demo.EDItoXML;

import java.io.StringReader;
import java.io.StringWriter;

public class EDItoXMLprocessor {


    private StringReader reader;
    private StringWriter writer;

    public String transform(String edi){
       reader = new StringReader(edi);
       writer = new StringWriter();
       new EDItoXML(reader,writer).run();
       return writer.toString();
    }





}
