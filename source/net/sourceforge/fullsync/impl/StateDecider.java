package net.sourceforge.fullsync.impl;

import net.sourceforge.fullsync.DataParseException;
import net.sourceforge.fullsync.FileComparer;
import net.sourceforge.fullsync.Location;
import net.sourceforge.fullsync.State;
import net.sourceforge.fullsync.fs.File;

/**
 * @author <a href="mailto:codewright@gmx.net">Jan Kopcsek</a>
 */
public class StateDecider implements net.sourceforge.fullsync.StateDecider
{
    protected FileComparer comparer;
    
    public StateDecider( FileComparer comparer )
    {
        this.comparer = comparer;
    }
    
    public State getState( File source, File destination )
    	throws DataParseException
    {
        if( !source.exists() )
             if( !destination.exists() )
                  return new State( State.NodeInSync, Location.None );
             else return new State( State.Orphan, Location.Destination );
        else if( !destination.exists() )
             return new State( State.Orphan, Location.Source );
        
        if( source.isDirectory() )
             if( destination.isDirectory() )
                  return new State( State.NodeInSync, Location.Both );
             else return new State( State.DirHereFileThere, Location.Source );
        else if( destination.isDirectory() )
             return new State( State.DirHereFileThere, Location.Destination );

        return comparer.compareFiles( source.getFileAttributes(), destination.getFileAttributes() );	
    }
}
