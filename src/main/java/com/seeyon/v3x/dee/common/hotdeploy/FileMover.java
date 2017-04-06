package com.seeyon.v3x.dee.common.hotdeploy;

import com.seeyon.v3x.dee.DirectoryWatcher;

import java.io.File;
import java.util.regex.Pattern;

public class FileMover extends DirectoryWatcher {
	private File _destinationDirectory;

	/**
	 * Creates new FileMover that will try to move a file to the specified
	 * directory.
	 * 
	 * @param directoryToWatch
	 *            directory to be watched.
	 * @param destinationDirectory
	 *            directory that files will be moved to.
	 * @param pattern
	 *            Pattern to filter files in the directory. If <em>null</em>,
	 *            then the default Pattern will be used. The default Pattern
	 *            allows all files.
	 * @throws NullPointerException
	 *             if supplied parameter is <em>null</em>.
	 * @see #accept(java.io.File)
	 */
	public FileMover(String directoryToWatch, String destinationDirectory,
			Pattern pattern) throws NullPointerException {
		this(new File(directoryToWatch), new File(destinationDirectory),
				pattern);
	}

	/**
	 * Creates new FileMover that will try to move a file to the specified
	 * directory.
	 *
	 * @param directoryToWatch
	 *            directory to be watched.
	 * @param destinationDirectory
	 *            directory that files will be moved to.
	 * @param pattern
	 *            Pattern to filter files in the directory. If <em>null</em>,
	 *            then the default Pattern will be used. The default Pattern
	 *            allows all files.
	 * @throws NullPointerException
	 *             if supplied parameter is <em>null</em>.
	 * @see #accept(java.io.File)
	 */
	public FileMover(File directoryToWatch, File destinationDirectory,
			Pattern pattern) throws NullPointerException {
		super(directoryToWatch, pattern);
		if (destinationDirectory == null) {
			throw new NullPointerException(
					"\"destinationDirectory\" parameter cannot be null");
		}
		_destinationDirectory = destinationDirectory;
	}

	/**
	 * Tries to move the given file to specified directory.
	 * 
	 * @return destination file or <em>null</em> if the file could not be moved.
	 */
	protected File processFile(File file) {
		File destFile = new File(_destinationDirectory, file.getName());
		boolean isOK = file.renameTo(destFile);
		if (isOK) {
			if (_isDebugEnabled) {
				log.debug("FileMover: File " + file.getName()
						+ " was moved to " + _destinationDirectory.getPath());
			}
		} else {
			log.error("FileMover: File " + file.getName()
					+ " cannot be moved to " + _destinationDirectory.getPath());
		}
		return isOK ? destFile : null;
	}
}
