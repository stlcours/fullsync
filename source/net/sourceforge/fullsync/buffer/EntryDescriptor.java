/*
 * Created on 18.07.2004
 */
package net.sourceforge.fullsync.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="mailto:codewright@gmx.net">Jan Kopcsek</a>
 */
public interface EntryDescriptor
{
    public Object getReferenceObject();
    public long getLength();
    
    // FIXME if those streams don't get closed, the entry descriptor should
    // return the same one as before (say the opened one)
    public InputStream getInputStream() throws IOException;
    public OutputStream getOutputStream() throws IOException;
    
    public void finishStore(); // into buffer
    public void finishWrite(); // to target
    //public void flush( Buffer buffer, Entry entry ) throws IOException;
    public String getOperationDescription();
}
