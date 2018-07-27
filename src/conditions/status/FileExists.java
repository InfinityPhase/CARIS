package conditions.status;

import java.io.File;

public class FileExists implements StatusCondition {
	private File file;
	
	public FileExists( String file ) {
		this.file = new File( file );
	}
	
	public FileExists( File file ) {
		this.file = file;
	}
	
	@Override
	public boolean check() {
		return file.exists();
	}

	@Override
	public void reset() {}
}
