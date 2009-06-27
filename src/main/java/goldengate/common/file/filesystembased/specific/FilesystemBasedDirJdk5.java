/**
 * Copyright 2009, Frederic Bregier, and individual contributors by the @author
 * tags. See the COPYRIGHT.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3.0 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package goldengate.common.file.filesystembased.specific;

import java.io.File;

/**
 * JDK5 version of specific functions for Filesystem.<br>
 * Note: this class depends on Apache commons Io.
 *
 * @author Frederic Bregier
 *
 */
public class FilesystemBasedDirJdk5 extends FilesystemBasedDirJdkAbstract {
    /**
     *
     * @param file
     * @return True if the file is executable
     */
    @Override
    public boolean canExecute(File file) {
        return false;
    }

    /**
     *
     * @param directory
     * @return the free space of the given Directory
     */
    @Override
    public long getFreeSpace(File directory) {
        if (FilesystemBasedDirJdkAbstract.ueApacheCommonsIo) {
            return FilesystemBasedCommonsIo.freeSpace(directory
                    .getAbsolutePath());
        }
        return -1;
    }
}
