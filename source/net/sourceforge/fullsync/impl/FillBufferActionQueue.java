package net.sourceforge.fullsync.impl;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import net.sourceforge.fullsync.Action;
import net.sourceforge.fullsync.TaskFinishedEvent;
import net.sourceforge.fullsync.ActionQueue;
import net.sourceforge.fullsync.IoStatistics;
import net.sourceforge.fullsync.Location;
import net.sourceforge.fullsync.Task;
import net.sourceforge.fullsync.TaskFinishedListener;
import net.sourceforge.fullsync.TaskTree;
import net.sourceforge.fullsync.buffer.Buffer;
import net.sourceforge.fullsync.buffer.EntryDescriptor;
import net.sourceforge.fullsync.buffer.EntryFinishedListener;
import net.sourceforge.fullsync.fs.File;

/**
 * @author <a href="mailto:codewright@gmx.net">Jan Kopcsek</a>
 */
public class FillBufferActionQueue implements ActionQueue, EntryFinishedListener
{
    private Vector listeners;
    private boolean statisticsOnly;
    private IoStatisticsImpl stats;
    private Buffer buffer;
    
    public FillBufferActionQueue( Buffer buffer )
    {
        this.listeners = new Vector();
        this.statisticsOnly = false;
        this.buffer = buffer;
        buffer.addEntryFinishedListener( this );
    }
    public IoStatistics createStatistics( TaskTree tree )
    {
        statisticsOnly = true;
        enqueue( tree );
        statisticsOnly = false;
        return stats;
    }
    public IoStatistics getStatistics()
    {
        return stats;
    }

    public void enqueue( TaskTree tree )
    {
        stats = new IoStatisticsImpl();
    	enqueue( tree.getRoot() );
    }
    protected void enqueueTaskChildren( Task t )
	{
	    for( Enumeration e = t.getChildren(); e.hasMoreElements(); )
	        enqueue( (Task)e.nextElement() );
	}
    public void enqueue( Task task )
    {
        if( !task.getCurrentAction().isBeforeRecursion() )
        	enqueueTaskChildren( task );

        executeTask( task );
    	
        if(  task.getCurrentAction().isBeforeRecursion() )
        	enqueueTaskChildren( task );
    }
    protected void storeDirCreation( Task task, File subject ) throws IOException
    {
        if( !statisticsOnly )
            buffer.storeEntry( new DirCreationEntryDescriptor( task, subject ) );
        stats.dirsCreated++;
    }
    protected void storeFileCopy( Task task, File source, File destination ) throws IOException
    {
        try {
	        if( !statisticsOnly )
	            buffer.storeEntry( new FileCopyEntryDescriptor( task, source, destination ) );
	        stats.filesCopied++;
	        stats.bytesTransferred += source.getFileAttributes().getLength();
        } catch( IOException ioe ) {
            fireTaskFinished( new TaskFinishedEvent( task, ioe.getMessage() ) );
        }
    }
    protected void storeDeleteNode( Task task, File subject ) throws IOException
    {
        if( !statisticsOnly )
            buffer.storeEntry( new DeleteNodeEntryDescriptor( task, subject ) );
        stats.deletions++;
    }
    protected void executeTask( Task task )
    {
        try {
	        // TODO lock tasks here
            
            Action action = task.getCurrentAction();
            File source = task.getSource();
            File destination = task.getDestination();
            
	        switch( action.getType() )
	        {
	        case Action.Add:
	        case Action.Update:
	            if( action.getLocation() == Location.Destination )
	            {
	                if( source.isDirectory() )
	                     storeDirCreation( task, destination );
	                else storeFileCopy( task, source, destination );
	            } else if( action.getLocation() == Location.Source ) {
	                if( destination.isDirectory() )
	                     storeDirCreation( task, source );
	                else storeFileCopy( task, destination, source );
	            }
	            break;
	        case Action.Delete:
	            if( action.getLocation() == Location.Destination )
	            {
	                storeDeleteNode( task, destination );
	            } else if( action.getLocation() == Location.Source ) {
	                storeDeleteNode( task, source );
	            }
	               
	            break;
	        default:
	        	break;
	        }
	        if( action.getBufferUpdate() > 0 && !statisticsOnly )
	            buffer.storeEntry( new BufferUpdateEntryDescriptor( source, destination, action.getBufferUpdate() ) );
        } catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }
    public void flush()
    {
        try {
            buffer.flush();
        } catch( IOException e ) {
            e.printStackTrace();
        }
    }
    public void entryFinished(EntryDescriptor entry) 
    {
        Object ref = entry.getReferenceObject();
        if( ref == null )
            return;
    	fireTaskFinished( new TaskFinishedEvent( (Task)ref, 0 ) );
	}
    protected void fireTaskFinished( TaskFinishedEvent event )
    {
        Enumeration e = listeners.elements();
    	while( e.hasMoreElements() )
    	{
    		((TaskFinishedListener)e.nextElement())
    			.taskFinished( event );
    	}
    }
    
    public void addTaskFinishedListener( TaskFinishedListener listener )
    {
    	listeners.add( listener );
    }
    public void removeTaskFinishedListener( TaskFinishedListener listener )
    {
    	listeners.remove( listener );
    }
}
