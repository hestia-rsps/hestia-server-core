package world.gregs.hestia.core.cache

import world.gregs.hestia.core.cache.store.Index

interface Cache {

    /**
     * Returns a files data
     * @param index The cache index to retrieve from
     * @param archive The archive to retrieve
     * @return The data
     */
    fun getFile(index: Int, archive: Int): ByteArray?

    /**
     * Returns a files data
     * @param index The cache index to retrieve from
     * @param file The archive to retrieve
     * @param file The file to retrieve
     * @return The data
     */
    fun getFile(index: Int, archive: Int, file: Int): ByteArray?

    /**
     * Returns an archive file data
     * @param index The cache index to retrieve from
     * @param archive The file archive to retrieve
     * @return The data
     */
    fun getArchive(index: Int, archive: Int): ByteArray?

    /**
     * Returns a cache index
     * @param index The cache index to retrieve
     * @return The data
     */
    fun getIndex(index: Int): Index

    /**
     * The number of loaded cache indexes
     * @return The index count
     */
    fun indexCount(): Int

}